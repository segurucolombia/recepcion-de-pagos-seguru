package com.gateway.transaccioneswompi.app.usecases;

import com.gateway.transaccioneswompi.domain.WompiWebhookEvent;
import com.gateway.transaccioneswompi.domain.ports.ExternalApiPort;
import com.gateway.transaccioneswompi.domain.ports.ExternalApiPort.ExternalApiResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * CASO DE USO - Lógica de Negocio (Application Layer)
 *
 * Este caso de uso orquesta la lógica de negocio:
 * 1. Recibe el evento de webhook (domain model)
 * 2. Envía los datos completos a Seguru
 * 3. Procesa las respuestas
 * 4. Retorna el resultado
 *
 * @Service le dice a Spring que esta clase es un servicio de negocio
 */
@Service
public class ProcessWompiWebhookUseCase {

    private static final Logger log = LoggerFactory.getLogger(ProcessWompiWebhookUseCase.class);

    // Inyectamos el PUERTO, no el adaptador concreto
    // Esto permite cambiar la implementación sin tocar el caso de uso
    private final ExternalApiPort externalApiPort;

    public ProcessWompiWebhookUseCase(ExternalApiPort externalApiPort) {
        this.externalApiPort = externalApiPort;
    }

    /**
     * Ejecuta el caso de uso: procesar webhook de Wompi
     *
     * Lógica de fallback:
     * 1. Intenta enviar a RESERVAS
     * 2. Si falla, intenta enviar a ALIADOS
     * 3. Si ambos fallan, devuelve error
     *
     * @param event Evento de webhook convertido a modelo de dominio
     * @return Resultado del procesamiento
     */
    public ProcessingResult execute(WompiWebhookEvent event) {
        log.info("=== Iniciando procesamiento de webhook ===");
        log.info("Evento: {}", event.getEvent());
        log.info("Transacción ID: {}", event.getData().getTransaction().getId());
        log.info("Estado: {}", event.getData().getTransaction().getStatus());

        try {
            // 1. PRIMER INTENTO: Enviar a RESERVAS
            log.info("Intentando enviar datos a RESERVAS (intento 1/2)...");
            ExternalApiResponse reservasResponse = externalApiPort.sendToReservas(event);

            if (reservasResponse.isSuccess()) {
                log.info("✓ Datos enviados exitosamente a RESERVAS");
                log.info("=== Webhook procesado exitosamente ===");
                return new ProcessingResult(
                    true,
                    "Webhook procesado y enviado a RESERVAS exitosamente"
                );
            }

            // 2. RESERVAS FALLÓ - Intentar ALIADOS como fallback
            log.warn("⚠ RESERVAS falló - Error: {}", reservasResponse.getMessage());
            log.info("Intentando enviar datos a ALIADOS (intento 2/2 - FALLBACK)...");

            ExternalApiResponse aliadosResponse = externalApiPort.sendToAliados(event);

            if (aliadosResponse.isSuccess()) {
                log.info("✓ Datos enviados exitosamente a ALIADOS (fallback)");
                log.info("=== Webhook procesado exitosamente ===");
                return new ProcessingResult(
                    true,
                    "Webhook procesado y enviado a ALIADOS exitosamente (fallback)"
                );
            }

            // 3. AMBOS FALLARON - Devolver error
            log.error("✗ Ambos endpoints fallaron");
            log.error("RESERVAS: {}", reservasResponse.getMessage());
            log.error("ALIADOS: {}", aliadosResponse.getMessage());

            return new ProcessingResult(
                false,
                String.format(
                    "Ambos endpoints fallaron. RESERVAS: %s | ALIADOS: %s",
                    reservasResponse.getMessage(),
                    aliadosResponse.getMessage()
                )
            );

        } catch (Exception e) {
            // Este catch captura cualquier error inesperado
            log.error("Error inesperado procesando webhook: {}", e.getMessage(), e);
            return new ProcessingResult(
                false,
                "Error inesperado: " + e.getMessage()
            );
        }
    }

    /**
     * Clase interna para el resultado del procesamiento
     */
    public static class ProcessingResult {
        private final boolean success;
        private final String message;

        public ProcessingResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public String toString() {
            return "ProcessingResult{" +
                    "success=" + success +
                    ", message='" + message + '\'' +
                    '}';
        }
    }
}
