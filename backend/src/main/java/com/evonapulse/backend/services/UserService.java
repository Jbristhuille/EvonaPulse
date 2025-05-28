package com.evonapulse.backend.services;

import com.evonapulse.backend.entities.UserEntity;
import com.evonapulse.backend.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
}
