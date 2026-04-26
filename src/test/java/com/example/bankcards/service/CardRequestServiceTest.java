package com.example.bankcards.service;

import com.example.bankcards.dto.request.CardCreateRequest;
import com.example.bankcards.dto.response.RequestResponse;
import com.example.bankcards.entity.CardCreationRequest;
import com.example.bankcards.enums.RequestStatus;
import com.example.bankcards.exception.InvalidRequestStatusException;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.factory.request.CardCreationRequestTestFactory;
import com.example.bankcards.repository.CardCreationRepository;
import com.example.bankcards.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardRequestServiceTest {

    @Mock
    private CardCreationRepository cardCreationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CardRequestService cardRequestService;

    // ===================== CREATE =====================

    @Test
    void createCardRequest_shouldReturnResponse_whenUserExists() {
        CardCreateRequest request = CardCreateRequest.builder()
                .userId(1L)
                .build();

        CardCreationRequest entity =
                CardCreationRequestTestFactory.makeRequest(1L);

        when(userRepository.existsById(1L)).thenReturn(true);
        when(cardCreationRepository.save(any())).thenReturn(entity);

        RequestResponse response = cardRequestService.createCardRequest(request);

        assertNotNull(response);
        verify(userRepository).existsById(1L);
        verify(cardCreationRepository).save(any());
    }

    @Test
    void createCardRequest_shouldThrow_whenUserNotExists() {
        CardCreateRequest request = CardCreateRequest.builder()
                .userId(1L)
                .build();

        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(NotFoundException.class, () ->
                cardRequestService.createCardRequest(request)
        );

        verify(cardCreationRepository, never()).save(any());
    }

    // ===================== REJECT =====================

    @Test
    void rejectRequest_shouldSetRejected_whenPending() {
        CardCreationRequest entity =
                CardCreationRequestTestFactory.pendingRequest(1L);

        when(cardCreationRepository.findById(1L))
                .thenReturn(Optional.of(entity));

        cardRequestService.rejectRequest(1L);

        assertEquals(RequestStatus.REJECTED, entity.getCardRequestStatus());
    }

    @Test
    void rejectRequest_shouldThrow_whenNotPending() {
        CardCreationRequest entity =
                CardCreationRequestTestFactory.approvedRequest(1L);

        when(cardCreationRepository.findById(1L))
                .thenReturn(Optional.of(entity));

        assertThrows(InvalidRequestStatusException.class, () ->
                cardRequestService.rejectRequest(1L)
        );
    }

    @Test
    void rejectRequest_shouldThrow_whenNotFound() {
        when(cardCreationRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                cardRequestService.rejectRequest(1L)
        );
    }

    // ===================== APPROVE =====================

    @Test
    void approveRequest_shouldSetApproved_whenPending() {
        CardCreationRequest entity =
                CardCreationRequestTestFactory.pendingRequest(1L);

        when(cardCreationRepository.findById(1L))
                .thenReturn(Optional.of(entity));

        cardRequestService.approveRequest(1L);

        assertEquals(RequestStatus.APPROVED, entity.getCardRequestStatus());
    }

    @Test
    void approveRequest_shouldThrow_whenNotPending() {
        CardCreationRequest entity =
                CardCreationRequestTestFactory.rejectedRequest(1L);

        when(cardCreationRepository.findById(1L))
                .thenReturn(Optional.of(entity));

        assertThrows(InvalidRequestStatusException.class, () ->
                cardRequestService.approveRequest(1L)
        );
    }

    // ===================== GET ALL =====================

    @Test
    void getAllRequests_shouldReturnList() {
        List<CardCreationRequest> list = List.of(
                CardCreationRequestTestFactory.makeRequest(1L),
                CardCreationRequestTestFactory.makeRequest(2L)
        );

        when(cardCreationRepository.findAll()).thenReturn(list);

        List<RequestResponse> result = cardRequestService.getAllRequests();

        assertEquals(2, result.size());
        verify(cardCreationRepository).findAll();
    }

    // ===================== GET BY ID =====================

    @Test
    void getRequestById_shouldReturnResponse() {
        CardCreationRequest entity =
                CardCreationRequestTestFactory.makeRequest(1L);

        when(cardCreationRepository.findById(1L))
                .thenReturn(Optional.of(entity));

        RequestResponse result = cardRequestService.getRequestById(1L);

        assertNotNull(result);
        verify(cardCreationRepository).findById(1L);
    }

    @Test
    void getRequestById_shouldThrow_whenNotFound() {
        when(cardCreationRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                cardRequestService.getRequestById(1L)
        );
    }
}
