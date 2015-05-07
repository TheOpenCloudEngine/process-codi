package org.uengine.codi.mw3.model;

import org.metaworks.annotation.AutowiredToClient;
import org.uengine.codi.mw3.admin.WindowPanel;

public class SNSTopPanel {

	public SNSTopPanel(Session session) throws Exception {
		setSession(session);
		setWindowPanel(new WindowPanel());
		tray = new Tray();
		tray.session = session;
		tray.load();
		
		notificationBadge = new NotificationBadge();
		notificationBadge.session = session;
		notificationBadge.refresh();
		
		setLoginUser(session.getUser());

		InstanceSearchBox searchBox = new InstanceSearchBox();
		searchBox.setKeyUpSearch(true);
		searchBox.setKeyEntetSearch(true);
		setSearchBox(searchBox);

	}
	
	SearchBox searchBox;		
		public SearchBox getSearchBox() {
			return searchBox;
		}
		public void setSearchBox(SearchBox searchBox) {
			this.searchBox = searchBox;
		}
		
	WindowPanel windowPanel;
		public WindowPanel getWindowPanel() {
			return windowPanel;
		}	
		public void setWindowPanel(WindowPanel windowPanel) {
			this.windowPanel = windowPanel;
		}
	
	Tray tray;
		public Tray getTray() {
			return tray;
		}
		public void setTray(Tray tray) {
			this.tray = tray;
		}
		
	NotificationBadge notificationBadge;
			
		public NotificationBadge getNotificationBadge() {
			return notificationBadge;
		}
		public void setNotificationBadge(NotificationBadge notificationBadge) {
			this.notificationBadge = notificationBadge;
		}

	Session session;
		@AutowiredToClient
		public Session getSession() {
			return session;
		}
		public void setSession(Session session) {
			this.session = session;
		}
		
	IUser loginUser;
		public IUser getLoginUser() {
			return loginUser;
		}
		public void setLoginUser(IUser loginUser) {
			this.loginUser = loginUser;
		}
		

}
