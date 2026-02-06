package com.gateway.transaccioneswompi.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para enviar datos al SEGUNDO endpoint externo
 *
 * Este DTO representa el formato JSON que espera el segundo servicio externo.
 * Ajusta los campos según la documentación de la API que vas a consumir.
 */
public class SecondEndpointRequest {

    @JsonProperty("transaction_id")
    private String transactionId;

    @JsonProperty("payee_email")
    private String payeeEmail;

    @JsonProperty("failure_reason")
    private String failureReason;

    // Constructor vacío
    public SecondEndpointRequest() {
    }

    // Constructor con parámetros
    public SecondEndpointRequest(String transactionId, String payeeEmail, String failureReason) {
        this.transactionId = transactionId;
        this.payeeEmail = payeeEmail;
        this.failureReason = failureReason;
    }

    // Getters y Setters
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getPayeeEmail() {
        return payeeEmail;
    }

    public void setPayeeEmail(String payeeEmail) {
        this.payeeEmail = payeeEmail;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    @Override
    public String toString() {
        return "SecondEndpointRequest{" +
                "transactionId='" + transactionId + '\'' +
                ", payeeEmail='" + payeeEmail + '\'' +
                ", failureReason='" + failureReason + '\'' +
                '}';
    }
}
