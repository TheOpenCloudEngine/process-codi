package org.uengine.codi.mw3.billing.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

/**
 * Created by uEngineYBS on 2016-06-21.
 */
public class SubscriptionUsageRecord {

    private UUID subscriptionId;
    private List<UnitUsageRecord> unitUsageRecords;

    public SubscriptionUsageRecord() {}

    @JsonCreator
    public SubscriptionUsageRecord(@JsonProperty("subscriptionId") final UUID subscriptionId,
                                   @JsonProperty("unitUsageRecords") final List<UnitUsageRecord> unitUsageRecords) {
        this.subscriptionId = subscriptionId;
        this.unitUsageRecords = unitUsageRecords;
    }

    public UUID getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(final UUID subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public List<UnitUsageRecord> getUnitUsageRecords() {
        return unitUsageRecords;
    }

    public void setUnitUsageRecords(final List<UnitUsageRecord> unitUsageRecords) {
        this.unitUsageRecords = unitUsageRecords;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SubscriptionUsageRecord{");
        sb.append("subscriptionId=").append(subscriptionId);
        sb.append(", unitUsageRecords=").append(unitUsageRecords);
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

        final SubscriptionUsageRecord that = (SubscriptionUsageRecord) o;

        if (subscriptionId != null ? !subscriptionId.equals(that.subscriptionId) : that.subscriptionId != null) {
            return false;
        }
        return unitUsageRecords != null ? unitUsageRecords.equals(that.unitUsageRecords) : that.unitUsageRecords == null;

    }

    @Override
    public int hashCode() {
        int result = subscriptionId != null ? subscriptionId.hashCode() : 0;
        result = 31 * result + (unitUsageRecords != null ? unitUsageRecords.hashCode() : 0);
        return result;
    }
}