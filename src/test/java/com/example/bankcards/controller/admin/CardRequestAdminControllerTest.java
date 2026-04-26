package com.example.bankcards.controller.admin;

import com.example.bankcards.TestSecurityConfig;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.dto.response.RequestResponse;
import com.example.bankcards.factory.response.CardResponseTestFactory;
import com.example.bankcards.factory.response.RequestResponseTestFactory;
import com.example.bankcards.service.CardRequestService;
import com.example.bankcards.service.CardService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CardRequestAdminController.class)
@Import(TestSecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
class CardRequestAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CardRequestService cardRequestService;

    @Autowired
    private CardService cardService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public CardRequestService cardRequestService() {
            return Mockito.mock(CardRequestService.class);
        }

        @Bean
        public CardService cardService() {
            return Mockito.mock(CardService.class);
        }
    }

    @Test
    void rejectCardRequest_shouldReturnNoContent() throws Exception {
        mockMvc.perform(post("/admin/card-requests/{requestId}/reject", 7L))
                .andExpect(status().isNoContent());

        verify(cardRequestService).rejectRequest(7L);
    }

    @Test
    void approveCardRequest_shouldReturnCardResponse() throws Exception {
        CardResponse response = CardResponseTestFactory.makeCardResponse();

        when(cardService.createCard(7L)).thenReturn(response);

        mockMvc.perform(post("/admin/card-requests/{requestId}/approve", 7L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.maskedCardNumber").value("**** **** **** 1234"))
                .andExpect(jsonPath("$.ownerName").value("Igor"))
                .andExpect(jsonPath("$.cardStatus").value("ACTIVE"));

        verify(cardRequestService).approveRequest(7L);
        verify(cardService).createCard(7L);
    }

    @Test
    void getAllRequests_shouldReturnListOfRequestResponse() throws Exception {
        List<RequestResponse> requestResponseList =
                RequestResponseTestFactory.makeListRequestResponse(3);

        when(cardRequestService.getAllRequests()).thenReturn(requestResponseList);
        mockMvc.perform(get("/admin/card-requests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].status").value("APPROVED"));
    }

    @Test
    void getRequestById_shouldReturnRequestResponse() throws Exception {
        RequestResponse response = RequestResponseTestFactory.makeRequestResponse(99L);

        when(cardRequestService.getRequestById(99L)).thenReturn(response);
        mockMvc.perform(get("/admin/card-requests/{requestId}", 99L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(99L))
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

}