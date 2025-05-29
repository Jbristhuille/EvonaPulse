package com.evonapulse.backend.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserAuthResponse {
    public String id;
    public String email;
    public LocalDateTime createdAt;
    public String token;
}
