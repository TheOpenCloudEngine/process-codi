package org.uengine.codi.mw3.subscription;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.ServiceMethod;
import org.uengine.codi.mw3.model.Session;
import org.uengine.codi.util.BillingHttpClient;

import java.util.List;

/**
 * Created by jjy on 2016. 6. 15..
 */
public class Invoice {

    @AutowiredFromClient
    public Session session;


    @Hidden
    public String invoiceList;
        public String getInvoiceList() { return invoiceList; }
        public void setInvoiceList(String invoiceList) { this.invoiceList = invoiceList; }

    @ServiceMethod
    public void refresh(){

    }


    /*
    Double amount;
        public Double getAmount() {
            return amount;
        }
        public void setAmount(Double amount) {
            this.amount = amount;
        }
*/

    public void load() {

        //setAmount(1000d); // value from killbill
        //String billingAccount = session.getCompany().getBillAccnt();
        String billingSubscription = session.getCompany().getBillSbscr();
        String invoiceInfo = "";
        if(billingSubscription != null) {
            BillingHttpClient billingHttpClient = new BillingHttpClient();
            List<org.uengine.codi.mw3.billing.model.Invoice> invoiceList = billingHttpClient.getInvoicesForAccount(session.getCompany().getBillAccnt());
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                this.setInvoiceList(objectMapper.writeValueAsString(invoiceList));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

        } else {
            //TODO
        }

    }
}
