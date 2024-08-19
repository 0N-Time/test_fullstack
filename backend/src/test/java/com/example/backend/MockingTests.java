package com.example.backend;

import com.example.backend.controller.AuthenticationRequest;
import com.example.backend.model.dao.Account;
import com.example.backend.model.dao.Role;
import com.example.backend.model.repository.UserRepository;
import com.example.backend.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
public class MockingTests {

    @MockBean
    private static UserRepository userRepository;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("test");
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(new Account(1, "test", "$2a$10$flm60D0HwCEvghEeOrljhOUpFbfL3sdeoCTO92q607lxzlzen0fCW", "test", BigDecimal.valueOf(0), "White", "TTESTT", Role.USER, null, null)));
    }

    @Test
    void authenticateUserThatAlreadyExists_shouldReturnValidToken_whenUserWIthNameEmailAndPassword_Test_Exists() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest("test", ".Test01");

        String expectedResponse = objectMapper.writeValueAsString(Map.of("token", "test"));

        mockMvc.perform(post("/api/auth/authenticate")
                .content(objectMapper.writeValueAsString(request))
                .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expectedResponse));
    }
}
