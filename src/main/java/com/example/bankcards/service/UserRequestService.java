package com.example.bankcards.service;

import com.example.bankcards.dto.request.UserCreateRequest;
import com.example.bankcards.dto.response.UserCreationRequestResponse;
import com.example.bankcards.entity.UserCreationRequest;
import com.example.bankcards.enums.UserRequestStatus;
import com.example.bankcards.repository.UserCreationRepository;
import com.example.bankcards.util.UserRequestMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserRequestService {
    private final UserCreationRepository repository;

    public UserCreationRequestResponse createRequest(UserCreateRequest request) {
        UserCreationRequest entity = repository.save(UserRequestMapper.toEntity(request));
        return UserRequestMapper.toResponse(entity);
    }

    @Transactional
    public void rejectRequest(long requestId) {
        UserCreationRequest request = repository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Request not found"));
        if (request.getUserRequestStatus() != UserRequestStatus.PENDING) {
            throw new IllegalStateException("Only pending request can be rejected");
        }
        request.setUserRequestStatus(UserRequestStatus.REJECTED);
    }

    @Transactional
    public void approveRequest(long requestId) {
        UserCreationRequest request = repository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Request not found"));
        if (request.getUserRequestStatus() != UserRequestStatus.PENDING) {
            throw new IllegalStateException("Only pending request can be approved");
        }
        request.setUserRequestStatus(UserRequestStatus.APPROVED);
    }
}
