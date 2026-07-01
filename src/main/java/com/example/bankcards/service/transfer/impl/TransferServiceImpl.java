package com.example.bankcards.service.transfer.impl;

import com.example.bankcards.dto.card.CardBalanceResponse;
import com.example.bankcards.dto.transfer.TransferRequest;
import com.example.bankcards.entity.CardEntity;
import com.example.bankcards.entity.UserEntity;
import com.example.bankcards.exception.impl.CardDataException;
import com.example.bankcards.exception.impl.CardNotFoundException;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.service.transfer.TransferService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {
  private static final Logger log = LogManager.getLogger(TransferServiceImpl.class);

  private final CardRepository cardRepo;
  private final CardMapper cardMapper;

  @Override
  @Transactional
  public List<CardBalanceResponse> transfer(TransferRequest request) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserEntity currentUser = (UserEntity) authentication.getPrincipal();

    Long idFrom = request.idFrom();
    Long idTo = request.idTo();
    BigDecimal amount = request.amount();

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

    List<CardBalanceResponse> cards = new ArrayList<>();

    cards.add(cardMapper.toBalanceResponse(cardFrom));
    cards.add(cardMapper.toBalanceResponse(cardTo));

    return cards;
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
