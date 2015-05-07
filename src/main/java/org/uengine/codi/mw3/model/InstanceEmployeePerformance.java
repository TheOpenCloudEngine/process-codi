package org.uengine.codi.mw3.model;

import org.metaworks.dao.Database;

public class InstanceEmployeePerformance extends Database<IInstanceEmployeePerformance> implements IInstanceEmployeePerformance{

	Long instId;
		public Long getInstId() {
			return instId;
		}
		public void setInstId(Long instId) {
			this.instId = instId;
		}

	String empCode;
		public String getEmpCode() {
			return empCode;
		}
		public void setEmpCode(String empCode) {
			this.empCode = empCode;
		}
		
	int businessValue;
		public int getBusinessValue() {
			return businessValue;
		}
		public void setBusinessValue(int value) {
			this.businessValue = value;
		}

}