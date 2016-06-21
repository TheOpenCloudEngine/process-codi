package org.uengine.codi.mw3.billing.catalog.api;

import java.util.List;

/**
 * Created by uEngineYBS on 2016-06-20.
 */
public interface PriceListSet {

    public static final String DEFAULT_PRICELIST_NAME = "DEFAULT";

    /**
     * @param priceListName the name of the {@code PriceList}
     * @param product       the {@code Product}
     * @param period        the {@code BillingPeriod}
     * @return the {@code Plan}
     */
    public Plan getPlanListFrom(String priceListName, Product product, BillingPeriod period);

    /**
     *
     * @return all the price {@code PriceList}
     */
    public List<PriceList> getAllPriceLists();

}