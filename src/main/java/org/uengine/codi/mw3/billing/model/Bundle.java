package org.uengine.codi.mw3.billing.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * Created by uEngineYBS on 2016-06-21.
 */
public class Bundle extends BillingObject {

    private UUID accountId;
    private UUID bundleId;
    private String externalKey;
    private List<Subscriptions> subscriptions;
    private BundleTimeline timeline;

    @JsonCreator
    public Bundle(@JsonProperty("accountId") @Nullable final UUID accountId,
                  @JsonProperty("bundleId") @Nullable final UUID bundleId,
                  @JsonProperty("externalKey") @Nullable final String externalKey,
                  @JsonProperty("subscriptions") @Nullable final List<Subscriptions> subscriptions,
                  @JsonProperty("timeline") @Nullable final BundleTimeline timeline,
                  @JsonProperty("auditLogs") @Nullable final List<AuditLog> auditLogs) {
        super(auditLogs);
        this.accountId = accountId;
        this.bundleId = bundleId;
        this.externalKey = externalKey;
        this.subscriptions = subscriptions;
        this.timeline = timeline;
    }

    public Bundle() {}

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

    public String getExternalKey() {
        return externalKey;
    }

    public void setExternalKey(final String externalKey) {
        this.externalKey = externalKey;
    }

    public List<Subscriptions> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(final List<Subscriptions> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public BundleTimeline getTimeline() {
        return timeline;
    }

    public void setTimeline(final BundleTimeline timeline) {
        this.timeline = timeline;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Bundle{");
        sb.append("accountId=").append(accountId);
        sb.append(", bundleId=").append(bundleId);
        sb.append(", externalKey='").append(externalKey).append('\'');
        sb.append(", subscriptions=").append(subscriptions);
        sb.append(", timeline=").append(timeline);
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

        final Bundle bundle = (Bundle) o;

        if (accountId != null ? !accountId.equals(bundle.accountId) : bundle.accountId != null) {
            return false;
        }
        if (bundleId != null ? !bundleId.equals(bundle.bundleId) : bundle.bundleId != null) {
            return false;
        }
        if (externalKey != null ? !externalKey.equals(bundle.externalKey) : bundle.externalKey != null) {
            return false;
        }
        if (subscriptions != null ? !subscriptions.equals(bundle.subscriptions) : bundle.subscriptions != null) {
            return false;
        }
        if (timeline != null ? !timeline.equals(bundle.timeline) : bundle.timeline != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = accountId != null ? accountId.hashCode() : 0;
        result = 31 * result + (bundleId != null ? bundleId.hashCode() : 0);
        result = 31 * result + (externalKey != null ? externalKey.hashCode() : 0);
        result = 31 * result + (subscriptions != null ? subscriptions.hashCode() : 0);
        result = 31 * result + (timeline != null ? timeline.hashCode() : 0);
        return result;
    }
}
