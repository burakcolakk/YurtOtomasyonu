package com.yurt.project.integration;

import com.yurt.project.dto.request.AllocationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ErrorHandlingIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void getStudent_WhenIdNotExists_ShouldReturn400() throws Exception {
        // Olmayan bir ID (99999) ile detay çekmeye çalışıyoruz
        // Service katmanındaki "studentRepository.findById(...).orElseThrow(...)" satırını test eder.
        mockMvc.perform(get("/api/students/99999"))
                .andExpect(status().isBadRequest()); // GlobalExceptionHandler BusinessException yakalar
        // Eğer kodda NotFoundException kullanıyorsan ve handler 404 dönüyorsa burayı isNotFound() yap.
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void allocate_WhenStudentNotExists_ShouldReturnError() throws Exception {
        // Olmayan öğrenciyi odaya yerleştirmeye çalış
        AllocationRequest request = new AllocationRequest();
        request.setStudentId(99999L);
        request.setBedId(1L);

        mockMvc.perform(post("/api/allocations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest()); // Service katmanındaki validation'ı tetikler
    }
}