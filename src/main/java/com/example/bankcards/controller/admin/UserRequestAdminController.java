package com.example.bankcards.controller.admin;

import com.example.bankcards.dto.response.RequestResponse;
import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.service.UserRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin/user-requests")
@RequiredArgsConstructor
public class UserRequestAdminController {
    private final UserRequestService userRequestService;

    @PostMapping("/{requestId}/approve")
    public UserResponse approveUserRequest(@PathVariable Long requestId) {
        return userRequestService.approveRequest(requestId);
    }

    @PostMapping("/{requestId}/reject")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void rejectUserRequest(@PathVariable Long requestId) {
        userRequestService.rejectRequest(requestId);

    }

    @GetMapping
    public List<RequestResponse> getAllResponses() {
        return userRequestService.getAllRequests();
    }

    @GetMapping("/{requestId}")
    public RequestResponse getResponseById(@PathVariable Long requestId) {
        return userRequestService.getRequestById(requestId);
    }


}
