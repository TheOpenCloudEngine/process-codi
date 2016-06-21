package org.uengine.codi.mw3.billing.entitlement;

import org.joda.time.LocalDate;
import org.uengine.codi.mw3.billing.catalog.api.*;
import org.uengine.codi.mw3.billing.exception.EntitlementApiException;
import org.uengine.codi.mw3.billing.payment.api.PluginProperty;
import org.uengine.codi.mw3.billing.security.Permission;
import org.uengine.codi.mw3.billing.security.RequiresPermissions;
import org.uengine.codi.mw3.billing.util.callcontext.CallContext;

import java.util.List;
import java.util.UUID;

/**
 * Created by uEngineYBS on 2016-06-20.
 */
public interface Entitlement {

    /**
     * Used to control the effective date that should be used on Plan change or on cancellation.
     */
    public enum EntitlementActionPolicy {
        /* Immediate */
        IMMEDIATE,
        /* End of Term */
        END_OF_TERM
    }

    /**
     * Possible states of an <code>Entitlement</code>
     */
    public enum EntitlementState {
        /* The entitlement was created in that initial state */
        ACTIVE,
        /* The system blocked the entitlement */
        BLOCKED,
        /* The user cancelled the entitlement */
        CANCELLED
    }

    /**
     * Possible <code>Entitlement</code> source types.
     */
    public enum EntitlementSourceType {
        NATIVE,
        MIGRATED,
        TRANSFERRED
    }

    /**
     * @return the unique id of the base entitlement
     */
    public UUID getBaseEntitlementId();

    /**
     * @return the unique Id of the SubscriptionBundle
     */
    public UUID getBundleId();

    /**
     * @return the account id
     */
    public UUID getAccountId();

    /**
     * @return the external key that was supplied when creating the base entitlement
     */
    public String getExternalKey();

    /**
     * @return the state of the entitlement
     */
    public EntitlementState getState();

    /**
     * @return the entitlement source type
     */
    public EntitlementSourceType getSourceType();

    /**
     * @return the start date of the entitlement
     */
    public LocalDate getEffectiveStartDate();

    /**
     * @return the end date of the entitlement, that is the date at which it got cancelled.
     */
    public LocalDate getEffectiveEndDate();

    /**
     * @return the last <code>Product</code> prior to cancellation
     */
    public Product getLastActiveProduct();

    /**
     * @return the last <code>Plan</code> prior to cancellation
     */
    public Plan getLastActivePlan();

    /**
     * @return the last <code>Phase</code> prior to cancellation
     */
    public PlanPhase getLastActivePhase();

    /**
     * @return the last active PriceList
     */
    public PriceList getLastActivePriceList();

    /**
     * @return the last active ProductCategory
     */
    public ProductCategory getLastActiveProductCategory();

    /**
     * Cancels the <code>Entitlement</code> at the specified date.
     * After this operation, the existing object becomes stale.
     * <p/>
     * <p/>
     * The date is interpreted by the system to be in the timezone specified at the <code>Account</code>
     *
     * @param effectiveDate                the date at which the entitlement should be cancelled
     * @param overrideBillingEffectiveDate use effectiveDate for billing cancellation date as well
     * @param properties                   plugin specific properties
     * @param context                      the context
     * @return the new <code>Entitlement</code> after the cancellation was performed
     * @throws EntitlementApiException if cancellation failed
     */
    @RequiresPermissions(Permission.ENTITLEMENT_CAN_CANCEL)
    public Entitlement cancelEntitlementWithDate(final LocalDate effectiveDate, final boolean overrideBillingEffectiveDate, final Iterable<PluginProperty> properties, final CallContext context)
            throws EntitlementApiException;

    /**
     * Cancel the <code>Entitlement</code> with a policy.
     * After this operation, the existing object becomes stale.
     *
     * @param policy     the policy that is used by the system to calculate the cancellation date
     * @param properties plugin specific properties
     * @param context    the context
     * @return the new <code>Entitlement</code> after the cancellation was performed
     * @throws EntitlementApiException if cancellation failed
     */
    @RequiresPermissions(Permission.ENTITLEMENT_CAN_CANCEL)
    public Entitlement cancelEntitlementWithPolicy(final EntitlementActionPolicy policy, final Iterable<PluginProperty> properties, final CallContext context)
            throws EntitlementApiException;

    /**
     * Cancels the <code>Entitlement</code> at the specified date
     * After this operation, the existing object becomes stale.
     * <p/>
     * The date is interpreted by the system to be in the timezone specified at the <code>Account</code>
     *
     * @param effectiveDate the date at which the entitlement should be cancelled
     * @param billingPolicy the billingPolicy
     * @param properties    plugin specific properties
     * @param context       the context
     * @return the new <code>Entitlement</code> after the cancellation was performed
     * @throws EntitlementApiException if cancellation failed
     */
    @RequiresPermissions(Permission.ENTITLEMENT_CAN_CANCEL)
    public Entitlement cancelEntitlementWithDateOverrideBillingPolicy(final LocalDate effectiveDate, final BillingActionPolicy billingPolicy, final Iterable<PluginProperty> properties, final CallContext context)
            throws EntitlementApiException;

