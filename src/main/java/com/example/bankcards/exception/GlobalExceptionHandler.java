package com.example.bankcards.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
    return ResponseEntity
      .status(e.getStatus())
      .body(new ErrorResponse(
        e.getMessage(),
        List.of(),
        LocalDateTime.now()
        )
      );
  }
}
