package com.example.bankcards.util;

import com.example.bankcards.dto.request.UserCreateRequest;
import com.example.bankcards.dto.response.UserCreationRequestResponse;
import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.entity.UserCreationRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserRequestMapper {
    public UserCreationRequest toEntity(UserCreateRequest dto) {
        return UserCreationRequest.builder()
                .name(dto.getName())
                .surname(dto.getSurname())
                .age(dto.getAge())
                .password(dto.getPassword())
                .build();
    }

    public UserCreationRequestResponse toResponse(UserCreationRequest entity) {
        return UserCreationRequestResponse.builder()
                .id(entity.getId())
                .status(entity.getUserRequestStatus())
                .build();
    }
}
