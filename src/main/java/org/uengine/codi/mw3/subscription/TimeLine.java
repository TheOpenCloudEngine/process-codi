package org.uengine.codi.mw3.subscription;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Hidden;
import org.uengine.codi.mw3.billing.model.*;
import org.uengine.codi.mw3.model.Session;
import org.uengine.codi.util.BillingHttpClient;

import java.util.List;

/**
 * Created by uEngineYBS on 2016-06-21.
 */
public class TimeLine {

    @AutowiredFromClient
    public Session session;


    @Hidden
    public String timeLineInfo;
        public String getTimeLineInfo() { return timeLineInfo; }
        public void setTimeLineInfo(String timeLineInfo) { this.timeLineInfo = timeLineInfo; }


    public void load() {

        String billingSubscription = session.getCompany().getKillbillSubscription();
        String timeLineInfo = "";
        if(billingSubscription != null) {
            BillingHttpClient billingHttpClient = new BillingHttpClient();
            AccountTimeline timeLine = billingHttpClient.getAccountTimeline(session.getCompany().getKillbillAccount());
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                this.setTimeLineInfo(objectMapper.writeValueAsString(timeLine));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

        } else {
            //TODO
        }

    }

}
