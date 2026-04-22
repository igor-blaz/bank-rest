package com.example.bankcards.service;

import com.example.bankcards.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
@RequiredArgsConstructor
public class CardNumberGenerator {

    private final CardRepository cardRepository;
    private static final long LOWER_BOUND = 1000_0000_0000_0000L;
    private static final long UPPER_BOUND = 9999_9999_9999_9999L;

    public String makeNumber() {
        SecureRandom random = new SecureRandom();
        String cardNumber;

        do {
            long number = random.nextLong(LOWER_BOUND, UPPER_BOUND);
            cardNumber = String.valueOf(number);
        } while (cardRepository.existsByCardNumber(cardNumber));

        return cardNumber;
    }
}
