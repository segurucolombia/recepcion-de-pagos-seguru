package com.gateway.transaccioneswompi.domain.ports;

import com.gateway.transaccioneswompi.domain.WompiWebhookEvent;

/**
 * PUERTO (PORT) - Interface en el Domain
 *
 * En arquitectura hexagonal, un "puerto" es una INTERFACE que define
 * QUÉ necesita el dominio, pero NO CÓMO se implementa.
 *
 * El dominio dice: "Necesito enviar datos a un servicio externo"
 * La infrastructure dice: "Yo sé cómo hacerlo con HTTP, REST, etc."
 *
 * Este es un puerto de SALIDA (output port) porque el dominio
 * ENVÍA datos hacia afuera.
 */
public interface ExternalApiPort {

    /**
     * Envía el evento completo de Wompi al endpoint de Reservas Seguru
     * Los datos se envían tal cual como llegaron de Wompi
     *
     * @param event Evento completo de Wompi
     * @return Respuesta del servicio externo
     */
    ExternalApiResponse sendToReservas(WompiWebhookEvent event);

    /**
     * Envía el evento completo de Wompi al endpoint de Aliados Seguru
     * Este es el fallback cuando Reservas falla
     *
     * @param event Evento completo de Wompi
     * @return Respuesta del servicio externo
     */
    ExternalApiResponse sendToAliados(WompiWebhookEvent event);

    /**
     * Clase para la respuesta de las APIs externas
     * (Pertenece al domain porque es un concepto de negocio)
     */
    class ExternalApiResponse {
        private boolean success;
        private String message;
        private String errorCode;

        public ExternalApiResponse() {
        }

        public ExternalApiResponse(boolean success, String message, String errorCode) {
            this.success = success;
            this.message = message;
            this.errorCode = errorCode;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }

        @Override
        public String toString() {
            return "ExternalApiResponse{" +
                    "success=" + success +
                    ", message='" + message + '\'' +
                    ", errorCode='" + errorCode + '\'' +
                    '}';
        }
    }
}
