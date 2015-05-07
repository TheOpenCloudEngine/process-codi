package org.uengine.codi.mw3.model;

import org.metaworks.MetaworksContext;
import org.metaworks.website.MetaworksFile;

public class DeptPerspective extends CollapsePerspective {

	public DeptPerspective() {
		setLabel("$Group");
	}
	
	@Override
	protected void loadChildren() throws Exception {
		
		IDept dept = new Dept();
		dept.getMetaworksContext().setHow("tree");
		dept.setGlobalCom(session.getEmployee().getGlobalCom());
		
		DeptList deptList = new DeptList();
		deptList.setId("/ROOT/");		
		deptList.setDept(dept.findByGlobalCom());
		
		setChild(deptList);
		
		
//		IEmployee employee = new Employee();
//		employee.getMetaworksContext().setWhere("navigator");
//		employee.getMetaworksContext().setHow("tree");
//		employee.setGlobalCom(session.getEmployee().getGlobalCom());
//		
//		EmployeeList employeeList = new EmployeeList();			
//		employeeList.getMetaworksContext().setWhen("navigator");
//		employeeList.setEmployee(employee.findByDeptOther());
		
//		setDeptEmployee(employeeList);		
	}

	@Override
	protected void unloadChildren() throws Exception {		
		DeptList deptList = new DeptList();
		deptList.setId("/ROOT/");				
		setChild(deptList);
//		EmployeeList employeeList = new EmployeeList();
//		employeeList.setId("/ROOT/");
//		setDeptEmployee(employeeList);				
	}
	
	@Override
	public Popup popupAdd() {
		IDept dept = new Dept();
		dept.getMetaworksContext().setWhen(MetaworksContext.WHEN_NEW);
		dept.setLogoFile(new PortraitImageFile());
		
		return new Popup(500, 200, dept);

	}
}
