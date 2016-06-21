package org.uengine.codi.mw3.billing.catalog.api;

/**
 * Created by uEngineYBS on 2016-06-20.
 */
public interface Recurring {

    /**
     * @return @return the {@code BillingPeriod}
     */
    public BillingPeriod getBillingPeriod();

    /**
     * @return the recurring {@code InternationalPrice} for that {@code Recurring} section.
     */
    public InternationalPrice getRecurringPrice();
}
