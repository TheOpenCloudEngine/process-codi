package org.uengine.codi.mw3.billing.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.Nullable;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Created by uEngineYBS on 2016-06-20.
 */
public class Credit extends BillingObject {

    private BigDecimal creditAmount;
    private UUID invoiceId;
    private String invoiceNumber;
    private String effectiveDate;
    private UUID accountId;

    public Credit() {}

    @JsonCreator
    public Credit(@JsonProperty("creditAmount") final BigDecimal creditAmount,
                  @JsonProperty("invoiceId") final UUID invoiceId,
                  @JsonProperty("invoiceNumber") final String invoiceNumber,
                  @JsonProperty("effectiveDate") final String effectiveDate,
                  @JsonProperty("accountId") final UUID accountId,
                  @JsonProperty("auditLogs") @Nullable final List<AuditLog> auditLogs) {
        super(auditLogs);
        this.creditAmount = creditAmount;
        this.invoiceId = invoiceId;
        this.invoiceNumber = invoiceNumber;
        this.effectiveDate = effectiveDate;
        this.accountId = accountId;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(final BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public UUID getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(final UUID invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(final String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(final String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(final UUID accountId) {
        this.accountId = accountId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Credit{");
        sb.append("creditAmount=").append(creditAmount);
        sb.append(", invoiceId='").append(invoiceId).append('\'');
        sb.append(", invoiceNumber='").append(invoiceNumber).append('\'');
        sb.append(", effectiveDate=").append(effectiveDate);
        sb.append(", accountId='").append(accountId).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Credit credit = (Credit) o;

        if (accountId != null ? !accountId.equals(credit.accountId) : credit.accountId != null) {
            return false;
        }
        if (creditAmount != null ? creditAmount.compareTo(credit.creditAmount) != 0 : credit.creditAmount != null) {
            return false;
        }
        if (effectiveDate != null ? effectiveDate.compareTo(credit.effectiveDate) != 0 : credit.effectiveDate != null) {
            return false;
        }
        if (invoiceId != null ? !invoiceId.equals(credit.invoiceId) : credit.invoiceId != null) {
            return false;
        }
        if (invoiceNumber != null ? !invoiceNumber.equals(credit.invoiceNumber) : credit.invoiceNumber != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = creditAmount != null ? creditAmount.hashCode() : 0;
        result = 31 * result + (invoiceId != null ? invoiceId.hashCode() : 0);
        result = 31 * result + (invoiceNumber != null ? invoiceNumber.hashCode() : 0);
        result = 31 * result + (effectiveDate != null ? effectiveDate.hashCode() : 0);
        result = 31 * result + (accountId != null ? accountId.hashCode() : 0);
        return result;
    }
}