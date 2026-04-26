package com.example.bankcards.factory.request;

import com.example.bankcards.entity.CardCreationRequest;
import com.example.bankcards.enums.RequestStatus;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CardCreationRequestTestFactory {

    public static CardCreationRequest makeRequest(Long id) {
        return CardCreationRequest.builder()
                .id(id)
                .userId(1L)
                .cardRequestStatus(RequestStatus.PENDING)
                .build();
    }

    public static CardCreationRequest pendingRequest(Long id) {
        return CardCreationRequest.builder()
                .id(id)
                .userId(1L)
                .cardRequestStatus(RequestStatus.PENDING)
                .build();
    }

    public static CardCreationRequest approvedRequest(Long id) {
        return CardCreationRequest.builder()
                .id(id)
                .userId(1L)
                .cardRequestStatus(RequestStatus.APPROVED)
                .build();
    }

    public static CardCreationRequest rejectedRequest(Long id) {
        return CardCreationRequest.builder()
                .id(id)
                .userId(1L)
                .cardRequestStatus(RequestStatus.REJECTED)
                .build();
    }
}
