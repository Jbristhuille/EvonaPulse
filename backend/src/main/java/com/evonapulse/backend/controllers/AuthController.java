package com.evonapulse.backend.controllers;

import com.evonapulse.backend.dtos.UserPublicResponse;
import com.evonapulse.backend.dtos.UserRegisterRequest;
import com.evonapulse.backend.entities.UserEntity;
import com.evonapulse.backend.exceptions.RegistrationClosedException;
import com.evonapulse.backend.mappers.UserMapper;
import com.evonapulse.backend.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final UserMapper userMapper;

    public AuthController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<UserPublicResponse> register(@Valid @RequestBody UserRegisterRequest user) {
        if (!userService.anyUserExist()) {
            UserEntity newUserEntity = userService.create(user.email, user.password);
            return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toUserPublicResponse(newUserEntity));
        } else {
            throw new RegistrationClosedException("Registration closed");
        }
    }
}
