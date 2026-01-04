package com.yurt.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yurt.project.dto.request.CreateStudentRequest;
import com.yurt.project.dto.response.StudentResponse;
import com.yurt.project.service.StudentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean; // YENİ IMPORT (MockBean yerine)
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // ESKİ: @MockBean
    // YENİ: @MockitoBean (Spring Boot 3.4+)
    @MockitoBean
    private StudentService studentService;

    @Test
    @WithMockUser(roles = "ADMIN") // Admin rolüyle istek atıyoruz
    void createStudent_WhenAuthorized_ShouldReturn201() throws Exception {
        // 1. HAZIRLIK (Veri Oluşturma)
        CreateStudentRequest request = new CreateStudentRequest();
        request.setTcKimlikNo("12345678901");
        request.setFirstName("Veli");
        request.setLastName("Demir");
        // --- ZORUNLU ALANLAR EKLENDİ ---
        request.setEmail("veli.demir@ornek.com"); // E-posta eklendi
        request.setRoomId(1L);                    // Oda ID eklendi
        // -------------------------------

        // Mock Servis Cevabı
        StudentResponse response = new StudentResponse();
        response.setId(1L);
        response.setFirstName("Veli");
        response.setEmail("veli.demir@ornek.com");

        // Servis çağrıldığında ne döneceğini öğretiyoruz
        Mockito.when(studentService.createStudent(Mockito.any(CreateStudentRequest.class)))
                .thenReturn(response);

        // 2. EYLEM & KONTROL (İstek Atma)
        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated()); // 201 Bekliyoruz
    }
}