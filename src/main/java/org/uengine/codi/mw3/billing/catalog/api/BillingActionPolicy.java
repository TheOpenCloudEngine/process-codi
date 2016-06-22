package org.uengine.codi.mw3.billing.catalog.api;

/**
 * Created by uEngineYBS on 2016-06-20.
 */
public enum BillingActionPolicy {
    /**
     * The cancellation or {@code Plan} change effectiveDate will occur at the end of the current invoiced service
     * period, and that will not trigger any proration and credit.
     */
    END_OF_TERM,
    /**
     * The cancellation or {@code Plan} change effectiveDate will occur at the requestedDate
     */
    IMMEDIATE,
    ILLEGAL
}
