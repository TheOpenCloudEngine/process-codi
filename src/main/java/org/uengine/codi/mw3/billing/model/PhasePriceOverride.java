package org.uengine.codi.mw3.billing.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.Nullable;

import java.math.BigDecimal;

/**
 * Created by uEngineYBS on 2016-06-20.
 */
public class PhasePriceOverride {

    private final String phaseName;
    private final String phaseType;
    private final BigDecimal fixedPrice;
    private final BigDecimal recurringPrice;

    @JsonCreator
    public PhasePriceOverride(@JsonProperty("phaseName") final String phaseName,
                              @JsonProperty("phaseType") final String phaseType,
                              @Nullable @JsonProperty("fixedPrice") final BigDecimal fixedPrice,
                              @Nullable @JsonProperty("recurringPrice") final BigDecimal recurringPrice) {
        this.phaseName = phaseName;
        this.phaseType = phaseType;
        this.fixedPrice = fixedPrice;
        this.recurringPrice = recurringPrice;
    }

    public String getPhaseName() {
        return phaseName;
    }

    public String getPhaseType() {
        return phaseType;
    }

    public BigDecimal getFixedPrice() {
        return fixedPrice;
    }

    public BigDecimal getRecurringPrice() {
        return recurringPrice;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PhasePriceOverride)) {
            return false;
        }

        final PhasePriceOverride that = (PhasePriceOverride) o;

        if (phaseName != null ? !phaseName.equals(that.phaseName) : that.phaseName != null) {
            return false;
        }
        if (phaseType != null ? !phaseType.equals(that.phaseType) : that.phaseType != null) {
            return false;
        }
        if (fixedPrice != null ? fixedPrice.compareTo(that.fixedPrice) != 0 : that.fixedPrice != null) {
            return false;
        }
        if (recurringPrice != null ? recurringPrice.compareTo(that.recurringPrice) != 0 : that.recurringPrice != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = phaseName != null ? phaseName.hashCode() : 0;
        result = 31 * result + (phaseType != null ? phaseType.hashCode() : 0);
        result = 31 * result + (fixedPrice != null ? fixedPrice.hashCode() : 0);
        result = 31 * result + (recurringPrice != null ? recurringPrice.hashCode() : 0);
        return result;
    }
}