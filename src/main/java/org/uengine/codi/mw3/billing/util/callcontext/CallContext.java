package org.uengine.codi.mw3.billing.util.callcontext;

import org.joda.time.DateTime;

import java.util.UUID;

/**
 * Created by uEngineYBS on 2016-06-20.
 */
public interface CallContext extends TenantContext {

    public UUID getUserToken();

    public String getUserName();

    public CallOrigin getCallOrigin();

    public UserType getUserType();

    public String getReasonCode();

    public String getComments();

    public DateTime getCreatedDate();

    public DateTime getUpdatedDate();
}