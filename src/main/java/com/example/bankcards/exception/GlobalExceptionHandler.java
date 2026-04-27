package com.example.bankcards.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InsufficientFundsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInsufficientFunds(InsufficientFundsException ex) {
        log.warn("Insufficient funds: {}", ex.getMessage());

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Insufficient funds",
                ex.getMessage()
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleBadCredentials(BadCredentialsException ex) {
        log.warn("Authentication failed: {}", ex.getMessage());

        return buildErrorResponse(
                HttpStatus.UNAUTHORIZED,
                "Authentication failed",
                "Invalid username or password"
        );
    }

    @ExceptionHandler(CardBlockedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleBlocked(CardBlockedException ex) {
        log.warn("Attempt to use blocked card: {}", ex.getMessage());

        return buildErrorResponse(
                HttpStatus.FORBIDDEN,
                "Card blocked",
                ex.getMessage()
        );
    }

    @ExceptionHandler(SameCardTransferException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse sameCardTransfer(SameCardTransferException ex) {
        log.warn("Cannot transfer to the same card: {}", ex.getMessage());

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Same card transfer",
                ex.getMessage()
        );
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFound(NotFoundException ex) {
        log.warn("Required information not found: {}", ex.getMessage());

        return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                "Not found",
                ex.getMessage()
        );
    }

    @ExceptionHandler(CardExpiredException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleExpired(CardExpiredException ex) {
        log.warn("Attempt to use expired card: {}", ex.getMessage());

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Card expired",
                ex.getMessage()
        );
    }

    @ExceptionHandler(CardOwnershipException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleOwnership(CardOwnershipException ex) {
        log.warn("Card ownership violation: {}", ex.getMessage());

        return buildErrorResponse(
                HttpStatus.FORBIDDEN,
                "Card ownership violation",
                ex.getMessage()
        );
    }

    @ExceptionHandler(InvalidRequestStatusException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestStatus(InvalidRequestStatusException ex) {
        log.warn("Invalid request status transition: {}", ex.getMessage());

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Invalid request status",
                ex.getMessage()
        );
    }

    private ErrorResponse buildErrorResponse(HttpStatus status, String reason, String message) {
        return ErrorResponse.builder()
                .status(String.valueOf(status.value()))
                .reason(reason)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}


