package org.uengine.codi.mw3.model;

import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.ServiceMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.uengine.codi.mw3.Login;
import org.uengine.processmanager.ProcessManagerRemote;

public class InstanceViewItem {

	Session session;
	
	@Autowired
	public InstanceViewContent instanceViewContent;
		@Autowired
		public InstanceViewContent getInstanceViewContent() {
			return instanceViewContent;
		}	
		public void setInstanceViewContent(InstanceViewContent instanceViewContent) {
			this.instanceViewContent = instanceViewContent;
		}
		
	@Autowired
	public ProcessManagerRemote processManager;	
	
	String instanceId;
		@Id
		@Hidden
		public String getInstanceId() {
			return instanceId;
		}
		public void setInstanceId(String instanceId) {
			this.instanceId = instanceId;
		}
	String userId;
		@Hidden
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
	String passwd;
		@Hidden
		public String getPasswd() {
			return passwd;
		}
		public void setPasswd(String passwd) {
			this.passwd = passwd;
		}
	boolean loaded;
		public boolean isLoaded() {
			return loaded;
		}
		public void setLoaded(boolean loaded) {
			this.loaded = loaded;
		}
	@ServiceMethod(callByContent=true)
	public void load() throws Exception {
		
		String userId = getUserId();
		String password = getPasswd();
		
		Login login = new Login();
		login.setEmail(userId);
		login.setPassword(password);
		Session session = login.loginService();
		Instance theInstanceDAO = new Instance();
		theInstanceDAO.setInstId(new Long(getInstanceId()));
		
		instanceViewContent.session = session;
		instanceViewContent.load(theInstanceDAO.databaseMe());
		instanceViewContent.getInstanceView().getMetaworksContext().setWhen("public");
		
		setLoaded(true);
	}
}
