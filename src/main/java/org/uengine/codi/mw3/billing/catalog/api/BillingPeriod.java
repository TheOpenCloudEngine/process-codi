package org.uengine.codi.mw3.billing.catalog.api;

/**
 * Created by uEngineYBS on 2016-06-20.
 */
public enum BillingPeriod {
    MONTHLY(1),
    QUARTERLY(3),
    BIANNUAL(6),
    ANNUAL(12),
    BIENNIAL(24),
    NO_BILLING_PERIOD(0);

    private final int numberOfMonths;

    BillingPeriod(final int numberOfMonths) {
        this.numberOfMonths = numberOfMonths;
    }

    public int getNumberOfMonths() {
        return numberOfMonths;
    }
}
