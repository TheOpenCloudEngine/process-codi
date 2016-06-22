package org.uengine.codi.mw3.subscription;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;
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
    public Usages(){
    }

    @AutowiredFromClient
    public Session session;

    public String startDate;
        public String getStartDate() { return startDate; }
        public void setStartDate(String startDate) { this.startDate = startDate; }

    public String endDate;
        public String getEndDate() { return endDate; }
        public void setEndDate(String endDate) { this.endDate = endDate; }

    @Hidden
    public String usagesInfo;
        public String getUsagesInfo() { return usagesInfo; }
        public void setUsagesInfo(String usagesInfo) { this.usagesInfo = usagesInfo; }

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

    @ServiceMethod(callByContent=true)
    public void getUsages(){
            this.load();
    }

    public void load() {
        String startDate = this.getStartDate();
        String endDate = this.getEndDate();

        if(startDate != null && endDate != null) {
            BillingHttpClient billingHttpClient = new BillingHttpClient();
            RolledUpUsage resultRolledUpUsage = billingHttpClient.getRolledUpUsage(session.getCompany().getKillbillSubscription(), "GByte", startDate, endDate);
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                this.setUsagesInfo(objectMapper.writeValueAsString(resultRolledUpUsage));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

}
