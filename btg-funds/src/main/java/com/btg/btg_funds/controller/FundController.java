package com.btg.btg_funds.controller;

import com.btg.btg_funds.document.FundDocument;
import com.btg.btg_funds.service.FundService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/funds")
@Tag(name = "Fondos", description = "Operaciones relacionadas con los fondos de inversión")
@RequiredArgsConstructor
public class FundController {

    private final FundService fundService;

    @GetMapping
    @Operation(summary = "Obtener todos los fondos disponibles", description = "Retorna la lista de fondos de inversión disponibles en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fondos obtenidos correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public List<FundDocument> obtenerFondosActivos() {
        return fundService.obtenerFondosActivos();
    }
}
