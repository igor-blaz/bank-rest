package com.example.bankcards.controller.user;

import com.example.bankcards.dto.request.UserCreateRequest;
import com.example.bankcards.dto.response.RequestResponse;
import com.example.bankcards.service.UserRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "/public/user-requests")
@RequiredArgsConstructor
public class UserRequestPublicController {
    private final UserRequestService userRequestService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestResponse createUserRequest(@Valid @RequestBody UserCreateRequest createRequest) {
        return userRequestService.createRequest(createRequest);
    }

}
