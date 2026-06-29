package com.example.bankcards.dto.block;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BlockRequest(
  @NotNull
  Long cardId,
  @NotBlank
  String reason
) {
}
