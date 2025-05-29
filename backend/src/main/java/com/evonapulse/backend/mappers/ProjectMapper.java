package com.evonapulse.backend.mappers;

import com.evonapulse.backend.dtos.ProjectPublicResponse;
import com.evonapulse.backend.entities.ProjectEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    ProjectPublicResponse toProjectPublicResponse(ProjectEntity project);
}
