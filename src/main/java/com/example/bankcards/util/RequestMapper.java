package com.example.bankcards.util;

import com.example.bankcards.dto.request.CardCreateRequest;
import com.example.bankcards.dto.request.UserCreateRequest;
import com.example.bankcards.dto.response.RequestResponse;
import com.example.bankcards.entity.CardCreationRequest;
import com.example.bankcards.entity.UserCreationRequest;
import com.example.bankcards.enums.RequestStatus;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class RequestMapper {
    public UserCreationRequest toEntity(UserCreateRequest dto) {
        return UserCreationRequest.builder()
                .name(dto.getName())
                .surname(dto.getSurname())
                .email(dto.getEmail())
                .age(dto.getAge())
                .userRequestStatus(RequestStatus.PENDING)
                .password(dto.getPassword())
                .build();
    }

    public List<RequestResponse> toCardResponseList(List<CardCreationRequest> entities) {
        return entities.stream().map(RequestMapper::toResponse).toList();
    }

    public List<RequestResponse> toUserResponseList(List<UserCreationRequest> entities) {
        return entities.stream().map(RequestMapper::toResponse).toList();
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
