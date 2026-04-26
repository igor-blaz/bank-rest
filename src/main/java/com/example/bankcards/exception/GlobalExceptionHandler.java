package com.example.bankcards.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InsufficientFundsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInsufficientFunds(InsufficientFundsException ex) {
        log.warn("Insufficient funds: {}", ex.getMessage());
        return ex.getMessage();
    }
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleBadCredentials(BadCredentialsException ex) {
        log.warn("Authentication failed. Incorrect Password: {}", ex.getMessage());
        return "Invalid username or password";
    }

    @ExceptionHandler(CardBlockedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleBlocked(CardBlockedException ex) {
        log.warn("Attempt to use blocked card: {}", ex.getMessage());
        return ex.getMessage();
    }

    @ExceptionHandler(SameCardTransferException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String sameCardTransfer(SameCardTransferException ex) {
        log.warn("Cannot transfer to the same card: {}", ex.getMessage());
        return ex.getMessage();
    }

    @ExceptionHandler(CardExpiredException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleExpired(CardExpiredException ex) {
        log.warn("Attempt to use expired card: {}", ex.getMessage());
        return ex.getMessage();
    }

    @ExceptionHandler(CardOwnershipException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleOwnership(CardOwnershipException ex) {
        log.warn("Card ownership violation: {}", ex.getMessage());
        return ex.getMessage();
    }

    @ExceptionHandler(InvalidRequestStatusException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBadRequestStatus(InvalidRequestStatusException ex) {
        log.warn("Invalid request status transition: {}", ex.getMessage());
        return ex.getMessage();
    }
}


