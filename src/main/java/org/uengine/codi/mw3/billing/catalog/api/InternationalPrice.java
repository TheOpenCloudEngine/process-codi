package org.uengine.codi.mw3.billing.catalog.api;

import org.uengine.codi.mw3.billing.exception.CatalogApiException;

import java.math.BigDecimal;

/**
 * Created by uEngineYBS on 2016-06-20.
 */
public interface InternationalPrice {

    /**
     * @return an array of {@code Price} in the various currencies
     */
    public abstract Price[] getPrices();

    /**
     * @param currency the currency
     * @return the price associated with that currency
     * @throws CatalogApiException if there is no entry
     */
    public abstract BigDecimal getPrice(Currency currency) throws CatalogApiException;

    /**
     * @return whether this is a zero price
     */
    public abstract boolean isZero();
}
