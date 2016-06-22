package org.uengine.codi.mw3.billing.exception;

/**
 * Created by uEngineYBS on 2016-06-20.
 */
public class EntitlementApiException extends BillingExceptionBase {

    public EntitlementApiException(final BillingExceptionBase e) {
        super(e, e.getCode(), e.getMessage());
    }

    public EntitlementApiException(final Throwable e, final ErrorCode code, final Object... args) {
        super(e, code, args);
    }

    public EntitlementApiException(final Throwable e, final int code, final String message) {
        super(e, code, message);
    }

    public EntitlementApiException(final ErrorCode code, final Object... args) {
        super(code, args);
    }
}
