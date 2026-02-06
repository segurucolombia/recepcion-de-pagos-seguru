package com.gateway.transaccioneswompi.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * DTO (Data Transfer Object) para recibir el webhook de Wompi
 *
 * Esta clase pertenece a la capa de INFRASTRUCTURE (capa externa).
 * Su única responsabilidad es mapear el JSON que llega por HTTP al mundo Java.
 *
 * Usamos @JsonProperty para mapear los nombres JSON a atributos Java.
 * Ejemplo: "amountInCents" en JSON se mapea a "amountInCents" en Java
 */
public class WompiWebhookRequest {

    @JsonProperty("event")
    private String event;

    @JsonProperty("data")
    private DataWrapper data;

    @JsonProperty("signature")
    private Signature signature;

    @JsonProperty("timestamp")
    private Long timestamp;

    @JsonProperty("sent_at")
    private String sentAt;  // String porque lo convertiremos después

    @JsonProperty("environment")
    private String environment;  // "test" o "production"

    // Constructor vacío (OBLIGATORIO para que Jackson pueda deserializar)
    public WompiWebhookRequest() {
    }

    // Getters y Setters
    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public DataWrapper getData() {
        return data;
    }

    public void setData(DataWrapper data) {
        this.data = data;
    }

    public Signature getSignature() {
        return signature;
    }

    public void setSignature(Signature signature) {
        this.signature = signature;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSentAt() {
        return sentAt;
    }

    public void setSentAt(String sentAt) {
        this.sentAt = sentAt;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    /**
     * Wrapper para el campo "data"
     */
    public static class DataWrapper {

        @JsonProperty("transaction")
        private Transaction transaction;

        public DataWrapper() {
        }

        public Transaction getTransaction() {
            return transaction;
        }

        public void setTransaction(Transaction transaction) {
            this.transaction = transaction;
        }
    }

    /**
     * Clase Transaction del DTO
     */
    public static class Transaction {

        @JsonProperty("id")
        private String id;

        @JsonProperty("created_at")
        private String createdAt;

        @JsonProperty("finalized_at")
        private String finalizedAt;

        @JsonProperty("amount_in_cents")
        private Long amountInCents;

        @JsonProperty("reference")
        private String reference;

        @JsonProperty("customer_email")
        private String customerEmail;

        @JsonProperty("currency")
        private String currency;

        @JsonProperty("payment_method_type")
        private String paymentMethodType;

        @JsonProperty("status")
        private String status;

        @JsonProperty("shipping_address")
        private Object shippingAddress;  // null o un objeto

        @JsonProperty("payment_link_id")
        private String paymentLinkId;

        @JsonProperty("redirect_url")
        private String redirectUrl;

        @JsonProperty("payment_source_id")
        private String paymentSourceId;

        @JsonProperty("billing_data")
        private Object billingData;  // null o un objeto

        // Campos del modelo anterior (para payouts)
        @JsonProperty("payoutId")
        private String payoutId;

        @JsonProperty("payee")
        private Payee payee;

        @JsonProperty("failureReason")
        private FailureReason failureReason;

        @JsonProperty("appliedAt")
        private String appliedAt;

        public Transaction() {
        }

        // Getters y Setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPayoutId() {
            return payoutId;
        }

        public void setPayoutId(String payoutId) {
            this.payoutId = payoutId;
        }

        public Long getAmountInCents() {
            return amountInCents;
        }

        public void setAmountInCents(Long amountInCents) {
            this.amountInCents = amountInCents;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Payee getPayee() {
            return payee;
        }

        public void setPayee(Payee payee) {
            this.payee = payee;
        }

        public FailureReason getFailureReason() {
            return failureReason;
        }

        public void setFailureReason(FailureReason failureReason) {
            this.failureReason = failureReason;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getAppliedAt() {
            return appliedAt;
        }

        public void setAppliedAt(String appliedAt) {
            this.appliedAt = appliedAt;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getFinalizedAt() {
            return finalizedAt;
        }

        public void setFinalizedAt(String finalizedAt) {
            this.finalizedAt = finalizedAt;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public String getCustomerEmail() {
            return customerEmail;
        }

        public void setCustomerEmail(String customerEmail) {
            this.customerEmail = customerEmail;
        }

        public String getPaymentMethodType() {
            return paymentMethodType;
        }

        public void setPaymentMethodType(String paymentMethodType) {
            this.paymentMethodType = paymentMethodType;
        }

        public Object getShippingAddress() {
            return shippingAddress;
        }

        public void setShippingAddress(Object shippingAddress) {
            this.shippingAddress = shippingAddress;
        }

        public String getPaymentLinkId() {
            return paymentLinkId;
        }

        public void setPaymentLinkId(String paymentLinkId) {
            this.paymentLinkId = paymentLinkId;
        }

        public String getRedirectUrl() {
            return redirectUrl;
        }

        public void setRedirectUrl(String redirectUrl) {
            this.redirectUrl = redirectUrl;
        }

        public String getPaymentSourceId() {
            return paymentSourceId;
        }

        public void setPaymentSourceId(String paymentSourceId) {
            this.paymentSourceId = paymentSourceId;
        }

        public Object getBillingData() {
            return billingData;
        }

        public void setBillingData(Object billingData) {
            this.billingData = billingData;
        }
    }

    /**
     * Clase Payee del DTO
     */
    public static class Payee {

        @JsonProperty("name")
        private String name;

        @JsonProperty("document")
        private String document;

        @JsonProperty("bank")
        private String bank;

        @JsonProperty("accountType")
        private String accountType;

        @JsonProperty("accountNumber")
        private String accountNumber;

        @JsonProperty("email")
        private String email;

        public Payee() {
        }

        // Getters y Setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDocument() {
            return document;
        }

        public void setDocument(String document) {
            this.document = document;
        }

        public String getBank() {
            return bank;
        }

        public void setBank(String bank) {
            this.bank = bank;
        }

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    /**
     * Clase FailureReason del DTO
     */
    public static class FailureReason {

        @JsonProperty("code")
        private String code;

        @JsonProperty("message")
        private String message;

        public FailureReason() {
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    /**
     * Clase Signature del DTO
     */
    public static class Signature {

        @JsonProperty("properties")
        private List<String> properties;

        @JsonProperty("checksum")
        private String checksum;

        public Signature() {
        }

        public List<String> getProperties() {
            return properties;
        }

        public void setProperties(List<String> properties) {
            this.properties = properties;
        }

        public String getChecksum() {
            return checksum;
        }

        public void setChecksum(String checksum) {
            this.checksum = checksum;
        }
    }
}
