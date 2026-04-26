package com.example.bankcards.factory.card;

import com.example.bankcards.entity.Card;
import com.example.bankcards.enums.CardStatus;
import com.example.bankcards.factory.user.UserTestFactory;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.LocalDate;

@UtilityClass
public class CardTestFactory {

    public static Card makeCard(Long id) {
        return Card.builder()
                .id(id)
                .cardNumber("123456781234" + String.format("%04d", id))
                .owner(UserTestFactory.makeUser(1L))
                .expirationDate(LocalDate.now().plusYears(3))
                .cardStatus(CardStatus.ACTIVE)
                .balance(BigDecimal.valueOf(400))
                .build();
    }
    public static Card makeCard(Long id, Long balance) {
        return Card.builder()
                .id(id)
                .cardNumber("123456781234" + String.format("%04d", id))
                .owner(UserTestFactory.makeUser(1L))
                .expirationDate(LocalDate.now().plusYears(3))
                .cardStatus(CardStatus.ACTIVE)
                .balance(BigDecimal.valueOf(balance))
                .build();
    }

    public static Card makeCard(Long id, Long balance, Long ownerId) {
        return Card.builder()
                .id(id)
                .cardNumber("123456781234" + String.format("%04d", id))
                .owner(UserTestFactory.makeUser(ownerId))
                .expirationDate(LocalDate.now().plusYears(3))
                .cardStatus(CardStatus.ACTIVE)
                .balance(BigDecimal.valueOf(balance))
                .build();
    }

    public static Card activeCard(Long id) {
        return Card.builder()
                .id(id)
                .cardNumber("123456781234" + String.format("%04d", id))
                .owner(UserTestFactory.makeUser(1L))
                .expirationDate(LocalDate.now().plusYears(3))
                .cardStatus(CardStatus.ACTIVE)
                .balance(BigDecimal.valueOf(1000))
                .build();
    }

    public static Card makeBlockedCard(Long id) {
        return Card.builder()
                .id(id)
                .cardNumber("123456781234" + String.format("%04d", id))
                .owner(UserTestFactory.makeUser(1L))
                .expirationDate(LocalDate.now().plusYears(3))
                .cardStatus(CardStatus.BLOCKED)
                .balance(BigDecimal.valueOf(1000))
                .build();
    }

    public static Card makeExpiredCard(Long id) {
        return Card.builder()
                .id(id)
                .cardNumber("123456781234" + String.format("%04d", id))
                .owner(UserTestFactory.makeUser(1L))
                .expirationDate(LocalDate.now().minusDays(1))
                .cardStatus(CardStatus.EXPIRED)
                .balance(BigDecimal.valueOf(1000))
                .build();
    }
}
