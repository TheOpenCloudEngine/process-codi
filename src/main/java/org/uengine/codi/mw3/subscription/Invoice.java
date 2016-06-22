package org.uengine.codi.mw3.subscription;

import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.ServiceMethod;
import org.uengine.codi.mw3.model.Session;

/**
 * Created by jjy on 2016. 6. 15..
 */
public class Invoice {

    @AutowiredFromClient
    public Session session;

    @ServiceMethod
    public void refresh(){

    }


    Double amount;
        public Double getAmount() {
            return amount;
        }
        public void setAmount(Double amount) {
            this.amount = amount;
        }


    public void load() {

        setAmount(1000d); // value from killbill
    }
}
