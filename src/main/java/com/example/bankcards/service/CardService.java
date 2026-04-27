package com.example.bankcards.service;

import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardCreationRequest;
import com.example.bankcards.entity.User;
import com.example.bankcards.enums.CardStatus;
import com.example.bankcards.exception.CardOwnershipException;
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

    public CardResponse getUserCardsByCardId(Long userId, Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new NotFoundException("card id " + cardId + " not found"));
        if(!card.getOwner().getId().equals(userId)){
            throw new CardOwnershipException("this card in sot belong to current user");
        }
        return CardMapper.toResponse(card);
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
                .cardStatus(CardStatus.ACTIVE)
                .expirationDate(LocalDate.now().plusYears(4))
                .build();
        cardRepository.save(card);
        return CardMapper.toResponse(card);
    }

}
