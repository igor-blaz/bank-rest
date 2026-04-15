package com.example.bankcards.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserCreateRequest {
    @NotBlank
    @Size(min = 2, max = 250)
    String name;
    @NotBlank
    @Size(min = 2, max = 250)
    String surname;
    @NotNull
    @Min(18)
    @Max(120)
    Integer age;
}
