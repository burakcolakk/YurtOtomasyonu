package com.yurt.project.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yurt.project.dto.request.CreateStudentRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ValidationExceptionTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void createStudent_WithInvalidData_ShouldReturn400() throws Exception {
        // Boş bir request gönderiyoruz (@NotNull validasyonlarına takılmalı)
        CreateStudentRequest request = new CreateStudentRequest();
        // Hiçbir alan set etmedik (tc, isim vs. null)

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest()); // 400 Bad Request bekliyoruz
    }
}