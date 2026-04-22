package com.example.bankcards.service;

import com.example.bankcards.dto.request.SelfPaymentRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.enums.CardStatus;
import com.example.bankcards.exception.*;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.TransactionRepository;
import com.example.bankcards.util.TransactionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {
    private final CardRepository repository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public void makeSelfPayment(SelfPaymentRequest request, Long currentUserId) {
        Card senderCard = getCardById(request.getSenderCardId());
        Card receiverCard = getCardById(request.getReceiverCardId());

        validateOwnership(senderCard, receiverCard, currentUserId);
        validateEnoughFunds(senderCard, request.getTransferAmount());
        validateNotBlocked(senderCard, receiverCard);
        validateNotExpired(senderCard, receiverCard);

        senderCard.decreaseBalance(request.getTransferAmount());
        receiverCard.increaseBalance(request.getTransferAmount());
        transactionRepository.save(TransactionMapper.createTransactionEntity(request));
    }


    private Card getCardById(Long cardId) {
        return repository.findById(cardId)
                .orElseThrow(() -> new NotFoundException("Card with id " + cardId + " not found"));
    }

    private void validateOwnership(Card senderCard, Card receiverCard, Long currentUserId) {
        if (!senderCard.getOwner().getId().equals(receiverCard.getOwner().getId())) {
            throw new CardOwnershipException("Cards belong to different users");
        }

        if (!senderCard.getOwner().getId().equals(currentUserId)) {
            throw new CardOwnershipException("Cards do not belong to current user");
        }
    }

    private void validateEnoughFunds(Card senderCard, BigDecimal amount) {
        if (amount.compareTo(senderCard.getBalance()) > 0) {
            throw new InsufficientFundsException("Insufficient funds");
        }
    }

    private void validateNotBlocked(Card senderCard, Card receiverCard) {
        if (senderCard.getCardStatus() == CardStatus.BLOCKED ||
                receiverCard.getCardStatus() == CardStatus.BLOCKED) {
            throw new CardBlockedException("One of the cards is blocked");
        }
    }

    private void validateNotExpired(Card senderCard, Card receiverCard) {
        LocalDate now = LocalDate.now();

        if (senderCard.getExpirationDate().isBefore(now) ||
                receiverCard.getExpirationDate().isBefore(now)) {
            throw new CardExpiredException("One of the cards is expired");
        }
    }
}
