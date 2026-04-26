package com.example.bankcards.service;

import com.example.bankcards.dto.request.UserCreateRequest;
import com.example.bankcards.dto.response.RequestResponse;
import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.entity.UserCreationRequest;
import com.example.bankcards.enums.RequestStatus;
import com.example.bankcards.exception.InvalidRequestStatusException;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.repository.UserCreationRepository;
import com.example.bankcards.util.RequestMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserRequestService {
    private final UserService userService;
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
                .orElseThrow(() -> new NotFoundException("Request not found"));
        if (request.getUserRequestStatus() != RequestStatus.PENDING) {
            throw new InvalidRequestStatusException("Only pending request can be rejected");
        }
        request.setUserRequestStatus(RequestStatus.REJECTED);
    }

    @Transactional
    public UserResponse approveRequest(long requestId) {
        UserCreationRequest request = repository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request not found"));
        if (request.getUserRequestStatus() != RequestStatus.PENDING) {
            throw new InvalidRequestStatusException("Only pending request can be approved");
        }
        request.setUserRequestStatus(RequestStatus.APPROVED);
        return userService.createUser(request);
    }

    public List<RequestResponse> getAllRequests() {
        List<UserCreationRequest> entities = repository.findAll();
        return RequestMapper.toUserResponseList(entities);
    }

    public RequestResponse getRequestById(Long id) {
        UserCreationRequest userCreationRequests = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Request id " + id + "is not found"));
        return RequestMapper.toResponse(userCreationRequests);

    }
}
