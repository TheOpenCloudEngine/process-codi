package org.uengine.codi.mw3.billing.model;

import com.sun.istack.Nullable;

import java.util.List;
/**
 * Created by uEngineYBS on 2016-06-17.
 */
public class BillingObject {
    protected List<AuditLog> auditLogs;

    public BillingObject() {
        this(null);
    }

    public BillingObject(@Nullable final List<AuditLog> auditLogs) {
        this.auditLogs = auditLogs;
    }

    public List<AuditLog> getAuditLogs() {
        return auditLogs;
    }

    public void setAuditLogs(final List<AuditLog> auditLogs) {
        this.auditLogs = auditLogs;
    }
}
