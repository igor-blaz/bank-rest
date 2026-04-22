package com.example.bankcards.service;

import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardCreationRequest;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.repository.CardCreationRepository;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.CardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardNumberGenerator cardNumberGenerator;
    private final CardCreationRepository cardCreationRepository;

    @Value("${card.default-balance}")
    private long defaultBalance;

    public List<CardResponse> getUserCards(Long userId) {
        List<Card> entityList = cardRepository.findAllByOwnerId(userId);
        return CardMapper.toResponseList(entityList);
    }

    public List<CardResponse> getUserCardsByCardId(Long userId, Long cardId) {
        List<Card> entityList = cardRepository.findAllByOwnerIdAndId(userId, cardId);
        return CardMapper.toResponseList(entityList);
    }

    public List<CardResponse> getAllCards() {
        List<Card> entityList = cardRepository.findAll();
        return CardMapper.toResponseList(entityList);
    }

    public CardResponse getCardById(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new NotFoundException("card id " + cardId + " not found"));
        return CardMapper.toResponse(card);
    }

    @Transactional
    public CardResponse blockCardByCardId(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new NotFoundException("card id " + cardId + " not found"));
        card.markBlocked();
        return CardMapper.toResponse(card);
    }

    @Transactional
    public List<CardResponse> blockAllUsersCard(Long userId) {
        List<Card> cards = cardRepository.findAllByOwnerId(userId);
        for (Card card : cards) {
            card.markBlocked();
        }
        return CardMapper.toResponseList(cards);
    }

    public CardResponse createCard(Long requestId) {
        CardCreationRequest request = cardCreationRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("request id " + requestId + " not found"));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("user id " + request.getUserId() + " not found"));
        Card card = Card.builder()
                .cardNumber(cardNumberGenerator.makeNumber())
                .owner(user)
                .balance(BigDecimal.valueOf(defaultBalance))
                .expirationDate(LocalDate.now().plusYears(4))
                .build();
        return CardMapper.toResponse(card);
    }


    private void expirationDateListCheck(List<Card> cards) {
        LocalDate now = LocalDate.now();
        for (Card card : cards) {
            if (card.getExpirationDate().isBefore(now)) {
                card.markExpired();
            }
        }
    }

    private void expirationDateCheck(Card card) {
        if (card.getExpirationDate().isBefore(LocalDate.now())) {
            card.markExpired();
        }

    }
}
