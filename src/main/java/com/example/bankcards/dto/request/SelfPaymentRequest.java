package com.example.bankcards.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SelfPaymentRequest {
    @NotNull
    Long senderCardId;
    @NotNull
    Long receiverCardId;
    @NotNull
    @Positive
    BigDecimal transferAmount;
}
