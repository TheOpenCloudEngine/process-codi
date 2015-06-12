package org.uengine.codi.mw3.model;

import org.metaworks.annotation.ServiceMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.uengine.kernel.EventMessagePayload;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.ProcessInstance;
import org.uengine.processmanager.ProcessManagerRemote;

public class EventTrigger {
	
	String eventName;
		
		public String getEventName() {
			return eventName;
		}
	
		public void setEventName(String eventName) {
			this.eventName = eventName;
		}
	
	String instanceId;		
		public String getInstanceId() {
			return instanceId;
		}
	
		public void setInstanceId(String instanceId) {
			this.instanceId = instanceId;
		}
		
	String displayName;

		public String getDisplayName() {
			return displayName;
		}
		public void setDisplayName(String text) {
			this.displayName = text;
		}
		
	
	@ServiceMethod(callByContent = true)
	public InstanceViewContent fireEvent() throws Exception{
		ProcessInstance mainProcessInstance = processManager.getProcessInstance(getInstanceId());
		
		//send message first
		ProcessDefinition mainProcessDefinition = mainProcessInstance.getProcessDefinition();
//		EventMessagePayload eventMessagePayload = new EventMessagePayload();
//		eventMessagePayload.setEventName(eventName);
		//eventMessagePayload.setTriggerTracingTag(triggerActivityTracingTag);
		mainProcessDefinition.fireMessage("event", mainProcessInstance, getEventName());  ///eventMessagePayload);

		processManager.applyChanges(); //apply explicitly so that the worklist can be updated.
		
		//refreshes the instanceview so that the next workitem can be show up
		Instance instance = new Instance();
		instance.setInstId(new Long(getInstanceId()));
		instance.copyFrom(instance.databaseMe());
				
		instanceViewContent.load(instance);
		
		return instanceViewContent;
	}
	
	@Autowired
	public InstanceViewContent instanceViewContent;
	
	@Autowired
	public ProcessManagerRemote processManager;

}