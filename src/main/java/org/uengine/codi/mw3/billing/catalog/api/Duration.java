package org.uengine.codi.mw3.billing.catalog.api;

import org.joda.time.DateTime;
import org.joda.time.Period;

/**
 * Created by uEngineYBS on 2016-06-20.
 */
public interface Duration {

    /**
     * @return the {@code TimeUnit}
     */
    public TimeUnit getUnit();

    /**
     * @return the number of units
     */
    public int getNumber();

    /**
     * @param dateTime the date to add to that duration
     * @return the joda {@code Period}
     */
    public DateTime addToDateTime(DateTime dateTime);

    public Period toJodaPeriod();
}
