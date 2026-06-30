package com.example.bankcards.security.jwt.service.refresh.impl;

import com.example.bankcards.entity.RefreshTokenEntity;
import com.example.bankcards.exception.impl.TokenNotFoundException;
import com.example.bankcards.security.jwt.repository.RefreshTokenRepository;
import com.example.bankcards.security.jwt.service.refresh.RefreshTokenService;
import com.example.bankcards.util.TokenHash;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
  private static final Logger log = LogManager.getLogger(RefreshTokenServiceImpl.class);

  private final TokenHash hash;
  private final RefreshTokenRepository repo;

  @Value("${jwt.refresh-token-expiration}")
  private long refreshExpiration;

  @Override
  public void save(String refreshToken, Long userId) {
    LocalDateTime now = LocalDateTime.now();

    RefreshTokenEntity token = new RefreshTokenEntity();

    token.setTokenHash(
      hash.hash(refreshToken)
    );

    token.setUserId(userId);
    token.setCreatedAt(now);
    token.setExpiresAt(now.plusSeconds(refreshExpiration / 1000));
    token.setRevoked(false);
    repo.save(token);

    log.info("Saved refresh token for user: {}", token.getUserId());
  }

  @Override
  public RefreshTokenEntity find(String token) {
    String tokenHash = hash.hash(token);
    return repo.findById(tokenHash)
      .orElseThrow(
        () -> new TokenNotFoundException("Token not found")
      );
  }

  @Override
  public void delete(String tokenHash) {
    repo.deleteById(tokenHash);
  }

  @Override
  public void deleteAllByUserId(Long userId) {
    repo.findAllByUserId(userId)
      .forEach(token ->
        repo.deleteById(token.getTokenHash())
      );
  }
}
