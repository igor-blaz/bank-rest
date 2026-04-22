package com.example.bankcards.util;

import com.example.bankcards.dto.request.UserCreateRequest;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.entity.User;
import com.example.bankcards.enums.Role;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@UtilityClass
@Slf4j
public class UserMapper {
    public static UserDto toUserDto(UserCreateRequest request){
        return UserDto.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .age(request.getAge())
                .role(Role.USER)
                .build();

    }
    public List<UserResponse> toResponseList(List<User> entities){
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
    public User toEntity(UserDto dto) {
        return User.builder()
                .name(dto.getName())
                .surname(dto.getSurname())
                .age(dto.getAge())
                .role(dto.getRole())
                .build();
    }

}
