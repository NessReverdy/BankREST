package com.example.bankcards.dto.block;

import com.example.bankcards.status.CardStatus;
import jakarta.validation.constraints.NotNull;

public record BlockProcess(
  @NotNull
  CardStatus decision,
  String adminComment
) {
}
