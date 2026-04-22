package com.example.bankcards.dto.response;

import com.example.bankcards.enums.CardStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
public class CardResponse {
    String maskedCardNumber;
    LocalDate expirationDate;
    BigDecimal balance;
    String ownerName;
    String ownerSurname;
    CardStatus cardStatus;
}
