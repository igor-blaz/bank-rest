package com.example.bankcards.controller.user;

import com.example.bankcards.dto.request.SelfPaymentRequest;
import com.example.bankcards.security.CustomUserDetails;
import com.example.bankcards.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "/public/payment")
@RequiredArgsConstructor
public class PaymentPublicController {

    private final PaymentService paymentService;

    @PostMapping("/payments/self")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void selfPayment(@Valid @RequestBody SelfPaymentRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        Long userId = userDetails.getId();
        paymentService.makeSelfPayment(request, userId);
    }
}
