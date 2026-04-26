package com.example.bankcards.service;

import com.example.bankcards.dto.request.LoginRequest;
import com.example.bankcards.dto.response.AuthResponse;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.factory.user.UserTestFactory;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.CustomUserDetails;
import com.example.bankcards.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void login_shouldReturnToken_whenCredentialsAreValid() {
        LoginRequest request = LoginRequest.builder()
                .email("admin@email.com")
                .password("raw-password")
                .build();

        User user = UserTestFactory.makeUser(1L);

        when(userRepository.findByEmail("admin@email.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("raw-password", user.getPassword()))
                .thenReturn(true);

        when(jwtService.generateToken(any(CustomUserDetails.class)))
                .thenReturn("test-jwt-token");

        AuthResponse response = authService.login(request);

        assertEquals("test-jwt-token", response.getToken());

        verify(userRepository).findByEmail("admin@email.com");
        verify(passwordEncoder).matches("raw-password", user.getPassword());
        verify(jwtService).generateToken(any(CustomUserDetails.class));
    }

    @Test
    void login_shouldThrowNotFoundException_whenUserNotFound() {
        LoginRequest request = LoginRequest.builder()
                .email("unknown")
                .password("password")
                .build();

        when(userRepository.findByEmail("unknown"))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                authService.login(request)
        );

        verify(userRepository).findByEmail("unknown");
        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(jwtService);
    }

    @Test
    void login_shouldThrowBadCredentialsException_whenPasswordIsWrong() {
        LoginRequest request = LoginRequest.builder()
                .email("admin")
                .password("wrong-password")
                .build();

        User user = UserTestFactory.makeUser(1L);

        when(userRepository.findByEmail("admin"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("wrong-password", user.getPassword()))
                .thenReturn(false);

        assertThrows(BadCredentialsException.class, () ->
                authService.login(request)
        );

        verify(userRepository).findByEmail("admin");
        verify(passwordEncoder).matches("wrong-password", user.getPassword());
        verifyNoInteractions(jwtService);
    }
}
