package org.uengine.codi.mw3.billing.catalog.api;

/**
 * Created by uEngineYBS on 2016-06-20.
 */
public interface Usage {

    /**
     * @return the name of the usage section
     */
    public String getName();

    /**
     * @return the {@code BillingMode}
     */
    public BillingMode getBillingMode();

    /**
     * @return the {@code UsageType}
     */
    public UsageType getUsageType();

    /**
     * @return @return the {@code BillingPeriod}
     */
    public BillingPeriod getBillingPeriod();

    /**
     * @return compliance boolean
     */
    public boolean compliesWithLimits(String unit, double value);

    /**
     * @return the {@code Limit} associated with that usage section
     */
    public Limit[] getLimits();

    /**
     * @return the {@code Tier} associated with that usage section
     */
    public Tier[] getTiers();

    /**
     * @return the {@code Block} associated with that usage section
     */
    public Block[] getBlocks();

    /**
     * @return the fixed {@code InternationalPrice} for that {@code Usage} section.
     */
    public InternationalPrice getFixedPrice();

    /**
     * @return the recurring {@code InternationalPrice} for that {@code Usage} section.
     */
    public InternationalPrice getRecurringPrice();
}
