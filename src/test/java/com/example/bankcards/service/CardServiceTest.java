package com.example.bankcards.service;

import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardCreationRequest;
import com.example.bankcards.entity.User;
import com.example.bankcards.enums.CardStatus;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.factory.card.CardTestFactory;
import com.example.bankcards.factory.request.CardCreationRequestTestFactory;
import com.example.bankcards.factory.user.UserTestFactory;
import com.example.bankcards.repository.CardCreationRepository;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CardCreationRepository cardCreationRepository;

    @Mock
    private CardNumberGenerator cardNumberGenerator;

    @InjectMocks
    private CardService cardService;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(cardService, "defaultBalance", 1000L);
    }

    // ===================== GET USER CARDS =====================

    @Test
    void getUserCards_shouldReturnList() {
        List<Card> cards = List.of(
                CardTestFactory.activeCard(1L),
                CardTestFactory.activeCard(2L)
        );

        when(cardRepository.findAllByOwnerId(1L)).thenReturn(cards);

        List<CardResponse> result = cardService.getUserCards(1L);

        assertEquals(2, result.size());
        verify(cardRepository).findAllByOwnerId(1L);
    }

    // ===================== GET BY ID =====================

    @Test
    void getCardById_shouldReturnCard() {
        Card card = CardTestFactory.activeCard(1L);

        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));

        CardResponse result = cardService.getCardById(1L);

        assertNotNull(result);
        verify(cardRepository).findById(1L);
    }

    @Test
    void getCardById_shouldThrow_whenNotFound() {
        when(cardRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                cardService.getCardById(1L)
        );
    }

    // ===================== BLOCK ONE =====================

    @Test
    void blockCard_shouldSetBlocked() {
        Card card = CardTestFactory.activeCard(1L);

        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));

        cardService.blockCardByCardId(1L);

        assertEquals(CardStatus.BLOCKED, card.getCardStatus());
    }

    // ===================== BLOCK ALL =====================

    @Test
    void blockAllUsersCard_shouldBlockAll() {
        List<Card> cards = List.of(
                CardTestFactory.activeCard(1L),
                CardTestFactory.activeCard(2L)
        );

        when(cardRepository.findAllByOwnerId(1L)).thenReturn(cards);

        cardService.blockAllUsersCard(1L);

        assertEquals(CardStatus.BLOCKED, cards.get(0).getCardStatus());
        assertEquals(CardStatus.BLOCKED, cards.get(1).getCardStatus());
    }

    // ===================== CREATE CARD =====================

    @Test
    void createCard_shouldReturnCardResponse() {
        CardCreationRequest request =
                CardCreationRequestTestFactory.pendingRequest(1L);

        User user = UserTestFactory.makeUser(1L);

        when(cardCreationRepository.findById(1L))
                .thenReturn(Optional.of(request));

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(cardNumberGenerator.makeNumber())
                .thenReturn("1234567812341234");

        CardResponse result = cardService.createCard(1L);

        assertNotNull(result);
        verify(cardRepository).save(any(Card.class));
        verify(cardCreationRepository).findById(1L);
        verify(userRepository).findById(1L);
        verify(cardNumberGenerator).makeNumber();
    }

    @Test
    void createCard_shouldThrow_whenRequestNotFound() {
        when(cardCreationRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                cardService.createCard(1L)
        );
    }

    @Test
    void createCard_shouldThrow_whenUserNotFound() {
        CardCreationRequest request =
                CardCreationRequestTestFactory.pendingRequest(1L);

        when(cardCreationRepository.findById(1L))
                .thenReturn(Optional.of(request));

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                cardService.createCard(1L)
        );
    }
}