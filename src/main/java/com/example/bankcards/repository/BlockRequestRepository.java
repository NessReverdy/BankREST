package com.example.bankcards.repository;

import com.example.bankcards.dto.block.BlockResponse;
import com.example.bankcards.entity.BlockEntity;
import com.example.bankcards.entity.CardEntity;
import com.example.bankcards.entity.UserEntity;
import com.example.bankcards.status.CardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BlockRequestRepository extends JpaRepository<BlockEntity, Long> {
  Page<BlockResponse> findByStatus(CardStatus status, Pageable pageable);
  List<BlockResponse> findByCard(CardEntity card);
  Page<BlockResponse> findByUser(UserEntity user, Pageable pageable);
  Optional<BlockResponse> findByCardAndStatus(CardEntity card, CardStatus status);
  boolean existsByCardAndStatus(CardEntity card, CardStatus status);
  @Query("SELECT r FROM BlockEntity r WHERE r.status = :status ORDER BY r.createdAt ASC")
  List<BlockResponse> findPendingRequests(@Param("status") CardStatus status);
}
