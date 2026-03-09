package com.btg.btg_funds.controller;

import com.btg.btg_funds.dto.request.CancelSubscriptionRequest;
import com.btg.btg_funds.dto.request.SubscribeRequest;
import com.btg.btg_funds.dto.response.TransactionResponse;
import com.btg.btg_funds.service.SubscriptionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscriptions")
@Tag(name = "Suscripciones", description = "Operaciones para suscribirse, cancelar y consultar transacciones")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping
    @Operation(summary = "Suscribirse a un fondo", description = "Permite a un cliente suscribirse a un fondo de inversión")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suscripción realizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida o saldo insuficiente"),
            @ApiResponse(responseCode = "404", description = "Cliente o fondo no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<String> subscribe(@RequestBody SubscribeRequest request) {
        subscriptionService.subscribe(request);
        return ResponseEntity.ok("Suscripción realizada correctamente");
    }

    @DeleteMapping
    @Operation(summary = "Cancelar suscripción", description = "Permite cancelar una suscripción activa a un fondo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suscripción cancelada correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "404", description = "Suscripción no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<String> cancel(@RequestBody CancelSubscriptionRequest request) {
        subscriptionService.cancel(request);
        return ResponseEntity.ok("Suscripción cancelada correctamente");
    }

    @GetMapping("/transactions/{clientId}")
    @Operation(summary = "Consultar historial de transacciones", description = "Obtiene el historial de aperturas y cancelaciones de un cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transacciones obtenidas correctamente"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<TransactionResponse>> getTransactions(@PathVariable String clientId) {
        return ResponseEntity.ok(subscriptionService.getTransactionsByClientId(clientId));
    }

}
