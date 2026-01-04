package com.yurt.project.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yurt.project.dto.request.CreateStaffRequest; // Import ekledik
import com.yurt.project.entity.User;
import com.yurt.project.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SupportAndStaffIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private ObjectMapper objectMapper; // JSON dönüşümü için gerekli

    @Test
    @WithMockUser(roles = "ADMIN")
    void staffOperations_ShouldWork() throws Exception {
        // ESKİ KOD (Hata Veren): .param(...) kullanıyordu.
        // YENİ KOD: DTO ve JSON kullanıyor.

        CreateStaffRequest request = new CreateStaffRequest();
        request.setFirstName("Ayşe");
        request.setLastName("Yılmaz");
        request.setJobTitle("Temizlik");
        request.setPhoneNumber("5551234567");

        // Personel Ekle (JSON olarak)
        mockMvc.perform(post("/api/staff")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Ayşe"));

        // Personel Listele
        mockMvc.perform(get("/api/staff"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void supportTicketFlow_ShouldWork() throws Exception {
        // Önce bir kullanıcı lazım (Request için)
        User user = new User();
        user.setUsername("ticketUser");
        user.setPassword("pass");
        user.setEmail("t@t.com");
        userRepository.save(user);

        // 1. Arıza Kaydı Aç
        mockMvc.perform(post("/api/tickets")
                        .param("userId", user.getId().toString())
                        .param("title", "Musluk Bozuk")
                        .param("description", "Su damlatıyor"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("OPEN"));

        // 2. Arızaları Listele
        mockMvc.perform(get("/api/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}