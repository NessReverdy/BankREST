package com.example.bankcards.entity;

import com.example.bankcards.status.CardStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "block_requests")
@Getter
@NoArgsConstructor
public class BlockEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "card_id", nullable = false)
  private CardEntity card;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user;

  @Column(nullable = false)
  private String reason;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  @Setter
  private CardStatus status;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "processed_at")
  @Setter
  private LocalDateTime processedAt;

  @Column(name = "admin_comment")
  @Setter
  private String adminComment;

  public BlockEntity(CardEntity card, UserEntity user, String reason) {
    this.card = card;
    this.user = user;
    this.reason = reason;
    this.status = CardStatus.PENDING;
    this.createdAt = LocalDateTime.now();
  }
}
