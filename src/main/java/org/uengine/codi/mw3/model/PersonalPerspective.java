package org.uengine.codi.mw3.model;

import org.metaworks.annotation.ServiceMethod;

public class PersonalPerspective extends Perspective {

	public PersonalPerspective() {
		setLabel("$Personal");
	}

	public Object[] load(String type) throws Exception {
		// init search keyword
		session.setSearchKeyword(null);

		InstanceListPanel instanceListPanel = loadInstanceList(session, Perspective.MODE_PERSONAL, type, session.getLastSelectedItem());
		
		ListPanel listPanel = new ListPanel();
		listPanel.setInstanceListPanel(instanceListPanel);
		listPanel.setPerspectiveInfo(new PerspectiveInfo(session, type));
		
		return new Object[]{session, listPanel};
	}
	
	@ServiceMethod
	public Object[] loadAllICanSee() throws Exception{
		session.setLastSelectedItem(session.getUser().getUserId());
		
		return this.load(Perspective.TYPE_NEWSFEED);
	}
	
	@ServiceMethod
	public Object[] loadAll() throws Exception{
		session.setLastSelectedItem(session.getUser().getUserId());
		
		return this.load(Perspective.TYPE_FOLLOWING);
	}
	
	@ServiceMethod
	public Object[] loadInbox() throws Exception{
		session.setLastSelectedItem(session.getUser().getUserId());
		
		return this.load(Perspective.TYPE_INBOX);
	}
	
	@ServiceMethod
	public Object[] loadRequest() throws Exception{
		session.setLastSelectedItem(session.getUser().getUserId());
		
		return this.load(Perspective.TYPE_STARTEDBYME);
	}
	
	@ServiceMethod
	public Object[] calendar() throws Exception{
		session.setLastSelectedItem(session.getUser().getUserId());
		
//		session.setWindowTitle(null);
		
		return this.load(Perspective.TYPE_CALENDAR);
	}
}
