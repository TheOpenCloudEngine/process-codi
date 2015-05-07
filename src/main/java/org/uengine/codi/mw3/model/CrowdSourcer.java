package org.uengine.codi.mw3.model;

import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.ServiceMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.uengine.kernel.ProcessInstance;
import org.uengine.processmanager.ProcessManagerRemote;

public class CrowdSourcer {
	
	@Autowired
	public ProcessManagerRemote processManager;
		
	String instanceId;
		@Id
		public String getInstanceId() {
			return instanceId;
		}
		public void setInstanceId(String instanceId) {
			this.instanceId = instanceId;
		}
		
	String message;
			
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}

	String[] postIds;
		public String[] getPostIds() {
			return postIds;
		}
		public void setPostIds(String[] postIds) {
			this.postIds = postIds;
		}
		
	boolean open;
		public boolean isOpen() {
			return open;
		}
		public void setOpen(boolean open) {
			this.open = open;
		}
		
	Followers followers;
		public Followers getFollowers() {
			return followers;
		}
		public void setFollowers(Followers followers) {
			this.followers = followers;
		}
	
	@ServiceMethod(callByContent=true)
	public void crowdSourcing() throws Exception {
		try{
			ProcessInstance instance = processManager.getProcessInstance(getInstanceId());
		
			if(isOpen()){
				instance.setProperty("", "is_open", "close");
				setOpen(false);
			}else{
				instance.setProperty("", "facebook_postIds", getPostIds());
				instance.setProperty("", "is_open", "open");
				setOpen(true);
			}			
			
			processManager.applyChanges();
			
		}catch(Exception e){
			throw e;
		}finally{		
			processManager.remove();
		}
	}
	
	@ServiceMethod(target=ServiceMethodContext.TARGET_POPUP)
	public Popup worker() throws Exception {
		CrowdSourcerWorker crowdSourcerWorker = new CrowdSourcerWorker();
		
		Popup popup = new Popup();
		popup.setPanel(crowdSourcerWorker);

		return popup;
	}		
}
