package com.example.bankcards.util;

import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.UserCreationRequest;
import com.example.bankcards.enums.Role;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@UtilityClass
@Slf4j
public class UserMapper {
    public User toEntity(UserCreationRequest request) {
        return User.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .age(request.getAge())
                .email(request.getEmail())
                .role(Role.USER)
                .password(request.getPassword())
                .build();
    }

    public List<UserResponse> toResponseList(List<User> entities) {
        return entities.stream().map(UserMapper::toResponse).toList();
    }

    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .age(user.getAge())
                .role(user.getRole())
                .build();
    }


}
