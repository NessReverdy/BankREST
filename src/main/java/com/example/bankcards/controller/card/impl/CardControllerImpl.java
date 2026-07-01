package com.example.bankcards.controller.card.impl;

import com.example.bankcards.controller.card.CardController;
import com.example.bankcards.dto.card.CardBalanceResponse;
import com.example.bankcards.dto.card.CardRequest;
import com.example.bankcards.dto.card.CardResponse;
import com.example.bankcards.dto.card.CardSearchFilter;
import com.example.bankcards.service.card.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CardControllerImpl implements CardController {
  private final CardService cardService;

  @Override
  public ResponseEntity<CardResponse> createCard(CardRequest request) {
    return ResponseEntity.ok(cardService.createCard(request));
  }

  @Override
  public ResponseEntity<CardResponse> updateCard(Long id, CardRequest request) {
    return ResponseEntity.ok(cardService.updateCard(id, request));
  }

  @Override
  public ResponseEntity<Void> deleteCardById(Long id) {
    cardService.deleteCardById(id);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<CardResponse> getCardById(Long id) {
    return ResponseEntity.ok(cardService.getCardById(id));
  }

  @Override
  public ResponseEntity<Page<CardResponse>> getAllCards(Pageable pageable) {
    return ResponseEntity.ok(cardService.getAllCards(pageable));
  }

  @Override
  public ResponseEntity<CardBalanceResponse> getCardBalanceById(Long id) {
    return ResponseEntity.ok(cardService.getCardBalanceById(id));
  }

  @Override
  public ResponseEntity<Page<CardResponse>> searchCards(CardSearchFilter cardSearchFilter, Pageable pageable) {
    return ResponseEntity.ok(cardService.searchCards(cardSearchFilter, pageable));
  }
}
