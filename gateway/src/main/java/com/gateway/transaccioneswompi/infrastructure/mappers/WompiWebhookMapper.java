package com.gateway.transaccioneswompi.infrastructure.mappers;

import com.gateway.transaccioneswompi.domain.WompiWebhookEvent;
import com.gateway.transaccioneswompi.infrastructure.dto.WompiWebhookRequest;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * MAPPER - Convierte entre DTO (Infrastructure) y Domain
 *
 * En arquitectura hexagonal, el mapper es un adaptador que traduce
 * entre el mundo externo (HTTP/JSON) y el mundo del dominio (lógica de negocio).
 *
 * @Component indica que Spring puede crear una instancia de esta clase
 * automáticamente (inyección de dependencias)
 */
@Component
public class WompiWebhookMapper {

    /**
     * Convierte de DTO (lo que llega por HTTP) a Domain (modelo de negocio)
     *
     * @param request El DTO recibido del controlador
     * @return El objeto de dominio listo para la lógica de negocio
     */
    public WompiWebhookEvent toDomain(WompiWebhookRequest request) {
        if (request == null) {
            return null;
        }

        WompiWebhookEvent event = new WompiWebhookEvent();
        event.setEvent(request.getEvent());
        event.setTimestamp(request.getTimestamp());
        event.setEnvironment(request.getEnvironment());

        // Convertir String a Instant (fecha)
        if (request.getSentAt() != null) {
            event.setSentAt(Instant.parse(request.getSentAt()));
        }

        // Mapear la data
        if (request.getData() != null) {
            event.setData(mapTransactionData(request.getData()));
        }

        // Mapear la signature
        if (request.getSignature() != null) {
            event.setSignature(mapSignature(request.getSignature()));
        }

        return event;
    }

    /**
     * Mapea el wrapper de TransactionData
     */
    private WompiWebhookEvent.TransactionData mapTransactionData(WompiWebhookRequest.DataWrapper dataWrapper) {
        WompiWebhookEvent.TransactionData data = new WompiWebhookEvent.TransactionData();

        if (dataWrapper.getTransaction() != null) {
            data.setTransaction(mapTransaction(dataWrapper.getTransaction()));
        }

        return data;
    }

    /**
     * Mapea la Transaction completa
     */
    private WompiWebhookEvent.Transaction mapTransaction(WompiWebhookRequest.Transaction reqTx) {
        WompiWebhookEvent.Transaction tx = new WompiWebhookEvent.Transaction();

        // Mapear todos los campos básicos
        tx.setId(reqTx.getId());
        tx.setAmountInCents(reqTx.getAmountInCents());
        tx.setReference(reqTx.getReference());
        tx.setCustomerEmail(reqTx.getCustomerEmail());
        tx.setCurrency(reqTx.getCurrency());
        tx.setPaymentMethodType(reqTx.getPaymentMethodType());
        tx.setStatus(reqTx.getStatus());
        tx.setShippingAddress(reqTx.getShippingAddress());
        tx.setPaymentLinkId(reqTx.getPaymentLinkId());
        tx.setRedirectUrl(reqTx.getRedirectUrl());
        tx.setPaymentSourceId(reqTx.getPaymentSourceId());
        tx.setBillingData(reqTx.getBillingData());

        // Campos para payouts (pueden ser null)
        tx.setPayoutId(reqTx.getPayoutId());

        // Convertir fechas String a Instant
        if (reqTx.getCreatedAt() != null) {
            tx.setCreatedAt(Instant.parse(reqTx.getCreatedAt()));
        }
        if (reqTx.getFinalizedAt() != null) {
            tx.setFinalizedAt(Instant.parse(reqTx.getFinalizedAt()));
        }
        if (reqTx.getAppliedAt() != null) {
            tx.setAppliedAt(Instant.parse(reqTx.getAppliedAt()));
        }

        // Mapear objetos anidados (pueden ser null en transacciones normales)
        if (reqTx.getPayee() != null) {
            tx.setPayee(mapPayee(reqTx.getPayee()));
        }

        if (reqTx.getFailureReason() != null) {
            tx.setFailureReason(mapFailureReason(reqTx.getFailureReason()));
        }

        return tx;
    }

    /**
     * Mapea el Payee (beneficiario)
     */
    private WompiWebhookEvent.Payee mapPayee(WompiWebhookRequest.Payee reqPayee) {
        WompiWebhookEvent.Payee payee = new WompiWebhookEvent.Payee();

        payee.setName(reqPayee.getName());
        payee.setDocument(reqPayee.getDocument());
        payee.setBank(reqPayee.getBank());
        payee.setAccountType(reqPayee.getAccountType());
        payee.setAccountNumber(reqPayee.getAccountNumber());
        payee.setEmail(reqPayee.getEmail());

        return payee;
    }

    /**
     * Mapea el FailureReason (razón de fallo)
     */
    private WompiWebhookEvent.FailureReason mapFailureReason(WompiWebhookRequest.FailureReason reqReason) {
        WompiWebhookEvent.FailureReason reason = new WompiWebhookEvent.FailureReason();

        reason.setCode(reqReason.getCode());
        reason.setMessage(reqReason.getMessage());

        return reason;
    }

    /**
     * Mapea la Signature (firma digital)
     */
    private WompiWebhookEvent.Signature mapSignature(WompiWebhookRequest.Signature reqSig) {
        WompiWebhookEvent.Signature signature = new WompiWebhookEvent.Signature();

        signature.setProperties(reqSig.getProperties());
        signature.setChecksum(reqSig.getChecksum());

        return signature;
    }
}
