package com.example.bankcards.dto.user;

public record UserRequest(
  String firstName,
  String middleName,
  String lastName,
  String username,
  String password
) {
}
