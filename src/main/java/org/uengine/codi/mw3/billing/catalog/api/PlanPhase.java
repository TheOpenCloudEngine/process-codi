package org.uengine.codi.mw3.billing.catalog.api;

/**
 * Created by uEngineYBS on 2016-06-20.
 */
public interface PlanPhase {

    /**
     * @return the {@code Fixed} section
     */
    public Fixed getFixed();

    /**
     * @return the {@code Recurring} section
     */
    public Recurring getRecurring();

    /**
     * @return the {@code Usage} section
     */
    public Usage[] getUsages();

    /**
     * @return the unique name for that {@code Phase}
     */
    public String getName();

    /**
     * @return the duration of that {@code PlanPhase}
     */
    public Duration getDuration();

    /**
     * @return the {@code PhaseType}
     */
    public PhaseType getPhaseType();

    /**
     * @return compliance boolean
     */
    public boolean compliesWithLimits(String unit, double value);

}
