package com.example.bankcards.controller.admin;

import com.example.bankcards.dto.UserCreateRequest;
import com.example.bankcards.dto.UserResponse;
import com.example.bankcards.service.CardService;
import com.example.bankcards.util.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "/admin/cards")
@RequiredArgsConstructor
public class CardController {
    private CardService cardService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse approveRequest(@Valid @RequestBody UserCreateRequest createRequest) {
        return cardService.c(UserMapper.toUserDto(createRequest));
    }

}
