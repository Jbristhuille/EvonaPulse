package com.evonapulse.backend.services;

import com.evonapulse.backend.dtos.UserAuthResponse;
import com.evonapulse.backend.entities.UserEntity;
import com.evonapulse.backend.exceptions.PasswordIncorrectException;
import com.evonapulse.backend.mappers.UserMapper;
import com.evonapulse.backend.repositories.JwtService;
import com.evonapulse.backend.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       UserMapper userMapper,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
    }

    public boolean anyUserExist() {
        return userRepository.count() > 0;
    }

    public UserEntity create(String email, String password) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);
        userEntity.setPasswordHash(passwordEncoder.encode(password));
        return userRepository.save(userEntity);
    }

    public UserAuthResponse authenticate(String email, String rawPassword) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(PasswordIncorrectException::new);

        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new PasswordIncorrectException();
        }

        String token = jwtService.generateToken(user);

        UserAuthResponse userAuthResponse = userMapper.toUserAuthResponse(user);
        userAuthResponse.setToken(token);
        return userAuthResponse;
    }
}
