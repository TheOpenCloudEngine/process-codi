package org.uengine.codi.mw3.model;

import java.util.ArrayList;
import java.util.Date;

import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.Range;


@Face(displayName="$BillingInfo")
public class Billing {
	
	public Billing(){
		billingHistory = new ArrayList<Bill>();
		Bill bill = new Bill();
		bill.setBillingType("MO");
		bill.setCharge(3251000);
		bill.setIssueDate(new Date());
		billingHistory.add(bill);
	}
	
	String billingType;
	@Range(
			options={"$Yearly","$HalfYearly","$Quarterly", "$Monthly"},
			values= {"YR", "HY", "QT", "MO"}
			)
		public String getBillingType() {
			return billingType;
		}
	
		public void setBillingType(String billingType) {
			this.billingType = billingType;
		}

	ArrayList<Bill> billingHistory;
	
		public ArrayList<Bill> getBillingHistory() {
			return billingHistory;
		}
	
		public void setBillingHistory(ArrayList<Bill> billingHistory) {
			this.billingHistory = billingHistory;
		}

	ICompany company;
	@Hidden
		public ICompany getCompany() {
			return company;
		}
	
		public void setCompany(ICompany company) {
			this.company = company;
		}

}