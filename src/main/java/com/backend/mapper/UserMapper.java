package com.backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.backend.dto.request.RegisterRequest;
import com.backend.dto.response.UserResponse;
import com.backend.entity.UserInfo;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "name", source = "username")
    @Mapping(target = "roles", constant = "ROLE_USER")
    @Mapping(target = "enabled", constant = "0")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "otp", ignore = true)
    @Mapping(target = "forgotpassword", ignore = true)
    @Mapping(target = "likedItems", ignore = true)
    @Mapping(target = "watchlistItems", ignore = true)
    UserInfo toUser(RegisterRequest register);

    UserResponse toUserResponse(UserInfo user);
}
