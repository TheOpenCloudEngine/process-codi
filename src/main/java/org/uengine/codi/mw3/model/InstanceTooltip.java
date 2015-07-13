package org.uengine.codi.mw3.model;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.*;
import org.metaworks.widget.ModalWindow;
import org.springframework.beans.factory.annotation.Autowired;
import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.bpmn.Event;
import org.uengine.kernel.bpmn.TimerEvent;
import org.uengine.processmanager.ProcessManagerRemote;

import java.util.Date;
import java.util.Vector;

public class InstanceTooltip implements ContextAware {

	@AutowiredFromClient
	public Session session;
	
	@Autowired
	public ProcessManagerRemote processManager;	

	@AutowiredFromClient
	public Locale localeManager;

	Long instanceId;
		@Id
		public Long getInstanceId() {
			return instanceId;
		}
		public void setInstanceId(Long instanceId) {
			this.instanceId = instanceId;
		}
	
	String secuopt;
		public String getSecuopt() {
			return secuopt;
		}
		public void setSecuopt(String secuopt) {
			this.secuopt = secuopt;
		}
		
	String status;
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		
	MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}
		
	Date dueDate;
		public Date getDueDate() {
			return dueDate;
		}
		public void setDueDate(Date dueDate) {
			this.dueDate = dueDate;
		}
		
	String instanceDefId;
		public String getInstanceDefId() {
			return instanceDefId;
		}
		public void setInstanceDefId(String instanceDefId) {
			this.instanceDefId = instanceDefId;
		}

	EventTrigger[] eventTriggers;
		public EventTrigger[] getEventTriggers() {
			return eventTriggers;
		}
		public void setEventTriggers(EventTrigger[] eventTriggers) {
			this.eventTriggers = eventTriggers;
		}
		
	boolean loaded;
		public boolean isLoaded() {
			return loaded;
		}
		public void setLoaded(boolean loaded) {
			this.loaded = loaded;
		}
		
	public InstanceTooltip() throws Exception{
		setMetaworksContext(new MetaworksContext());
	}
	
	public void load(IInstance instance) throws Exception{
		this.setInstanceId(instance.getInstId());
		this.setStatus(instance.getStatus());
		this.setSecuopt(instance.getSecuopt());
		this.setDueDate(instance.getDueDate());
		this.setInstanceDefId(instance.getDefVerId());
	}
	
	@ServiceMethod(callByContent=true)
	public void eventTriggerCheck() throws Exception{
		if( Instance.INSTNACE_STATUS_RUNNING.equals(this.getStatus()) ){
			ProcessInstance processInstance = processManager.getProcessInstance(this.getInstanceId().toString());
			org.uengine.kernel.ProcessDefinition definition = processInstance.getProcessDefinition();

			if(processInstance.getCurrentRunningActivity()!=null){
				String currentTracingTag = processInstance.getCurrentRunningActivity().getActivity().getTracingTag();
				Vector mls = processInstance.getMessageListeners("event");

				EventTrigger[] eventTriggers = new EventTrigger[mls.size()];
				if(mls != null){
					for(int i = 0; i < mls.size(); i++){
						// mls.get(i) == tracing tag라 가정한다면 currentTracingTag와 비교하여
						// currentTracingTag와 event의 TracingTag 의 attachedToRef를 비교하여
						// 즉, 이벤트가 붙어있는 휴먼의 TracingTag가 attachedToRef이므로...
						// 그때서야 event를 등록한다.

						String attachedToRefTracingTag = ((Event) definition.getActivity(mls.get(i).toString())).getAttachedToRef();
						if( (currentTracingTag.equals(attachedToRefTracingTag)) &&
								(!("Timer".equals(definition.getActivity(mls.get(i).toString()).getName()))) ) {

							EventTrigger eventTrigger = new EventTrigger();
							eventTrigger.setInstanceId(this.getInstanceId().toString());
							eventTrigger.setDisplayName(definition.getActivity(mls.get(i).toString()).getName());
							eventTrigger.setEventName(definition.getActivity(mls.get(i).toString()).getName());
							eventTriggers[i] = eventTrigger;

							this.setEventTriggers(eventTriggers);

							// 단, TimerEvent는 trigger 대신 quartz를 호출.
						} else if( (currentTracingTag.equals(attachedToRefTracingTag)) &&
								("Timer".equals(definition.getActivity(mls.get(i).toString()).getName())) ) {

							// find mls.get(i).
							// mls.get(i) is Event's tracingTag
							// and run to TimeEvent onMessage
							TimerEvent timerEvent = (TimerEvent) definition.getActivity(mls.get(i).toString());
							timerEvent.onMessage(processInstance, definition.getActivity(mls.get(i).toString()).getName());

							// etc..
						} else {

						}
					}
				}
			}

		}
		this.setLoaded(true);
	}

	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_POPUP, loader="auto")
	public Popup schedule() throws Exception{
		
		Instance instance = new Instance();
		instance.processManager = processManager;
		instance.session = session;
		instance.localeManager = localeManager;
		instance.setInstId(this.getInstanceId());
		
		return instance.schedule();
	}
	
	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_POPUP, loader="auto")
	public Popup newSubInstance() throws Exception{
		Instance instance = new Instance();
		instance.processManager = processManager;
		instance.session = session;
		instance.localeManager = localeManager;
		instance.setInstId(this.getInstanceId());
		
		return instance.newSubInstance();
	}

	@Face(displayName="$Remove")
	@ServiceMethod(callByContent=true, needToConfirm=true, target=ServiceMethodContext.TARGET_APPEND)
	public Object[] remove() throws Exception{
		Instance instance = new Instance();
		instance.processManager = processManager;
		instance.session = session;
		instance.localeManager = localeManager;
		instance.setInstId(this.getInstanceId());
		
		return instance.remove();
	}
	
	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_SELF)
	public void toggleSecurityConversation() throws Exception{
		Instance instance = new Instance();
		instance.processManager = processManager;
		instance.session = session;
		instance.localeManager = localeManager;
		instance.setInstId(this.getInstanceId());		
		instance.setSecuopt(this.getSecuopt());
		instance.toggleSecurityConversation();
		
		this.setSecuopt(instance.getSecuopt());
	}
	
	@Available(condition="(dueDate)")
	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_SELF)
	public void complete() throws Exception{
		Instance instance = new Instance();
		instance.processManager = processManager;
		instance.session = session;
		instance.localeManager = localeManager;		
		instance.setInstId(this.getInstanceId());
		instance.setStatus(this.getStatus());
		instance.complete();
		
		this.setStatus(instance.getStatus());
	}

    @ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_POPUP)
    public ModalWindow monitor() throws Exception{
        return null;
    }

	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_POPUP)
	public ModalWindow processMonitor() throws Exception{

		ModalWindow modalWindow = new ModalWindow();

		InstanceMonitorPanel instanceMonitorPanel = new InstanceMonitorPanel();
		instanceMonitorPanel.load(this.getInstanceId(), processManager);

		modalWindow.setPanel(instanceMonitorPanel);
		modalWindow.setWidth(0);
		modalWindow.setHeight(0);

		return modalWindow;
	}
}
