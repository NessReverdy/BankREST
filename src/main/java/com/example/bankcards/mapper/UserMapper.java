package com.example.bankcards.mapper;

import com.example.bankcards.dto.user.UserRequest;
import com.example.bankcards.dto.user.UserResponse;
import com.example.bankcards.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
  componentModel = "spring",
  nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
  unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {
  UserResponse toResponse(UserEntity user);
  UserEntity toEntity(UserResponse userResponse);
  UserEntity toEntity(UserRequest userRequest);
  void updateUser(UserRequest userRequest, @MappingTarget UserEntity entity);
}
