package com.example.bankcards.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InsufficientFundsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInsufficientFunds(InsufficientFundsException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(CardBlockedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleBlocked(CardBlockedException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(CardExpiredException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleExpired(CardExpiredException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(CardOwnershipException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleOwnership(CardOwnershipException ex) {
        return ex.getMessage();
    }
}