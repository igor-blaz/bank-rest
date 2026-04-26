package com.example.bankcards.controller.user;

import com.example.bankcards.TestSecurityConfig;
import com.example.bankcards.dto.request.UserCreateRequest;
import com.example.bankcards.dto.response.RequestResponse;
import com.example.bankcards.factory.response.RequestResponseTestFactory;
import com.example.bankcards.service.UserRequestService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserRequestPublicController.class)
@Import(TestSecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
class UserRequestPublicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRequestService userRequestService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public UserRequestService userRequestService() {
            return Mockito.mock(UserRequestService.class);
        }
    }

    @Test
    void createUserRequest_shouldReturnCreatedRequestResponse() throws Exception {
        UserCreateRequest request = UserCreateRequest.builder()
                .name("Igor")
                .surname("Ivanov")
                .email("exampleuser@gmail.com")
                .age(23)
                .password("password")
                .build();

        RequestResponse response = RequestResponseTestFactory.makeRequestResponse(7L);

        when(userRequestService.createRequest(any(UserCreateRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/public/user-requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(7L))
                .andExpect(jsonPath("$.status").value(response.getStatus().name()));

        verify(userRequestService).createRequest(any(UserCreateRequest.class));
    }
}