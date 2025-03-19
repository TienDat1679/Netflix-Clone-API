package com.backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.backend.dto.request.RegisterRequest;
import com.backend.dto.request.UserUpdateRequest;
import com.backend.dto.response.UserResponse;
import com.backend.entity.UserInfo;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "name", source = "username")
    @Mapping(target = "enabled", constant = "0")
    UserInfo toUser(RegisterRequest register);

    UserResponse toUserResponse(UserInfo user);

    void updateUser(@MappingTarget UserInfo user, UserUpdateRequest register);
}
