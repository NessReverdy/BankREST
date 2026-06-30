package com.example.bankcards.service.auth;

import com.example.bankcards.dto.auth.AuthRequest;
import com.example.bankcards.dto.auth.AuthResponse;
import com.example.bankcards.dto.user.UserRequest;
import com.example.bankcards.security.jwt.dto.RefreshRequest;

public interface AuthService {
  AuthResponse register(UserRequest userRequest);
  AuthResponse login(AuthRequest authRequest);
  void logout(String refreshToken);
  AuthResponse refresh(RefreshRequest refreshRequest);

}
