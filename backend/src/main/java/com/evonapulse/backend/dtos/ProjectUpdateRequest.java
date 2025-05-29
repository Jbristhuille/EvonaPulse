package com.evonapulse.backend.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProjectUpdateRequest {
    @NotBlank
    private String name;
}
