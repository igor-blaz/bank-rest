package com.example.bankcards.dto.response;

import com.example.bankcards.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class RequestResponse {
    private Long id;
    private RequestStatus status;
}