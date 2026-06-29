package com.example.bankcards.repository;

import com.example.bankcards.entity.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends
  JpaRepository<CardEntity, Long>,
  JpaSpecificationExecutor<CardEntity> {
  @Query("SELECT COUNT(c) > 0 FROM CardEntity c WHERE c.number = :cardNumber")
  boolean existsByNumber(@Param("cardNumber") String cardNumber);
}
