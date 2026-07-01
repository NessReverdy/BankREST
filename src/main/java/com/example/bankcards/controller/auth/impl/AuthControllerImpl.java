package com.example.bankcards.controller.auth.impl;

import com.example.bankcards.controller.auth.AuthController;
import com.example.bankcards.dto.auth.AuthRequest;
import com.example.bankcards.dto.auth.AuthResponse;
import com.example.bankcards.dto.user.UserRequest;
import com.example.bankcards.security.jwt.dto.RefreshTokenRequest;
import com.example.bankcards.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {
  private final AuthService authService;

  @Override
  public ResponseEntity<AuthResponse> register(UserRequest userRequest) {
    return ResponseEntity.ok(authService.register(userRequest));
  }

  @Override
  public ResponseEntity<AuthResponse> login(AuthRequest authRequest) {
    return ResponseEntity.ok(authService.login(authRequest));
  }

  @Override
  public ResponseEntity<Void> logout(RefreshTokenRequest refreshTokenRequest) {
    authService.logout(refreshTokenRequest);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<AuthResponse> refresh(RefreshTokenRequest refreshTokenRequest) {
    return ResponseEntity.ok(authService.refresh(refreshTokenRequest));
  }
}
