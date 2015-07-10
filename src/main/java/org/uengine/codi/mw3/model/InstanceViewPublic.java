package org.uengine.codi.mw3.model;

import org.metaworks.MetaworksContext;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.ServiceMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.uengine.codi.mw3.Login;
import org.uengine.kernel.ProcessInstance;
import org.uengine.processmanager.ProcessManagerRemote;

@Component
@Face(options={"hideNewBtn", "hideTitle", "hideViewBox", "hideLabels", "hideEditBtn"}, values={"true", "true", "true", "true", "true"})
public class InstanceViewPublic {

	public InstanceViewPublic(){				
	}
	
	Session session;
		public Session getSession() {
			return session;
		}
		public void setSession(Session session) {
			this.session = session;
		}

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

	boolean open;
		public boolean isOpen() {
			return open;
		}
		public void setOpen(boolean open) {
			this.open = open;
		}
		
	transient MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		} 
		
	@ServiceMethod(when="hidden", callByContent=true)
	public void load() throws Exception {
		if(getSession() == null)
			throw new Exception("session error");

		Login login = new Login();
		login.setEmail(this.getSession().getUser().getUserId());		
		login.storeIntoServerSession(session);
		
		ProcessInstance instance = processManager.getProcessInstance(String.valueOf(getInstanceId()));
		
		boolean isOpen = false;
		if(instance.getProperty("", "is_open") != null){
			isOpen = ((String)instance.getProperty("", "is_open")).equals("open") ? true : false;
		}
		
		setOpen(isOpen);
		
		if(isOpen){
			Instance theInstanceDAO = new Instance();
			theInstanceDAO.setInstId(new Long(getInstanceId()));
			
			instanceViewContent.session = this.getSession();
			instanceViewContent.load(theInstanceDAO.databaseMe());
			instanceViewContent.getInstanceView().getMetaworksContext().setWhen("public");
		}
		
		setMetaworksContext(new MetaworksContext());
		getMetaworksContext().setWhen(MetaworksContext.WHEN_VIEW);
	}
}
