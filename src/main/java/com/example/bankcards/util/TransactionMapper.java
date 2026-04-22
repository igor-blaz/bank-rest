package com.example.bankcards.util;

import com.example.bankcards.dto.request.SelfPaymentRequest;
import com.example.bankcards.entity.Transaction;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class TransactionMapper {
    public Transaction createTransactionEntity(SelfPaymentRequest request) {
        return Transaction.builder()
                .senderCardId(request.getSenderCardId())
                .receiverCardId(request.getReceiverCardId())
                .amount(request.getTransferAmount())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
