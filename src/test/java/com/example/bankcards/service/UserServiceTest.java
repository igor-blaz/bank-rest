package com.example.bankcards.service;

import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.UserCreationRequest;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.factory.request.UserCreationRequestTestFactory;
import com.example.bankcards.factory.user.UserTestFactory;
import com.example.bankcards.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    // ===================== CREATE =====================

    @Test
    void createUser_shouldReturnUserResponse() {
        UserCreationRequest request = UserCreationRequestTestFactory.makeRequest();

        User savedUser = UserTestFactory.makeUser();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponse response = userService.createUser(request);

        assertEquals("Igor", response.getName());
        assertEquals("Ivanov", response.getSurname());
        assertEquals(23, response.getAge());

        verify(userRepository).save(any(User.class));
    }

    // ===================== GET ALL =====================

    @Test
    void getAllUsers_shouldReturnList() {
        List<User> users = UserTestFactory.makeUserList(5);
        when(userRepository.findAll()).thenReturn(users);

        List<UserResponse> result = userService.getAllUsers();

        assertEquals(5, result.size());
        assertEquals("Igor", result.get(0).getName());

        verify(userRepository).findAll();
    }

    // ===================== GET BY ID =====================

    @Test
    void getUserById_shouldReturnUser() {
        User user = UserTestFactory.makeUser();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponse result = userService.getUserById(1L);

        assertEquals("Igor", result.getName());

        verify(userRepository).findById(1L);
    }

    // ===================== NOT FOUND =====================

    @Test
    void getUserById_shouldThrowException_whenNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                userService.getUserById(1L)
        );

        verify(userRepository).findById(1L);
    }
}