package org.uengine.codi.mw3.billing.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by uEngineYBS on 2016-06-21.
 */
public class UnitUsageRecord {

    private String unitType;
    private List<UsageRecord> usageRecords;

    public UnitUsageRecord() {}

    @JsonCreator
    public UnitUsageRecord(@JsonProperty("unitType") final String unitType,
                           @JsonProperty("usageRecords") final List<UsageRecord> usageRecords) {
        this.unitType = unitType;
        this.usageRecords = usageRecords;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(final String unitType) {
        this.unitType = unitType;
    }

    public List<UsageRecord> getUsageRecords() {
        return usageRecords;
    }

    public void setUsageRecords(final List<UsageRecord> usageRecords) {
        this.usageRecords = usageRecords;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UnitUsageRecord{");
        sb.append("unitType=").append(unitType);
        sb.append(", usageRecords=").append(usageRecords);
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

        final UnitUsageRecord that = (UnitUsageRecord) o;

        if (unitType != null ? !unitType.equals(that.unitType) : that.unitType != null) {
            return false;
        }
        return usageRecords != null ? usageRecords.equals(that.usageRecords) : that.usageRecords == null;

    }

    @Override
    public int hashCode() {
        int result = unitType != null ? unitType.hashCode() : 0;
        result = 31 * result + (usageRecords != null ? usageRecords.hashCode() : 0);
        return result;
    }
}
