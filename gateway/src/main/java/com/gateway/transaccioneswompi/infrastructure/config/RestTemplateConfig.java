package com.gateway.transaccioneswompi.infrastructure.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * CONFIGURACIÓN - RestTemplate
 *
 * @Configuration le dice a Spring que esta clase contiene configuración
 * @Bean le dice a Spring que cree y maneje el objeto RestTemplate
 *
 * RestTemplate es la herramienta de Spring para hacer peticiones HTTP.
 */
@Configuration
public class RestTemplateConfig {

    /**
     * Crea y configura un RestTemplate
     *
     * @param builder Constructor proporcionado por Spring Boot
     * @return RestTemplate configurado y listo para usar
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
            .setConnectTimeout(Duration.ofSeconds(10))  // Timeout de conexión: 10 segundos
            .setReadTimeout(Duration.ofSeconds(10))     // Timeout de lectura: 10 segundos
            .build();
    }
}
