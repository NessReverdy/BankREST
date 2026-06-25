package com.example.bankcards.exception;

import org.springframework.http.HttpStatus;

public abstract class CustomException extends RuntimeException {

  protected CustomException(String message) {
    super(message);
  }

  protected CustomException(String message, Throwable cause) {
    super(message, cause);
  }

  protected CustomException(Throwable cause) {
    super(cause);
  }

  public abstract HttpStatus getStatus();
}
