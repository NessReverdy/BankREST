package com.example.bankcards.dto.user;

import com.example.bankcards.status.UserRole;

public record UserResponse(
  Long id,
  String firstName,
  String middleName,
  String lastName,
  String username,
  UserRole role
) {
}
