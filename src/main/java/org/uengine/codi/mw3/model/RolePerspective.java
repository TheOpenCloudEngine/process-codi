package org.uengine.codi.mw3.model;

import org.metaworks.MetaworksContext;
import org.metaworks.website.MetaworksFile;

public class RolePerspective extends CollapsePerspective {

	public RolePerspective() {
		setLabel("$Role");
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
		
		setChild(roleList);				
	}

	@Override
	protected void unloadChildren() throws Exception {
		setChild(null);
	}
	
	@Override
	public Popup popupAdd() {
		IRole role = new Role();
		role.getMetaworksContext().setWhere("admin");
		role.getMetaworksContext().setWhen(MetaworksContext.WHEN_NEW);
		role.setLogoFile(new MetaworksFile());
		
		return new Popup(500, 200, role);
	}
	
}
