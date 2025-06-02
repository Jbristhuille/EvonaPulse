package com.evonapulse.backend.mappers;

import com.evonapulse.backend.dtos.MetricCreateRequest;
import com.evonapulse.backend.dtos.MetricPublicResponse;
import com.evonapulse.backend.entities.MetricEntity;
import com.evonapulse.backend.entities.ProjectEntity;

public interface MetricMapper {
    MetricPublicResponse toPublicResponse(MetricEntity metric);
    MetricEntity fromCreateDto(MetricCreateRequest request, ProjectEntity project);
}
