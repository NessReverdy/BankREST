package com.example.bankcards.service.card.impl;

import com.example.bankcards.dto.card.CardBalanceResponse;
import com.example.bankcards.dto.card.CardRequest;
import com.example.bankcards.dto.card.CardResponse;
import com.example.bankcards.dto.card.CardSearchFilter;
import com.example.bankcards.entity.CardEntity;
import com.example.bankcards.exception.impl.CardNotFoundException;
import com.example.bankcards.mapper.card.CardMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.service.card.CardService;
import com.example.bankcards.status.CardStatus;
import com.example.bankcards.util.CardCreator;
import com.example.bankcards.util.CardSpecification;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
  private static final Logger log = LogManager.getLogger(CardServiceImpl.class);

  private final CardRepository repo;
  private final CardMapper mapper;
  private final CardCreator creator;

  @Override
  @Transactional
  @PreAuthorize("hasRole('ADMIN')")
  public CardResponse createCard(CardRequest card) {
    CardEntity newCard = new CardEntity();

    newCard.setOwner(card.owner());
    newCard.setNumber(creator);
    newCard.setExpiryDate(card.year(), creator);
    newCard.setStatus(CardStatus.ACTIVE);

    repo.save(newCard);
    log.info("Create card with id: {}", newCard.getId());

    return mapper.toResponse(newCard);
  }

  @Override
  @Transactional
  @PreAuthorize("hasRole('ADMIN')")
  public CardResponse updateCard(Long id, CardRequest card) {
    CardEntity oldCard = findCardById(id);

    mapper.updateCard(card, oldCard);
    log.info("Update card with id: {}", id);

    return mapper.toResponse(oldCard);
  }

  @Override
  @Transactional
  @PreAuthorize("hasRole('ADMIN')")
  public boolean deleteCardById(Long id) {
    try {
      repo.deleteById(id);
      log.info("Delete card with id: {}", id);
      return true;
    } catch (CardNotFoundException e) {
      throw new CardNotFoundException("Card with id: " + id + " not found for deleting.");
    }
  }

  @Override
  public CardResponse getCardById(Long id) {
    return mapper.toResponse(findCardById(id));
  }

  @Override
  public Page<CardResponse> getAllCards(Pageable pageable) {
    Page<CardEntity> cardPage = repo.findAll(pageable);
    return cardPage.map(mapper::toResponse);
  }

  @Override
  public CardResponse blockCardById(Long id) {
    CardEntity card = findCardById(id);

    card.setStatus(CardStatus.BLOCKED);
    repo.save(card);
    log.info("Block card with id: {}", id);

    return mapper.toResponse(card);
  }

  @Override
  public CardResponse activateCardById(Long id) {
    CardEntity card = findCardById(id);

    card.setStatus(CardStatus.ACTIVE);
    repo.save(card);
    log.info("Activate card with id: {}", id);

    return mapper.toResponse(card);
  }

  @Override
  public CardBalanceResponse getCardBalanceById(Long id) {
    CardEntity card = findCardById(id);
    return mapper.toBalanceResponse(card);
  }

  @Override
  public Page<CardResponse> searchCards(CardSearchFilter filter, Pageable pageable) {
    log.info("Searching cards with filter: {}, pageable: {}", filter, pageable);
    Specification<CardEntity> spec = CardSpecification.filterBy(filter);

    return repo
      .findAll(spec, pageable)
      .map(mapper::toResponse);
  }

  private CardEntity findCardById(Long id) {
    return repo.findById(id).orElseThrow(
      () -> new CardNotFoundException("Card with id: " + id + " not found.")
    );
  }
}
