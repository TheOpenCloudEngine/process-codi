package org.uengine.codi.mw3.billing.exception;

/**
 * Created by uEngineYBS on 2016-06-20.
 */
public class CatalogApiException extends BillingExceptionBase {

    public CatalogApiException(final BillingExceptionBase cause) {
        super(cause);
    }

    public CatalogApiException(final Throwable cause, final ErrorCode code, final Object... args) {
        super(cause, code, args);
    }

    public CatalogApiException(final ErrorCode code, final Object... args) {
        super(code, args);
    }
}
