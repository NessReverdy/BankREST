package com.example.bankcards.service.transfer;

import com.example.bankcards.dto.card.CardBalanceResponse;
import com.example.bankcards.dto.transfer.TransferRequest;

import java.util.List;

public interface TransferService {
  List<CardBalanceResponse> transfer(TransferRequest request);
}
