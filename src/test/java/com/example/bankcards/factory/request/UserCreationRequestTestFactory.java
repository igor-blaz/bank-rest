package com.example.bankcards.factory.request;

import com.example.bankcards.entity.UserCreationRequest;
import com.example.bankcards.enums.RequestStatus;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserCreationRequestTestFactory {

    public static UserCreationRequest makeRequest() {
        return UserCreationRequest.builder()
                .id(1L)
                .name("Igor")
                .surname("Ivanov")
                .age(23)
                .password("password")
                .userRequestStatus(RequestStatus.PENDING)
                .build();
    }

    public static UserCreationRequest makeRequest(Long id) {
        return UserCreationRequest.builder()
                .id(id)
                .name("Igor")
                .surname("Ivanov")
                .age(23)
                .password("password")
                .userRequestStatus(RequestStatus.PENDING)
                .build();
    }
}