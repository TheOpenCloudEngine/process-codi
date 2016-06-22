package org.uengine.codi.mw3.subscription;

import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.ServiceMethod;
import org.uengine.codi.mw3.billing.model.RolledUpUsage;
import org.uengine.codi.mw3.billing.model.SubscriptionUsageRecord;
import org.uengine.codi.mw3.billing.model.UnitUsageRecord;
import org.uengine.codi.mw3.billing.model.UsageRecord;
import org.uengine.codi.mw3.model.Session;
import org.uengine.codi.util.BillingHttpClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by uEngineYBS on 2016-06-21.
 */
public class Usages {

    @AutowiredFromClient
    public Session session;

    @ServiceMethod
    public void createUsages(){

        BillingHttpClient billingHttpClient = new BillingHttpClient();
        SubscriptionUsageRecord subscriptionUsageRecord = new SubscriptionUsageRecord();
        subscriptionUsageRecord.setSubscriptionId(UUID.fromString(session.getCompany().getKillbillSubscription()));
        List<UnitUsageRecord> unitUsageRecords = new ArrayList<>();
        UnitUsageRecord unitUsageRecord = new UnitUsageRecord();
        unitUsageRecord.setUnitType("GByte");
        List<UsageRecord> usageRecords = new ArrayList<>();
        UsageRecord usageRecord = new UsageRecord();
        usageRecord.setAmount(Long.parseLong("1"));

        Date now = new Date();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
        String nowDateString = transFormat.format(now);

        usageRecord.setRecordDate(nowDateString);
        usageRecords.add(usageRecord);
        unitUsageRecord.setUsageRecords(usageRecords);
        unitUsageRecords.add(unitUsageRecord);
        subscriptionUsageRecord.setUnitUsageRecords(unitUsageRecords);
        billingHttpClient.createSubscriptionUsageRecord(subscriptionUsageRecord, "admin");

    }

    @ServiceMethod
    public void getUsages(){
        BillingHttpClient billingHttpClient = new BillingHttpClient();
        String startDate = "2016-06-01";
        String endDate = "2016-07-01";
        RolledUpUsage result = billingHttpClient.getRolledUpUsage(session.getCompany().getKillbillSubscription(),"GByte", startDate, endDate);
        System.out.println(result);
    }

    public void load() {

    }

}
