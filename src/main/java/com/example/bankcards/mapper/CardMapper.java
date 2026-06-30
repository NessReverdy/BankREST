package com.example.bankcards.mapper;

import com.example.bankcards.dto.card.CardBalanceResponse;
import com.example.bankcards.dto.card.CardRequest;
import com.example.bankcards.dto.card.CardResponse;
import com.example.bankcards.entity.CardEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
  componentModel = "spring",
  nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
  unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CardMapper {
  CardResponse toResponse(CardEntity card);
  CardEntity toEntity(CardResponse cardResponse);
  CardEntity toEntity(CardRequest cardRequest);
  void updateCard(CardRequest cardRequest, @MappingTarget CardEntity card);
  CardBalanceResponse toBalanceResponse(CardEntity card);
}
