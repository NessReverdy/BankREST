package com.example.bankcards.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
  String message,
  List<FieldError> errors,
  LocalDateTime timestamp
) {
}
