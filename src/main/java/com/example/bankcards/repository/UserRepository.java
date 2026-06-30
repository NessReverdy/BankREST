package com.example.bankcards.repository;

import com.example.bankcards.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
  Optional<UserEntity> findByUsername(String username);
  @Query("SELECT COUNT(u) > 0 FROM UserEntity u WHERE u.username = :username")
  boolean existsByUsername(@Param("username") String username);
}