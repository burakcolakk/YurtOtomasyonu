package com.yurt.project.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yurt.project.dto.request.CreateVisitorRequest;
import com.yurt.project.repository.VisitorRepository;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class VisitorCrudIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private VisitorRepository visitorRepository;
    @Autowired private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void visitorLifecycle_ShouldWork() throws Exception {
        // 1. OLUŞTURMA (Create)
        // Değişken ismini 'request' olarak tanımlıyoruz
        CreateVisitorRequest request = new CreateVisitorRequest();
        request.setVisitorName("Mehmet Ziyaretçi");
        request.setStudentName("Ali Öğrenci");
        request.setRelation("Amcası");

        // Burada 'request' değişkenini kullanıyoruz (İsimler eşleşmeli!)
        mockMvc.perform(post("/api/visitors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.visitorName").value("Mehmet Ziyaretçi"));

        // 2. ID'yi Bul (Çıkış işlemi için lazım)
        // Veritabanından son eklenen ziyaretçiyi alıyoruz
        Long visitorId = visitorRepository.findAll().stream()
                .filter(v -> v.getVisitorName().equals("Mehmet Ziyaretçi"))
                .findFirst()
                .orElseThrow()
                .getId();

        // 3. ÇIKIŞ YAP (Check-Out)
        // PUT isteği atıyoruz
        mockMvc.perform(put("/api/visitors/" + visitorId + "/checkout"))
                .andExpect(status().isOk());

        // 4. KONTROL (Çıkış saati yazılmış mı?)
        // Repository'den tekrar çekip kontrol ediyoruz
        assertNotNull(visitorRepository.findById(visitorId).get().getExitTime());
    }
}