package com.example.bankcards.service;

import com.example.bankcards.dto.request.UserCreateRequest;
import com.example.bankcards.dto.response.RequestResponse;
import com.example.bankcards.entity.UserCreationRequest;
import com.example.bankcards.enums.RequestStatus;
import com.example.bankcards.repository.UserCreationRepository;
import com.example.bankcards.util.RequestMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserRequestService {
    private final UserCreationRepository repository;
    private final PasswordEncoder passwordEncoder;

    public RequestResponse createRequest(UserCreateRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        request.setPassword(encodedPassword);
        UserCreationRequest entity = repository.save(RequestMapper.toEntity(request));
        return RequestMapper.toResponse(entity);
    }

    @Transactional
    public void rejectRequest(long requestId) {
        UserCreationRequest request = repository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Request not found"));
        if (request.getUserRequestStatus() != RequestStatus.PENDING) {
            throw new IllegalStateException("Only pending request can be rejected");
        }
        request.setUserRequestStatus(RequestStatus.REJECTED);
    }

    @Transactional
    public void approveRequest(long requestId) {
        UserCreationRequest request = repository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Request not found"));
        if (request.getUserRequestStatus() != RequestStatus.PENDING) {
            throw new IllegalStateException("Only pending request can be approved");
        }
        request.setUserRequestStatus(RequestStatus.APPROVED);
    }
}
