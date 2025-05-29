package com.evonapulse.backend.controllers;

import com.evonapulse.backend.dtos.UserAuthResponse;
import com.evonapulse.backend.dtos.UserPublicResponse;
import com.evonapulse.backend.dtos.UserAuthRequest;
import com.evonapulse.backend.entities.UserEntity;
import com.evonapulse.backend.exceptions.RegistrationClosedException;
import com.evonapulse.backend.mappers.UserMapper;
import com.evonapulse.backend.services.UserService;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthControllerTest {
    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegistrationSuccess() {
        UserAuthRequest request = new UserAuthRequest();
        request.setEmail("test@mail.fr");
        request.setPassword("123456789");

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("test@mail.fr");

        UserPublicResponse userPublicResponse = new UserPublicResponse();
        userPublicResponse.setId(userEntity.getId().toString());
        userPublicResponse.setEmail(userEntity.getEmail());
        userPublicResponse.setCreatedAt(userEntity.getCreatedAt());

        when(userService.anyUserExist()).thenReturn(false);
        when(userService.create(request.getEmail(), request.getPassword())).thenReturn(userEntity);
        when(userMapper.toUserPublicResponse(userEntity)).thenReturn(userPublicResponse);

        ResponseEntity<UserPublicResponse> response = authController.register(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(userPublicResponse, response.getBody());

        verify(userService).anyUserExist();
        verify(userService).create(request.getEmail(), request.getPassword());
        verify(userMapper).toUserPublicResponse(userEntity);
    }

    @Test
    void testRegisterUserAlreadyExists() {
        UserAuthRequest request = new UserAuthRequest();
        request.setEmail("test@mail.fr");
        request.setPassword("123456789");

        when(userService.anyUserExist()).thenReturn(true);

        assertThrows(RegistrationClosedException.class, () -> authController.register(request));

        verify(userService).anyUserExist();
        verify(userService, never()).create(anyString(), anyString());
        verify(userMapper, never()).toUserPublicResponse(any());
    }

    @Test
    void testDtoValidationFails() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        UserAuthRequest dto = new UserAuthRequest();
        dto.setEmail("test@mail.fr");
        dto.setPassword("123");

        Set<ConstraintViolation<UserAuthRequest>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());

        boolean hasPasswordSizeError = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("password") &&
                        v.getMessage().contains("at least 8 characters"));

        assertTrue(hasPasswordSizeError);
    }

    @Test
    void testLoginSuccess() {
        UserAuthRequest request = new UserAuthRequest();
        request.setEmail("user@mail.com");
        request.setPassword("securepassword");

        UserAuthResponse expectedResponse = new UserAuthResponse();
        expectedResponse.setId("user-id");
        expectedResponse.setEmail("user@mail.com");
        expectedResponse.setToken("jwt-token");

        when(userService.authenticate(request.getEmail(), request.getPassword())).thenReturn(expectedResponse);

        ResponseEntity<UserAuthResponse> response = authController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());

        verify(userService).authenticate(request.getEmail(), request.getPassword());
    }

    @Test
    void testLoginFailure() {
        UserAuthRequest request = new UserAuthRequest();
        request.setEmail("invalid@mail.com");
        request.setPassword("wrongpassword");

        when(userService.authenticate(request.getEmail(), request.getPassword()))
                .thenThrow(new RuntimeException("Invalid credentials"));

        assertThrows(RuntimeException.class, () -> authController.login(request));
        verify(userService).authenticate(request.getEmail(), request.getPassword());
    }
}
