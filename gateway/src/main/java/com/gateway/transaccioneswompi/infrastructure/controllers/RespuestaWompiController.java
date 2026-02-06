package com.gateway.transaccioneswompi.infrastructure.controllers;

import java.util.HashMap;
import java.util.Map;

import com.gateway.transaccioneswompi.app.usecases.ProcessWompiWebhookUseCase;
import com.gateway.transaccioneswompi.app.usecases.ProcessWompiWebhookUseCase.ProcessingResult;
import com.gateway.transaccioneswompi.domain.WompiWebhookEvent;
import com.gateway.transaccioneswompi.infrastructure.dto.WompiWebhookRequest;
import com.gateway.transaccioneswompi.infrastructure.mappers.WompiWebhookMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CONTROLADOR - Capa de Infrastructure (Adaptador de entrada)
 *
 * Este controlador recibe las peticiones HTTP y:
 * 1. Recibe el JSON como DTO (WompiWebhookRequest)
 * 2. Lo convierte a modelo de dominio usando el mapper
 * 3. Llama a la lógica de negocio (Use Case)
 * 4. Retorna una respuesta HTTP
 */
@RestController
@RequestMapping("/api-gateway/wompi")
public class RespuestaWompiController {

    private static final Logger log = LoggerFactory.getLogger(RespuestaWompiController.class);

    // Inyección de dependencias
    private final WompiWebhookMapper mapper;
    private final ProcessWompiWebhookUseCase processWebhookUseCase;

    // Constructor para inyección de dependencias
    public RespuestaWompiController(
            WompiWebhookMapper mapper,
            ProcessWompiWebhookUseCase processWebhookUseCase) {
        this.mapper = mapper;
        this.processWebhookUseCase = processWebhookUseCase;
    }

    /**
     * Endpoint que recibe el webhook de Wompi
     *
     * Según la documentación de Wompi:
     * - Debe devolver HTTP 200 si el webhook se procesó exitosamente
     * - Cualquier status diferente a 200 hará que Wompi reintente hasta 3 veces
     * - El cuerpo de la respuesta no es importante para Wompi
     *
     * Este endpoint devuelve:
     * - HTTP 200: Si al menos uno de los endpoints (Reservas o Aliados) funcionó
     * - HTTP 500: Si ambos endpoints fallaron
     *
     * @RequestBody indica que Spring debe convertir el JSON a WompiWebhookRequest
     */
    @PostMapping("/update-transaction")
    public ResponseEntity<Map<String, Object>> updateTransaction(@RequestBody WompiWebhookRequest request) {

        // 1. Log del request (DTO)
        log.info("=== WEBHOOK RECIBIDO DE WOMPI ===");
        log.info("Evento: {}", request.getEvent());

        // 2. Convertir DTO a Domain usando el mapper
        WompiWebhookEvent domainEvent = mapper.toDomain(request);
        log.info("Transacción ID: {}", domainEvent.getData().getTransaction().getId());
        log.info("Estado: {}", domainEvent.getData().getTransaction().getStatus());

        // 3. Llamar al caso de uso (lógica de negocio con fallback)
        ProcessingResult result = processWebhookUseCase.execute(domainEvent);

        // 4. Preparar respuesta según el resultado
        Map<String, Object> response = new HashMap<>();
        response.put("transactionId", domainEvent.getData().getTransaction().getId());
        response.put("event", request.getEvent());

        if (result.isSuccess()) {
            // Al menos uno de los endpoints (Reservas o Aliados) funcionó
            response.put("status", "success");
            response.put("message", result.getMessage());

            log.info("✓ Webhook procesado exitosamente: {}", result.getMessage());
            log.info("=== DEVOLVIENDO HTTP 200 A WOMPI ===");
            return ResponseEntity.ok(response);

        } else {
            // Ambos endpoints fallaron
            response.put("status", "error");
            response.put("message", result.getMessage());

            log.error("✗ Error procesando webhook: {}", result.getMessage());
            log.error("=== DEVOLVIENDO HTTP 500 A WOMPI (Wompi reintentará) ===");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
