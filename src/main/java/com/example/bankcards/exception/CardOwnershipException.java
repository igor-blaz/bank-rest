package com.example.bankcards.exception;

public class CardOwnershipException extends RuntimeException {
    public CardOwnershipException(String message) {
        super(message);
    }
}
