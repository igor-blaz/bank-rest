package com.example.bankcards.controller.admin;

import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.service.CardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin/cards")
@RequiredArgsConstructor
public class CardAdminController {
    private CardService cardService;


    @GetMapping
    public List<CardResponse> getAllCards() {
        return cardService.getAllCards();
    }

    @GetMapping("/{cardId}")
    public CardResponse getCardById(@PathVariable Long cardId) {
        return cardService.getCardById(cardId);
    }

    @PatchMapping("/{cardId}/block")
    public CardResponse blockCardByCardId(@PathVariable Long cardId){
        return cardService.blockCardByCardId(cardId);
    }

    @PatchMapping("/by-user/{userId}/block")
    public List<CardResponse> blockAllUsersCards(@PathVariable Long userId){
        return cardService.blockAllUsersCard(userId);
    }


}
