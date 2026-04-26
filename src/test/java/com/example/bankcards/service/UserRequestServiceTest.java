package com.example.bankcards.service;

import com.example.bankcards.dto.request.UserCreateRequest;
import com.example.bankcards.dto.response.RequestResponse;
import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.entity.UserCreationRequest;
import com.example.bankcards.enums.RequestStatus;
import com.example.bankcards.exception.InvalidRequestStatusException;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.factory.request.UserCreationRequestTestFactory;
import com.example.bankcards.factory.response.UserResponseTestFactory;
import com.example.bankcards.repository.UserCreationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRequestServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private UserCreationRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserRequestService userRequestService;

    // ===================== CREATE =====================

    @Test
    void createRequest_shouldEncodePassword_andReturnResponse() {
        UserCreateRequest request = UserCreateRequest.builder()
                .name("Igor")
                .surname("Ivanov")
                .age(23)
                .password("raw-password")
                .build();

        UserCreationRequest entity =
                UserCreationRequestTestFactory.makeRequest(1L);

        when(passwordEncoder.encode("raw-password"))
                .thenReturn("encoded-password");

        when(repository.save(any()))
                .thenReturn(entity);

        RequestResponse response = userRequestService.createRequest(request);

        assertNotNull(response);
        assertEquals("encoded-password", request.getPassword());

        verify(passwordEncoder).encode("raw-password");
        verify(repository).save(any());
    }

    // ===================== REJECT =====================

    @Test
    void rejectRequest_shouldSetRejected_whenPending() {
        UserCreationRequest entity =
                UserCreationRequestTestFactory.makeRequest(1L); // PENDING

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        userRequestService.rejectRequest(1L);

        assertEquals(RequestStatus.REJECTED, entity.getUserRequestStatus());
    }

    @Test
    void rejectRequest_shouldThrow_whenNotPending() {
        UserCreationRequest entity =
                UserCreationRequestTestFactory.makeRequest(1L);
        entity.setUserRequestStatus(RequestStatus.APPROVED);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        assertThrows(InvalidRequestStatusException.class, () ->
                userRequestService.rejectRequest(1L)
        );
    }

    @Test
    void rejectRequest_shouldThrow_whenNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                userRequestService.rejectRequest(1L)
        );
    }

    // ===================== APPROVE =====================

    @Test
    void approveRequest_shouldReturnUser_whenPending() {
        UserCreationRequest entity =
                UserCreationRequestTestFactory.makeRequest(1L);

        UserResponse userResponse =
                UserResponseTestFactory.makeUserResponse(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(userService.createUser(entity)).thenReturn(userResponse);

        UserResponse result = userRequestService.approveRequest(1L);

        assertEquals(RequestStatus.APPROVED, entity.getUserRequestStatus());
        assertNotNull(result);

        verify(userService).createUser(entity);
    }

    @Test
    void approveRequest_shouldThrow_whenNotPending() {
        UserCreationRequest entity =
                UserCreationRequestTestFactory.makeRequest(1L);
        entity.setUserRequestStatus(RequestStatus.REJECTED);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        assertThrows(InvalidRequestStatusException.class, () ->
                userRequestService.approveRequest(1L)
        );
    }

    @Test
    void approveRequest_shouldThrow_whenNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                userRequestService.approveRequest(1L)
        );
    }

    // ===================== GET ALL =====================

    @Test
    void getAllRequests_shouldReturnList() {
        List<UserCreationRequest> list = List.of(
                UserCreationRequestTestFactory.makeRequest(1L),
                UserCreationRequestTestFactory.makeRequest(2L)
        );

        when(repository.findAll()).thenReturn(list);

        List<RequestResponse> result = userRequestService.getAllRequests();

        assertEquals(2, result.size());
        verify(repository).findAll();
    }

    // ===================== GET BY ID =====================

    @Test
    void getRequestById_shouldReturnResponse() {
        UserCreationRequest entity =
                UserCreationRequestTestFactory.makeRequest(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        RequestResponse result = userRequestService.getRequestById(1L);

        assertNotNull(result);
        verify(repository).findById(1L);
    }

    @Test
    void getRequestById_shouldThrow_whenNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                userRequestService.getRequestById(1L)
        );
    }
}
