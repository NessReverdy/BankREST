package com.example.bankcards.dto.transfer;

import java.math.BigDecimal;

public record TransferRequest(
  Long idFrom,
  Long idTo,
  BigDecimal amount
) {
}
