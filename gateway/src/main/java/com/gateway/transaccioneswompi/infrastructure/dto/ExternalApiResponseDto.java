package com.gateway.transaccioneswompi.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO genérico para recibir respuestas de las APIs externas
 *
 * Ajusta los campos según lo que retornen tus APIs.
 * Si cada API retorna un formato diferente, crea DTOs separados.
 */
public class ExternalApiResponseDto {

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("message")
    private String message;

    @JsonProperty("error_code")
    private String errorCode;

    @JsonProperty("data")
    private Object data;  // Datos adicionales que pueda retornar

    public ExternalApiResponseDto() {
    }

    // Getters y Setters
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ExternalApiResponseDto{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", data=" + data +
                '}';
    }
}
