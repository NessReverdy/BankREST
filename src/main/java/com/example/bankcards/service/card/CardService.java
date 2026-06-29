package com.example.bankcards.service.card;

import com.example.bankcards.dto.card.CardBalanceResponse;
import com.example.bankcards.dto.card.CardRequest;
import com.example.bankcards.dto.card.CardResponse;
import com.example.bankcards.dto.card.CardSearchFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CardService {
  CardResponse createCard(CardRequest card);
  CardResponse updateCard(Long id, CardRequest card);
  boolean deleteCardById(Long id);
  CardResponse getCardById(Long id);
  Page<CardResponse> getAllCards(Pageable pageable);

  CardResponse blockCardById(Long id);
  CardResponse activateCardById(Long id);
  CardBalanceResponse getCardBalanceById (Long id);
  Page<CardResponse> searchCards(CardSearchFilter filter, Pageable pageable);
}
