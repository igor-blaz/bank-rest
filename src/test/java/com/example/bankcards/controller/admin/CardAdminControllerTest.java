package com.example.bankcards.controller.admin;

import com.example.bankcards.TestSecurityConfig;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.factory.response.CardResponseTestFactory;
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

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CardAdminController.class)
@Import(TestSecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
class CardAdminControllerTest {

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

    // ===================== GET ALL =====================

    @Test
    void getAllCards_shouldReturnList() throws Exception {
        List<CardResponse> list =
                CardResponseTestFactory.makeListCardResponse(3);

        when(cardService.getAllCards()).thenReturn(list);

        mockMvc.perform(get("/admin/cards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].ownerName").value("Igor"));

        verify(cardService).getAllCards();
    }

    // ===================== GET BY ID =====================

    @Test
    void getCardById_shouldReturnCard() throws Exception {
        CardResponse response =
                CardResponseTestFactory.makeCardResponse();

        when(cardService.getCardById(10L)).thenReturn(response);

        mockMvc.perform(get("/admin/cards/{cardId}", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ownerName").value("Igor"))
                .andExpect(jsonPath("$.cardStatus").value("ACTIVE"));

        verify(cardService).getCardById(10L);
    }

    // ===================== BLOCK ONE =====================

    @Test
    void blockCard_shouldReturnUpdatedCard() throws Exception {
        CardResponse response =
                CardResponseTestFactory.makeBlockedCardResponse();

        when(cardService.blockCardByCardId(7L)).thenReturn(response);

        mockMvc.perform(patch("/admin/cards/{cardId}/block", 7L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardStatus").value("BLOCKED"));

        verify(cardService).blockCardByCardId(7L);
    }

    // ===================== BLOCK ALL USER =====================

    @Test
    void blockAllUsersCards_shouldReturnList() throws Exception {
        List<CardResponse> list =
                CardResponseTestFactory.makeListBlockedCards(2);

        when(cardService.blockAllUsersCard(5L)).thenReturn(list);

        mockMvc.perform(patch("/admin/cards/by-user/{userId}/block", 5L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].cardStatus").value("BLOCKED"));

        verify(cardService).blockAllUsersCard(5L);
    }
}