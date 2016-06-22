package org.uengine.codi.mw3.billing.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.Nullable;
import org.joda.time.LocalDate;
import org.uengine.codi.mw3.billing.catalog.api.Currency;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Created by uEngineYBS on 2016-06-20.
 */
public class Invoice extends BillingObject {

    private BigDecimal amount;
    private Currency currency;
    private UUID invoiceId;
    private String invoiceDate;
    private String targetDate;
    private Integer invoiceNumber;
    private BigDecimal balance;
    private BigDecimal creditAdj;
    private BigDecimal refundAdj;
    private UUID accountId;
    private List<InvoiceItem> items;
    private String bundleKeys;
    private List<Credit> credits;

    public Invoice() {}

    @JsonCreator
    public Invoice(@JsonProperty("amount") final BigDecimal amount,
                   @JsonProperty("currency") final Currency currency,
                   @JsonProperty("creditAdj") final BigDecimal creditAdj,
                   @JsonProperty("refundAdj") final BigDecimal refundAdj,
                   @JsonProperty("invoiceId") final UUID invoiceId,
                   @JsonProperty("invoiceDate") final String invoiceDate,
                   @JsonProperty("targetDate") final String targetDate,
                   @JsonProperty("invoiceNumber") final Integer invoiceNumber,
                   @JsonProperty("balance") final BigDecimal balance,
                   @JsonProperty("accountId") final UUID accountId,
                   @JsonProperty("externalBundleKeys") final String bundleKeys,
                   @JsonProperty("credits") final List<Credit> credits,
                   @JsonProperty("items") final List<InvoiceItem> items,
                   @JsonProperty("auditLogs") @Nullable final List<AuditLog> auditLogs) {
        super(auditLogs);
        this.amount = amount;
        this.currency = currency;
        this.creditAdj = creditAdj;
        this.refundAdj = refundAdj;
        this.invoiceId = invoiceId;
        this.invoiceDate = invoiceDate;
        this.targetDate = targetDate;
        this.invoiceNumber = invoiceNumber;
        this.balance = balance;
        this.accountId = accountId;
        this.bundleKeys = bundleKeys;
        this.credits = credits;
        this.items = items;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(final Currency currency) {
        this.currency = currency;
    }

    public UUID getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(final UUID invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(final String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(final String targetDate) {
        this.targetDate = targetDate;
    }

    public Integer getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(final Integer invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(final BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getCreditAdj() {
        return creditAdj;
    }

    public void setCreditAdj(final BigDecimal creditAdj) {
        this.creditAdj = creditAdj;
    }

    public BigDecimal getRefundAdj() {
        return refundAdj;
    }

    public void setRefundAdj(final BigDecimal refundAdj) {
        this.refundAdj = refundAdj;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(final UUID accountId) {
        this.accountId = accountId;
    }

    public List<InvoiceItem> getItems() {
        return items;
    }

    public void setItems(final List<InvoiceItem> items) {
        this.items = items;
    }

    public String getBundleKeys() {
        return bundleKeys;
    }

    public void setBundleKeys(final String bundleKeys) {
        this.bundleKeys = bundleKeys;
    }

    public List<Credit> getCredits() {
        return credits;
    }

    public void setCredits(final List<Credit> credits) {
        this.credits = credits;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Invoice{");
        sb.append("amount=").append(amount);
        sb.append(", currency='").append(currency).append('\'');
        sb.append(", invoiceId='").append(invoiceId).append('\'');
        sb.append(", invoiceDate=").append(invoiceDate);
        sb.append(", targetDate=").append(targetDate);
        sb.append(", invoiceNumber='").append(invoiceNumber).append('\'');
        sb.append(", balance=").append(balance);
        sb.append(", creditAdj=").append(creditAdj);
        sb.append(", refundAdj=").append(refundAdj);
        sb.append(", accountId='").append(accountId).append('\'');
        sb.append(", items=").append(items);
        sb.append(", bundleKeys='").append(bundleKeys).append('\'');
        sb.append(", credits=").append(credits);
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

        final Invoice invoice = (Invoice) o;

        if (accountId != null ? !accountId.equals(invoice.accountId) : invoice.accountId != null) {
            return false;
        }
        if (amount != null ? amount.compareTo(invoice.amount) != 0 : invoice.amount != null) {
            return false;
        }
        if (balance != null ? balance.compareTo(invoice.balance) != 0 : invoice.balance != null) {
            return false;
        }
        if (bundleKeys != null ? !bundleKeys.equals(invoice.bundleKeys) : invoice.bundleKeys != null) {
            return false;
        }
        if (creditAdj != null ? creditAdj.compareTo(invoice.creditAdj) != 0 : invoice.creditAdj != null) {
            return false;
        }
        if (credits != null ? !credits.equals(invoice.credits) : invoice.credits != null) {
            return false;
        }
        if (currency != null ? !currency.equals(invoice.currency) : invoice.currency != null) {
            return false;
        }
        if (invoiceDate != null ? invoiceDate.compareTo(invoice.invoiceDate) != 0 : invoice.invoiceDate != null) {
            return false;
        }
        if (invoiceId != null ? !invoiceId.equals(invoice.invoiceId) : invoice.invoiceId != null) {
            return false;
        }
        if (invoiceNumber != null ? !invoiceNumber.equals(invoice.invoiceNumber) : invoice.invoiceNumber != null) {
            return false;
        }
        if (items != null ? !items.equals(invoice.items) : invoice.items != null) {
            return false;
        }
        if (refundAdj != null ? refundAdj.compareTo(invoice.refundAdj) != 0 : invoice.refundAdj != null) {
            return false;
        }
        if (targetDate != null ? targetDate.compareTo(invoice.targetDate) != 0 : invoice.targetDate != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = amount != null ? amount.hashCode() : 0;
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        result = 31 * result + (invoiceId != null ? invoiceId.hashCode() : 0);
        result = 31 * result + (invoiceDate != null ? invoiceDate.hashCode() : 0);
        result = 31 * result + (targetDate != null ? targetDate.hashCode() : 0);
        result = 31 * result + (invoiceNumber != null ? invoiceNumber.hashCode() : 0);
        result = 31 * result + (balance != null ? balance.hashCode() : 0);
        result = 31 * result + (creditAdj != null ? creditAdj.hashCode() : 0);
        result = 31 * result + (refundAdj != null ? refundAdj.hashCode() : 0);
        result = 31 * result + (accountId != null ? accountId.hashCode() : 0);
        result = 31 * result + (items != null ? items.hashCode() : 0);
        result = 31 * result + (bundleKeys != null ? bundleKeys.hashCode() : 0);
        result = 31 * result + (credits != null ? credits.hashCode() : 0);
        return result;
    }
}