package com.example.bankcards.factory.response;

import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.enums.Role;
import lombok.experimental.UtilityClass;


@UtilityClass
public class UserResponseTestFactory {

    public static UserResponse makeUserResponse(Long id) {
        return UserResponse.builder()
                .id(id)
                .name("Igor")
                .surname("Blazhievsky")
                .age(23)
                .role(Role.USER)
                .build();
    }
}