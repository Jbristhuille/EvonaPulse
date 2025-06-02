package com.evonapulse.backend.dtos;

import com.evonapulse.backend.entities.MetricType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.UUID;

@Data
public class MetricCreateRequest {

    @NotBlank
    private String label;

    @NotBlank
    @Pattern(regexp = "^[a-z0-9\\-_]+$", message = "The name must be lowercase, without accents, and may only contain hyphens or underscores.")
    private String name;

    @NotNull
    private MetricType type;

    @NotNull
    private UUID projectId;
}