package com.evonapulse.backend.controllers;

import com.evonapulse.backend.dtos.UserAuthResponse;
import com.evonapulse.backend.dtos.UserPublicResponse;
import com.evonapulse.backend.dtos.UserAuthRequest;
import com.evonapulse.backend.entities.UserEntity;
import com.evonapulse.backend.exceptions.PasswordIncorrectException;
import com.evonapulse.backend.exceptions.RegistrationClosedException;
import com.evonapulse.backend.mappers.UserMapper;
import com.evonapulse.backend.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<UserPublicResponse> register(@Valid @RequestBody UserAuthRequest user) {
        if (!userService.anyUserExist()) {
            UserEntity newUserEntity = userService.create(user.email, user.password);
            return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toUserPublicResponse(newUserEntity));
        } else {
            throw new RegistrationClosedException("Registration closed");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserAuthResponse> login(@Valid @RequestBody UserAuthRequest req) {
        UserAuthResponse response = userService.authenticate(req.getEmail(), req.getPassword());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Hello world");
    }
}
