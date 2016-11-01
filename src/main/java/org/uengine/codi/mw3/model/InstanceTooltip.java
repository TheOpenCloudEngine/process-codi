package org.uengine.codi.mw3.model;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.Refresh;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.*;
import org.metaworks.dwr.MetaworksRemoteService;
import org.metaworks.widget.ModalWindow;
import org.springframework.beans.factory.annotation.Autowired;
import org.uengine.kernel.Activity;
import org.uengine.kernel.ComplexActivity;
import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.bpmn.Event;
import org.uengine.kernel.view.IInstanceMonitor;
import org.uengine.processmanager.ProcessManagerRemote;
import org.uengine.util.ActivityForLoop;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

	List<EventTrigger> eventTriggers;
		public List<EventTrigger> getEventTriggers() {
			return eventTriggers;
		}
		public void setEventTriggers(List<EventTrigger> eventTriggers) {
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

		try {
			loadEventTriggers(); //event triggers is not required to load
			setEventTriggersAreLoaded(true);
		}catch (Exception e){
		}
	}

	boolean eventTriggersAreLoaded;
		public boolean isEventTriggersAreLoaded() {
			return eventTriggersAreLoaded;
		}
		public void setEventTriggersAreLoaded(boolean eventTriggersAreLoaded) {
			this.eventTriggersAreLoaded = eventTriggersAreLoaded;
		}



	@ServiceMethod(callByContent=true)
	public void loadEventTriggers() throws Exception{
		if( Instance.INSTNACE_STATUS_RUNNING.equals(this.getStatus()) ){
			ProcessInstance processInstance = processManager.getProcessInstance(this.getInstanceId().toString());
			org.uengine.kernel.ProcessDefinition definition = processInstance.getProcessDefinition();

			if(processInstance.getCurrentRunningActivity()!=null){
				String currentTracingTag = processInstance.getCurrentRunningActivity().getActivity().getTracingTag();
				Vector mls = processInstance.getMessageListeners("event");

				if(mls != null){
					ArrayList<EventTrigger> eventTriggers = new ArrayList<EventTrigger>();

					for(int i = 0; i < mls.size(); i++){

						Event event = (Event) definition.getActivity(mls.get(i).toString());
						String attachedToRefTracingTag = event.getAttachedToRef();
						if(attachedToRefTracingTag!=null) {

							EventTrigger eventTrigger = new EventTrigger();
							eventTrigger.setInstanceId(this.getInstanceId().toString());
							eventTrigger.setDisplayName(event.getName());
							eventTrigger.setEventName(mls.get(i).toString());

							eventTriggers.add(eventTrigger);
						}
					}

					setEventTriggers(eventTriggers);
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
	
//	@Available(condition="(dueDate)")
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
	public void suspend() throws Exception{

		ProcessInstance instance = processManager.getProcessInstance(String.valueOf(getInstanceId()));
		for (Activity activity : instance.getCurrentRunningActivities()){
			activity.suspend(instance);
		}

		IInstanceMonitor instanceMonitor = MetaworksRemoteService.getComponent(IInstanceMonitor.class);
		instanceMonitor.setInstanceId(String.valueOf(getInstanceId()));
		instanceMonitor.load();

		processManager.applyChanges(); // we need to apply implicitly. since we didn't use processManager's set interface

		MetaworksRemoteService.wrapReturn(new Refresh(instanceMonitor));
	}

	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_POPUP)
	public void resume() throws Exception{
		final ProcessInstance instance = processManager.getProcessInstance(String.valueOf(getInstanceId()));
		ActivityForLoop forLoop = new ActivityForLoop(){
			public void logic(Activity act){
				try{
					String status1 = act.getStatus(instance);
					if(!(act instanceof ComplexActivity) &&
							(status1.equals(Activity.STATUS_STOPPED)|| status1.equals(Activity.STATUS_SUSPENDED))){
						act.resume(instance);
					}
				}catch(Exception e){
					throw new RuntimeException(e);
				}
			}
		};

		forLoop.run(instance.getProcessDefinition());

		processManager.setChanged();


		IInstanceMonitor instanceMonitor = MetaworksRemoteService.getComponent(IInstanceMonitor.class);
		instanceMonitor.setInstanceId(String.valueOf(getInstanceId()));
		instanceMonitor.load();

		MetaworksRemoteService.wrapReturn(new Refresh(instanceMonitor));
	}

	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_POPUP)
	public void stop() throws Exception{
		final ProcessInstance instance = processManager.getProcessInstance(String.valueOf(getInstanceId()));

		instance.stop();

//
//		ActivityForLoop forLoop = new ActivityForLoop(){
//			public void logic(Activity act){
//				try{
//					String status1 = act.getStatus(instance);
//					if(!(act instanceof ComplexActivity) &&
//							(status1.equals(Activity.STATUS_STOPPED)|| status1.equals(Activity.STATUS_SUSPENDED))){
//						act.stop(instance);
//					}
//				}catch(Exception e){
//					throw new RuntimeException(e);
//				}
//			}
//		};
//
//		forLoop.run(instance.getProcessDefinition());

		processManager.setChanged();


		IInstanceMonitor instanceMonitor = MetaworksRemoteService.getComponent(IInstanceMonitor.class);
		instanceMonitor.setInstanceId(String.valueOf(getInstanceId()));
		instanceMonitor.load();

		MetaworksRemoteService.wrapReturn(new Refresh(instanceMonitor));
	}



	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_POPUP)
    public ModalWindow monitor() throws Exception{
        return null;
    }

	@ServiceMethod(callByContent=true)
	public InstanceViewDetail workItems() throws Exception{

		InstanceView instanceView = new InstanceView();
		instanceView.setInstanceId(String.valueOf(getInstanceId()));
		MetaworksRemoteService.autowire(instanceView);

		return instanceView.createInstanceViewDetail();
	}
}
