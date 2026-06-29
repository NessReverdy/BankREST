package com.example.bankcards.dto.block;

import com.example.bankcards.status.CardStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BlockResponse(
  Long id,
  Long cardId,
  String cardNumber,
  Long userId,
  String reason,
  CardStatus status,
  LocalDateTime createdAt,
  LocalDateTime processedAt,
  String adminComment
) {
}
