package com.example.bankcards.service.transfer.impl;

import com.example.bankcards.entity.CardEntity;
import com.example.bankcards.entity.UserEntity;
import com.example.bankcards.exception.impl.CardDataException;
import com.example.bankcards.exception.impl.CardNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.transfer.TransferService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {
  private static final Logger log = LogManager.getLogger(TransferServiceImpl.class);

  private final CardRepository cardRepo;

  @Override
  @Transactional
  public boolean transfer(Long idFrom, Long idTo, BigDecimal amount) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserEntity currentUser = (UserEntity) authentication.getPrincipal();

    CardEntity cardFrom = findCardById(idFrom);
    CardEntity cardTo = findCardById(idTo);

    if (!isBothCardOfOneUser(cardFrom, cardTo, currentUser)) {
      throw new CardNotFoundException("You can only transfer between your different own cards");
    }

    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Transfer amount must be positive");
    }

    if (cardFrom.getBalance().compareTo(amount) < 0) {
      throw new CardDataException(
        String.format("Insufficient funds on card: %d. Balance: %s, Required: %s",
          idFrom, cardFrom.getBalance(), amount)
      );
    }

    cardFrom.withdraw(amount);
    cardTo.deposit(amount);

    cardRepo.save(cardFrom);
    cardRepo.save(cardTo);

    log.info("Transfer {} from card {} to card {} by user {}",
      amount, idFrom, idTo, currentUser.getUsername());

    return true;
  }

  private CardEntity findCardById(Long id) {
    return cardRepo.findById(id)
      .orElseThrow(
        () -> new CardNotFoundException("Card not found with id: " + id)
      );
  }

  private boolean isBothCardOfOneUser(CardEntity cardFrom, CardEntity cardTo, UserEntity currentUser) {
    Long idUserFrom = cardFrom.getOwner().getId();
    Long idUserTo = cardTo.getOwner().getId();

    return !idUserFrom.equals(currentUser.getId()) &&
      !idUserTo.equals(currentUser.getId()) &&
      idUserFrom.equals(idUserTo);
  }
}
