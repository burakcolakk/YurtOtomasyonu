package com.yurt.project.integration;

import com.yurt.project.exception.BusinessException;
import com.yurt.project.service.StudentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean; // Yeni MockitoBean
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ExceptionHandlingIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    // Gerçek servisi taklit ediyoruz
    @MockitoBean
    private StudentService studentService;

    @Test
    @WithMockUser // Yetkili kullanıcı taklidi
    void whenBusinessExceptionThrown_ShouldReturn400() throws Exception {
        // SENARYO:
        // Herhangi bir öğrenci listeleme isteği geldiğinde, servis "Özel Hata" fırlatsın.
        Mockito.when(studentService.getAllStudents())
                .thenThrow(new BusinessException("Beklenen test hatası oluştu!"));

        // EYLEM:
        // /api/students endpointine istek atıyoruz.
        // Normalde 200 dönmesi gerekirken, servisi manipüle ettiğimiz için exception fırlatacak.
        // Exception Handler devreye girip bunu yakalayacak ve 400'e çevirecek.
        mockMvc.perform(get("/api/students")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) // 400 Bekliyoruz (Daha önce 500 geliyordu)
                .andExpect(jsonPath("$.message").value("Beklenen test hatası oluştu!")); // Mesaj kontrolü
    }
}