package com.evonapulse.backend.dtos;

import com.evonapulse.backend.entities.MetricType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class LogPublicResponse {
    public UUID id;
    public LocalDateTime timestamp;
    public String value;
}
