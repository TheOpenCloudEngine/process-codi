package org.uengine.codi.mw3.billing.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by uEngineYBS on 2016-06-21.
 */
public class UsageRecord {

    private String recordDate;
    private Long amount;

    public UsageRecord() {}

    @JsonCreator
    public UsageRecord(@JsonProperty("recordDate") final String recordDate,
                       @JsonProperty("amount") final Long amount) {
        this.recordDate = recordDate;
        this.amount = amount;
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(final String recordDate) {
        this.recordDate = recordDate;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(final Long amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UsageRecord{");
        sb.append("recordDate=").append(recordDate);
        sb.append(", amount=").append(amount);
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

        final UsageRecord that = (UsageRecord) o;

        if (recordDate != null ? recordDate.compareTo(that.recordDate) != 0 : that.recordDate != null) {
            return false;
        }
        return amount != null ? amount.equals(that.amount) : that.amount == null;
    }

    @Override
    public int hashCode() {
        int result = recordDate != null ? recordDate.hashCode() : 0;
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        return result;
    }
}
