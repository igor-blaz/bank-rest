package com.example.bankcards.controller.admin;

import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
public class UserAdminController {
    private final UserService userService;

    @GetMapping
    public List<UserResponse> getAllUsers(){
       return userService.getAllUsers();
    }
    @GetMapping("/{userId}")
    public UserResponse getUserById(@PathVariable  Long userId){
        return userService.getUserById(userId);
    }

}
