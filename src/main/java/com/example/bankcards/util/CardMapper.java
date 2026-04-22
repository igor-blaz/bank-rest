package com.example.bankcards.util;

import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.entity.Card;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@UtilityClass
@Slf4j
public class CardMapper {
    public CardResponse toResponse(Card card){
        return CardResponse.builder()
                .maskedCardNumber(card.getMaskedNumber())
                .expirationDate(card.getExpirationDate())
                .balance(card.getBalance())
                .ownerName(card.getOwner().getName())
                .ownerSurname(card.getOwner().getSurname())
                .cardStatus(card.getCardStatus())
                .build();
    }

    public List<CardResponse> toResponseList(List<Card> cardList) {
        return cardList.stream()
                .map(CardMapper::toResponse)
                .toList();
    }
}
