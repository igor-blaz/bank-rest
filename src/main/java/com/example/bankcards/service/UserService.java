package com.example.bankcards.service;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponse createUser(UserDto dto) {
        User user = UserMapper.toEntity(dto);
        User response = userRepository.save(user);
        return UserMapper.toResponse(response);
    }
    public List<UserResponse> getAllUsers(){
       return UserMapper.toResponseList(userRepository.findAll());
    }
    public UserResponse getUserById(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new NotFoundException("User id "+ userId + " not found"));
        return UserMapper.toResponse(user);
    }
}
