package com.evonapulse.backend.mappers;

import com.evonapulse.backend.dtos.LogPublicResponse;
import com.evonapulse.backend.entities.LogEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LogMapper {
    LogPublicResponse toPublicResponse(LogEntity log);
}
