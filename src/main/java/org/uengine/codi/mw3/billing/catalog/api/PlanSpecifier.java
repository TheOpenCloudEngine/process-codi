package org.uengine.codi.mw3.billing.catalog.api;

/**
 * Created by uEngineYBS on 2016-06-20.
 */
public class PlanSpecifier {

    private final String productName;
    private final ProductCategory productCategory;
    private final BillingPeriod billingPeriod;
    private final String priceListName;

    public PlanSpecifier(final String productName, final ProductCategory productCategory, final BillingPeriod billingPeriod,
                         final String priceListName) {
        super();
        this.productName = productName;
        this.productCategory = productCategory;
        this.billingPeriod = billingPeriod;
        this.priceListName = priceListName;
    }

    public PlanSpecifier(final PlanPhaseSpecifier planPhase) {
        super();
        this.productName = planPhase.getProductName();
        this.productCategory = planPhase.getProductCategory();
        this.billingPeriod = planPhase.getBillingPeriod();
        this.priceListName = planPhase.getPriceListName();
    }

    /**
     * @return the name of the product
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
}