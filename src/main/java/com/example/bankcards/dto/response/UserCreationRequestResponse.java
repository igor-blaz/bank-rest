package com.example.bankcards.dto.response;

import com.example.bankcards.enums.UserRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserCreationRequestResponse {
    private Long id;
    private UserRequestStatus status;
}