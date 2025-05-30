package com.evonapulse.backend.services;

import com.evonapulse.backend.entities.UserEntity;
import com.evonapulse.backend.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        jwtService.jwtSecret = "MySuperSecretKeyThatIsAtLeast256BitsLong123456";
        jwtService.jwtExpirationMs = 3600000; // 1h
    }

    @Test
    void testGenerateAndValidateToken() {
        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID());
        user.setEmail("test@mail.com");

        String token = jwtService.generateToken(user);

        assertNotNull(token);
        assertTrue(jwtService.isTokenValid(token));
        assertEquals(user.getId().toString(), jwtService.getUserIdFromToken(token));
    }

    @Test
    void testInvalidToken() {
        String invalidToken = "invalid.jwt.token";

        assertFalse(jwtService.isTokenValid(invalidToken));
        assertThrows(Exception.class, () -> jwtService.getUserIdFromToken(invalidToken));
    }

    @Test
    void testExpiredToken() throws InterruptedException {
        jwtService.jwtExpirationMs = 1;

        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID());
        user.setEmail("expired@mail.com");

        String token = jwtService.generateToken(user);

        Thread.sleep(10);
    }
}