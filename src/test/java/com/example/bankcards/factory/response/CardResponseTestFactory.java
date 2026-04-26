package com.example.bankcards.factory.response;

import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.enums.CardStatus;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class CardResponseTestFactory {
    public CardResponse makeCardResponse() {
        return new CardResponse(
                "**** **** **** 1234",
                LocalDate.now().plusYears(3),
                BigDecimal.valueOf(1000),
                "Igor",
                "Blazhievsky",
                CardStatus.ACTIVE
        );
    }

    public CardResponse makeBlockedCardResponse() {
        return new CardResponse(
                "**** **** **** 1234",
                LocalDate.now().plusYears(3),
                BigDecimal.valueOf(1000),
                "Igor",
                "Blazhievsky",
                CardStatus.BLOCKED
        );
    }


    public List<CardResponse> makeListCardResponse(int size) {

        List<CardResponse> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(makeCardResponse());
        }
        return list;
    }

    public List<CardResponse> makeListBlockedCards(int size) {
        List<CardResponse> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(makeBlockedCardResponse());
        }
        return list;
    }


}
