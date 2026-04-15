package com.example.bankcards.dto;

import com.example.bankcards.enums.Role;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    String name;
    String surname;
    Integer age;
    Role role;
}
