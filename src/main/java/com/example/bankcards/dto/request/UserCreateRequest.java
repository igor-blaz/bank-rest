package com.example.bankcards.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCreateRequest {
    @NotBlank
    @Size(min = 2, max = 250)
    String name;
    @NotBlank
    @Size(min = 2, max = 250)
    String surname;
    @NotBlank
    @Email
    private String email;
    @NotNull
    @Min(18)
    @Max(120)
    Integer age;
    @NotBlank
    @Size(min = 6, max = 255)
    private String password;
}
