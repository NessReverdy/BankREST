package com.example.bankcards.dto.card;

import com.example.bankcards.entity.UserEntity;

public record CardRequest(
  UserEntity owner,
  int year
) {
}
