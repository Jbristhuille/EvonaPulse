package com.evonapulse.backend.mappers;

import com.evonapulse.backend.dtos.MetricCreateRequest;
import com.evonapulse.backend.dtos.MetricPublicResponse;
import com.evonapulse.backend.entities.MetricEntity;
import com.evonapulse.backend.entities.ProjectEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MetricMapper {
    MetricPublicResponse toPublicResponse(MetricEntity metric);

    @Mapping(target = "project", source = "project")
    @Mapping(target = "label", source = "request.label")
    @Mapping(target = "name", source = "request.name")
    @Mapping(target = "type", source = "request.type")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    MetricEntity fromCreateDto(MetricCreateRequest request, ProjectEntity project);
}
