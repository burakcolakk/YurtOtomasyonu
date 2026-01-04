package com.yurt.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yurt.project.dto.request.LoginRequest;
import com.yurt.project.entity.Role;
import com.yurt.project.entity.User;
import com.yurt.project.repository.RoleRepository;
import com.yurt.project.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // Test verisini temizlemek için
class AuthControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Test için geçici bir kullanıcı oluştur
        if (!userRepository.existsByUsername("testuser")) {
            Role role = roleRepository.save(new Role("ROLE_TEST", "Test Rolu"));
            Set<Role> roles = new HashSet<>();
            roles.add(role);

            User user = new User();
            user.setUsername("testuser");
            user.setPassword(passwordEncoder.encode("pass123")); // Şifreli kaydet
            user.setEmail("test@test.com");
            user.setRoles(roles);
            userRepository.save(user);
        }
    }

    @Test
    void login_WithCorrectCredentials_ShouldReturnToken() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("pass123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk()) // 200 OK
                .andExpect(jsonPath("$.token").exists()) // Token alanı var mı?
                .andExpect(jsonPath("$.message").value("Giriş Başarılı"));
    }

    @Test
    void login_WithWrongPassword_ShouldFail() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("yanlisSifre");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized()); // DÜZELTME: isForbidden() yerine isUnauthorized() (401)
    }
}