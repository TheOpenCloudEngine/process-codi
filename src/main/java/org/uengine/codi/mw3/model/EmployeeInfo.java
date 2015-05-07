package org.uengine.codi.mw3.model;

import org.metaworks.annotation.Face;

@Face(ejsPath="genericfaces/CleanObjectFace.ejs")
public class EmployeeInfo {
	
	public EmployeeInfo(IEmployee employee){
		setEmployee(employee);
	}
	
	IEmployee employee;
	public IEmployee getEmployee() {
		return employee;
	}	
	public void setEmployee(IEmployee employee) {
		this.employee = employee;
	}
}
