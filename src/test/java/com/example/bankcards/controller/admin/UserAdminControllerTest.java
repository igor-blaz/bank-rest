package com.example.bankcards.controller.admin;

import com.example.bankcards.TestSecurityConfig;
import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.factory.response.UserResponseTestFactory;
import com.example.bankcards.service.UserService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserAdminController.class)
@Import(TestSecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
class UserAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public UserService userService() {
            return Mockito.mock(UserService.class);
        }
    }

    @Test
    void getAllUsers_shouldReturnList() throws Exception {
        List<UserResponse> users = List.of(
                UserResponseTestFactory.makeUserResponse(1L),
                UserResponseTestFactory.makeUserResponse(2L),
                UserResponseTestFactory.makeUserResponse(3L)
        );

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Igor"))
                .andExpect(jsonPath("$[0].role").value("USER"));

        verify(userService).getAllUsers();
    }

    @Test
    void getUserById_shouldReturnUserResponse() throws Exception {
        UserResponse response = UserResponseTestFactory.makeUserResponse(7L);

        when(userService.getUserById(7L)).thenReturn(response);

        mockMvc.perform(get("/admin/users/{userId}", 7L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7L))
                .andExpect(jsonPath("$.name").value("Igor"))
                .andExpect(jsonPath("$.age").value(23))
                .andExpect(jsonPath("$.role").value("USER"));

        verify(userService).getUserById(7L);
    }
}
