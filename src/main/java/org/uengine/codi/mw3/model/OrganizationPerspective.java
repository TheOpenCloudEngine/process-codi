package org.uengine.codi.mw3.model;

import org.metaworks.MetaworksContext;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.ServiceMethod;

public class OrganizationPerspective extends CollapsePerspective {

	public OrganizationPerspective() {
		setLabel("Organization");
	}
	
	DeptList deptList;		
		public DeptList getDeptList() {
			return deptList;
		}
		public void setDeptList(DeptList deptList) {
			this.deptList = deptList;
		}
		
	RoleList roleList;
		public RoleList getRoleList() {
			return roleList;
		}
		public void setRoleList(RoleList roleList) {
			this.roleList = roleList;
		}
		
	@Override
	protected void loadChildren() throws Exception {
		IDept dept = new Dept();
		dept.getMetaworksContext().setWhere("navigator");
		dept.setGlobalCom(session.getEmployee().getGlobalCom());
		
		DeptList deptList = new DeptList();
		deptList.getMetaworksContext().setWhen("navigator");		
		deptList.setId("/ROOT/");		
		deptList.setDept(dept.findByGlobalCom());
		
		setDeptList(deptList);
		
		
		IRole role = new Role();
		role.getMetaworksContext().setWhere("navigator");
		role.setComCode(session.getEmployee().getGlobalCom());
		
		RoleList roleList = new RoleList();
		roleList.getMetaworksContext().setWhen("navigator");
		roleList.setId("/ROOT/");
		roleList.setRole(role.findByGlobalCom());
		
		setRoleList(roleList);				
	}

	@Override
	protected void unloadChildren() throws Exception {
		setDeptList(null);
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

	@ServiceMethod(target=ServiceMethodContext.TARGET_POPUP)
	public Popup addRole() {
		IRole role = new Role();
		role.getMetaworksContext().setWhere("admin");
		role.getMetaworksContext().setWhen(MetaworksContext.WHEN_NEW);
		
		Popup popup = new Popup();
		popup.setPanel(role);
		
		return popup;
	}
	
}
