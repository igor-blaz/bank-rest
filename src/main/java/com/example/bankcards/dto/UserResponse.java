package com.example.bankcards.dto;

import com.example.bankcards.enums.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    Long id;
    String name;
    String surname;
    Integer age;
    Role role;
}
