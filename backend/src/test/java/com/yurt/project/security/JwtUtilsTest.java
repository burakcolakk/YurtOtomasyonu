package com.yurt.project.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    private UserDetails userDetails;

    // Senin servisteki anahtarın aynısını buraya kopyalıyoruz ki
    // "Sahte Süresi Geçmiş Token" üretebilelim.
    private static final String SECRET_KEY = "7d3a2c5b1e9f8a4d6c7b0e1f3a2c5b8e9f4d6c7b0e1f3a2c5b8e9f4d6c7b0e1f";

    @BeforeEach
    void setUp() {
        // Test için sahte bir kullanıcı oluşturuyoruz
        userDetails = new User("testUser", "password123", Collections.emptyList());
    }

    @Test
    void generateToken_ShouldReturnToken() {
        // 1. Token üret
        String token = jwtService.generateToken(userDetails);

        // 2. Kontrol et
        assertNotNull(token);
        assertFalse(token.isEmpty());
        // Token 3 parçadan oluşmalı (Header.Payload.Signature)
        assertEquals(3, token.split("\\.").length);
    }

    @Test
    void extractUsername_ShouldReturnCorrectUsername() {
        // 1. Token üret
        String token = jwtService.generateToken(userDetails);

        // 2. Username'i çıkar
        String username = jwtService.extractUsername(token);

        // 3. Kontrol et
        assertEquals("testUser", username);
    }

    @Test
    void isTokenValid_WithValidToken_ShouldReturnTrue() {
        // 1. Token üret
        String token = jwtService.generateToken(userDetails);

        // 2. Doğrula
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        // 3. Sonuç true olmalı
        assertTrue(isValid);
    }

    @Test
    void isTokenValid_WithWrongUser_ShouldReturnFalse() {
        // 1. "testUser" için token üret
        String token = jwtService.generateToken(userDetails);

        // 2. Başka bir kullanıcı oluştur ("hacker")
        UserDetails wrongUser = new User("hacker", "pass", Collections.emptyList());

        // 3. Doğrula (İsimler uyuşmadığı için false dönmeli)
        boolean isValid = jwtService.isTokenValid(token, wrongUser);

        assertFalse(isValid);
    }

    @Test
    void isTokenValid_WithExpiredToken_ShouldThrowException() {
        // Bu test biraz teknik:
        // Servisin içinde expiration 24 saat olduğu için, süresi geçmiş token'ı
        // servisin metoduyla üretemeyiz. Manuel olarak geçmiş tarihli token üretiyoruz.

        String expiredToken = Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 48)) // 48 saat önce
                .setExpiration(new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24)) // 24 saat önce bitti
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

        // JJWT kütüphanesi süresi geçmiş token parse edilirken otomatik olarak
        // "ExpiredJwtException" fırlatır. Bunu bekliyoruz.
        assertThrows(io.jsonwebtoken.ExpiredJwtException.class, () -> {
            jwtService.isTokenValid(expiredToken, userDetails);
        });
    }

    // Helper metod (Test sınıfı içinde anahtarı imzalamak için)
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}