package org.uengine.codi.mw3.model;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.Id;

public class EmployeeList implements ContextAware {
	
	public EmployeeList() {
		setMetaworksContext(new MetaworksContext());
	}
	
	String id;
		@Id
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		
	IEmployee employee;
		public IEmployee getEmployee() {
			return employee;
		}
		public void setEmployee(IEmployee employee) {
			this.employee = employee;
		}
	boolean checkBox; 
		public boolean isCheckBox() {
			return checkBox;
		}
		public void setCheckBox(boolean checkBox) {
			this.checkBox = checkBox;
		}

	MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}
	
}