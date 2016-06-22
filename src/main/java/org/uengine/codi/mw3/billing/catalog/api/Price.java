package org.uengine.codi.mw3.billing.catalog.api;

import java.math.BigDecimal;

/**
 * Created by uEngineYBS on 2016-06-20.
 */
public interface Price {

    /**
     * @return the {@code Currency}
     */
    public Currency getCurrency();

    /**
     * @return the price amount
     * @throws CurrencyValueNull
     */
    public BigDecimal getValue() throws CurrencyValueNull;

}
