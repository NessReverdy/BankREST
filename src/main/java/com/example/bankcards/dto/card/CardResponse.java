package com.example.bankcards.dto.card;

import com.example.bankcards.entity.UserEntity;
import com.example.bankcards.status.CardStatus;

import java.time.YearMonth;

public record CardResponse(
  UserEntity owner,
  String number,
  YearMonth expiryAt,
  CardStatus status
) {
}
