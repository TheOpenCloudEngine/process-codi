package org.uengine.codi.mw3.billing.catalog.api;

/**
 * Created by uEngineYBS on 2016-06-20.
 */
public interface Limit {

    public Unit getUnit();

    public Double getMax();

    public Double getMin();

    public boolean compliesWith(double value);
}
