package org.uengine.codi.mw3.subscription.subscription;

import org.metaworks.annotation.Group;
import org.metaworks.annotation.Order;
import org.metaworks.dwr.MetaworksRemoteService;
import org.uengine.codi.mw3.model.Session;

import static org.metaworks.dwr.MetaworksRemoteService.*;

/**
 * Created by jjy on 2016. 6. 15..
 */
public class TenantConfiguration {


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

    Usages usages;
    @Group(name="Usages")
    @Order(3)
        public Usages getUsages() { return usages; }

        public void setUsages(Usages usages) { this.usages = usages; }

    TimeLine timeLine;
    @Group(name="TimeLine")
    @Order(4)
        public TimeLine getTimeLine() { return timeLine; }

        public void setTimeLine(TimeLine timeLine) { this.timeLine = timeLine; }
}