    /**
     * Cancels the <code>Entitlement</code> at the specified date and overrides the default billing policy.
     * After this operation, the existing object becomes stale.
     *
     * @param policy        the policy that is used by the system to calculate the cancellation date
     * @param billingPolicy the override billing policy
     * @param properties    plugin specific properties
     * @param context       the context
     * @return the new <code>Entitlement</code> after the cancellation was performed
     * @throws EntitlementApiException if cancellation failed
     */
    @RequiresPermissions(Permission.ENTITLEMENT_CAN_CANCEL)
    public Entitlement cancelEntitlementWithPolicyOverrideBillingPolicy(final EntitlementActionPolicy policy, final BillingActionPolicy billingPolicy, final Iterable<PluginProperty> properties, final CallContext context)
            throws EntitlementApiException;

    /**
     * Removes a pending future cancellation on an entitlement.
     * <p/>
     * The call will only succeed if the entitlement has been cancelled previously and if the effectiveDate of the cancellation
     * did not occur yet. In such a case it will remove both the cancellation event at the entitlement and billing level-- regardless
     * of when is the effectiveDate of the billing cancellation event.
     *
     * @param properties plugin specific properties
     * @param context    the context
     */
    @RequiresPermissions(Permission.ENTITLEMENT_CAN_CANCEL)
    public void uncancelEntitlement(final Iterable<PluginProperty> properties, final CallContext context)
            throws EntitlementApiException;

    /**
     * Change <code>Entitlement</code> plan using default policy.
     * After this operation, the existing object becomes stale.
     * <p/>
     * The date is interpreted by the system to be in the timezone specified at the <code>Account</code>
     *
     * @param productName   the new product name
     * @param billingPeriod the new billing period
     * @param priceList     the new priceList
     * @param overrides     the price override for each phase and for a specific currency
     * @param properties    plugin specific properties
     * @param context       the context
     * @return the new <code>Entitlement</code> after the change was performed
     * @throws EntitlementApiException if change failed
     */
    @RequiresPermissions(Permission.ENTITLEMENT_CAN_CHANGE_PLAN)
    public Entitlement changePlan(final String productName, final BillingPeriod billingPeriod, final String priceList, final List<PlanPhasePriceOverride> overrides, final Iterable<PluginProperty> properties, final CallContext context)
            throws EntitlementApiException;

    /**
     * Change <code>Entitlement</code> plan at the specified date.
     * After this operation, the existing object becomes stale.
     * <p/>
     * The date is interpreted by the system to be in the timezone specified at the <code>Account</code>
     *
     * @param productName   the new product name
     * @param billingPeriod the new billing period
     * @param priceList     the new priceList
     * @param overrides     the price override for each phase and for a specific currency
     * @param effectiveDate the date at which the entitlement should be changed
     * @param properties    plugin specific properties
     * @param context       the context
     * @return the new <code>Entitlement</code> after the change was performed
     * @throws EntitlementApiException if change failed
     */
    @RequiresPermissions(Permission.ENTITLEMENT_CAN_CHANGE_PLAN)
    public Entitlement changePlanWithDate(final String productName, final BillingPeriod billingPeriod, final String priceList, final List<PlanPhasePriceOverride> overrides, final LocalDate effectiveDate, final Iterable<PluginProperty> properties, final CallContext context)
            throws EntitlementApiException;

    /**
     * Change <code>Entitlement</code> plan at the specified date and overrides the billing policy.
     * After this operation, the existing object becomes stale.
     * <p/>
     * The date is interpreted by the system to be in the timezone specified at the <code>Account</code>
     *
     * @param productName   the new product name
     * @param billingPeriod the new billing period
     * @param priceList     the new priceList
     * @param overrides     the price override for each phase and for a specific currency
     * @param effectiveDate the date at which the entitlement should be changed
     * @param billingPolicy the override billing policy
     * @param properties    plugin specific properties
     * @param context       the context
     * @return the new <code>Entitlement</code> after the change was performed
     * @throws EntitlementApiException if change failed
     */
    @RequiresPermissions(Permission.ENTITLEMENT_CAN_CHANGE_PLAN)
    public Entitlement changePlanOverrideBillingPolicy(final String productName, final BillingPeriod billingPeriod, final String priceList, final List<PlanPhasePriceOverride> overrides, final LocalDate effectiveDate,
                                                       final BillingActionPolicy billingPolicy, final Iterable<PluginProperty> properties, final CallContext context)
            throws EntitlementApiException;

    /**
     * Pauses an <code>Entitlement</code> until it gets resumed.
     * After this operation, the existing object becomes stale.
     * <p/>
     * The date is interpreted by the system to be in the timezone specified at the <code>Account</code>
     *
     * @param serviceName   the service name of who is blocking
     * @param effectiveDate the date at which the entitlement should be paused
     * @param context       the context
     * @return the new <code>Entitlement</code> after the operation was performed
     * @throws EntitlementApiException if the entitlement was not in <tt>ACTIVE</tt> state
    public Entitlement block(String serviceName, final LocalDate effectiveDate, final CallContext context)
    throws EntitlementApiException;
     */

    /**
     * Resumes an <code>Entitlement</code> that was paused
     * After this operation, the existing object becomes stale.
     * <p/>
     * The date is interpreted by the system to be in the timezone specified at the <code>Account</code>
     *
     * @param serviceName   the service name of who is blocking
     * @param effectiveDate the date at which the entitlement should be resumed
     * @param context       the context
     * @return the new <code>Entitlement</code> after the operation was performed
     * @throws EntitlementApiException
    public Entitlement unblock(String serviceName, final LocalDate effectiveDate, final CallContext context)
    throws EntitlementApiException;
     */
}
