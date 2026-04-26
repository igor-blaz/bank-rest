package com.example.bankcards.controller.user;

import com.example.bankcards.TestSecurityConfig;
import com.example.bankcards.dto.request.SelfPaymentRequest;
import com.example.bankcards.security.CustomUserDetails;
import com.example.bankcards.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentPublicController.class)
@Import(TestSecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
class PaymentPublicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PaymentService paymentService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public PaymentService paymentService() {
            return Mockito.mock(PaymentService.class);
        }
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private void setAuth(Long userId) {
        CustomUserDetails userDetails = Mockito.mock(CustomUserDetails.class);
        when(userDetails.getId()).thenReturn(userId);

        Authentication auth = Mockito.mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(userDetails);

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void selfPayment_shouldReturnNoContent() throws Exception {
        setAuth(5L);

        SelfPaymentRequest request = SelfPaymentRequest.builder()
                .senderCardId(1L)
                .receiverCardId(2L)
                .transferAmount(BigDecimal.valueOf(500))
                .build();

        mockMvc.perform(post("/public/payment/payments/self")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(paymentService).makeSelfPayment(any(SelfPaymentRequest.class), eq(5L));
    }
}