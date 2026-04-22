package com.example.bankcards.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CardCreateRequest {
    Long userId;
}
