package com.example.bankcards.security.jwt.service.refresh;

import com.example.bankcards.entity.RefreshTokenEntity;

public interface RefreshTokenService {
  void save(String refreshToken, Long userId);
  RefreshTokenEntity find(String tokenHash);
  void delete(String tokenHash);
  void deleteAllByUserId(Long userId);
}