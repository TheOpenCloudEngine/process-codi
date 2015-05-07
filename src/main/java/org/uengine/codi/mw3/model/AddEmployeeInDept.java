package org.uengine.codi.mw3.model;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.Refresh;
import org.metaworks.Remover;
import org.metaworks.ServiceMethodContext;
import org.metaworks.ToOpener;
import org.metaworks.annotation.ServiceMethod;
import org.uengine.codi.mw3.admin.SearchEmpolyeePanel;

public class AddEmployeeInDept implements ContextAware {
	MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}
	String deptId;
		public String getDeptId() {
			return deptId;
		}
		public void setDeptId(String deptId) {
			this.deptId = deptId;
		}
	String empCodeList;
		public String getEmpCodeList() {
			return empCodeList;
		}
		public void setEmpCodeList(String empCodeList) {
			this.empCodeList = empCodeList;
		}
	SearchEmpolyeePanel searchEmpolyeePanel;
		public SearchEmpolyeePanel getSearchEmpolyeePanel() {
			return searchEmpolyeePanel;
		}
		public void setSearchEmpolyeePanel(SearchEmpolyeePanel searchEmpolyeePanel) {
			this.searchEmpolyeePanel = searchEmpolyeePanel;
		}
	public AddEmployeeInDept(){
		searchEmpolyeePanel = new SearchEmpolyeePanel();
		setMetaworksContext(new MetaworksContext());
	}
	
	@ServiceMethod(callByContent=true , target=ServiceMethodContext.TARGET_APPEND)
	public Object[] saveEmployeeInDept() throws Exception{
		if( empCodeList != null && deptId != null ){
			String empCodes[] = empCodeList.split(",");
			for(int i =0; i < empCodes.length; i++){
				String empcode = empCodes[i];
				Employee employee = new Employee();
				employee.setEmpCode(empcode);
				employee.databaseMe().setPartCode(deptId);
			}
		}
		return new Object[]{new Remover(new Popup() , true)};
	}
}
