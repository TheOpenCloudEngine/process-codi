package org.uengine.codi.mw3.billing.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.Nullable;
import org.uengine.codi.mw3.billing.catalog.api.BillingPeriod;
import org.uengine.codi.mw3.billing.catalog.api.PhaseType;
import org.uengine.codi.mw3.billing.catalog.api.ProductCategory;
import org.uengine.codi.mw3.billing.entitlement.Entitlement.EntitlementSourceType;
import org.uengine.codi.mw3.billing.entitlement.Entitlement.EntitlementState;

import java.util.List;
import java.util.UUID;

/**
 * Created by uEngineYBS on 2016-06-20.
 */
public class Subscriptions extends BillingObject {

    private UUID accountId;
    private UUID bundleId;
    private UUID subscriptionId;
    private String externalKey;
    private String startDate;
    private String productName;
    private ProductCategory productCategory;
    private BillingPeriod billingPeriod;
    private PhaseType phaseType;
    private String priceList;
    private EntitlementState state;
    private EntitlementSourceType sourceType;
    private String cancelledDate;
    private String chargedThroughDate;
    private String billingStartDate;
    private String billingEndDate;
    private List<EventSubscription> events;
    private List<PhasePriceOverride> priceOverrides;

    public Subscriptions() { }

    @JsonCreator
    public Subscriptions(@JsonProperty("accountId") @Nullable final UUID accountId,
                        @JsonProperty("bundleId") @Nullable final UUID bundleId,
                        @JsonProperty("subscriptionId") @Nullable final UUID subscriptionId,
                        @JsonProperty("externalKey") @Nullable final String externalKey,
                        @JsonProperty("startDate") @Nullable final String startDate,
                        @JsonProperty("productName") @Nullable final String productName,
                        @JsonProperty("productCategory") @Nullable final ProductCategory productCategory,
                        @JsonProperty("billingPeriod") @Nullable final BillingPeriod billingPeriod,
                        @JsonProperty("phaseType") @Nullable final PhaseType phaseType,
                        @JsonProperty("priceList") @Nullable final String priceList,
                        @JsonProperty("state") @Nullable final EntitlementState state,
                        @JsonProperty("sourceType") @Nullable final EntitlementSourceType sourceType,
                        @JsonProperty("cancelledDate") @Nullable final String cancelledDate,
                        @JsonProperty("chargedThroughDate") @Nullable final String chargedThroughDate,
                        @JsonProperty("billingStartDate") @Nullable final String billingStartDate,
                        @JsonProperty("billingEndDate") @Nullable final String billingEndDate,
                        @JsonProperty("events") @Nullable final List<EventSubscription> events,
                        @JsonProperty("priceOverrides") @Nullable final List<PhasePriceOverride> priceOverrides,
                        @JsonProperty("auditLogs") @Nullable final List<AuditLog> auditLogs) {
        super(auditLogs);
        this.startDate = startDate;
        this.productName = productName;
        this.productCategory = productCategory;
        this.billingPeriod = billingPeriod;
        this.phaseType = phaseType;
        this.priceList = priceList;
        this.state = state;
        this.sourceType = sourceType;
        this.cancelledDate = cancelledDate;
        this.chargedThroughDate = chargedThroughDate;
        this.billingStartDate = billingStartDate;
        this.billingEndDate = billingEndDate;
        this.accountId = accountId;
        this.bundleId = bundleId;
        this.subscriptionId = subscriptionId;
        this.externalKey = externalKey;
        this.events = events;
        this.priceOverrides = priceOverrides;
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

    public String getExternalKey() {
        return externalKey;
    }

    public void setExternalKey(final String externalKey) {
        this.externalKey = externalKey;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(final String startDate) { this.startDate = startDate; }

    public String getProductName() {
        return productName;
    }

    public void setProductName(final String productName) {
        this.productName = productName;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(final ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public BillingPeriod getBillingPeriod() {
        return billingPeriod;
    }

    public void setBillingPeriod(final BillingPeriod billingPeriod) {
        this.billingPeriod = billingPeriod;
    }

    public PhaseType getPhaseType() {
        return phaseType;
    }

    public void setPhaseType(final PhaseType phaseType) {
        this.phaseType = phaseType;
    }

    public String getPriceList() {
        return priceList;
    }

    public void setPriceList(final String priceList) {
        this.priceList = priceList;
    }

    public EntitlementState getState() {
        return state;
    }

    public void setState(final EntitlementState state) {
        this.state = state;
    }

    public EntitlementSourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(final EntitlementSourceType sourceType) {
        this.sourceType = sourceType;
    }

    public String getCancelledDate() {
        return cancelledDate;
    }

    public void setCancelledDate(final String cancelledDate) { this.cancelledDate = cancelledDate; }

    public String getChargedThroughDate() {
        return chargedThroughDate;
    }

    public void setChargedThroughDate(final String chargedThroughDate) { this.chargedThroughDate = chargedThroughDate; }

    public String getBillingStartDate() {
        return billingStartDate;
    }

    public void setBillingStartDate(final String billingStartDate) { this.billingStartDate = billingStartDate; }

    public String getBillingEndDate() {
        return billingEndDate;
    }

    public void setBillingEndDate(final String billingEndDate) { this.billingStartDate = billingEndDate; }

    public List<EventSubscription> getEvents() {
        return events;
    }

    public void setEvents(final List<EventSubscription> events) {
        this.events = events;
    }

    public List<PhasePriceOverride> getPriceOverrides() {
        return priceOverrides;
    }

    public void setPriceOverrides(final List<PhasePriceOverride> priceOverrides) {
        this.priceOverrides = priceOverrides;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Subscription{");
        sb.append("accountId=").append(accountId);
        sb.append(", bundleId=").append(bundleId);
        sb.append(", subscriptionId=").append(subscriptionId);
        sb.append(", externalKey='").append(externalKey).append('\'');
        sb.append(", startDate=").append(startDate);
        sb.append(", productName='").append(productName).append('\'');
        sb.append(", productCategory=").append(productCategory);
        sb.append(", billingPeriod=").append(billingPeriod);
        sb.append(", phaseType=").append(phaseType);
        sb.append(", priceList='").append(priceList).append('\'');
        sb.append(", state=").append(state);
        sb.append(", sourceType=").append(sourceType);
        sb.append(", cancelledDate=").append(cancelledDate);
        sb.append(", chargedThroughDate=").append(chargedThroughDate);
        sb.append(", billingStartDate=").append(billingStartDate);
        sb.append(", billingEndDate=").append(billingEndDate);
        sb.append(", events=").append(events);
        sb.append(", priceOverrides=").append(priceOverrides);
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

        final Subscriptions that = (Subscriptions) o;

        if (accountId != null ? !accountId.equals(that.accountId) : that.accountId != null) {
            return false;
        }
        if (billingEndDate != null ? billingEndDate.compareTo(that.billingEndDate) != 0 : that.billingEndDate != null) {
            return false;
        }
        if (billingPeriod != that.billingPeriod) {
            return false;
        }
        if (billingStartDate != null ? billingStartDate.compareTo(that.billingStartDate) != 0 : that.billingStartDate != null) {
            return false;
        }
        if (bundleId != null ? !bundleId.equals(that.bundleId) : that.bundleId != null) {
            return false;
        }
        if (cancelledDate != null ? cancelledDate.compareTo(that.cancelledDate) != 0 : that.cancelledDate != null) {
            return false;
        }
        if (chargedThroughDate != null ? chargedThroughDate.compareTo(that.chargedThroughDate) != 0 : that.chargedThroughDate != null) {
            return false;
        }
        if (events != null ? !events.equals(that.events) : that.events != null) {
            return false;
        }
        if (priceOverrides != null ? !priceOverrides.equals(that.priceOverrides) : that.priceOverrides != null) {
            return false;
        }
        if (externalKey != null ? !externalKey.equals(that.externalKey) : that.externalKey != null) {
            return false;
        }
        if (phaseType != that.phaseType) {
            return false;
        }
        if (priceList != null ? !priceList.equals(that.priceList) : that.priceList != null) {
            return false;
        }
        if (productCategory != that.productCategory) {
            return false;
        }
        if (productName != null ? !productName.equals(that.productName) : that.productName != null) {
            return false;
        }
        if (sourceType != that.sourceType) {
            return false;
        }
        if (startDate != null ? startDate.compareTo(that.startDate) != 0 : that.startDate != null) {
            return false;
        }
        if (state != that.state) {
            return false;
        }
        if (subscriptionId != null ? !subscriptionId.equals(that.subscriptionId) : that.subscriptionId != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = accountId != null ? accountId.hashCode() : 0;
        result = 31 * result + (bundleId != null ? bundleId.hashCode() : 0);
        result = 31 * result + (subscriptionId != null ? subscriptionId.hashCode() : 0);
        result = 31 * result + (externalKey != null ? externalKey.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (productName != null ? productName.hashCode() : 0);
        result = 31 * result + (productCategory != null ? productCategory.hashCode() : 0);
        result = 31 * result + (billingPeriod != null ? billingPeriod.hashCode() : 0);
        result = 31 * result + (phaseType != null ? phaseType.hashCode() : 0);
        result = 31 * result + (priceList != null ? priceList.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (sourceType != null ? sourceType.hashCode() : 0);
        result = 31 * result + (cancelledDate != null ? cancelledDate.hashCode() : 0);
        result = 31 * result + (chargedThroughDate != null ? chargedThroughDate.hashCode() : 0);
        result = 31 * result + (billingStartDate != null ? billingStartDate.hashCode() : 0);
        result = 31 * result + (billingEndDate != null ? billingEndDate.hashCode() : 0);
        result = 31 * result + (events != null ? events.hashCode() : 0);
        result = 31 * result + (priceOverrides != null ? priceOverrides.hashCode() : 0);
        return result;
    }
}
