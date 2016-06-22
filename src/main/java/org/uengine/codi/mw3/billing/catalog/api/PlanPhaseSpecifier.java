package org.uengine.codi.mw3.billing.catalog.api;

/**
 * Created by uEngineYBS on 2016-06-20.
 */
public class PlanPhaseSpecifier {

    private final PhaseType phaseType;
    private final String productName;
    private final ProductCategory productCategory;
    private final BillingPeriod billingPeriod;
    private final String priceListName;

    public PlanPhaseSpecifier(final String productName, final ProductCategory productCategory, final BillingPeriod billingPeriod,
                              final String priceListName, final PhaseType phaseType) {
        this.phaseType = phaseType;
        this.productName = productName;
        this.productCategory = productCategory;
        this.billingPeriod = billingPeriod;
        this.priceListName = priceListName;
    }

    /**
     * @return the {@code Product} name
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @return the {@code ProductCategory}
     */
    public ProductCategory getProductCategory() {
        return productCategory;
    }

    /**
     * @return the {@code BillingPeriod}
     */
    public BillingPeriod getBillingPeriod() {
        return billingPeriod;
    }

    /**
     * @return the name of the {@code PriceList}
     */
    public String getPriceListName() {
        return priceListName;
    }

    /**
     * @return the {@code PhaseType}
     */
    public PhaseType getPhaseType() {
        return phaseType;
    }

    /**
     * @return the {@code PlanSpecifier}
     */
    public PlanSpecifier toPlanSpecifier() {
        return new PlanSpecifier(productName, productCategory, billingPeriod, priceListName);
    }
}
