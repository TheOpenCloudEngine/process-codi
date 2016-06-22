package org.uengine.codi.mw3.billing.catalog.api;

/**
 * Created by uEngineYBS on 2016-06-20.
 */
public interface Tier {

    /**
     * @return the {@code Limit} for that {@code Tier} section.
     */
    public Limit[] getLimits();

    /**
     * @return the {@code Block} for that {@code Tier} section.
     */
    public TieredBlock[] getTieredBlocks();

    /**
     * @return the fixed {@code InternationalPrice} for that {@code Tier} section.
     */
    public InternationalPrice getFixedPrice();

    /**
     * @return the recurring {@code InternationalPrice} for that {@code Tier} section.
     */
    public InternationalPrice getRecurringPrice();
}
