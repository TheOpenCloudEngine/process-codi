package org.uengine.codi.mw3.model;

import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.ServiceMethod;

public class PerspectiveInfo {

	@AutowiredFromClient
	public Session session;

	String type;
		@Hidden
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		
	String title;
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}	
		
	PerspectiveTopPanel topPanel;
		public PerspectiveTopPanel getTopPanel() {
			return topPanel;
		}
		public void setTopPanel(PerspectiveTopPanel topPanel) {
			this.topPanel = topPanel;
		}
		
	public PerspectiveInfo(){
		setTopPanel(new PerspectiveTopPanel());
	}
	
	public PerspectiveInfo(Session session, String type){
		this();
		
		setTitle(session.getWindowTitle());
		this.session = session;
		this.setType(type);
	}
	
	public Object[] load(String type) throws Exception{
		return new Object[]{session, Perspective.loadInstanceList(session, session.getLastPerspecteMode(), type, session.getLastSelectedItem())};
	}
	
	@Face(displayName = "$All")
	@ServiceMethod
	public Object[] switchToInstanceList() throws Exception {
		return this.load(Perspective.TYPE_NEWSFEED);
	}
	
	@Face(displayName = "$Request")
	@ServiceMethod
	public Object[] switchToRequest() throws Exception{
		return this.load(Perspective.TYPE_STARTEDBYME);
	}

	@Face(displayName = "$Following")
	@ServiceMethod
	public Object[] switchToInstanceListFollowing() throws Exception {
		return this.load(Perspective.TYPE_FOLLOWING);
	}

	@Face(displayName = "$MyToDo")
	@ServiceMethod
	public Object[] switchToInstanceListMyToDo() throws Exception {
		return this.load(Perspective.TYPE_INBOX);
	}

	@Face(displayName = "$Calendar")
	@ServiceMethod
	public Object[] switchToScheduleCalendar() throws Exception {
		return this.load(Perspective.TYPE_CALENDAR);
	}
}
