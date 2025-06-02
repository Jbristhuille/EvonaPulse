package com.evonapulse.backend.mappers;

import com.evonapulse.backend.dtos.MetricPublicResponse;
import com.evonapulse.backend.entities.MetricEntity;

public interface MetricMapper {
    MetricPublicResponse toPublicResponse(MetricEntity metric);
}
