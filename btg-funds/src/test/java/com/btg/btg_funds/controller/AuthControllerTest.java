package com.btg.btg_funds.controller;

import com.btg.btg_funds.config.SecurityConfig;
import com.btg.btg_funds.dto.request.LoginRequest;
import com.btg.btg_funds.dto.request.RegisterRequest;
import com.btg.btg_funds.dto.response.AuthResponse;
import com.btg.btg_funds.security.CustomUserDetailsService;
import com.btg.btg_funds.security.JwtAuthenticationFilter;
import com.btg.btg_funds.security.JwtService;
import com.btg.btg_funds.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void deberiaRegistrarUsuarioCorrectamente() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("juan");
        request.setPassword("123456");
        request.setName("Juan Diego");
        request.setEmail("juan@test.com");
        request.setPhone("3001234567");
        request.setNotificationPreference("EMAIL");

        doNothing().when(authService).register(any(RegisterRequest.class));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Usuario registrado correctamente"));
    }

    @Test
    void deberiaHacerLoginCorrectamente() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("juan");
        request.setPassword("123456");

        AuthResponse response = new AuthResponse();
        response.setToken("fake-jwt-token");

        when(authService.login(any(LoginRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}