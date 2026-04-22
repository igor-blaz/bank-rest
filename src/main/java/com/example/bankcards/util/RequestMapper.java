package com.example.bankcards.util;

import com.example.bankcards.dto.request.CardCreateRequest;
import com.example.bankcards.dto.request.UserCreateRequest;
import com.example.bankcards.dto.response.RequestResponse;
import com.example.bankcards.entity.CardCreationRequest;
import com.example.bankcards.entity.UserCreationRequest;
import com.example.bankcards.enums.RequestStatus;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RequestMapper {
    public UserCreationRequest toEntity(UserCreateRequest dto) {
        return UserCreationRequest.builder()
                .name(dto.getName())
                .surname(dto.getSurname())
                .age(dto.getAge())
                .password(dto.getPassword())
                .build();
    }

    public RequestResponse toResponse(UserCreationRequest entity) {
        return RequestResponse.builder()
                .id(entity.getId())
                .status(entity.getUserRequestStatus())
                .build();
    }

    public CardCreationRequest toEntity(CardCreateRequest dto) {
        return CardCreationRequest.builder()
                .userId(dto.getUserId())
                .cardRequestStatus(RequestStatus.PENDING)
                .build();
    }

    public RequestResponse toResponse(CardCreationRequest entity) {
        return RequestResponse.builder()
                .id(entity.getId())
                .status(entity.getCardRequestStatus())
                .build();
    }
}
