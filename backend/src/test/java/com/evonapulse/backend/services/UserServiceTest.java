package com.evonapulse.backend.services;

import com.evonapulse.backend.dtos.UserAuthResponse;
import com.evonapulse.backend.entities.UserEntity;
import com.evonapulse.backend.exceptions.ApiException;
import com.evonapulse.backend.mappers.UserMapper;
import com.evonapulse.backend.security.JwtService;
import com.evonapulse.backend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAnyUserExist_ReturnsTrue() {
        when(userRepository.count()).thenReturn(1L);
        assertTrue(userService.anyUserExist());
        verify(userRepository).count();
    }

    @Test
    void testAnyUserExist_ReturnsFalse() {
        when(userRepository.count()).thenReturn(0L);
        assertFalse(userService.anyUserExist());
        verify(userRepository).count();
    }

    @Test
    void testCreate_Success() {
        String email = "test@mail.com";
        String rawPassword = "password";
        String encodedPassword = "encoded";

        UserEntity savedUser = new UserEntity();
        savedUser.setEmail(email);
        savedUser.setPasswordHash(encodedPassword);

        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        when(userRepository.save(any(UserEntity.class))).thenReturn(savedUser);

        UserEntity result = userService.create(email, rawPassword);

        assertEquals(email, result.getEmail());
        assertEquals(encodedPassword, result.getPasswordHash());
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void testAuthenticate_Success() {
        String email = "user@mail.com";
        String rawPassword = "password";
        String hashedPassword = "hashedPassword";
        String token = "jwt-token";

        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setPasswordHash(hashedPassword);

        UserAuthResponse expectedResponse = new UserAuthResponse();
        expectedResponse.setId("user-id");
        expectedResponse.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, hashedPassword)).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn(token);
        when(userMapper.toUserAuthResponse(user)).thenReturn(expectedResponse);

        UserAuthResponse result = userService.authenticate(email, rawPassword);

        assertEquals(email, result.getEmail());
        assertEquals(token, result.getToken());
        verify(userRepository).findByEmail(email);
        verify(passwordEncoder).matches(rawPassword, hashedPassword);
        verify(jwtService).generateToken(user);
        verify(userMapper).toUserAuthResponse(user);
    }

    @Test
    void testAuthenticate_UserNotFound() {
        when(userRepository.findByEmail("unknown@mail.com")).thenReturn(Optional.empty());

        ApiException ex = assertThrows(ApiException.class, () ->
                userService.authenticate("unknown@mail.com", "anyPassword")
        );
        assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatus());
        assertEquals("Invalid email", ex.getMessage());

    }

    @Test
    void testAuthenticate_InvalidPassword() {
        UserEntity user = new UserEntity();
        user.setEmail("user@mail.com");
        user.setPasswordHash("hashed");

        when(userRepository.findByEmail("user@mail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "hashed")).thenReturn(false);

        ApiException ex = assertThrows(ApiException.class, () ->
                userService.authenticate("user@mail.com", "wrong")
        );
        assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatus());
        assertEquals("Invalid password", ex.getMessage());

    }
}
