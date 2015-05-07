package org.uengine.codi.mw3.admin;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.ServiceMethod;
import org.uengine.codi.mw3.model.Employee;
import org.uengine.codi.mw3.model.EmployeeList;
import org.uengine.codi.mw3.model.IEmployee;

public class SearchEmpolyeePanel implements ContextAware {
	
	MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}
	String empName;
		public String getEmpName() {
			return empName;
		}
		public void setEmpName(String empName) {
			this.empName = empName;
		}

	EmployeeList employeeList;
		public EmployeeList getEmployeeList() {
			return employeeList;
		}
		public void setEmployeeList(EmployeeList employeeList) {
			this.employeeList = employeeList;
		}
	
	@ServiceMethod(callByContent=true , target= ServiceMethodContext.TARGET_AUTO)
	public Object[] searchEmployee() throws Exception{
		if( getEmpName() != null && !"".equals(getEmpName())){
			employeeList = new EmployeeList();
			employeeList.setId("search");
			employeeList.setCheckBox(true);
			employeeList.getMetaworksContext().setHow("searchList");
			Employee employee = new Employee();
			employee.setEmpName(getEmpName());
			IEmployee emp = employee.findMeByEmpName();
			employeeList.setEmployee(emp);
			return new Object[]{this};
		}
		return null;
	}
}
