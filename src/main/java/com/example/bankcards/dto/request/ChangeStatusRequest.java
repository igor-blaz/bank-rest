package com.example.bankcards.dto.request;

import com.example.bankcards.enums.ChangeCardRequest;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangeStatusRequest {
    private Long userId;
    private Long cardId;
    private ChangeCardRequest changeCardRequest;
}
