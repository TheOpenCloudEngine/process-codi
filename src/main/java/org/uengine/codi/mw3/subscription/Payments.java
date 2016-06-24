package org.uengine.codi.mw3.subscription;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Hidden;
import org.uengine.codi.mw3.billing.model.Payment;
import org.uengine.codi.mw3.model.Session;
import org.uengine.codi.util.BillingHttpClient;

import java.util.List;

/**
 * Created by uEngineYBS on 2016-06-21.
 */
public class Payments {

    @AutowiredFromClient
    public Session session;

    @Hidden
    public String paymentsInfo;
        public String getPaymentsInfo() { return paymentsInfo; }
        public void setPaymentsInfo(String paymentsInfo) { this.paymentsInfo = paymentsInfo; }


    public void load() {

        String billingSubscription = session.getCompany().getBillSbscr();
        if(billingSubscription != null) {
            BillingHttpClient billingHttpClient = new BillingHttpClient();
            List<Payment> paymentsList = billingHttpClient.getPaymentForAccount(session.getCompany().getBillAccnt());
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                this.setPaymentsInfo(objectMapper.writeValueAsString(paymentsList));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

        } else {
            //TODO
        }

    }

}
