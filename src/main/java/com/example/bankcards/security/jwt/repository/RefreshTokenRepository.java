package com.example.bankcards.security.jwt.repository;

import com.example.bankcards.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, String> {
  List<RefreshTokenEntity> findAllByUserId(Long userId);
}
