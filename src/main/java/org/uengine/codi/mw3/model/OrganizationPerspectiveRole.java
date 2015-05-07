package org.uengine.codi.mw3.model;

import org.metaworks.MetaworksContext;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.website.MetaworksFile;

public class OrganizationPerspectiveRole extends CollapsePerspective {

	public OrganizationPerspectiveRole() {
		setLabel("Organization");
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
		setRoleList(null);
	}
	
	@ServiceMethod(target=ServiceMethodContext.TARGET_STICK)
	public Popup addRole() {
		IRole role = new Role();
		role.getMetaworksContext().setWhere("admin");
		role.getMetaworksContext().setWhen(MetaworksContext.WHEN_NEW);
		role.setLogoFile(new MetaworksFile());
		
		Popup popup = new Popup();
		popup.setPanel(role);
		
		return popup;
	}
	
}
