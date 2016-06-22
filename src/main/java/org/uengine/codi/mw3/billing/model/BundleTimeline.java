package org.uengine.codi.mw3.billing.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.Nullable;

import java.util.List;

/**
 * Created by uEngineYBS on 2016-06-21.
 */
public class BundleTimeline extends BillingObject {

    private String accountId;
    private String bundleId;
    private String externalKey;
    private List<EventSubscription> events;

    @JsonCreator
    public BundleTimeline(@JsonProperty("accountId") @Nullable final String accountId,
                          @JsonProperty("bundleId") @Nullable final String bundleId,
                          @JsonProperty("externalKey") @Nullable final String externalKey,
                          @JsonProperty("events") @Nullable final List<EventSubscription> events,
                          @JsonProperty("auditLogs") @Nullable final List<AuditLog> auditLogs) {
        super(auditLogs);
        this.accountId = accountId;
        this.bundleId = bundleId;
        this.externalKey = externalKey;
        this.events = events;
    }

    public BundleTimeline() {}

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(final String accountId) {
        this.accountId = accountId;
    }

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(final String bundleId) {
        this.bundleId = bundleId;
    }

    public String getExternalKey() {
        return externalKey;
    }

    public void setExternalKey(final String externalKey) {
        this.externalKey = externalKey;
    }

    public List<EventSubscription> getEvents() {
        return events;
    }

    public void setEvents(final List<EventSubscription> events) {
        this.events = events;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BundleTimeline{");
        sb.append("accountId='").append(accountId).append('\'');
        sb.append(", bundleId='").append(bundleId).append('\'');
        sb.append(", externalKey='").append(externalKey).append('\'');
        sb.append(", events=").append(events);
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

        final BundleTimeline that = (BundleTimeline) o;

        if (accountId != null ? !accountId.equals(that.accountId) : that.accountId != null) {
            return false;
        }
        if (bundleId != null ? !bundleId.equals(that.bundleId) : that.bundleId != null) {
            return false;
        }
        if (events != null ? !events.equals(that.events) : that.events != null) {
            return false;
        }
        if (externalKey != null ? !externalKey.equals(that.externalKey) : that.externalKey != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = accountId != null ? accountId.hashCode() : 0;
        result = 31 * result + (bundleId != null ? bundleId.hashCode() : 0);
        result = 31 * result + (externalKey != null ? externalKey.hashCode() : 0);
        result = 31 * result + (events != null ? events.hashCode() : 0);
        return result;
    }
}
