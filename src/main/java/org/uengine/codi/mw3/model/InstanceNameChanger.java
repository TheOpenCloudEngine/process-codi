package org.uengine.codi.mw3.model;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.Refresh;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.Name;
import org.metaworks.annotation.Range;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dwr.MetaworksRemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.uengine.codi.mw3.Login;
import org.uengine.codi.mw3.calendar.ScheduleCalendar;
import org.uengine.codi.mw3.calendar.ScheduleCalendarEvent;
import org.uengine.processmanager.ProcessManagerRemote;


public class InstanceNameChanger implements ContextAware{
	
	public InstanceNameChanger() {
		this.setMetaworksContext(new MetaworksContext());
		this.getMetaworksContext().setWhen(metaworksContext.WHEN_EDIT);
	}
	
	public MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}

	@AutowiredFromClient
	public Session session;
	
	@Autowired
	public ProcessManagerRemote processManager;
	
	String instanceName;
		@Name
		@Range(size=30)
		//@Face(ejsPath="genericfaces/richText.ejs", options={"cols=")
		public String getInstanceName() {
			return instanceName;
		}
		public void setInstanceName(String instanceName) {
			this.instanceName = instanceName;
		}

	String instanceId;
		@Id
		public String getInstanceId() {
			return instanceId;
		}
		public void setInstanceId(String instanceId) {
			this.instanceId = instanceId;
		}

	//good example for Refresh
	@ServiceMethod(callByContent=true, target="popup")
	public Refresh change() throws Exception{
		Instance instance = new Instance();
		instance.setInstId(new Long(instanceId));
		
		IInstance instanceRef = instance.databaseMe();
		if( getInstanceName() != null && !getInstanceName().equals(instanceRef.getName()) ){
			instanceRef.setName(getInstanceName());
			
			ScheduleCalendarEvent scEvent = new ScheduleCalendarEvent();
			scEvent.setTitle(instanceRef.getName());
			scEvent.setId(instanceRef.getInstId().toString());
			scEvent.setStart(instanceRef.getDueDate());
			scEvent.setEnd(instanceRef.getDueDate());
			scEvent.setAllDay(true);
			scEvent.setCallType(ScheduleCalendar.CALLTYPE_INSTANCE);
			scEvent.setComplete(Instance.INSTNACE_STATUS_COMPLETED.equals(instanceRef.getStatus()));
			
			MetaworksRemoteService.pushTargetScript(Login.getSessionIdWithUserId(session.getUser().getUserId()),
					"if(mw3.getAutowiredObject('org.uengine.codi.mw3.calendar.ScheduleCalendar')!=null) mw3.getAutowiredObject('org.uengine.codi.mw3.calendar.ScheduleCalendar').__getFaceHelper().addEvent",
					new Object[]{scEvent});
			
			SystemWorkItem comment = new SystemWorkItem();
			comment.processManager = processManager;
			comment.session = session;
			comment.setInstId(instanceRef.getInstId());
			comment.getMetaworksContext().setWhen(MetaworksContext.WHEN_NEW);
			comment.setSystemMessage(session.getUser().getName() + "님이 제목을 [" + getInstanceName() + "] 으로 변경하였습니다.");
			
			
			comment.add();
			
			return new Refresh(instance.databaseMe());
		}else{
			return null;
		}
	}
	
}
