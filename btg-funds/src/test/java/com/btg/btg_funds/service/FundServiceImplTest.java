package com.btg.btg_funds.service;

import com.btg.btg_funds.document.FundDocument;
import com.btg.btg_funds.repository.FundRepository;
import com.btg.btg_funds.service.impl.FundServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FundServiceImplTest {

    @Mock
    private FundRepository fundRepository;

    @InjectMocks
    private FundServiceImpl fundService;

    @Test
    void deberiaRetornarFondosActivos() {
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

        when(fundRepository.findByActiveTrue()).thenReturn(List.of(fund1, fund2));

        List<FundDocument> result = fundService.obtenerFondosActivos();

        assertEquals(2, result.size());
        assertEquals("FPV_BTG_PACTUAL_RECAUDADORA", result.get(0).getName());
        assertEquals("FDO-ACCIONES", result.get(1).getName());

        verify(fundRepository, times(1)).findByActiveTrue();
    }

    @Test
    void deberiaRetornarListaVaciaCuandoNoHayFondosActivos() {
        when(fundRepository.findByActiveTrue()).thenReturn(List.of());

        List<FundDocument> result = fundService.obtenerFondosActivos();

        assertEquals(0, result.size());
        verify(fundRepository, times(1)).findByActiveTrue();
    }
}
