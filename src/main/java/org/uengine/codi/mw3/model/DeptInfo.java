package org.uengine.codi.mw3.model;

import org.metaworks.EventContext;
import org.metaworks.MetaworksContext;
import org.metaworks.ToEvent;
import org.metaworks.common.MetaworksUtil;
import org.metaworks.website.MetaworksFile;

public class DeptInfo extends FollowerPerspectiveInfo{

	public final static int MODIFY_POPUP_HEIGHT = 200;
	public final static int MODIFY_POPUP_WIDTH = 500;
	
	public DeptInfo(){
		
	}
	
	public DeptInfo(Session session, String type) throws Exception{
		super(session, type);
		
		this.setId(session.getLastSelectedItem());
		this.load();
	}

	@Override
	public void load() throws Exception {
		DeptFollower follower = new DeptFollower();
		follower.session = session;
		follower.setParentId(this.getId());

		this.setFollower(follower);
		
		super.load();
	}
	
	@Override
	public Popup modify() throws Exception {
		
		Dept dept = new Dept();
		dept.setPartCode(this.getId());
		dept.copyFrom(dept.databaseMe());
		
		dept.getMetaworksContext().setWhere("admin");
		dept.getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);
		dept.setLogoFile(new PortraitImageFile());
		
		return new Popup(MODIFY_POPUP_WIDTH, MODIFY_POPUP_HEIGHT, dept);
	}
	
	@Override
	public Object[] remove() throws Exception {
		Dept dept = new Dept();
		dept.setPartCode(this.getId());		
		dept.copyFrom(dept.databaseMe());
		dept.setIsDeleted("1");
				
		dept.syncToDatabaseMe();
		dept.flushDatabaseMe();
		
		return MetaworksUtil.putObjectArray(MetaworksUtil.makeRefreshObjectArray(super.remove()), new ToEvent(new DeptPerspective(), EventContext.EVENT_CHANGE));
	}
	/*
	@ServiceMethod(target=ServiceMethodContext.TARGET_POPUP)
	@Face(displayName="$part.addNewChildDept")
	public Object addNewChildDept() throws Exception {
		IDept newDept = new Dept();
		newDept.setParent_PartCode(this.getId());
		newDept.getMetaworksContext().setWhen(MetaworksContext.WHEN_NEW);
		newDept.getMetaworksContext().setWhere("admin");
		newDept.setLogoFile(new MetaworksFile());
		
		Popup popup = new Popup();
		popup.setPanel(newDept);
		
		return popup;
	}

	

	@Override
	public void followersLoad() throws Exception {
		followers = new DeptFollowers();
		followers.session = session;
		followers.load();
	}
	

	@Override
	public Object[] subscribe() throws Exception {
		
		User user = new User();
		user.setUserId(session.getEmployee().getEmpCode());
		user.setName(session.getEmployee().getEmpName());
		user.session = this.session;
		user.setMetaworksContext(new MetaworksContext());
		user.getMetaworksContext().setHow("follower");
		user.getMetaworksContext().setWhen(Followers.ADD_DEPTFOLLOWERS);
		
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
		user.getMetaworksContext().setWhen("deptFollowers");
		
		followers.removeFollower(user);

		return new Object[]{new ToEvent(ServiceMethodContext.TARGET_SELF, EventContext.EVENT_CHANGE)};
		
	}
	
	
	@Override
	public void settingJoined() throws Exception {

		Employee emp = new Employee();
		emp.setEmpCode(session.getEmployee().getEmpCode());
		emp.setPartCode(this.getId());
		IEmployee findEmp = emp.findMe();
		
		if(this.getId().equals(findEmp.getPartCode()))
			this.setJoined(true);
		else
			this.setJoined(false);
		
				
	}
	*/
}
