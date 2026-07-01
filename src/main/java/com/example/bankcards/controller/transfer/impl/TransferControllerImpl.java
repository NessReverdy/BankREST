package com.example.bankcards.controller.transfer.impl;

import com.example.bankcards.controller.transfer.TransferController;
import com.example.bankcards.dto.card.CardBalanceResponse;
import com.example.bankcards.dto.transfer.TransferRequest;
import com.example.bankcards.service.transfer.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TransferControllerImpl implements TransferController {
  private final TransferService transferService;

  @Override
  public ResponseEntity<List<CardBalanceResponse>> transfer(TransferRequest transferRequest) {
    return ResponseEntity.ok(transferService.transfer(transferRequest));
  }
}
