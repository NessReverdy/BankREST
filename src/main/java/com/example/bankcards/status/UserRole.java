package com.example.bankcards.status;

public enum UserRole {
  USER,
  ADMIN;

  public boolean isAdmin() {
    return this == ADMIN;
  }

  public boolean isUser() {
    return this == USER;
  }
}
