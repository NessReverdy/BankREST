package com.example.bankcards.exception;

public record FieldError(
  String field,
  String message
) {
}
