package org.uengine.codi.mw3.billing.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by uEngineYBS on 2016-06-21.
 */
public class AccountTimeline extends BillingObject {

    private Account account;
    private List<Bundle> bundles;
    private List<Invoice> invoices;
    private List<InvoicePayment> payments;

    public AccountTimeline() { }

    @JsonCreator
    public AccountTimeline(@JsonProperty("account") final Account account,
                           @JsonProperty("bundles") final List<Bundle> bundles,
                           @JsonProperty("invoices") final List<Invoice> invoices,
                           @JsonProperty("payments") final List<InvoicePayment> payments) {
        this.account = account;
        this.bundles = bundles;
        this.invoices = invoices;
        this.payments = payments;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(final Account account) {
        this.account = account;
    }

    public List<Bundle> getBundles() {
        return bundles;
    }

    public void setBundles(final List<Bundle> bundles) {
        this.bundles = bundles;
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(final List<Invoice> invoices) {
        this.invoices = invoices;
    }

    public List<InvoicePayment> getPayments() {
        return payments;
    }

    public void setPayments(final List<InvoicePayment> payments) {
        this.payments = payments;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AccountTimeline{");
        sb.append("account=").append(account);
        sb.append(", bundles=").append(bundles);
        sb.append(", invoices=").append(invoices);
        sb.append(", payments=").append(payments);
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

        final AccountTimeline that = (AccountTimeline) o;

        if (account != null ? !account.equals(that.account) : that.account != null) {
            return false;
        }
        if (bundles != null ? !bundles.equals(that.bundles) : that.bundles != null) {
            return false;
        }
        if (invoices != null ? !invoices.equals(that.invoices) : that.invoices != null) {
            return false;
        }
        if (payments != null ? !payments.equals(that.payments) : that.payments != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = account != null ? account.hashCode() : 0;
        result = 31 * result + (bundles != null ? bundles.hashCode() : 0);
        result = 31 * result + (invoices != null ? invoices.hashCode() : 0);
        result = 31 * result + (payments != null ? payments.hashCode() : 0);
        return result;
    }
}
