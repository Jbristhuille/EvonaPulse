package com.evonapulse.backend.dtos;

import com.evonapulse.backend.entities.MetricType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LogIngestionRequest {
    @NotNull
    public MetricType type;

    @NotBlank
    public String value;

    @NotBlank
    public String metricName;
}
