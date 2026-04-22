package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findAllByOwnerId(Long id);

    List<Card> findAllByOwnerIdAndId(Long id, Long cardId);

    boolean existsByCardNumber(String cardNumber);
}
