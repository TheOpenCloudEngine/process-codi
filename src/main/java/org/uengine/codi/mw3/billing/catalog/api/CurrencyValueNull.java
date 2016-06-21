package org.uengine.codi.mw3.billing.catalog.api;

import org.uengine.codi.mw3.billing.exception.CatalogApiException;
import org.uengine.codi.mw3.billing.exception.ErrorCode;

/**
 * Created by uEngineYBS on 2016-06-20.
 */
public class CurrencyValueNull extends CatalogApiException {

    private static final long serialVersionUID = 1L;

    public CurrencyValueNull(final Throwable cause, final Object... args) {
        super(cause, ErrorCode.CAT_PRICE_VALUE_NULL_FOR_CURRENCY, args);
    }

    public CurrencyValueNull(final Object... args) {
        super(ErrorCode.CAT_PRICE_VALUE_NULL_FOR_CURRENCY, args);
    }

}
