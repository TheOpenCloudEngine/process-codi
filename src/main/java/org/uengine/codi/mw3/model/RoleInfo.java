package org.uengine.codi.mw3.model;

import org.metaworks.EventContext;
import org.metaworks.MetaworksContext;
import org.metaworks.ToEvent;
import org.metaworks.common.MetaworksUtil;
import org.metaworks.website.MetaworksFile;

public class RoleInfo extends FollowerPerspectiveInfo {

	public final static int MODIFY_POPUP_HEIGHT = 200;
	public final static int MODIFY_POPUP_WIDTH = 500;
	
	public RoleInfo(){
		
	}
	
	public RoleInfo(Session session) throws Exception{
		super(session, Perspective.TYPE_NEWSFEED);
		
		this.setId(session.getLastSelectedItem());
		this.load();
	}
	
	@Override
	public void load() throws Exception {
		RoleFollower follower = new RoleFollower();
		follower.setParentId(this.getId());
		
		this.setFollower(follower);
		
		super.load();
	}
	
	@Override
	public Object[] remove() throws Exception {
		Role role = new Role();
		role.setRoleCode(this.getId());		
		role.copyFrom(role.databaseMe());
		role.setIsDeleted("1");
		
		role.syncToDatabaseMe();
		role.flushDatabaseMe();
		
		return MetaworksUtil.putObjectArray(MetaworksUtil.makeRefreshObjectArray(super.remove()), new ToEvent(new RolePerspective(), EventContext.EVENT_CHANGE));
		
	}

	@Override
	public Popup modify() throws Exception {
		Role role = new Role();
		role.setRoleCode(this.getId());
		role.copyFrom(role.databaseMe());
		
		role.getMetaworksContext().setWhere("admin");
		role.getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);
		role.setLogoFile(new MetaworksFile());
		
		return new Popup(MODIFY_POPUP_WIDTH, MODIFY_POPUP_HEIGHT, role);
	}

	/*
	@Override
	public Object[] subscribe() throws Exception {
		
		User user = new User();
		user.setUserId(session.getEmployee().getEmpCode());
		user.setName(session.getEmployee().getEmpName());
		user.session = this.session;
		user.setMetaworksContext(new MetaworksContext());
		user.getMetaworksContext().setHow("follower");
		user.getMetaworksContext().setWhen(Followers.ADD_ROLEFOLLOWERS);
		
		followers.addFollower(user);
		
		return new Object[]{new ToEvent(ServiceMethodContext.TARGET_SELF, EventContext.EVENT_CHANGE)};
	
	}
	
	
	@Override
	public Object[] unSubscribe() throws Exception {
		User user = new User();
		user.setUserId(session.getEmployee().getEmpCode());
		user.setName(session.getEmployee().getEmpName());
		user.session = this.session;
		user.setMetaworksContext(new MetaworksContext());
		user.getMetaworksContext().setWhen("roleFollowers");
		
		followers.removeFollower(user);

		return new Object[]{new ToEvent(ServiceMethodContext.TARGET_SELF, EventContext.EVENT_CHANGE)};
	}
	@Override
	public void settingJoined() throws Exception {

		RoleUser roleUser = new RoleUser();
		roleUser.setRoleCode(this.getId());
		roleUser.setEmpCode(session.getEmployee().getEmpCode());
		IRoleUser findRoleUser = roleUser.findMe();
		
		if(findRoleUser != null)
			this.setJoined(true);
		else
			this.setJoined(false);

		
				
	}
	*/

}
