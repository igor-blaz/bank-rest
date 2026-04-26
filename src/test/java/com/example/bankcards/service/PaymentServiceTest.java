package com.example.bankcards.service;

import com.example.bankcards.dto.request.SelfPaymentRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Transaction;
import com.example.bankcards.exception.*;
import com.example.bankcards.factory.card.CardTestFactory;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void makeSelfPayment_whenValidRequest_shouldTransferMoneyAndSaveTransaction() {
        // given
        Long currentUserId = 1L;

        SelfPaymentRequest request = SelfPaymentRequest.builder()
                .senderCardId(10L)
                .receiverCardId(20L)
                .transferAmount(new BigDecimal("100.00"))
                .build();

        Card senderCard = CardTestFactory.makeCard(10L, 500L);


        Card receiverCard = CardTestFactory.makeCard(20L, 50L);

        Assertions.assertNotNull(senderCard);
        when(cardRepository.findById(10L)).thenReturn(Optional.of(senderCard));
        Assertions.assertNotNull(receiverCard);
        when(cardRepository.findById(20L)).thenReturn(Optional.of(receiverCard));

        // when
        paymentService.makeSelfPayment(request, currentUserId);

        // then
        assertThat(senderCard.getBalance()).isEqualByComparingTo("400.00");
        assertThat(receiverCard.getBalance()).isEqualByComparingTo("150.00");

        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void makeSelfPayment_whenSenderCardNotFound_shouldThrowNotFoundException() {
        // given
        Long currentUserId = 1L;

        SelfPaymentRequest request = SelfPaymentRequest.builder()
                .senderCardId(10L)
                .receiverCardId(20L)
                .transferAmount(new BigDecimal("100.00"))
                .build();

        when(cardRepository.findById(10L)).thenReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> paymentService.makeSelfPayment(request, currentUserId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Card with id 10 not found");

        verify(transactionRepository, never()).save(any());
    }

    @Test
    void makeSelfPayment_whenCardsBelongToDifferentUsers_shouldThrowCardOwnershipException() {
        // given
        Long currentUserId = 1L;

        SelfPaymentRequest request = SelfPaymentRequest.builder()
                .senderCardId(10L)
                .receiverCardId(20L)
                .transferAmount(new BigDecimal("100.00"))
                .build();

        Card senderCard = CardTestFactory.makeCard(10L, 500L);
        Card receiverCard = CardTestFactory.makeCard(20L, 50L, 88L);

        Assertions.assertNotNull(senderCard);
        when(cardRepository.findById(10L)).thenReturn(Optional.of(senderCard));
        Assertions.assertNotNull(receiverCard);
        when(cardRepository.findById(20L)).thenReturn(Optional.of(receiverCard));

        // when / then
        assertThatThrownBy(() -> paymentService.makeSelfPayment(request, currentUserId))
                .isInstanceOf(CardOwnershipException.class)
                .hasMessageContaining("Cards belong to different users");

        verify(transactionRepository, never()).save(any());
    }

    @Test
    void makeSelfPayment_whenSenderCardDoesNotBelongToCurrentUser_shouldThrowCardOwnershipException() {
        // given
        Long currentUserId = 99L;

        SelfPaymentRequest request = SelfPaymentRequest.builder()
                .senderCardId(10L)
                .receiverCardId(20L)
                .transferAmount(new BigDecimal("100.00"))
                .build();

        Card senderCard = CardTestFactory.makeCard(10L);
        Card receiverCard = CardTestFactory.makeCard(20L);

        Assertions.assertNotNull(senderCard);
        when(cardRepository.findById(10L)).thenReturn(Optional.of(senderCard));
        Assertions.assertNotNull(receiverCard);
        when(cardRepository.findById(20L)).thenReturn(Optional.of(receiverCard));

        // when / then
        assertThatThrownBy(() -> paymentService.makeSelfPayment(request, currentUserId))
                .isInstanceOf(CardOwnershipException.class)
                .hasMessageContaining("Cards do not belong to current user");

        verify(transactionRepository, never()).save(any());
    }

    @Test
    void makeSelfPayment_whenNotEnoughFunds_shouldThrowInsufficientFundsException() {
        // given
        Long currentUserId = 1L;

        SelfPaymentRequest request = SelfPaymentRequest.builder()
                .senderCardId(10L)
                .receiverCardId(20L)
                .transferAmount(new BigDecimal("1000.00"))
                .build();

        Card senderCard = CardTestFactory.makeCard(10L);
        Card receiverCard = CardTestFactory.makeCard(20L);

        Assertions.assertNotNull(senderCard);
        when(cardRepository.findById(10L)).thenReturn(Optional.of(senderCard));
        Assertions.assertNotNull(receiverCard);
        when(cardRepository.findById(20L)).thenReturn(Optional.of(receiverCard));

        // when / then
        assertThatThrownBy(() -> paymentService.makeSelfPayment(request, currentUserId))
                .isInstanceOf(InsufficientFundsException.class)
                .hasMessageContaining("Insufficient funds");

        verify(transactionRepository, never()).save(any());
    }

    @Test
    void makeSelfPayment_whenOneCardBlocked_shouldThrowCardBlockedException() {
        // given
        Long currentUserId = 1L;

        SelfPaymentRequest request = SelfPaymentRequest.builder()
                .senderCardId(10L)
                .receiverCardId(20L)
                .transferAmount(new BigDecimal("100.00"))
                .build();

        Card senderCard = CardTestFactory.makeCard(10L);
        Card receiverCard = CardTestFactory.makeBlockedCard(20L);

        Assertions.assertNotNull(senderCard);
        when(cardRepository.findById(10L)).thenReturn(Optional.of(senderCard));
        Assertions.assertNotNull(receiverCard);
        when(cardRepository.findById(20L)).thenReturn(Optional.of(receiverCard));

        // when / then
        assertThatThrownBy(() -> paymentService.makeSelfPayment(request, currentUserId))
                .isInstanceOf(CardBlockedException.class)
                .hasMessageContaining("One of the cards is blocked");

        verify(transactionRepository, never()).save(any());
    }

    @Test
    void makeSelfPayment_whenOneCardExpired_shouldThrowCardExpiredException() {
        // given
        Long currentUserId = 1L;

        SelfPaymentRequest request = SelfPaymentRequest.builder()
                .senderCardId(10L)
                .receiverCardId(20L)
                .transferAmount(new BigDecimal("100.00"))
                .build();

        Card senderCard = CardTestFactory.makeCard(10L);
        Card receiverCard = CardTestFactory.makeExpiredCard(20L);

        Assertions.assertNotNull(senderCard);
        when(cardRepository.findById(10L)).thenReturn(Optional.of(senderCard));
        Assertions.assertNotNull(receiverCard);
        when(cardRepository.findById(20L)).thenReturn(Optional.of(receiverCard));

        // when / then
        assertThatThrownBy(() -> paymentService.makeSelfPayment(request, currentUserId))
                .isInstanceOf(CardExpiredException.class)
                .hasMessageContaining("One of the cards is expired");

        verify(transactionRepository, never()).save(any());
    }

    
}
