package com.example.bankcards.dto.card;

import com.example.bankcards.entity.UserEntity;

import java.math.BigDecimal;

public record CardBalanceResponse(
  Long id,
  UserEntity owner,
  String number,
  BigDecimal balance
) {
}
