package com.example.bankcards.security.jwt.service.jwt.impl;

import com.example.bankcards.entity.UserEntity;
import com.example.bankcards.security.jwt.service.jwt.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {
  private final String secret;

  @Value("${jwt.access-token-expiration}")
  private long accessExpiration;

  @Value("${jwt.refresh-token-expiration}")
  private long refreshExpiration;

  public JwtServiceImpl(
    @Value("${jwt.secret}")
    String secret) {
    this.secret = secret;
  }

  private SecretKey getSigningKey() {
    return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  @Override
  public String generateAccessToken(UserEntity user) {
    return buildToken(user, accessExpiration);
  }

  @Override
  public String generateRefreshToken(UserEntity user) {
    return buildToken(user, refreshExpiration);
  }

  @Override
  public String extractUsername(String token) {
    return extractAllClaims(token).getSubject();
  }

  @Override
  public Claims extractAllClaims(String token) {
    return Jwts.parser()
      .verifyWith(getSigningKey())
      .build()
      .parseSignedClaims(token)
      .getPayload();
  }

  @Override
  public boolean isTokenExpired(String token) {
    return extractAllClaims(token)
      .getExpiration()
      .before(new Date());
  }

  @Override
  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return username.equals(userDetails.getUsername())
      && !isTokenExpired(token);
  }


  private String buildToken(UserEntity user, long expiration){
    return Jwts.builder()
      .subject(user.getUsername())
      .claim("id", user.getId())
      .claim("role", user.getRole().name())
      .claim("username", user.getUsername())
      .issuedAt(new Date())
      .expiration(new Date(System.currentTimeMillis() + expiration))
      .signWith(getSigningKey())
      .compact();
  }
}
