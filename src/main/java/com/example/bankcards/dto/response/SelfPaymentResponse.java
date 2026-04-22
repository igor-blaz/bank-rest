package com.example.bankcards.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SelfPaymentResponse {
        private Long transactionId;
        private BigDecimal amount;
        private Long senderCardId;
        private Long receiverCardId;
        private LocalDateTime timestamp;
        private String status;
}
