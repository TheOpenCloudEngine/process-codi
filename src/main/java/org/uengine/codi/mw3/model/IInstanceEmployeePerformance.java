package org.uengine.codi.mw3.model;

import org.metaworks.annotation.Id;
import org.metaworks.annotation.Table;
import org.metaworks.dao.IDAO;

@Table(name="inst_emp_perf")
public interface IInstanceEmployeePerformance extends IDAO{
	
	@Id
	public Long getInstId();
	public void setInstId(Long instId);
	
	public String getEmpCode();
	public void setEmpCode(String empCode);
	
	public int getBusinessValue();
	public void setBusinessValue(int value);
	
}
