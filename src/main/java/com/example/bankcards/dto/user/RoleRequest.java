package com.example.bankcards.dto.user;

import com.example.bankcards.status.UserRole;

public record RoleRequest(
  UserRole role
) {
}
