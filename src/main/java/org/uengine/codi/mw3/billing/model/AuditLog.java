package org.uengine.codi.mw3.billing.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

/**
 * Created by uEngineYBS on 2016-06-17.
 */
public class AuditLog {
    private String changeType;
    private DateTime changeDate;
    private String changedBy;
    private String reasonCode;
    private String comments;
    private String userToken;

    public AuditLog() {}

    @JsonCreator
    public AuditLog(@JsonProperty("changeType") final String changeType,
                    @JsonProperty("changeDate") final DateTime changeDate,
                    @JsonProperty("changedBy") final String changedBy,
                    @JsonProperty("reasonCode") final String reasonCode,
                    @JsonProperty("comments") final String comments,
                    @JsonProperty("userToken") final String userToken) {
        this.changeType = changeType;
        this.changeDate = changeDate;
        this.changedBy = changedBy;
        this.reasonCode = reasonCode;
        this.comments = comments;
        this.userToken = userToken;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(final String changeType) {
        this.changeType = changeType;
    }

    public DateTime getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(final DateTime changeDate) {
        this.changeDate = changeDate;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(final String changedBy) {
        this.changedBy = changedBy;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(final String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(final String comments) {
        this.comments = comments;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(final String userToken) {
        this.userToken = userToken;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AuditLog{");
        sb.append("changeType='").append(changeType).append('\'');
        sb.append(", changeDate=").append(changeDate);
        sb.append(", changedBy='").append(changedBy).append('\'');
        sb.append(", reasonCode='").append(reasonCode).append('\'');
        sb.append(", comments='").append(comments).append('\'');
        sb.append(", userToken='").append(userToken).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final AuditLog auditLog = (AuditLog) o;

        if (changeDate != null ? changeDate.compareTo(auditLog.changeDate) != 0 : auditLog.changeDate != null) {
            return false;
        }
        if (changeType != null ? !changeType.equals(auditLog.changeType) : auditLog.changeType != null) {
            return false;
        }
        if (changedBy != null ? !changedBy.equals(auditLog.changedBy) : auditLog.changedBy != null) {
            return false;
        }
        if (comments != null ? !comments.equals(auditLog.comments) : auditLog.comments != null) {
            return false;
        }
        if (reasonCode != null ? !reasonCode.equals(auditLog.reasonCode) : auditLog.reasonCode != null) {
            return false;
        }
        if (userToken != null ? !userToken.equals(auditLog.userToken) : auditLog.userToken != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = changeType != null ? changeType.hashCode() : 0;
        result = 31 * result + (changeDate != null ? changeDate.hashCode() : 0);
        result = 31 * result + (changedBy != null ? changedBy.hashCode() : 0);
        result = 31 * result + (reasonCode != null ? reasonCode.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        result = 31 * result + (userToken != null ? userToken.hashCode() : 0);
        return result;
    }
}
