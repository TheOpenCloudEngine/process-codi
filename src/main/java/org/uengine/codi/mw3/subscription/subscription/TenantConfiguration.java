package org.uengine.codi.mw3.subscription.subscription;

import org.uengine.codi.mw3.model.Session;

/**
 * Created by jjy on 2016. 6. 15..
 */
public class TenantConfiguration {


    public TenantConfiguration(){
    }

    public TenantConfiguration(Session session){
        //load subscription and invoice of session information

        setSubscription(new Subscription());
        setInvoice(new Invoice());
    }

    Subscription subscription;
        public Subscription getSubscription() {
            return subscription;
        }

        public void setSubscription(Subscription subscription) {
            this.subscription = subscription;
        }


    Invoice invoice;
        public Invoice getInvoice() {
            return invoice;
        }

        public void setInvoice(Invoice invoice) {
            this.invoice = invoice;
        }

}
