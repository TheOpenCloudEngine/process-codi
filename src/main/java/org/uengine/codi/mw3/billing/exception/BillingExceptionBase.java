package org.uengine.codi.mw3.billing.exception;

/**
 * Created by uEngineYBS on 2016-06-20.
 */
public class BillingExceptionBase extends Exception {

    private final Throwable cause;
    private final int code;
    private final String formattedMsg;

    public BillingExceptionBase(final Throwable cause, final int code, final String msg) {
        this.formattedMsg = msg;
        this.code = code;
        this.cause = cause;
    }

    public BillingExceptionBase(final BillingExceptionBase cause) {
        this.formattedMsg = cause.getMessage();
        this.code = cause.getCode();
        this.cause = cause;
    }

    public BillingExceptionBase(/* @Nullable */ final Throwable cause, final ErrorCode code, final Object... args) {
        final String tmp;
        tmp = String.format(code.getFormat(), args);
        this.formattedMsg = tmp;
        this.code = code.getCode();
        this.cause = cause;
    }

    public BillingExceptionBase(final ErrorCode code, final Object... args) {
        this(null, code, args);
    }

    @Override
    public String getMessage() {
        return formattedMsg;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{cause=").append(cause);
        sb.append(", code=").append(code);
        sb.append(", formattedMsg='").append(formattedMsg).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
