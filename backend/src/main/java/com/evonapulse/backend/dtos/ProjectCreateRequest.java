package com.evonapulse.backend.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProjectCreateRequest {
    @NotBlank
    private String name;
}
