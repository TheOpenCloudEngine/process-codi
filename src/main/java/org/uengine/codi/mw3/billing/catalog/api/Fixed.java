package org.uengine.codi.mw3.billing.catalog.api;

/**
 * Created by uEngineYBS on 2016-06-20.
 */
public interface Fixed {

    /**
     * @return the {@code FixedType}
     */
    public FixedType getType();

    /**
     * @return the fixed {@code InternationalPrice} for that {@code Fixed} section.
     */
    public InternationalPrice getPrice();
}
