package com.gateway.transaccioneswompi.infrastructure.adapters;

import com.gateway.transaccioneswompi.domain.WompiWebhookEvent;
import com.gateway.transaccioneswompi.domain.ports.ExternalApiPort;
import com.gateway.transaccioneswompi.infrastructure.dto.ExternalApiResponseDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

/**
 * ADAPTADOR - Implementación del Puerto (Infrastructure)
 *
 * Esta clase IMPLEMENTA la interface ExternalApiPort.
 * Aquí es donde realmente hacemos las llamadas HTTP a las APIs externas.
 *
 * Usamos RestTemplate (de Spring) para hacer peticiones HTTP POST.
 *
 * @Component le dice a Spring que maneje esta clase como un bean
 */
@Component
public class ExternalApiAdapter implements ExternalApiPort {

    private static final Logger log = LoggerFactory.getLogger(ExternalApiAdapter.class);

    // RestTemplate es la herramienta de Spring para hacer peticiones HTTP
    private final RestTemplate restTemplate;

    // @Value inyecta valores desde application.properties
    @Value("${external.api.reservas.seguru.url}")
    private String reservasSegururUrl;

    @Value("${external.api.aliados.seguru.url}")
    private String aliadosSegururUrl;

    // Constructor - Spring inyecta RestTemplate automáticamente
    public ExternalApiAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Envía el evento completo de Wompi al endpoint de Reservas Seguru
     * Los datos se envían TAL CUAL como llegaron de Wompi
     */
    @Override
    public ExternalApiResponse sendToReservas(WompiWebhookEvent event) {
        log.info("=== Iniciando envío a RESERVAS Seguru ===");
        log.info("URL COMPLETA destino: {}", reservasSegururUrl);
        log.info("Transacción ID: {}", event.getData().getTransaction().getId());
        log.info("Event type: {}", event.getEvent());

        try {
            // 1. Configurar headers HTTP
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 2. Crear la entidad HTTP con el evento completo
            // El evento se serializa automáticamente a JSON
            HttpEntity<WompiWebhookEvent> entity = new HttpEntity<>(event, headers);

            log.info(">>> POST a: {}", reservasSegururUrl);
            log.info(">>> Headers: {}", headers);
            log.info("Enviando datos a Reservas...");

            // 3. Hacer la petición POST
            ResponseEntity<ExternalApiResponseDto> response = restTemplate.exchange(
                reservasSegururUrl,
                HttpMethod.POST,
                entity,
                ExternalApiResponseDto.class
            );

            // 4. Procesar respuesta exitosa
            ExternalApiResponseDto responseDto = response.getBody();
            HttpStatus statusCode = (HttpStatus) response.getStatusCode();

            log.info("✓ Respuesta exitosa de RESERVAS - Status Code: {}", statusCode.value());
            log.info("Respuesta: {}", responseDto);

            return new ExternalApiResponse(
                true,
                responseDto != null ? responseDto.getMessage() : "Enviado exitosamente a Reservas",
                null
            );

        } catch (HttpClientErrorException e) {
            // Error 4xx (400, 401, 404, etc.)
            log.error("✗ Error del cliente al llamar a RESERVAS - Status: {} - Body: {}",
                     e.getStatusCode().value(), e.getResponseBodyAsString());
            return new ExternalApiResponse(
                false,
                "Error del cliente: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(),
                "HTTP_CLIENT_ERROR_" + e.getStatusCode().value()
            );

        } catch (HttpServerErrorException e) {
            // Error 5xx (500, 502, 503, etc.)
            log.error("✗ Error del servidor en RESERVAS - Status: {} - Body: {}",
                     e.getStatusCode().value(), e.getResponseBodyAsString());
            return new ExternalApiResponse(
                false,
                "Error del servidor: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(),
                "HTTP_SERVER_ERROR_" + e.getStatusCode().value()
            );

        } catch (ResourceAccessException e) {
            // Error de red/timeout/conexión
            log.error("✗ Error de conexión con RESERVAS: {}", e.getMessage(), e);
            return new ExternalApiResponse(
                false,
                "Error de conexión: " + e.getMessage(),
                "CONNECTION_ERROR"
            );

        } catch (Exception e) {
            // Cualquier otro error
            log.error("✗ Error inesperado al llamar a RESERVAS: {}", e.getMessage(), e);
            return new ExternalApiResponse(
                false,
                "Error inesperado: " + e.getMessage(),
                "UNEXPECTED_ERROR"
            );
        }
    }

    /**
     * Envía el evento completo de Wompi al endpoint de Aliados Seguru
     * Este método se usa como FALLBACK cuando Reservas falla
     */
    @Override
    public ExternalApiResponse sendToAliados(WompiWebhookEvent event) {
        log.info("=== Iniciando envío a ALIADOS Seguru (FALLBACK) ===");
        log.info("URL COMPLETA destino: {}", aliadosSegururUrl);
        log.info("Transacción ID: {}", event.getData().getTransaction().getId());
        log.info("Event type: {}", event.getEvent());

        try {
            // 1. Configurar headers HTTP
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 2. Crear la entidad HTTP con el evento completo
            HttpEntity<WompiWebhookEvent> entity = new HttpEntity<>(event, headers);

            log.info(">>> POST a: {}", aliadosSegururUrl);
            log.info(">>> Headers: {}", headers);
            log.info("Enviando datos a Aliados...");

            // 3. Hacer la petición POST
            ResponseEntity<ExternalApiResponseDto> response = restTemplate.exchange(
                aliadosSegururUrl,
                HttpMethod.POST,
                entity,
                ExternalApiResponseDto.class
            );

            // 4. Procesar respuesta exitosa
            ExternalApiResponseDto responseDto = response.getBody();
            HttpStatus statusCode = (HttpStatus) response.getStatusCode();

            log.info("✓ Respuesta exitosa de ALIADOS - Status Code: {}", statusCode.value());
            log.info("Respuesta: {}", responseDto);

            return new ExternalApiResponse(
                true,
                responseDto != null ? responseDto.getMessage() : "Enviado exitosamente a Aliados",
                null
            );

        } catch (HttpClientErrorException e) {
            log.error("✗ Error del cliente al llamar a ALIADOS - Status: {} - Body: {}",
                     e.getStatusCode().value(), e.getResponseBodyAsString());
            return new ExternalApiResponse(
                false,
                "Error del cliente: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(),
                "HTTP_CLIENT_ERROR_" + e.getStatusCode().value()
            );

        } catch (HttpServerErrorException e) {
            log.error("✗ Error del servidor en ALIADOS - Status: {} - Body: {}",
                     e.getStatusCode().value(), e.getResponseBodyAsString());
            return new ExternalApiResponse(
                false,
                "Error del servidor: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(),
                "HTTP_SERVER_ERROR_" + e.getStatusCode().value()
            );

        } catch (ResourceAccessException e) {
            log.error("✗ Error de conexión con ALIADOS: {}", e.getMessage(), e);
            return new ExternalApiResponse(
                false,
                "Error de conexión: " + e.getMessage(),
                "CONNECTION_ERROR"
            );

        } catch (Exception e) {
            log.error("✗ Error inesperado al llamar a ALIADOS: {}", e.getMessage(), e);
            return new ExternalApiResponse(
                false,
                "Error inesperado: " + e.getMessage(),
                "UNEXPECTED_ERROR"
            );
        }
    }
}
