package com.example.bankcards.controller.admin;

import com.example.bankcards.TestSecurityConfig;
import com.example.bankcards.dto.response.RequestResponse;
import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.factory.response.RequestResponseTestFactory;
import com.example.bankcards.factory.response.UserResponseTestFactory;
import com.example.bankcards.service.UserRequestService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserRequestAdminController.class)
@Import(TestSecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserRequestAdminControllerTest {

    @Autowired
    private UserRequestService userRequestService;

    @Autowired
    private MockMvc mockMvc;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public UserRequestService userRequestService() {
            return Mockito.mock(UserRequestService.class);
        }
    }

    @Test
    void rejectUserRequest_shouldReturnNoContent() throws Exception {
        mockMvc.perform(post("/admin/user-requests/{requestId}/reject", 7L))
                .andExpect(status().isNoContent());

        verify(userRequestService).rejectRequest(7L);
    }

    @Test
    void approveUserRequest_shouldReturnUserResponse() throws Exception {
        UserResponse response = UserResponseTestFactory.makeUserResponse(7L);

        when(userRequestService.approveRequest(7L)).thenReturn(response);

        mockMvc.perform(post("/admin/user-requests/{requestId}/approve", 7L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7L))
                .andExpect(jsonPath("$.name").value("Igor"))
                .andExpect(jsonPath("$.surname").value("Blazhievsky"))
                .andExpect(jsonPath("$.age").value(23))
                .andExpect(jsonPath("$.role").value("USER"));

        verify(userRequestService).approveRequest(7L);
    }

    @Test
    void getAllResponses_shouldReturnList() throws Exception {
        List<RequestResponse> list =
                RequestResponseTestFactory.makeListRequestResponse(3);

        when(userRequestService.getAllRequests()).thenReturn(list);

        mockMvc.perform(get("/admin/user-requests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id").value(list.get(0).getId()))
                .andExpect(jsonPath("$[0].status")
                        .value(list.get(0).getStatus().name()));

        verify(userRequestService).getAllRequests();
    }

    @Test
    void getResponseById_shouldReturnResponse() throws Exception {
        RequestResponse response =
                RequestResponseTestFactory.makeRequestResponse(99L);

        when(userRequestService.getRequestById(99L)).thenReturn(response);

        mockMvc.perform(get("/admin/user-requests/{requestId}", 99L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(99L))
                .andExpect(jsonPath("$.status")
                        .value(response.getStatus().name()));

        verify(userRequestService).getRequestById(99L);
    }

}
