package org.uengine.codi.mw3.model;

import org.metaworks.Refresh;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.ServiceMethod;


public class NotificationBadge{ 
	
	int newItemCount;
		public int getNewItemCount() {
			return newItemCount;
		}
		public void setNewItemCount(int newItemCount) {
			this.newItemCount = newItemCount;
		}

	boolean loader;
		public boolean isLoader() {
			return loader;
		}
		public void setLoader(boolean loader) {
			this.loader = loader;
		}

	String appName;
		public String getAppName() {
			return appName;
		}
		public void setAppName(String appName) {
			this.appName = appName;
		}


	public NotificationBadge(){
		this.setLoader(true);
		setAppName("Codi");
	}
	
	@ServiceMethod
	public void refresh() throws Exception{
		this.setLoader(false);

		if(session != null){
			Notification notiList = new Notification();
			setNewItemCount(notiList.count(session));
		}
	}

	@ServiceMethod(target=ServiceMethodContext.TARGET_STICK, loader="org.uengine.codi.mw3.model.Popup")
	public Object[] showList() throws Exception{
		Popup popup = new Popup();
		popup.setName("$Notification");
		Notification notiList = new Notification();
		popup.setPanel(	notiList.list(session));
		setNewItemCount(notiList.count(session));
		
		
		//한방에 다 클리어!
		Notification notificationType = new Notification();
		INotification notifictionsInSameInstance  = notificationType.sql("update bpm_noti set confirm=1 where userId=?userId");
		notifictionsInSameInstance.setConfirm(true);
		//notifictionsInSameInstance.setInstId(inst.getInstId());
		notifictionsInSameInstance.setUserId(session.getUser().getUserId());
		notifictionsInSameInstance.update();

		setNewItemCount(0);
		
		return new Object[]{popup, new Refresh(this)};
	}
	
	@AutowiredFromClient
	public Session session;
	
}
