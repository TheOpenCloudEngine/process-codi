package org.uengine.codi.mw3.admin;

import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.AutowiredToClient;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dwr.MetaworksRemoteService;
import org.metaworks.widget.ModalWindow;
import org.uengine.codi.mw3.model.*;

public class TopPanel {

	public TopPanel(){

	}
	
	public TopPanel(Session session) {
		//setSession(session);
		setUx(session.getUx());
		
		notificationBadge = new NotificationBadge();

		todoBadge = new TodoBadge();
		
		TopPanelUser topPanelUser = new TopPanelUser();
		try {
			topPanelUser.copyFrom(session.getUser());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setLoginUser(topPanelUser);
		this.getLoginUser().getMetaworksContext().setHow(IUser.HOW_SELF);
		
		if(!SNS.isPhone()){
			TopMenuPanel topMenuPanel = new TopMenuPanel();
			this.setTopMenuPanel(topMenuPanel);
		}

		setCompany(session.getCompany());

		topSearchBox = new TopSearchBox();
		
	}
	
	NotificationBadge notificationBadge;
		public NotificationBadge getNotificationBadge() {
			return notificationBadge;
		}
		public void setNotificationBadge(NotificationBadge notificationBadge) {
			this.notificationBadge = notificationBadge;
		}
		
	TodoBadge todoBadge;
		public TodoBadge getTodoBadge(){
			return todoBadge;
		}
		public void setTodoBadge(TodoBadge todoBadge){
			this.todoBadge = todoBadge;
		}
		
	String pageType;
		public String getPageType() {
			return pageType;
		}
		public void setPageType(String pageType) {
			this.pageType = pageType;
		}

	String topCenterTitle;
		@Hidden
		public String getTopCenterTitle() {
			return topCenterTitle==null?"Dashboard":topCenterTitle;
		}
		public void setTopCenterTitle(String topCenterTitle) {
			this.topCenterTitle = topCenterTitle;
		}

	@AutowiredFromClient
	public Session session;
//	@AutowiredToClient
//		public Session getSession() {
//			return session;
//		}
//		public void setSession(Session session) {
//			this.session = session;
//		}


	String ux;
		public String getUx() {
			return ux;
		}

		public void setUx(String ux) {
			this.ux = ux;
		}


	TopPanelUser loginUser;
		public TopPanelUser getLoginUser() {
			return loginUser;
		}
		public void setLoginUser(TopPanelUser loginUser) {
			this.loginUser = loginUser;
		}
		
	String mode;
		public String getMode() {
			return mode;
		}
		public void setMode(String mode) {
			this.mode = mode;
		}

	ICompany company;
		public ICompany getCompany() {
			return company;
		}
		public void setCompany(ICompany company) {
			this.company = company;
		}
		
	TopCenterPanel topCenterPanel;
	public TopCenterPanel getTopCenterPanel() {
		return topCenterPanel;
	}
	public void setTopCenterPanel(TopCenterPanel topCenterPanel) {
		this.topCenterPanel = topCenterPanel;
	}

	TopMenuPanel topMenuPanel;
	public TopMenuPanel getTopMenuPanel() {
		return topMenuPanel;
	}

	TopSearchBox topSearchBox;
	public TopSearchBox getTopSearchBox() {
		return topSearchBox;
	}
	public void setTopSearchBox(TopSearchBox topSearchBox) {
		this.topSearchBox = topSearchBox;
	}

	public void setTopMenuPanel(TopMenuPanel topMenuPanel) {
		this.topMenuPanel = topMenuPanel;
	}

	@ServiceMethod(target=ServiceMethodContext.TARGET_STICK, onLoad = true)
	public Popup showApps() throws Exception{
		AllAppList allAppList = MetaworksRemoteService.getComponent(AllAppList.class);
		//allAppList.session = session;

		Popup popup = new Popup();
		popup.setPanel(allAppList);

		return popup;
	}
}
