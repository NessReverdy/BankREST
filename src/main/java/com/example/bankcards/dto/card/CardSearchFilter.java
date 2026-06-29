package com.example.bankcards.dto.card;

import com.example.bankcards.status.CardStatus;

import java.math.BigDecimal;
import java.time.YearMonth;

public record CardSearchFilter(
  CardStatus status,
  BigDecimal balanceMin,
  BigDecimal balanceMax,
  YearMonth expireDateFrom,
  YearMonth expireDateTo,
  String number
) {
}
