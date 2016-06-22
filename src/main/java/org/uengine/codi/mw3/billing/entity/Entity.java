package org.uengine.codi.mw3.billing.entity;

import org.joda.time.DateTime;

import java.util.UUID;

/**
 * Created by uEngineYBS on 2016-06-20.
 */
public interface Entity {

    /**
     *
     * @return UUID
     */
    public UUID getId();

    /**
     * @return DateTime
     */
    public DateTime getCreatedDate();

    /**
     * @return DateTime
     */
    public DateTime getUpdatedDate();
}
