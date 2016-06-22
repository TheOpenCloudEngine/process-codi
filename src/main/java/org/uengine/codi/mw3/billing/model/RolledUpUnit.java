package org.uengine.codi.mw3.billing.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by uEngineYBS on 2016-06-21.
 */
public class RolledUpUnit {

    private String unitType;
    private Long amount;

    public RolledUpUnit() {}

    @JsonCreator
    public RolledUpUnit(@JsonProperty("unitType") final String unitType,
                        @JsonProperty("amount") final Long amount) {
        this.unitType = unitType;
        this.amount = amount;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(final String unitType) {
        this.unitType = unitType;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(final Long amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RolledUpUnit{");
        sb.append("unitType=").append(unitType);
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

        final RolledUpUnit that = (RolledUpUnit) o;

        if (unitType != null ? !unitType.equals(that.unitType) : that.unitType != null) {
            return false;
        }
        return amount != null ? amount.equals(that.amount) : that.amount == null;

    }

    @Override
    public int hashCode() {
        int result = unitType != null ? unitType.hashCode() : 0;
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        return result;
    }
}
