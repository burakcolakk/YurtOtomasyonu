package com.yurt.project.integration;

import com.yurt.project.entity.Student;
import com.yurt.project.entity.User;
import com.yurt.project.repository.StudentRepository;
import com.yurt.project.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PaymentAndSecurityIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private StudentRepository studentRepository;
    @Autowired private UserRepository userRepository;

    @Test
    @WithMockUser(roles = "ADMIN")
    void paymentFlow_ShouldWork() throws Exception {
        // 1. Altyapı: Öğrenci oluştur
        User user = new User();
        user.setUsername("payUser");
        user.setPassword("123");
        user.setEmail("p@p.com");
        userRepository.save(user);

        Student student = new Student();
        student.setFirstName("Richie");
        student.setTcKimlikNo("90000000001");
        student.setUser(user);
        student = studentRepository.save(student);

        // 2. Ödeme Ekle
        mockMvc.perform(post("/api/payments")
                        .param("studentId", student.getId().toString())
                        .param("amount", "2500.50")
                        .param("description", "Ocak Kirası"))
                .andExpect(status().isCreated());

        // 3. Ödemeleri Listele (DTO mapping'lerini test eder)
        mockMvc.perform(get("/api/payments/student/" + student.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount").value(2500.50))
                .andExpect(jsonPath("$[0].description").value("Ocak Kirası"));
    }

    @Test
    void accessWithoutToken_ShouldBeForbidden() throws Exception {
        // @WithMockUser YOK! Token olmadan erişim denemesi.
        // Security Filter Chain'i test eder.
        mockMvc.perform(get("/api/students"))
                .andExpect(status().isForbidden()); // 403 Forbidden
    }
}