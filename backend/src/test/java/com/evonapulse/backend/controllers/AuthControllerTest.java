package com.evonapulse.backend.controllers;

import com.evonapulse.backend.dtos.UserPublicResponse;
import com.evonapulse.backend.dtos.UserRegisterRequest;
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
        UserRegisterRequest request = new UserRegisterRequest();
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
        UserRegisterRequest request = new UserRegisterRequest();
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

        UserRegisterRequest dto = new UserRegisterRequest();
        dto.setEmail("test@mail.fr");
        dto.setPassword("123");

        Set<ConstraintViolation<UserRegisterRequest>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());

        boolean hasPasswordSizeError = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("password") &&
                        v.getMessage().contains("at least 8 characters"));

        assertTrue(hasPasswordSizeError);
    }
}
