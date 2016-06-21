package org.uengine.codi.mw3.subscription.subscription;

import org.metaworks.ContextAware;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.ServiceMethod;
import org.uengine.codi.mw3.billing.model.AccountTimeline;
import org.uengine.codi.mw3.billing.model.Subscriptions;
import org.uengine.codi.mw3.model.Session;
import org.uengine.codi.util.BillingHttpClient;

/**
 * Created by uEngineYBS on 2016-06-21.
 */
public class TimeLine {

    @AutowiredFromClient
    public Session session;

    @ServiceMethod
    public void getTimeLine(){
        BillingHttpClient billingHttpClient = new BillingHttpClient();
        AccountTimeline result = billingHttpClient.getAccountTimeline(session.getCompany().getKillbillAccount());
        System.out.println(result);
    }

    public void load() {

    }

}
