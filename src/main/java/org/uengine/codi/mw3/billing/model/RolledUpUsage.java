package org.uengine.codi.mw3.billing.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

/**
 * Created by uEngineYBS on 2016-06-21.
 */
public class RolledUpUsage {

    private UUID subscriptionId;
    private String startDate;
    private String endDate;
    private List<RolledUpUnit> rolledUpUnits;

    public RolledUpUsage() {}

    @JsonCreator
    public RolledUpUsage(@JsonProperty("subscriptionId") final UUID subscriptionId,
                         @JsonProperty("startDate") final String startDate,
                         @JsonProperty("endDate") final String endDate,
                         @JsonProperty("rolledUpUnits") final List<RolledUpUnit> rolledUpUnits) {
        this.subscriptionId = subscriptionId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rolledUpUnits = rolledUpUnits;
    }

    public UUID getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(final UUID subscriptionId) {
        this.subscriptionId = subscriptionId;
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

    public List<RolledUpUnit> getRolledUpUnits() {
        return rolledUpUnits;
    }

    public void setRolledUpUnits(final List<RolledUpUnit> rolledUpUnits) {
        this.rolledUpUnits = rolledUpUnits;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RolledUpUsage{");
        sb.append("subscriptionId=").append(subscriptionId);
        sb.append(", startDate=").append(startDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", rolledUpUnits=").append(rolledUpUnits);
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

        final RolledUpUsage that = (RolledUpUsage) o;

        if (subscriptionId != null ? !subscriptionId.equals(that.subscriptionId) : that.subscriptionId != null) {
            return false;
        }
        if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null) {
            return false;
        }
        if (endDate != null ? !endDate.equals(that.endDate) : that.endDate != null) {
            return false;
        }
        return rolledUpUnits != null ? rolledUpUnits.equals(that.rolledUpUnits) : that.rolledUpUnits == null;

    }

    @Override
    public int hashCode() {
        int result = subscriptionId != null ? subscriptionId.hashCode() : 0;
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (rolledUpUnits != null ? rolledUpUnits.hashCode() : 0);
        return result;
    }
}
