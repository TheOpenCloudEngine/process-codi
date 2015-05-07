package org.uengine.codi.mw3.model;

import java.util.ArrayList;

import org.directwebremoting.Browser;
import org.directwebremoting.ScriptSessions;
import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.Refresh;
import org.metaworks.Remover;
import org.metaworks.ServiceMethodContext;
import org.metaworks.ToPrepend;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.annotation.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.uengine.codi.mw3.Login;
import org.uengine.processmanager.ProcessManagerRemote;

public class UnstructuredProcessInstanceStarter implements ContextAware {

	public UnstructuredProcessInstanceStarter() {
		setMetaworksContext(new MetaworksContext());
		getMetaworksContext().setWhen(MetaworksContext.WHEN_NEW);
	}

	String title;
		//@Face(ejsPath="genericfaces/richText.ejs", options={"rows"}, values={"5"})
		public String getTitle() {
			return title;
		}
	
		public void setTitle(String title) {
			this.title = title;
		}		

	IUser friend;
		public IUser getFriend() {
			return friend;
		}
		public void setFriend(IUser friend) {
			this.friend = friend;
		}

	MetaworksContext metaworksContext;	
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}

	@ServiceMethod(callByContent = true, keyBinding="enter", target=ServiceMethodContext.TARGET_APPEND)
	@Test(scenario="first", starter=true, instruction="지금 생각하고 계신 것을 간략히 입력하고 클릭합니다.", next="autowiredObject.org.uengine.codi.mw3.model.InstanceView.newActivity()")
	public Object[] start() throws Exception{
		
		//good example: fully reused code are below:
		
		ResourceFile unstructuredProcessDefinition = new ResourceFile();
		unstructuredProcessDefinition.processManager = processManager;
		unstructuredProcessDefinition.session = session;
		unstructuredProcessDefinition.instanceViewContent = instanceViewContent;
//		unstructuredProcessDefinition.setAlias("Unstructured.process");
		
		ArrayList<IUser> friends = new ArrayList<IUser>();
		friends.add(friend);
		unstructuredProcessDefinition.friends = friends;
		
		Object[] instanceViewAndInstanceList = unstructuredProcessDefinition.initiate();
		
		InstanceViewContent instanceView = (InstanceViewContent) instanceViewAndInstanceList[0];
		
		final String instId = instanceView.getInstanceView().instanceId;
		Instance instance = new Instance();
		instance.setInstId(new Long(instId));
		User initUser = new User();
		initUser.setUserId(session.getUser().getUserId());
		initUser.setName(session.getUser().getName());
		instance.databaseMe().setInitiator(initUser);
		instance.databaseMe().setDueDate(null);
		instance.databaseMe().setName(getTitle());
		instance.databaseMe().setStatus(WorkItem.WORKITEM_STATUS_RUNNING);
		instance.fillFollower();
		
		if(session.getEmployee() != null)
			instance.databaseMe().setInitComCd(session.getEmployee().getGlobalCom());
		
			
		if("chat".equals(getMetaworksContext().getWhen()))
			instance.databaseMe().setSecuopt("1"); //채팅이면 보안 대화로 
		else
			instance.databaseMe().setSecuopt("0"); // 아니면 전체 공개  
			
		
		CommentWorkItem comment = (CommentWorkItem) instanceView.getInstanceView().getNewItem();
		comment.processManager = processManager;
		comment.session = session;
		comment.setTitle(getTitle());
		comment.getMetaworksContext().setWhen(MetaworksContext.WHEN_NEW);
//		Object[] returnObject = comment.add();
		
		instance.copyFrom(comment.save());
		Object[] returnObject = comment.makeReturn(null, instance);
		
		instanceView.load(instance);
	
		if(getFriend() != null && getFriend().getUserId() != null){
			Browser.withSession(Login.getSessionIdWithUserId(session.getEmployee().getEmpCode()), new Runnable(){
				@Override
				public void run() {
					ScriptSessions.addFunctionCall("mw3.getAutowiredObject('org.uengine.codi.mw3.model.Tray').__getFaceHelper().addTray", new Object[]{getFriend().getName(), instId});
				}
				
			});
		
			Browser.withSession(Login.getSessionIdWithUserId(getFriend().getUserId()), new Runnable(){
				@Override
				public void run() {
					ScriptSessions.addFunctionCall("mw3.getAutowiredObject('org.uengine.codi.mw3.model.Tray').__getFaceHelper().addTray", new Object[]{session.getUser().getName(), instId, true});
				}
				
			});
		}
		
		
		if("sns".equals(session.getEmployee().getPreferUX())){
			Object[] returnObject2 = new Object[ returnObject.length + 1 ];
			for( int i = 0; i < returnObject.length; i++){
				returnObject2[i] = returnObject[i];
			}
			returnObject2[returnObject.length] = new Remover(new Popup());
			return returnObject2;
		}else{
			Object[] returnObject2 = new Object[ returnObject.length + 2 ];
			for( int i = 0; i < returnObject.length; i++){
				returnObject2[i] = returnObject[i];
			}
			returnObject2[returnObject.length] = new Refresh(instanceView);
			returnObject2[returnObject.length + 1] = new Remover(new Popup());
			return returnObject2;
		}
		
	}
	
	@AutowiredFromClient
	public NewInstancePanel newInstancePanel;
	
	@AutowiredFromClient
	public Session session;
	
	@Autowired
	public ProcessManagerRemote processManager;
	
	@Autowired
	public InstanceViewContent instanceViewContent;

	@AutowiredFromClient
	public Tray tray;

}
