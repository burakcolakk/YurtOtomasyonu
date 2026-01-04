package com.yurt.project.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yurt.project.dto.request.CreateStaffRequest;
import com.yurt.project.dto.request.UpdateStaffRequest;
import com.yurt.project.repository.StaffRepository;
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
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class StaffCrudIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private StaffRepository staffRepository;
    @Autowired private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void staffLifecycle_ShouldWork() throws Exception {
        // 1. OLUŞTURMA (Create)
        CreateStaffRequest createRequest = new CreateStaffRequest();
        createRequest.setFirstName("Ahmet");
        createRequest.setLastName("Yılmaz");
        createRequest.setJobTitle("Güvenlik");
        createRequest.setPhoneNumber("555-111-22-33");

        mockMvc.perform(post("/api/staff")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated());

        // ID'yi bul (Basitlik için listeden çekiyoruz veya repository'den son ekleneni alıyoruz)
        Long staffId = staffRepository.findAll().stream()
                .filter(s -> s.getFirstName().equals("Ahmet"))
                .findFirst().orElseThrow().getId();

        // 2. GÜNCELLEME (Update)
        UpdateStaffRequest updateRequest = new UpdateStaffRequest();
        updateRequest.setJobTitle("Baş Güvenlik"); // Terfi etti :)
        updateRequest.setPhoneNumber("555-999-00-00");

        mockMvc.perform(put("/api/staff/" + staffId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jobTitle").value("Baş Güvenlik"));

        // 3. SİLME (Delete)
        mockMvc.perform(delete("/api/staff/" + staffId))
                .andExpect(status().isNoContent());

        // 4. KONTROL
        assertFalse(staffRepository.existsById(staffId));
    }
}