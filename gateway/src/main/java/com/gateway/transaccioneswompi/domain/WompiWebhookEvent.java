package com.gateway.transaccioneswompi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.List;

/**
 * Evento de webhook de Wompi - Modelo de Dominio
 *
 * Esta clase representa el concepto de negocio "Evento de Webhook de Wompi".
 * En arquitectura hexagonal, el dominio NO debe depender de frameworks externos.
 * Por eso usamos tipos básicos de Java (String, Long, Instant, etc.)
 */
public class WompiWebhookEvent {

    @JsonProperty("event")
    private String event;                    // Tipo de evento: "transaction.updated"

    @JsonProperty("data")
    private TransactionData data;            // Datos de la transacción

    @JsonProperty("signature")
    private Signature signature;             // Firma para verificar autenticidad

    @JsonProperty("timestamp")
    private Long timestamp;                  // Timestamp en milisegundos

    @JsonProperty("sent_at")
    private Instant sentAt;                  // Fecha de envío del webhook

    @JsonProperty("environment")
    private String environment;              // "test" o "production"

    // Constructor vacío (necesario para frameworks como Jackson)
    public WompiWebhookEvent() {
    }

    // Constructor con todos los parámetros
    public WompiWebhookEvent(String event, TransactionData data, Signature signature,
                             Long timestamp, Instant sentAt) {
        this.event = event;
        this.data = data;
        this.signature = signature;
        this.timestamp = timestamp;
        this.sentAt = sentAt;
    }

    // GETTERS Y SETTERS
    // En Java necesitamos estos métodos para acceder y modificar los atributos privados

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public TransactionData getData() {
        return data;
    }

    public void setData(TransactionData data) {
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

    public Instant getSentAt() {
        return sentAt;
    }

    public void setSentAt(Instant sentAt) {
        this.sentAt = sentAt;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    /**
     * Clase interna que envuelve la transacción
     */
    public static class TransactionData {
        @JsonProperty("transaction")
        private Transaction transaction;

        public TransactionData() {
        }

        public TransactionData(Transaction transaction) {
            this.transaction = transaction;
        }

        public Transaction getTransaction() {
            return transaction;
        }

        public void setTransaction(Transaction transaction) {
            this.transaction = transaction;
        }
    }

    /**
     * Clase que representa una Transacción
     * Contiene toda la información del pago/transferencia
     */
    public static class Transaction {
        @JsonProperty("id")
        private String id;                   // ID único de la transacción

        @JsonProperty("created_at")
        private Instant createdAt;           // Fecha de creación

        @JsonProperty("finalized_at")
        private Instant finalizedAt;         // Fecha de finalización

        @JsonProperty("amount_in_cents")
        private Long amountInCents;          // Monto en centavos (2000 = $20.00)

        @JsonProperty("reference")
        private String reference;            // Referencia única de la transacción

        @JsonProperty("customer_email")
        private String customerEmail;        // Email del cliente

        @JsonProperty("currency")
        private String currency;             // Moneda: COP, USD, etc.

        @JsonProperty("payment_method_type")
        private String paymentMethodType;    // NEQUI, PSE, CARD, etc.

        @JsonProperty("status")
        private String status;               // APPROVED, DECLINED, PENDING, etc.

        @JsonProperty("shipping_address")
        private Object shippingAddress;      // Dirección de envío (puede ser null)

        @JsonProperty("payment_link_id")
        private String paymentLinkId;        // ID del link de pago (puede ser null)

        @JsonProperty("redirect_url")
        private String redirectUrl;          // URL de redirección

        @JsonProperty("payment_source_id")
        private String paymentSourceId;      // ID de la fuente de pago (puede ser null)

        @JsonProperty("billing_data")
        private Object billingData;          // Datos de facturación (puede ser null)

        // Campos para payouts (pueden estar ausentes en transacciones normales)
        @JsonProperty("payout_id")
        private String payoutId;             // ID del pago

        @JsonProperty("payee")
        private Payee payee;                 // Datos del beneficiario

        @JsonProperty("failure_reason")
        private FailureReason failureReason; // Razón de fallo (si aplica)

        @JsonProperty("applied_at")
        private Instant appliedAt;           // Fecha de aplicación

        public Transaction() {
        }

        public Transaction(String id, String payoutId, Long amountInCents, String status,
                          Payee payee, FailureReason failureReason, String currency,
                          Instant appliedAt, Instant createdAt) {
            this.id = id;
            this.payoutId = payoutId;
            this.amountInCents = amountInCents;
            this.status = status;
            this.payee = payee;
            this.failureReason = failureReason;
            this.currency = currency;
            this.appliedAt = appliedAt;
            this.createdAt = createdAt;
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

        public Instant getAppliedAt() {
            return appliedAt;
        }

        public void setAppliedAt(Instant appliedAt) {
            this.appliedAt = appliedAt;
        }

        public Instant getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Instant createdAt) {
            this.createdAt = createdAt;
        }

        public Instant getFinalizedAt() {
            return finalizedAt;
        }

        public void setFinalizedAt(Instant finalizedAt) {
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
     * Clase que representa al beneficiario del pago
     */
    public static class Payee {
        @JsonProperty("name")
        private String name;            // Nombre completo

        @JsonProperty("document")
        private String document;        // Número de documento

        @JsonProperty("bank")
        private String bank;            // Banco

        @JsonProperty("account_type")
        private String accountType;     // Tipo de cuenta: SAVINGS, CHECKING

        @JsonProperty("account_number")
        private String accountNumber;   // Número de cuenta

        @JsonProperty("email")
        private String email;           // Email

        public Payee() {
        }

        public Payee(String name, String document, String bank, String accountType,
                    String accountNumber, String email) {
            this.name = name;
            this.document = document;
            this.bank = bank;
            this.accountType = accountType;
            this.accountNumber = accountNumber;
            this.email = email;
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
     * Razón del fallo de una transacción
     */
    public static class FailureReason {
        @JsonProperty("code")
        private String code;       // Código de error: "C01", "C02", etc.

        @JsonProperty("message")
        private String message;    // Mensaje descriptivo del error

        public FailureReason() {
        }

        public FailureReason(String code, String message) {
            this.code = code;
            this.message = message;
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
     * Firma digital para verificar la autenticidad del webhook
     */
    public static class Signature {
        @JsonProperty("properties")
        private List<String> properties;  // Campos que se firmaron

        @JsonProperty("checksum")
        private String checksum;          // Hash de verificación

        public Signature() {
        }

        public Signature(List<String> properties, String checksum) {
            this.properties = properties;
            this.checksum = checksum;
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
