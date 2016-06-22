package org.uengine.codi.mw3.subscription;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Group;
import org.metaworks.annotation.Order;
import org.uengine.codi.mw3.model.Session;

import static org.metaworks.dwr.MetaworksRemoteService.*;

/**
 * Created by jjy on 2016. 6. 15..
 */
public class TenantConfiguration implements ContextAware {


    public TenantConfiguration(){
    }

    public TenantConfiguration(Session session){
        //load subscription and invoice of session information

        setSubscription(new Subscription());
        autowire(getSubscription());
        getSubscription().load();


        setInvoice(new Invoice());
        autowire(getInvoice());
        getInvoice().load();

        setPayments((new Payments()));
        autowire(getPayments());
        getPayments().load();

        setUsages(new Usages());
        autowire(getUsages());
        getUsages().load();

        setTimeLine(new TimeLine());
        autowire(getTimeLine());
        getTimeLine().load();

    }

    Subscription subscription;
    @Group(name="Subscription")
    @Order(1)
        public Subscription getSubscription() {
            return subscription;
        }
        public void setSubscription(Subscription subscription) {
            this.subscription = subscription;
        }


    Invoice invoice;
    @Group(name="Invoice")
    @Order(2)
        public Invoice getInvoice() {
            return invoice;
        }

        public void setInvoice(Invoice invoice) {
            this.invoice = invoice;
        }

    Payments payments;
    @Group(name="Payments")
    @Order(3)
        public Payments getPayments() { return payments; }

        public void setPayments(Payments payments) { this.payments = payments; }

    Usages usages;
    @Group(name="Usages")
    @Order(4)
        public Usages getUsages() { return usages; }

        public void setUsages(Usages usages) { this.usages = usages; }

    TimeLine timeLine;
    @Group(name="TimeLine")
    @Order(5)
        public TimeLine getTimeLine() { return timeLine; }

        public void setTimeLine(TimeLine timeLine) { this.timeLine = timeLine; }

    @Override
    public MetaworksContext getMetaworksContext() {
        return null;
    }

    @Override
    public void setMetaworksContext(MetaworksContext metaworksContext) {

    }
}
