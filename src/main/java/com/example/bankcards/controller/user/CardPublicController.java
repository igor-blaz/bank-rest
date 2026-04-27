package com.example.bankcards.controller.user;

import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.security.CustomUserDetails;
import com.example.bankcards.service.CardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/public/cards")
@RequiredArgsConstructor
public class CardPublicController {
    private final CardService cardService;

    @GetMapping
    public List<CardResponse> getMyCards() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        Long userId = userDetails.getId();
        return cardService.getUserCards(userId);
    }

    @GetMapping("/{cardId}")
    public CardResponse getMyCardById(@PathVariable Long cardId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        Long userId = userDetails.getId();
        return cardService.getUserCardsByCardId(userId, cardId);
    }
}
