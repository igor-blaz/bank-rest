package com.example.bankcards.controller.user;


import com.example.bankcards.TestSecurityConfig;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.factory.response.CardResponseTestFactory;
import com.example.bankcards.security.CustomUserDetails;
import com.example.bankcards.service.CardService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CardPublicController.class)
@Import(TestSecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
class CardPublicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CardService cardService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public CardService cardService() {
            return Mockito.mock(CardService.class);
        }
    }

    // ===================== SETUP AUTH =====================

    private void setAuth(Long userId) {
        CustomUserDetails userDetails = Mockito.mock(CustomUserDetails.class);
        when(userDetails.getId()).thenReturn(userId);

        Authentication auth = Mockito.mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(userDetails);

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    // ===================== TESTS =====================

    @Test
    void getMyCards_shouldReturnList() throws Exception {
        setAuth(5L);

        List<CardResponse> list =
                CardResponseTestFactory.makeListCardResponse(3);

        when(cardService.getUserCards(5L)).thenReturn(list);

        mockMvc.perform(get("/public/cards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].ownerName").value("Igor"));

        verify(cardService).getUserCards(5L);
    }

    @Test
    void getMyCardById_shouldReturnCard() throws Exception {
        setAuth(5L);

        CardResponse response =
                CardResponseTestFactory.makeCardResponse();

        when(cardService.getUserCardsByCardId(5L, 10L)).thenReturn(response);


        mockMvc.perform(get("/public/cards/{cardId}", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.maskedCardNumber").value(response.getMaskedCardNumber()))
                .andExpect(jsonPath("$.balance").value(response.getBalance().doubleValue()))
                .andExpect(jsonPath("$.ownerName").value(response.getOwnerName()))
                .andExpect(jsonPath("$.ownerSurname").value(response.getOwnerSurname()))
                .andExpect(jsonPath("$.cardStatus").value(response.getCardStatus().toString()));

        verify(cardService).getUserCardsByCardId(5L, 10L);
    }
}