package org.uengine.codi.mw3.billing.catalog.api;

import java.math.BigDecimal;

/**
 * Created by uEngineYBS on 2016-06-20.
 */
public interface PlanPhasePriceOverride {

    /**
     *
     * @return the name of the phase
     */
    public String getPhaseName();

    /**
     *
     * @return the planPhase specifier
     */
    public PlanPhaseSpecifier getPlanPhaseSpecifier();

    /**
     *
     * @return the currency
     */
    public Currency getCurrency();

    /**
     *
     * @return the fixed price for that currency
     */
    public BigDecimal getFixedPrice();

    /**
     *
     * @return the recurring price for that currency
     */
    public BigDecimal getRecurringPrice();

}