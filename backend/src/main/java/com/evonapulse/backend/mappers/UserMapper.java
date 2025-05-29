package com.evonapulse.backend.mappers;

import com.evonapulse.backend.dtos.UserAuthResponse;
import com.evonapulse.backend.dtos.UserPublicResponse;
import com.evonapulse.backend.entities.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserPublicResponse toUserPublicResponse(UserEntity user);
    UserAuthResponse toUserAuthResponse(UserEntity user);
}
