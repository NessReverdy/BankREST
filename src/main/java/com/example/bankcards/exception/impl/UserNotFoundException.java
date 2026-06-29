package com.example.bankcards.exception.impl;

import com.example.bankcards.exception.CustomException;
import lombok.experimental.StandardException;
import org.springframework.http.HttpStatus;

@StandardException
public class UserNotFoundException extends CustomException {
  @Override
  public HttpStatus getStatus() {
    return HttpStatus.NOT_FOUND;
  }
}
