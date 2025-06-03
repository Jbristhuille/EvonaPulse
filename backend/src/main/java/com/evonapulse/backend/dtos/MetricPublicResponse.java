package com.evonapulse.backend.dtos;

import com.evonapulse.backend.entities.MetricType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class MetricPublicResponse {
    private UUID id;
    private String label;
    private String name;
    private MetricType type;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    public List<LogPublicResponse> logs;
}