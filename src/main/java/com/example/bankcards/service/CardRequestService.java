package com.example.bankcards.service;

import com.example.bankcards.dto.request.CardCreateRequest;
import com.example.bankcards.dto.response.RequestResponse;
import com.example.bankcards.entity.CardCreationRequest;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.UserCreationRequest;
import com.example.bankcards.enums.RequestStatus;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.repository.CardCreationRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.RequestMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CardRequestService {

    private final CardCreationRepository cardCreationRepository;
    private final UserRepository userRepository;

    public RequestResponse createCardRequest(CardCreateRequest request) {
        boolean existsById = userRepository.existsById(request.getUserId());
        if (!existsById) {
            throw new NotFoundException("User " + request.getUserId() + " not found.");
        }

        CardCreationRequest entity = cardCreationRepository.save(RequestMapper.toEntity(request));
        return RequestMapper.toResponse(entity);
    }

    @Transactional
    public void rejectRequest(long requestId) {
        CardCreationRequest request = cardCreationRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request not found"));
        if (request.getCardRequestStatus() != RequestStatus.PENDING) {
            throw new IllegalStateException("Only pending request can be rejected");
        }
        request.setCardRequestStatus(RequestStatus.REJECTED);
    }

    @Transactional
    public void approveRequest(long requestId) {
        CardCreationRequest request = cardCreationRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request not found"));
        if (request.getCardRequestStatus() != RequestStatus.PENDING) {
            throw new IllegalStateException("Only pending request can be approved");
        }
        request.setCardRequestStatus(RequestStatus.APPROVED);
        
    }


}
