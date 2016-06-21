package org.uengine.codi.mw3.billing.catalog.api;

import org.joda.time.DateTime;
import org.uengine.codi.mw3.billing.exception.CatalogApiException;

import java.util.Date;
import java.util.Iterator;

/**
 * Created by uEngineYBS on 2016-06-20.
 */
public interface Plan {

    /**
     * @return an array of {@code PlanPhase}
     */
    public PlanPhase[] getInitialPhases();

    /**
     * @return the {@code Product} associated with that {@code Plan}
     */
    public Product getProduct();

    /**
     * @return the name of the {@code Plan}
     */
    public String getName();

    /**
     * @return an iterator through the {@code PlanPhase}
     */
    public Iterator<PlanPhase> getInitialPhaseIterator();

    /**
     * @return the final {@code PlanPhase}
     */
    public PlanPhase getFinalPhase();

    /**
     * @return the {@code BillingPeriod}
     */
    public BillingPeriod getRecurringBillingPeriod();

    /**
     * @return the number of instance of subscriptions in a bundle with that {@code Plan}
     */
    public int getPlansAllowedInBundle();

    /**
     * @return an array of {@code PlanPhase}
     */
    public PlanPhase[] getAllPhases();

    /**
     * @return the date for which existing subscriptions become effective with that {@code Plan}
     */
    public Date getEffectiveDateForExistingSubscriptons();

    /**
     * @param name the name of the {@code PlanPhase}
     * @return the {@code PlanPhase}
     * @throws CatalogApiException if there is not such {@code PlanPhase}
     */
    public PlanPhase findPhase(String name) throws CatalogApiException;

    /**
     * @param subscriptionStartDate the subscriptionStartDate
     * @param intialPhaseType       the type of the initial phase
     * @return the date at which we see the first recurring non zero charge
     */
    public DateTime dateOfFirstRecurringNonZeroCharge(DateTime subscriptionStartDate, PhaseType intialPhaseType);

}
