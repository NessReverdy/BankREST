package com.example.bankcards.util;

import com.example.bankcards.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class CardCreator {
  private static final int MAX_ATTEMPTS = 1000;

  private final CardRepository repo;

  public String generateCardNumber() {
    int attempts = 0;
    String cardNumber = "";

    do {
      StringBuilder cardNumberStr = new StringBuilder();
      for (int i = 0; i < 15; i++) {
        cardNumberStr.append(ThreadLocalRandom.current().nextInt(0, 10));
      }

      int checksum = calculateLuhnChecksum(cardNumberStr.toString());
      cardNumberStr.append(checksum);
      cardNumber = cardNumberStr.toString();

      attempts++;

      if (attempts > MAX_ATTEMPTS) {
        throw new RuntimeException("Не удалось сгенерировать уникальный номер карты после " +
          MAX_ATTEMPTS + " попыток");
      }
    } while (isCardNumberExistsInDatabase(cardNumber));

    return cardNumber;
  }

  public YearMonth setExpireDate(int year) {
    if (year < 1 || year > 10) {
      throw new IllegalArgumentException("The validity period is from 1 to 10 years");
    }
    return YearMonth.now().plusYears(year);
  }

  private int calculateLuhnChecksum(String number) {
    int sum = 0;
    boolean alternate = false;

    for (int i = number.length() - 1; i >= 0; i--) {
      int digit = Character.getNumericValue(number.charAt(i));
      if (alternate) {
        digit *= 2;
        if (digit > 9) {
          digit = digit - 9;
        }
      }
      sum += digit;
      alternate = !alternate;
    }

    return (10 - (sum % 10)) % 10;
  }

  private boolean isCardNumberExistsInDatabase(String cardNumber) {
    return repo.existsByNumber(cardNumber);
  }

  private String formatExpireDate(YearMonth expireDate) {
    return expireDate.format(DateTimeFormatter.ofPattern("MM/yy"));
  }
}
