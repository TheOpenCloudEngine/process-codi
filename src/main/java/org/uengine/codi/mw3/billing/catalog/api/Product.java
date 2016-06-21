package org.uengine.codi.mw3.billing.catalog.api;

/**
 * Created by uEngineYBS on 2016-06-20.
 */
public interface Product {

    /**
     * @return the name of the {@code Product}
     */
    public String getName();

    /**
     * @return an array of other {@code Product} that can be purchased with that one
     */
    public Product[] getAvailable();

    /**
     * @return an array of other {@code Product} that are already included within this one
     */
    public Product[] getIncluded();

    /**
     * @return the {@code ProductCategory} associated with that {@code Product}
     */
    public ProductCategory getCategory();

    /**
     * @return the name of the catalog where this {@code Product} has been defined
     */
    public String getCatalogName();

    /**
     * @return the limits associated with this product
     */
    public Limit[] getLimits();

    /**
     * @return whether the given unit-value pair meets the limits of the product
     */
    public boolean compliesWithLimits(String unit, double value);
}
