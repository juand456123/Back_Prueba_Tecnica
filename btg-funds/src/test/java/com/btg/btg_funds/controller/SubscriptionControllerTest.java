package com.btg.btg_funds.controller;

import com.btg.btg_funds.config.SecurityConfig;
import com.btg.btg_funds.dto.request.CancelSubscriptionRequest;
import com.btg.btg_funds.dto.request.SubscribeRequest;
import com.btg.btg_funds.dto.response.TransactionResponse;
import com.btg.btg_funds.security.CustomUserDetailsService;
import com.btg.btg_funds.security.JwtAuthenticationFilter;
import com.btg.btg_funds.security.JwtService;
import com.btg.btg_funds.service.SubscriptionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubscriptionController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
class SubscriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private SubscriptionService subscriptionService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @WithMockUser(roles = {"CLIENT"})
    void deberiaSuscribirCorrectamente() throws Exception {
        SubscribeRequest request = new SubscribeRequest();
        request.setClientId("client1");
        request.setFundId("fund1");
        request.setAmount(75000.0);

        doNothing().when(subscriptionService).subscribe(any(SubscribeRequest.class));

        mockMvc.perform(post("/api/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Suscripción realizada correctamente"));
    }

    @Test
    @WithMockUser(roles = {"CLIENT"})
    void deberiaCancelarSuscripcionCorrectamente() throws Exception {
        CancelSubscriptionRequest request = new CancelSubscriptionRequest();
        request.setClientId("client1");
        request.setFundId("fund1");

        doNothing().when(subscriptionService).cancel(any(CancelSubscriptionRequest.class));

        mockMvc.perform(delete("/api/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Suscripción cancelada correctamente"));
    }

    @Test
    @WithMockUser(roles = {"CLIENT"})
    void deberiaRetornarTransaccionesCorrectamenteComoCliente() throws Exception {
        TransactionResponse transaction = new TransactionResponse(
                "tx1", "client1", "fund1", "SUBSCRIPTION", 75000.0,
                LocalDateTime.of(2026, 3, 8, 10, 0)
        );

        when(subscriptionService.getTransactionsByClientId("client1")).thenReturn(List.of(transaction));

        mockMvc.perform(get("/api/subscriptions/transactions/client1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("tx1"))
                .andExpect(jsonPath("$[0].clientId").value("client1"))
                .andExpect(jsonPath("$[0].fundId").value("fund1"))
                .andExpect(jsonPath("$[0].type").value("SUBSCRIPTION"))
                .andExpect(jsonPath("$[0].amount").value(75000.0));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void deberiaRetornarTransaccionesCorrectamenteComoAdmin() throws Exception {
        TransactionResponse transaction = new TransactionResponse(
                "tx1", "client1", "fund1", "SUBSCRIPTION", 75000.0,
                LocalDateTime.of(2026, 3, 8, 10, 0)
        );

        when(subscriptionService.getTransactionsByClientId("client1")).thenReturn(List.of(transaction));

        mockMvc.perform(get("/api/subscriptions/transactions/client1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deberiaRetornarForbiddenSinUsuario() throws Exception {
        mockMvc.perform(post("/api/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "clientId":"client1",
                                  "fundId":"fund1",
                                  "amount":75000.0
                                }
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void deberiaRetornarForbiddenCuandoAdminIntentaSuscribirse() throws Exception {
        mockMvc.perform(post("/api/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "clientId":"client1",
                                  "fundId":"fund1",
                                  "amount":75000.0
                                }
                                """))
                .andExpect(status().isForbidden());
    }
}
