package com.gateway.transaccioneswompi.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para enviar datos al PRIMER endpoint externo
 *
 * Este DTO representa el formato JSON que espera el primer servicio externo.
 * Ajusta los campos según la documentación de la API que vas a consumir.
 */
public class FirstEndpointRequest {

    @JsonProperty("transaction_id")
    private String transactionId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("amount")
    private Long amount;

    // Constructor vacío
    public FirstEndpointRequest() {
    }

    // Constructor con parámetros
    public FirstEndpointRequest(String transactionId, String status, Long amount) {
        this.transactionId = transactionId;
        this.status = status;
        this.amount = amount;
    }

    // Getters y Setters
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "FirstEndpointRequest{" +
                "transactionId='" + transactionId + '\'' +
                ", status='" + status + '\'' +
                ", amount=" + amount +
                '}';
    }
}
