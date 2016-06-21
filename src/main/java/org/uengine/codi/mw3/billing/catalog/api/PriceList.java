package org.uengine.codi.mw3.billing.catalog.api;

/**
 * Created by uEngineYBS on 2016-06-20.
 */
public interface PriceList {

    public abstract String getName();

    public abstract Plan findPlan(Product product, BillingPeriod period);

    public Plan[] getPlans();
}
