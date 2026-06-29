package com.example.bankcards.service.block.impl;

import com.example.bankcards.dto.block.BlockProcess;
import com.example.bankcards.dto.block.BlockRequest;
import com.example.bankcards.dto.block.BlockResponse;
import com.example.bankcards.entity.BlockEntity;
import com.example.bankcards.entity.CardEntity;
import com.example.bankcards.entity.UserEntity;
import com.example.bankcards.exception.impl.*;
import com.example.bankcards.mapper.card.BlockMapper;
import com.example.bankcards.repository.BlockRequestRepository;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.block.BlockRequestService;
import com.example.bankcards.status.CardStatus;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BlockRequestServiceImpl implements BlockRequestService {
  private static final Logger log = LogManager.getLogger(BlockRequestServiceImpl.class);

  private final BlockRequestRepository blockRepo;
  private final CardRepository cardRepo;
  private final UserRepository userRepo;
  private final BlockMapper mapper;

  @Override
  @Transactional
  public BlockResponse createBlockRequest(Long userId, BlockRequest request) {
    CardEntity card = findCardById(request.cardId());
    validateBlockRequest(card, userId);
    UserEntity user = findUserById(userId);

    BlockEntity blockRequest = new BlockEntity(card, user, request.reason());
    blockRepo.save(blockRequest);

    card.setStatus(CardStatus.BLOCK_REQUESTED);
    cardRepo.save(card);
    log.info("Block request created with id: {}", blockRequest.getId());

    return mapper.toResponse(blockRequest);
  }

  @Override
  @Transactional
  public BlockResponse processBlockRequest(Long requestId, BlockProcess request) {
    BlockEntity block = findBlockById(requestId);

    if (block.getStatus() != CardStatus.PENDING) {
      throw new CardDataException("Request already processed");
    }
    CardEntity card = block.getCard();

    if (request.decision() == CardStatus.APPROVED) {
      card.setStatus(CardStatus.BLOCKED);
      block.setStatus(CardStatus.APPROVED);
      log.info("Card {} blocked by admin", card.getNumber());
    } else if (request.decision() == CardStatus.REJECTED) {
      card.setStatus(CardStatus.ACTIVE);
      block.setStatus(CardStatus.REJECTED);
      log.info("Block request rejected for card {}", card.getNumber());
    } else {
      throw new IllegalArgumentException("Invalid decision: " + request.decision());
    }

    block.setProcessedAt(LocalDateTime.now());
    block.setAdminComment(request.adminComment());

    cardRepo.save(card);
    blockRepo.save(block);

    return mapper.toResponse(block);
  }

  @Override
  public Page<BlockResponse> getAllRequests(Pageable pageable) {
    log.info("Getting all block requests");
    return blockRepo.findAll(pageable)
      .map(mapper::toResponse);
  }

  @Override
  public Page<BlockResponse> getUserRequests(Long userId, Pageable pageable) {
    UserEntity user = findUserById(userId);
    return blockRepo.findByUser(user, pageable);
  }

  @Override
  public BlockResponse getRequestById(Long requestId) {
    return mapper.toResponse(blockRepo.findById(requestId)
      .orElseThrow(
        () -> new BlockResponseNotFoundException("Block request with id: " + requestId + " not found")
      )
    );
  }

  @Override
  public void cancelRequest(Long requestId, Long userId) {
    BlockEntity block = findBlockById(requestId);

    if (!block.getUser().getId().equals(userId)) {
      throw new UserNotAuthorizedException("You can only cancel your own requests");
    }
    if (block.getStatus() != CardStatus.PENDING) {
      throw new CardDataException("Cannot cancel processed request");
    }

    block.setStatus(CardStatus.CANCELLED);
    block.setProcessedAt(LocalDateTime.now());
    block.setAdminComment("Cancelled by user");

    CardEntity card = block.getCard();
    card.setStatus(CardStatus.ACTIVE);
    cardRepo.save(card);

    blockRepo.save(block);
    log.info("Request {} cancelled successfully", requestId);
  }

  private CardEntity findCardById(Long id) {
    return cardRepo.findById(id).orElseThrow(
      () -> new CardNotFoundException("Card with id: " + id + " not found.")
    );
  }

  private UserEntity findUserById(Long id) {
    return userRepo.findById(id).orElseThrow(
      () -> new UserNotFoundException("User not found.")
    );
  }

  private BlockEntity findBlockById(Long id) {
    return blockRepo.findById(id).orElseThrow(
      () -> new BlockResponseNotFoundException("Block response not found.")
    );
  }

  private void validateBlockRequest(CardEntity card, Long userId) {
    if (!card.getOwner().getId().equals(userId)) {
      throw new UserNotAuthorizedException("You can only block your own cards");
    }

    if (card.getStatus() == CardStatus.BLOCKED) {
      throw new CardDataException("Card is already blocked");
    }

    if (card.getStatus() == CardStatus.EXPIRED) {
      throw new CardDataException("Cannot block expired card");
    }

    if (blockRepo.existsByCardAndStatus(card, CardStatus.PENDING)) {
      throw new CardDataException("Block request already exists for this card");
    }
  }
}
