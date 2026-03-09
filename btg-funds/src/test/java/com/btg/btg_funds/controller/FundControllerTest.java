package com.btg.btg_funds.controller;

import com.btg.btg_funds.config.SecurityConfig;
import com.btg.btg_funds.document.FundDocument;
import com.btg.btg_funds.security.CustomUserDetailsService;
import com.btg.btg_funds.security.JwtAuthenticationFilter;
import com.btg.btg_funds.security.JwtService;
import com.btg.btg_funds.service.FundService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FundController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
class FundControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FundService fundService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @WithMockUser(roles = {"CLIENT"})
    void deberiaRetornarFondosActivosComoCliente() throws Exception {
        FundDocument fund1 = FundDocument.builder()
                .id("1")
                .name("FPV_BTG_PACTUAL_RECAUDADORA")
                .minimumAmount(75000)
                .category("FPV")
                .active(true)
                .build();

        FundDocument fund2 = FundDocument.builder()
                .id("2")
                .name("FDO-ACCIONES")
                .minimumAmount(125000)
                .category("FIC")
                .active(true)
                .build();

        when(fundService.obtenerFondosActivos()).thenReturn(List.of(fund1, fund2));

        mockMvc.perform(get("/api/funds")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("FPV_BTG_PACTUAL_RECAUDADORA"))
                .andExpect(jsonPath("$[0].minimumAmount").value(75000))
                .andExpect(jsonPath("$[0].category").value("FPV"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].name").value("FDO-ACCIONES"))
                .andExpect(jsonPath("$[1].minimumAmount").value(125000))
                .andExpect(jsonPath("$[1].category").value("FIC"));
    }

    @Test
    void deberiaRetornarForbiddenCuandoNoHayUsuario() throws Exception {
        mockMvc.perform(get("/api/funds")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
