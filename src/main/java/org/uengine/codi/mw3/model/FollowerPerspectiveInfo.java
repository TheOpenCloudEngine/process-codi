package org.uengine.codi.mw3.model;

import org.metaworks.EventContext;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.Available;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Group;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.ServiceMethod;

public class FollowerPerspectiveInfo extends PerspectiveInfo{
	
	String id;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}

	Follower follower;
		public Follower getFollower() {
			return follower;
		}
		public void setFollower(Follower follower) {
			this.follower = follower;
		}

	Followers followers;
		public Followers getFollowers() {
			return followers;
		}
		public void setFollowers(Followers followers) {
			this.followers = followers;
		}
		
	boolean joined;
		public boolean getJoined() {
			return joined;
		}
		public void setJoined(boolean joined) {
			this.joined = joined;
		}
		
	public FollowerPerspectiveInfo(){
		
	}
	
	public FollowerPerspectiveInfo(Session session, String type){
		super(session, type);
	}
		
	public void load() throws Exception {
		Followers followers = new Followers(follower);
		followers.session = follower.session;
		followers.load();

		boolean isJoined = false;
		while(followers.getFollowers().next()){
			IUser user = followers.getFollowers().getUser();
			
			if(user != null && session.getUser().getUserId().equals(user.getUserId())){
				isJoined = true;
				
				break;
			}
		}
		followers.getFollowers().beforeFirst();
		
		this.setFollowers(followers);
		this.setJoined(isJoined);
	}

	@Face(displayName="$Join")
	@Available(condition="!joined")
	@ServiceMethod(callByContent=true, except="followers")
	public Object[] join() throws Exception {
		this.getFollower().session = session;
		this.getFollower().put(session.getUser());
		this.load();
		
		InstanceListPanel instanceListPanel = Perspective.loadInstanceList(session, session.getLastPerspecteMode(), session.getLastPerspecteType(), this.getId());
		
		return new Object[]{instanceListPanel, this};
	}
	
	@Group(name="menu")
	@Face(displayName="$Leave")
	@Available(condition="joined")
	@ServiceMethod(callByContent=true, except="followers")
	public Object[] leave() throws Exception {
		this.getFollower().session = session;
		this.getFollower().delegate(session.getUser());
		this.load();
		
		InstanceListPanel instanceListPanel = Perspective.loadInstanceList(session, Perspective.MODE_PERSONAL, Perspective.TYPE_NEWSFEED, session.getEmployee().getEmpCode());
		
		ListPanel listPanel = new ListPanel();
		listPanel.setInstanceListPanel(instanceListPanel);
		listPanel.setPerspectiveInfo(new PerspectiveInfo(session, Perspective.TYPE_NEWSFEED));
		
		return new Object[]{listPanel};
	}
	
	@Group(name="menu")
	@Face(displayName="$Modify")
	@ServiceMethod(callByContent=true, except="followers", target=ServiceMethodContext.TARGET_STICK)
	public Popup modify() throws Exception {
		throw new Exception("not defined modify method");
	}
	
	@Group(name="menu")
	@Face(displayName="$Remove")
	@ServiceMethod(callByContent=true, except="followers", target=ServiceMethodContext.TARGET_APPEND)
	public Object[] remove() throws Exception {
		PersonalPerspective personalPerspective = new PersonalPerspective();
		personalPerspective.session = session;
		
		return personalPerspective.loadAllICanSee();
	}

	@Hidden
	@ServiceMethod(callByContent=true, except="followers", eventBinding=EventContext.EVENT_CHANGE)
	public void refresh() throws Exception{
		this.load();
	}
}
