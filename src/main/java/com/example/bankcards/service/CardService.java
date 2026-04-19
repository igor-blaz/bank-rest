package com.example.bankcards.service;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.util.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;

    public UserResponse createCards(UserDto dto) {
        User user = UserMapper.toEntity(dto);
        User response = userRepository.save(user);
        return UserMapper.toResponse(response);
    }
}
