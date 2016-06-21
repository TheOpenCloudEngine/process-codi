package org.uengine.codi.mw3.billing.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.Nullable;
import org.uengine.codi.mw3.billing.catalog.api.Currency;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Created by uEngineYBS on 2016-06-20.
 */
public class InvoiceItem extends BillingObject {

    private UUID invoiceItemId;
    private UUID invoiceId;
    private UUID linkedInvoiceItemId;
    private UUID accountId;
    private UUID bundleId;
    private UUID subscriptionId;
    private String planName;
    private String phaseName;
    private String usageName;
    private String itemType;
    private String description;
    private String startDate;
    private String endDate;
    private BigDecimal amount;
    private Currency currency;

    public InvoiceItem() {}

    @JsonCreator
    public InvoiceItem(@JsonProperty("invoiceItemId") final UUID invoiceItemId,
                       @JsonProperty("invoiceId") final UUID invoiceId,
                       @JsonProperty("linkedInvoiceItemId") final UUID linkedInvoiceItemId,
                       @JsonProperty("accountId") final UUID accountId,
                       @JsonProperty("bundleId") final UUID bundleId,
                       @JsonProperty("subscriptionId") final UUID subscriptionId,
                       @JsonProperty("planName") final String planName,
                       @JsonProperty("phaseName") final String phaseName,
                       @JsonProperty("usageName") final String usageName,
                       @JsonProperty("itemType") final String itemType,
                       @JsonProperty("description") final String description,
                       @JsonProperty("startDate") final String startDate,
                       @JsonProperty("endDate") final String endDate,
                       @JsonProperty("amount") final BigDecimal amount,
                       @JsonProperty("currency") final Currency currency,
                       @JsonProperty("auditLogs") @Nullable final List<AuditLog> auditLogs) {
        super(auditLogs);
        this.invoiceItemId = invoiceItemId;
        this.invoiceId = invoiceId;
        this.linkedInvoiceItemId = linkedInvoiceItemId;
        this.accountId = accountId;
        this.bundleId = bundleId;
        this.subscriptionId = subscriptionId;
        this.planName = planName;
        this.phaseName = phaseName;
        this.itemType = itemType;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
        this.currency = currency;
    }

    public UUID getInvoiceItemId() {
        return invoiceItemId;
    }

    public void setInvoiceItemId(final UUID invoiceItemId) {
        this.invoiceItemId = invoiceItemId;
    }

    public UUID getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(final UUID invoiceId) {
        this.invoiceId = invoiceId;
    }

    public UUID getLinkedInvoiceItemId() {
        return linkedInvoiceItemId;
    }

    public void setLinkedInvoiceItemId(final UUID linkedInvoiceItemId) {
        this.linkedInvoiceItemId = linkedInvoiceItemId;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(final UUID accountId) {
        this.accountId = accountId;
    }

    public UUID getBundleId() {
        return bundleId;
    }

    public void setBundleId(final UUID bundleId) {
        this.bundleId = bundleId;
    }

    public UUID getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(final UUID subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getPlanName() {
        return planName;
    }

    public String getUsageName() {
        return usageName;
    }

    public void setPlanName(final String planName) {
        this.planName = planName;
    }

    public String getPhaseName() {
        return phaseName;
    }

    public void setPhaseName(final String phaseName) {
        this.phaseName = phaseName;
    }

    public void setUsageName(final String usageName) {
        this.usageName = usageName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(final String itemType) {
        this.itemType = itemType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(final String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(final String endDate) {
        this.endDate = endDate;
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final InvoiceItem that = (InvoiceItem) o;

        if (accountId != null ? !accountId.equals(that.accountId) : that.accountId != null) {
            return false;
        }
        if (amount != null ? amount.compareTo(that.amount) != 0 : that.amount != null) {
            return false;
        }
        if (bundleId != null ? !bundleId.equals(that.bundleId) : that.bundleId != null) {
            return false;
        }
        if (currency != that.currency) {
            return false;
        }
        if (description != null ? !description.equals(that.description) : that.description != null) {
            return false;
        }
        if (endDate != null ? endDate.compareTo(that.endDate) != 0 : that.endDate != null) {
            return false;
        }
        if (invoiceId != null ? !invoiceId.equals(that.invoiceId) : that.invoiceId != null) {
            return false;
        }
        if (invoiceItemId != null ? !invoiceItemId.equals(that.invoiceItemId) : that.invoiceItemId != null) {
            return false;
        }
        if (itemType != null ? !itemType.equals(that.itemType) : that.itemType != null) {
            return false;
        }
        if (linkedInvoiceItemId != null ? !linkedInvoiceItemId.equals(that.linkedInvoiceItemId) : that.linkedInvoiceItemId != null) {
            return false;
        }
        if (phaseName != null ? !phaseName.equals(that.phaseName) : that.phaseName != null) {
            return false;
        }
        if (planName != null ? !planName.equals(that.planName) : that.planName != null) {
            return false;
        }
        if (usageName != null ? !usageName.equals(that.usageName) : that.usageName != null) {
            return false;
        }
        if (startDate != null ? startDate.compareTo(that.startDate) != 0 : that.startDate != null) {
            return false;
        }
        if (subscriptionId != null ? !subscriptionId.equals(that.subscriptionId) : that.subscriptionId != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = invoiceItemId != null ? invoiceItemId.hashCode() : 0;
        result = 31 * result + (invoiceId != null ? invoiceId.hashCode() : 0);
        result = 31 * result + (linkedInvoiceItemId != null ? linkedInvoiceItemId.hashCode() : 0);
        result = 31 * result + (accountId != null ? accountId.hashCode() : 0);
        result = 31 * result + (bundleId != null ? bundleId.hashCode() : 0);
        result = 31 * result + (subscriptionId != null ? subscriptionId.hashCode() : 0);
        result = 31 * result + (planName != null ? planName.hashCode() : 0);
        result = 31 * result + (phaseName != null ? phaseName.hashCode() : 0);
        result = 31 * result + (usageName != null ? usageName.hashCode() : 0);
        result = 31 * result + (itemType != null ? itemType.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        return result;
    }
}
