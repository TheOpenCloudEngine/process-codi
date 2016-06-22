package org.uengine.codi.mw3.billing.catalog.api;

/**
 * Created by uEngineYBS on 2016-06-20.
 */
public interface TieredBlock extends Block {

    /**
     * @return the max used usage to consider this tiered block.
     */
    public Double getMax();
}
