package com.btg.btg_funds.controller;

import com.btg.btg_funds.entity.ClienteEntity;
import com.btg.btg_funds.service.ClienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "Operaciones relacionadas con clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping("/productos-disponibles-sucursales-visitadas")
    @Operation(
            summary = "Consultar clientes con productos disponibles en sucursales visitadas",
            description = "Retorna los clientes que tienen productos disponibles en las sucursales que han visitado"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public List<ClienteEntity> obtenerClientes() {
        return clienteService.obtenerClientesConProductosDisponiblesEnSucursalesQueVisitan();
    }
}
