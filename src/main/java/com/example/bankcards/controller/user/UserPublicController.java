package com.example.bankcards.controller.user;

import com.example.bankcards.dto.request.UserCreateRequest;
import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.service.UserService;
import com.example.bankcards.util.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "/public/users")
@RequiredArgsConstructor
public class UserPublicController {
    private final UserService userService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUserRequest(@Valid @RequestBody UserCreateRequest createRequest) {
        return userService.createUser(UserMapper.toUserDto(createRequest));
    }

}
