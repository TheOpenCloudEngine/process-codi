package org.uengine.codi.mw3.model;

import java.util.ArrayList;

import org.metaworks.annotation.Face;
import org.uengine.kernel.EventHandler;
import org.uengine.kernel.ProcessInstance;
import org.uengine.util.UEngineUtil;

public class EventTriggerPanel {
	
	public EventTriggerPanel() {
	}

	public EventTriggerPanel(ProcessInstance instance) throws Exception{
		EventHandler[] eventHandlers = instance.getEventHandlersInAction();
		
		eventTriggers = new ArrayList<EventTrigger>();
		
		for(int i=0; i<eventHandlers.length; i++){
			EventHandler eventHandler = eventHandlers[i];
			
			EventTrigger eventTrigger = new EventTrigger();
			eventTrigger.setInstanceId(instance.getInstanceId());
			eventTrigger.setEventName(eventHandler.getName());
			
//System.out.println("display Name considered = "+ (UEngineUtil.isNotEmpty(eventHandler.getDisplayName().getText()) ? eventHandler.getDisplayName().getText() : eventHandler.getName()) );
			
			eventTrigger.setDisplayName(UEngineUtil.isNotEmpty(eventHandler.getDisplayName().getText()) ? eventHandler.getDisplayName().getText() : eventHandler.getName() );
			//eventTrigger.setDisplayName("sfsdfasdfas");
			eventTriggers.add(eventTrigger);
		}
	}
	
	ArrayList<EventTrigger> eventTriggers;
	@Face(options="alignment", values="horizontal")
		public ArrayList<EventTrigger> getEventTriggers() {
			return eventTriggers;
		}
	
		public void setEventTriggers(ArrayList<EventTrigger> eventTriggers) {
			this.eventTriggers = eventTriggers;
		}

}
