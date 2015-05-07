package org.uengine.codi.mw3.model;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.ServiceMethod;

public class DeptPicker {

	public DeptPicker() {
	}

	public DeptPicker(String comCode) {
		setGlobalCom(comCode);
	}

	String globalCom;
		@Hidden
		public String getGlobalCom() {
			return globalCom;
		}
		public void setGlobalCom(String globalCom) {
			this.globalCom = globalCom;
		}

	DeptList deptList;
		public DeptList getDeptList() {
			return deptList;
		}
	
		public void setDeptList(DeptList deptList) {
			this.deptList = deptList;
		}
		
	public void load() throws Exception {
		IDept dept = new Dept();
		dept.getMetaworksContext().setHow("tree");
		dept.getMetaworksContext().setWhere("deptPicker");
		dept.setGlobalCom(this.getGlobalCom());
		
		DeptList deptList = new DeptList();		
		deptList.setId("/ROOT/");		
		deptList.setDept(dept.findByGlobalCom());
		
		setDeptList(deptList);
	}
	
	@ServiceMethod(target=ServiceMethodContext.TARGET_POPUP)
	public Popup addDept() {
		IDept dept = new Dept();
		dept.getMetaworksContext().setWhere("admin");
		dept.getMetaworksContext().setWhen(MetaworksContext.WHEN_NEW);
		
		Popup popup = new Popup();
		popup.setPanel(dept);
		
		return popup;
	}	
}
