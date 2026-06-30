package com.example.bankcards.security.jwt.service.jwt;

import com.example.bankcards.entity.UserEntity;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
  String generateAccessToken(UserEntity user);
  String generateRefreshToken(UserEntity user);
  String extractUsername(String token);
  Claims extractAllClaims(String token);
  boolean isTokenExpired(String token);
  boolean isTokenValid(String token, UserDetails userDetails);
}
