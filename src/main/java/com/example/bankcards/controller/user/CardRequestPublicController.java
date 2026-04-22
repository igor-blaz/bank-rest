package com.example.bankcards.controller.user;

import com.example.bankcards.dto.request.CardCreateRequest;
import com.example.bankcards.dto.response.RequestResponse;
import com.example.bankcards.service.CardRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "/public/card-requests")
@RequiredArgsConstructor
public class CardRequestPublicController {

private final CardRequestService cardRequestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestResponse createCardRequest(@Valid @RequestBody CardCreateRequest createRequest) {
        return cardRequestService.createCardRequest(createRequest);
    }
}
