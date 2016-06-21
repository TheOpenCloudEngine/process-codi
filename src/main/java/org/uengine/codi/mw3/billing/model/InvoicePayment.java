package org.uengine.codi.mw3.billing.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.Nullable;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Created by uEngineYBS on 2016-06-21.
 */
public class InvoicePayment extends Payment {

    private UUID targetInvoiceId;

    public InvoicePayment() {
    }

    @JsonCreator
    public InvoicePayment(@JsonProperty("targetInvoiceId") final UUID targetInvoiceId,
                          @JsonProperty("accountId") final UUID accountId,
                          @JsonProperty("paymentId") final UUID paymentId,
                          @JsonProperty("paymentNumber") final Integer paymentNumber,
                          @JsonProperty("paymentExternalKey") final String paymentExternalKey,
                          @JsonProperty("authAmount") final BigDecimal authAmount,
                          @JsonProperty("capturedAmount") final BigDecimal capturedAmount,
                          @JsonProperty("purchasedAmount") final BigDecimal purchasedAmount,
                          @JsonProperty("refundedAmount") final BigDecimal refundedAmount,
                          @JsonProperty("creditedAmount") final BigDecimal creditedAmount,
                          @JsonProperty("currency") final String currency,
                          @JsonProperty("paymentMethodId") final UUID paymentMethodId,
                          @JsonProperty("transactions") final List<PaymentTransaction> paymentTransactions,
                          @JsonProperty("auditLogs") @Nullable final List<AuditLog> auditLogs) {
        super(accountId, paymentId, paymentNumber, paymentExternalKey, authAmount, capturedAmount, purchasedAmount, refundedAmount, creditedAmount,
                currency, paymentMethodId, paymentTransactions, auditLogs);
        this.targetInvoiceId = targetInvoiceId;
    }

    public UUID getTargetInvoiceId() {
        return targetInvoiceId;
    }

    public void setTargetInvoiceId(final UUID targetInvoiceId) {
        this.targetInvoiceId = targetInvoiceId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InvoicePayment)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        final InvoicePayment that = (InvoicePayment) o;

        if (targetInvoiceId != null ? !targetInvoiceId.equals(that.targetInvoiceId) : that.targetInvoiceId != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (targetInvoiceId != null ? targetInvoiceId.hashCode() : 0);
        return result;
    }
}