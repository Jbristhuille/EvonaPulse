package com.evonapulse.backend.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProjectPublicResponse {
    public String id;
    public String name;
    public String apiKey;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
}
