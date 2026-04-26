package com.example.bankcards.controller.admin;

import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.dto.response.RequestResponse;
import com.example.bankcards.service.CardRequestService;
import com.example.bankcards.service.CardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin/card-requests")
@RequiredArgsConstructor
public class CardRequestAdminController {

    private final CardRequestService cardRequestService;
    private final CardService cardService;

    @PostMapping("/{requestId}/approve")
    @ResponseStatus(HttpStatus.OK)
    public CardResponse approveUserRequest(@PathVariable Long requestId) {
        cardRequestService.approveRequest(requestId);
        return cardService.createCard(requestId);
    }

    @PostMapping("/{requestId}/reject")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void rejectUserRequest(@PathVariable Long requestId) {
        cardRequestService.rejectRequest(requestId);
    }

    @GetMapping
    public List<RequestResponse> getAllRequests() {
        return cardRequestService.getAllRequests();
    }

    @GetMapping("/{requestId}")
    public RequestResponse getRequestById(@PathVariable Long requestId) {
        return cardRequestService.getRequestById(requestId);
    }
}
