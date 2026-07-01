package com.example.bankcards.service.transfer;

import java.math.BigDecimal;

public interface TransferService {
  boolean transfer(Long idFrom, Long idTo, BigDecimal amount);
}
