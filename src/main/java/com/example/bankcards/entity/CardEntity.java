package com.example.bankcards.entity;

import com.example.bankcards.exception.impl.CardDataException;
import com.example.bankcards.status.CardStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "cards")
public class CardEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity owner;

  @Column(
    unique = true,
    nullable = false,
    length = 16
  )
  private String number;

  @Column(nullable = false)
  private LocalDate expiryDate;

  @Setter
  @Enumerated(EnumType.STRING)
  private CardStatus status;

  @Column(
    nullable = false,
    precision = 14,
    scale = 2
  )
  private BigDecimal balance;

  public void deposit(BigDecimal amount) {
    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new CardDataException("Amount must be positive");
    }

    balance = balance.add(amount);
  }

  public void withdraw(BigDecimal amount) {
    if (amount.compareTo(balance) > 0) {
      throw new CardDataException("Insufficient funds");
    }

    balance = balance.subtract(amount);
  }
}
