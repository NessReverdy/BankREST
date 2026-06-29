package com.example.bankcards.util;

import com.example.bankcards.dto.card.CardSearchFilter;
import com.example.bankcards.entity.CardEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class CardSpecification {

  public static Specification<CardEntity> filterBy(CardSearchFilter filter) {
    return (root, query, criteriaBuilder) -> {
      Predicate predicate = criteriaBuilder.conjunction();

      if (filter.status() != null) {
        predicate = criteriaBuilder.and(predicate,
          criteriaBuilder.equal(root.get("status"), filter.status()));
      }

      if (filter.balanceMin() != null) {
        predicate = criteriaBuilder.and(predicate,
          criteriaBuilder.greaterThanOrEqualTo(root.get("balance"), filter.balanceMin()));
      }

      if (filter.balanceMax() != null) {
        predicate = criteriaBuilder.and(predicate,
          criteriaBuilder.lessThanOrEqualTo(root.get("balance"), filter.balanceMax()));
      }

      if (filter.expireDateFrom() != null) {
        predicate = criteriaBuilder.and(predicate,
          criteriaBuilder.greaterThanOrEqualTo(root.get("expireDate"), filter.expireDateFrom()));
      }

      if (filter.expireDateTo() != null) {
        predicate = criteriaBuilder.and(predicate,
          criteriaBuilder.lessThanOrEqualTo(root.get("expireDate"), filter.expireDateTo()));
      }

      if (filter.number() != null && !filter.number().isEmpty()) {
        predicate = criteriaBuilder.and(predicate,
          criteriaBuilder.like(
            criteriaBuilder.lower(root.get("cardNumber")),
            "%" + filter.number().toLowerCase() + "%"
          ));
      }

      return predicate;
    };
  }
}
