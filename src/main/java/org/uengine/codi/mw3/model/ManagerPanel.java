package org.uengine.codi.mw3.model;

import org.metaworks.annotation.Face;

@Face(ejsPath="genericfaces/Tab.ejs")
public class ManagerPanel {

	ICompany company;
	
		public ICompany getCompany() {
			return company;
		}
	
		public void setCompany(ICompany company) {
			this.company = company;
		}
		
	Billing billing;
	
		public Billing getBilling() {
			return billing;
		}
	
		public void setBilling(Billing billing) {
			this.billing = billing;
		}

	OrganizationPerspective organizationChart;
	
		public OrganizationPerspective getOrganizationChart() {
			return organizationChart;
		}
	
		public void setOrganizationChart(OrganizationPerspective organizationChart) {
			this.organizationChart = organizationChart;
		}
		
		

	public ManagerPanel(Session session) throws Exception{
		organizationChart = new OrganizationPerspective();
		organizationChart.session = session;
		organizationChart.loadChildren();

		Company myCompany = new Company();
		myCompany.setComCode(session.getEmployee().getGlobalCom());
		company = myCompany.databaseMe();
		company.getMetaworksContext().setWhen("edit");
		
		billing = new Billing();
	}
		
	
	
}
