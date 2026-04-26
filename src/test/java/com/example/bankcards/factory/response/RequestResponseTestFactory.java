package com.example.bankcards.factory.response;

import com.example.bankcards.dto.response.RequestResponse;
import com.example.bankcards.enums.RequestStatus;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class RequestResponseTestFactory {
    public RequestResponse makeRequestResponse() {
        return RequestResponse.builder()
                .id(7L)
                .status(RequestStatus.APPROVED)
                .build();
    }

    public RequestResponse makeRequestResponse(Long id) {
        return RequestResponse.builder()
                .id(id)
                .status(RequestStatus.APPROVED)
                .build();
    }

    public List<RequestResponse> makeListRequestResponse(int size) {
        List<RequestResponse> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(makeRequestResponse());
        }
        return list;
    }
}
