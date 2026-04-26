package com.example.bankcards.controller.user;


import com.example.bankcards.TestSecurityConfig;
import com.example.bankcards.dto.request.CardCreateRequest;
import com.example.bankcards.dto.response.RequestResponse;
import com.example.bankcards.factory.response.RequestResponseTestFactory;
import com.example.bankcards.service.CardRequestService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CardRequestPublicController.class)
@Import(TestSecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
class CardRequestPublicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CardRequestService cardRequestService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public CardRequestService cardRequestService() {
            return Mockito.mock(CardRequestService.class);
        }
    }

    @Test
    void createCardRequest_shouldReturnCreatedRequestResponse() throws Exception {
        CardCreateRequest request = new CardCreateRequest(7L);
        RequestResponse response = RequestResponseTestFactory.makeRequestResponse(7L);

        when(cardRequestService.createCardRequest(any(CardCreateRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/public/card-requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(7L))
                .andExpect(jsonPath("$.status").value("APPROVED"));

        verify(cardRequestService).createCardRequest(any(CardCreateRequest.class));
    }
}