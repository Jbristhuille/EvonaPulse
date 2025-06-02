package com.evonapulse.backend.dtos;

import com.evonapulse.backend.entities.MetricType;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class MetricPublicResponse {
    private UUID id;
    private String label;
    private String name;
    private MetricType type;
    private UUID projectId;
    private Instant createdAt;
    private Instant updatedAt;
}