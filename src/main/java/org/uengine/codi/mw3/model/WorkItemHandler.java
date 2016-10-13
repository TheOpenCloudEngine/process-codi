package org.uengine.codi.mw3.model;

import org.metaworks.*;
import org.metaworks.annotation.*;
import org.metaworks.dao.TransactionContext;
import org.metaworks.dwr.MetaworksRemoteService;
import org.metaworks.model.MetaworksElement;
import org.metaworks.widget.ModalWindow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.uengine.codi.ITool;
import org.uengine.codi.mw3.Login;
import org.uengine.codi.mw3.filter.AllSessionFilter;
import org.uengine.codi.mw3.filter.OtherSessionFilter;
import org.uengine.kernel.*;
import org.uengine.kernel.RoleMapping;
import org.uengine.processmanager.ProcessManagerRemote;

import javax.validation.Valid;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.*;

@Component
@Scope("prototype")
//@Face(ejsPath="faces/org/metaworks/widget/Window.ejs", options={"hideLabels"}, values={"true"}, displayName="업무 처리 화면")
public class WorkItemHandler implements ContextAware {

	public WorkItemHandler(){} //to be spring bean without argument, this is required.

	public void load() throws Exception{

		Long instanceId = new Long(getInstanceId());
		Long taskId = getTaskId();
		String tracingTag = getTracingTag();

		ProcessInstance instance = processManager.getProcessInstance(instanceId.toString());

		if(getExecutionScope()!=null)
			instance.setExecutionScope(getExecutionScope());

		HumanActivity humanActivity = null;

		if(humanActivity==null && instanceId!=null && tracingTag!=null){
			humanActivity = (HumanActivity) instance.getProcessDefinition().getActivity(tracingTag);
		}

		inputParameters = new ArrayList<ParameterValue>();
		outputParameters = new ArrayList<ParameterValue>();

		if(humanActivity != null && humanActivity.getParameters()!=null){
			// load map for ITool
			loadMapForITool((Map<String, Object>)makeMapForITool(humanActivity));

			//creates work item handler
			//parameters = new ParameterValue[humanActivity.getParameters().length];
			for(int i=0; i<humanActivity.getParameters().length; i++){
				ParameterContext pc = humanActivity.getParameters()[i];

				ParameterValue pv = new ParameterValue();

				//TODO: why this occurs?
				if(pc.getVariable()==null)
					continue;

				pv.setVariableName(pc.getVariable().getName());
				if(pv.getArgument()==null){
					pv.setArgument(pv.getVariableName());
				}else
					pv.setArgument(pc.getArgument().getText(session!=null && session.getEmployee()!=null ? session.getEmployee().getLocale() : null));

				pv.setDirection(pc.getDirection());

				String when = this.getMetaworksContext().getWhen();

				MetaworksContext mc = new MetaworksContext();

				when = MetaworksContext.WHEN_EDIT;

//				if(MetaworksContext.WHEN_EDIT.equals(when)){
				if(ParameterContext.DIRECTION_IN.equals(pc.getDirection())) {
					when = MetaworksContext.WHEN_VIEW;
					inputParameters.add(pv);
				}else{
					outputParameters.add(pv);
				}

//				}

				mc.setWhen(when);
				pv.setMetaworksContext(mc);


				ProcessVariableValue processVariableValue = pc.getVariable().getMultiple(instance, "");

				if(processVariableValue==null) {
					processVariableValue = new ProcessVariableValue();

					if(pc.getVariable().getDefaultValue()!=null){
						processVariableValue.setValue(pc.getVariable().getDefaultValue());
					}else {
						processVariableValue.setValue(pc.getVariable().createNewValue());
					}
				}

				processVariableValue.beforeFirst();
				do{

					Serializable value = processVariableValue.getValue();

					if(value instanceof ContextAware){
						ContextAware contextAware = (ContextAware) value;

						if(contextAware.getMetaworksContext()==null)
							contextAware.setMetaworksContext(new MetaworksContext());

						contextAware.getMetaworksContext().setWhen(when);
					}

					if(value instanceof org.uengine.kernel.ITool){
						((org.uengine.kernel.ITool) value).onLoad();
					}

				}while(processVariableValue.next());


				pv.setMultipleInput(pc.isMultipleInput());

				processVariableValue.beforeFirst();

				if(pc.isMultipleInput()) {
					ProcessVariableValueList pvvl = new ProcessVariableValueList();
					pvvl.setDefaultValue(processVariableValue.getValue());

					if(processVariableValue!=null && processVariableValue.size() > 0) {
						pvvl.setElements(new ArrayList<MetaworksElement>());

						do {
							MetaworksElement element = new MetaworksElement();
							element.setValue(processVariableValue.getValue());
							pvvl.getElements().add(element);
						} while (processVariableValue.next());
					}

					//pv.setValueObject(processVariableValue);
					pv.setProcessVariableValueList(pvvl);
				}else{
					pv.setValue(processVariableValue.getValue());
				}
			}

			releaseMapForITool();
		}

		setInstanceId(instanceId.toString());
		setTracingTag(humanActivity.getTracingTag());
		setTaskId(taskId);

		// 댓글이 있는지 찾는다.
		if( taskId != -1 && taskId != 0){
			WorkItem item = new WorkItem();

			workItem = item.findParentWorkItem(taskId.toString());
		}
	}

	transient IWorkItem workItem;
	@Hidden
		public IWorkItem getWorkItem() {
			return workItem;
		}
		public void setWorkItem(IWorkItem workItem) {
			this.workItem = workItem;
		}


	ArrayList<ParameterValue> inputParameters;
	@Available(where = "detail")
		public ArrayList<ParameterValue> getInputParameters() {
			return inputParameters;
		}
		public void setInputParameters(ArrayList<ParameterValue> inputParameters) {
			this.inputParameters = inputParameters;
		}


	ArrayList<ParameterValue> outputParameters;
	@Available(where = "detail")
		public ArrayList<ParameterValue> getOutputParameters() {
			return outputParameters;
		}
		public void setOutputParameters(ArrayList<ParameterValue> outputParameters) {
			this.outputParameters = outputParameters;
		}





	String instanceId;
	@Hidden
		public String getInstanceId() {
			return instanceId;
		}
		public void setInstanceId(String instanceId) {
			this.instanceId = instanceId;
		}


	String tracingTag;
	@Hidden
		public String getTracingTag() {
			return tracingTag;
		}
		public void setTracingTag(String tracingTag) {
			this.tracingTag = tracingTag;
		}


	Long taskId;
		@Id
		@Hidden
		public Long getTaskId() {
			return taskId;
		}
		public void setTaskId(Long taskId) {
			this.taskId = taskId;
		}

	String executionScope;
		@Hidden
		public String getExecutionScope() {
			return executionScope;
		}
		public void setExecutionScope(String executionScope) {
			this.executionScope = executionScope;
		}


	Long rootInstId;
		@Hidden
		public Long getRootInstId() {
			return rootInstId;
		}
		public void setRootInstId(Long rootInstId) {
			this.rootInstId = rootInstId;
		}

	String replyFieldName;
		@Hidden
		public String getReplyFieldName() {
			return replyFieldName;
		}
		public void setReplyFieldName(String replyFieldName) {
			this.replyFieldName = replyFieldName;
		}
	String replyTitle;
		@Hidden
		public String getReplyTitle() {
			return replyTitle;
		}
		public void setReplyTitle(String replyTitle) {
			this.replyTitle = replyTitle;
		}


	@ServiceMethod(callByContent=true, when= MetaworksContext.WHEN_EDIT)
	public Object[] cancel() throws Exception{
		ProcessInstance instance = processManager.getProcessInstance(instanceId);

		HumanActivity humanActivity;
		humanActivity = (HumanActivity) instance.getProcessDefinition().getActivity(tracingTag);

//		WorkList worklist = instance.getWorkList();
//		worklist.cancelWorkItem(getTaskId().toString(), new KeyedParameter[]{}, instance.getProcessTransactionContext());
//
//		humanActivity.setStatus(instance, Activity.ACTIVITY_STOPPED)

		humanActivity.stop(instance);

		processManager.applyChanges();

		CommentWorkItem cancelledHistory = new CommentWorkItem();
		cancelledHistory.processManager = processManager;
		cancelledHistory.session = session;
		cancelledHistory.setInstId(new Long(getInstanceId()));
		cancelledHistory.setTitle(humanActivity.getName() + " task has been cancelled by me.");


		cancelledHistory.setWriter(session.getUser());
		cancelledHistory.add();

		Instance instanceDAO = new Instance();
		instanceDAO.setInstId(this.getRootInstId());
		instanceDAO.copyFrom(instanceDAO.databaseMe());

		instanceViewContent.session = session;
		instanceViewContent.load(instanceDAO);

		this.sendPush(instanceDAO,null,cancelledHistory);

		if("oce".equals(session.getUx())){
			InstanceViewThreadPanel panel = new InstanceViewThreadPanel();
			panel.getMetaworksContext().setHow("instanceList");
			panel.getMetaworksContext().setWhere("sns");
			panel.session = session;
			panel.load(this.getRootInstId().toString());

			return new Object[]{panel, new Remover(new ModalWindow() , true )};
		}else {
			return new Object[]{instanceViewContent, new Remover(new ModalWindow(), true)};
//			if("sns".equals(session.getEmployee().getPreferUX())){
//				return new Remover(new ModalWindow());
//			}else{
//				return this;
//			}
		}
	}

	@ServiceMethod(callByContent=true, when= MetaworksContext.WHEN_EDIT)
	public Object[] skip() throws Exception{
		ProcessInstance instance = processManager.getProcessInstance(instanceId);

		HumanActivity humanActivity;

		humanActivity = (HumanActivity) instance.getProcessDefinition()
					.getActivity(tracingTag);

//		WorkList worklist = instance.getWorkList();
//		worklist.cancelWorkItem(getTaskId().toString(), new KeyedParameter[]{}, instance.getProcessTransactionContext());
//
//		humanActivity.setStatus(instance, Activity.ACTIVITY_STOPPED)

		humanActivity.skip(instance);

		List activities = humanActivity.getPropagatedActivities(instance);

		//resume the process
//		Vector activityInstances =  instance.getCurrentRunningActivitiesDeeply();
		for(int i=0; i<activities.size(); i++){
			Activity nextAct = (Activity) activities.get(i);
			nextAct.resume(instance);
		}

		processManager.applyChanges();

		CommentWorkItem cancelledHistory = new CommentWorkItem();
		cancelledHistory.processManager = processManager;
		cancelledHistory.session = session;
		cancelledHistory.setInstId(new Long(getInstanceId()));

		cancelledHistory.setTitle(humanActivity.getName() + " has been skipped by me.");
		cancelledHistory.setWriter(session.getUser());
		cancelledHistory.add();

		Instance instanceDAO = new Instance();
		instanceDAO.setInstId(this.getRootInstId());
		instanceDAO.copyFrom(instanceDAO.databaseMe());

		instanceViewContent.session = session;
		instanceViewContent.load(instanceDAO);

		this.sendPush(instanceDAO,null,cancelledHistory);

		if("oce".equals(session.getUx())){
			InstanceViewThreadPanel panel = new InstanceViewThreadPanel();
			panel.getMetaworksContext().setHow("instanceList");
			panel.getMetaworksContext().setWhere("sns");
			panel.session = session;
			panel.load(this.getRootInstId().toString());

			return new Object[]{panel, new Remover(new ModalWindow() , true )};
		}else{
			return new Object[]{instanceViewContent, new Remover(new ModalWindow(), true)};
		}
	}

	@AutowiredFromClient
	public Session session;

	@ServiceMethod(callByContent=true, when= MetaworksContext.WHEN_EDIT, validate=true, target= ServiceMethodContext.TARGET_APPEND)
//	@Available(when={"NEW"})
	public Object[] complete() throws RemoteException, ClassNotFoundException, Exception{

		ProcessInstance instance = processManager.getProcessInstance(instanceId);

		if(getExecutionScope()!=null)
			instance.setExecutionScope(getExecutionScope());


		HumanActivity humanActivity = null;
		if (instanceId != null && tracingTag != null) {
			humanActivity = (HumanActivity) instance.getProcessDefinition()
					.getActivity(tracingTag);
		}

		if (!humanActivity.getActualMapping(instance).getEndpoint()
				.equals(session.getUser().getUserId())) {
			throw new Exception("$NotPermittedToComplete");

		}
		// load map for ITool
		loadMapForITool((Map<String, Object>)makeMapForITool(humanActivity));

		ResultPayload rp = createResultPayload();

		processManager.completeWorkitem(getInstanceId() + (getExecutionScope() != null ? "@" + getExecutionScope():""), getTracingTag(), getTaskId().toString(), rp);
		processManager.applyChanges();

		releaseMapForITool();


		// TODO pushTargetClientObjects 를 하고 나면 copyOfInstance 가 변경이 되는 상황이 발생하여 새로운 객체를 생성하여줌
		Instance inst = new Instance();
		inst.setInstId(this.getRootInstId());
		inst.copyFrom(inst.databaseMe());

		this.saveLastComment(inst, humanActivity);
		inst.flushDatabaseMe();

		//refreshes the instanceview so that the next workitem can be show up
		InstanceViewThreadPanel panel = new InstanceViewThreadPanel();
		MetaworksRemoteService.autowire(panel);
		panel.load(this.getRootInstId().toString());

		return new Object[]{new Remover(new ModalWindow(), true ), new Refresh(panel, true)};

	}

	public void sendPush(Instance inst, ArrayList<ProcessWorkItem> newlyAddedWorkItems, IWorkItem workItemMe) throws Exception{
		/**
		 *  === noti push 부분 ===
		 *  위쪽에서 topic notiuser를 구하였지만 noti를 보내는 사람을 구하는 로직은 다를수 있으니 다시한번 구한다.
		 */
		HashMap<String, String> notiUsers = new HashMap<String, String>();
		Notification notification = new Notification();
		notification.session = session;
		notiUsers = notification.findInstanceNotiUser(inst.getInstId().toString());
		if(inst.getTopicId() != null){
			HashMap<String, String> topicNotiUsers = notification.findTopicNotiUser(inst.getTopicId());
			Iterator<String> iterator = topicNotiUsers.keySet().iterator();
			while(iterator.hasNext()){
				String followerUserId = (String)iterator.next();
				notiUsers.put(followerUserId, topicNotiUsers.get(followerUserId));
			}
		}

		// noti 저장
		Iterator<String> iterator = notiUsers.keySet().iterator();
		while(iterator.hasNext()){
			String followerUserId = (String)iterator.next();
			Notification noti = new Notification();
			INotiSetting notiSetting = new NotiSetting();
			INotiSetting findResult = notiSetting.findByUserId(followerUserId);
			if(!findResult.next() || findResult.isWriteInstance()){
				noti.setNotiId(System.currentTimeMillis()); //TODO: why generated is hard to use
				noti.setUserId(followerUserId);
				noti.setActorId(session.getUser().getUserId());
				noti.setConfirm(false);
				noti.setInputDate(Calendar.getInstance().getTime());
				noti.setTaskId(getTaskId());
				noti.setInstId(new Long(getInstanceId()));
				noti.setActAbstract(session.getUser().getName() + " completed workItem : " + inst.getName());
				noti.add(inst);
			}
		}

		MetaworksRemoteService.pushTargetScriptFiltered(new AllSessionFilter(notiUsers),
				"mw3.getAutowiredObject('" + NotificationBadge.class.getName() + "').refresh",
				new Object[]{});

		/**
		 *  === todo count push 부분 ===
		 */
		TodoBadge todoBadge = new TodoBadge();
		todoBadge.loader = true;

		MetaworksRemoteService.pushTargetClientObjects(Login.getSessionIdWithUserId(session.getUser().getUserId()), new Object[]{new Refresh(todoBadge, true)});
		// follower 될 사용자의 todo count 를 refresh
		MetaworksRemoteService.pushClientObjectsFiltered(
				new OtherSessionFilter(notiUsers, session.getUser().getUserId()),
				new Object[]{new Refresh(todoBadge, true)});

		/**
		 *  === instance push 부분 ===
		 */
		// 2014.11.25 ik
		if(Login.getSessionIdWithCompany(session.getEmployee().getGlobalCom()) != null){
			notiUsers.putAll(Login.getSessionIdWithCompany(session.getEmployee().getGlobalCom()));
		}

		inst.getMetaworksContext().setWhere(Instance.WHERE_INSTANCELIST);

		// 본인의 instanceList 에 push
		MetaworksRemoteService.pushTargetClientObjects(Login.getSessionIdWithUserId(session.getUser().getUserId()), new Object[]{new InstanceListener(inst)});

		// 본인 이외에 다른 사용자에게 push
		// 새로 추가되는 workItem이 있는 경우 - 1. 새로추가된 workItem은 append를 하고 2.완료시킨 워크아이템은 리프레쉬를 시킨다
		if( newlyAddedWorkItems != null &&newlyAddedWorkItems.size() > 0 ){
			for(int j=0; j < newlyAddedWorkItems.size(); j++){
				ProcessWorkItem wt = (ProcessWorkItem)newlyAddedWorkItems.get(j);
				wt.setMetaworksContext(new MetaworksContext());
				MetaworksRemoteService.pushClientObjectsFiltered(
						new OtherSessionFilter(notiUsers, session.getUser().getUserId().toUpperCase()),
						new Object[]{new WorkItemListener(WorkItemListener.COMMAND_APPEND, wt)});
			}
		}
		if( workItemMe != null ){
			// 새로 추가되는 workItem이 없는 경우 1. 완료시킨 워크아이템은 리프레쉬를 시킨다
			MetaworksRemoteService.pushClientObjectsFiltered(
					new OtherSessionFilter(notiUsers, session.getUser().getUserId().toUpperCase()),
					new Object[]{new InstanceListener(inst), new WorkItemListener(WorkItemListener.COMMAND_REFRESH, workItemMe)});
		}else{
			MetaworksRemoteService.pushClientObjectsFiltered(
					new OtherSessionFilter(notiUsers, session.getUser().getUserId().toUpperCase()),
					new Object[]{new InstanceListener(inst)});
		}

	}

	private IInstance saveLastComment(Instance instanceRef, HumanActivity humanActivity) throws Exception{
		String title = humanActivity.getDescription() != null ? humanActivity.getDescription() : null;

		IUser writer = new User();
		writer.setUserId(session.getUser().getUserId());
		writer.setName(session.getUser().getName());

		//마지막 워크아이템의 제목을 인스턴스의 적용
		if( title != null && !"".equals(title)){
			if(instanceRef.getLastCmnt() == null){
				instanceRef.setLastCmnt(title);
				instanceRef.setLastCmntUser(writer);
				instanceRef.setLastcmntTaskId(this.getTaskId());
				// database update
				instanceRef.databaseMe().setLastCmnt(title);
				instanceRef.databaseMe().setLastCmntUser(writer);
				instanceRef.databaseMe().setLastcmntTaskId(this.getTaskId());
			}else{
				if(instanceRef.getLastCmnt2() == null){
					instanceRef.setLastCmnt2(title);
					instanceRef.setLastCmnt2User(session.getUser());
					instanceRef.setLastcmnt2TaskId(this.getTaskId());
					// database update
					instanceRef.databaseMe().setLastCmnt2(title);
					instanceRef.databaseMe().setLastCmnt2User(writer);
					instanceRef.databaseMe().setLastcmnt2TaskId(this.getTaskId());
				}else {
					instanceRef.setLastCmnt(instanceRef.getLastCmnt2());
					instanceRef.setLastCmntUser(instanceRef.getLastCmnt2User());
					instanceRef.setLastcmntTaskId(instanceRef.getLastcmnt2TaskId());

					instanceRef.setLastCmnt2(title);
					instanceRef.setLastCmnt2User(writer);
					instanceRef.setLastcmnt2TaskId(this.getTaskId());
					// database update
					instanceRef.databaseMe().setLastCmnt(instanceRef.getLastCmnt());
					instanceRef.databaseMe().setLastCmntUser(instanceRef.getLastCmntUser());
					instanceRef.databaseMe().setLastcmntTaskId(instanceRef.getLastcmntTaskId());

					instanceRef.databaseMe().setLastCmnt2(instanceRef.getLastCmnt2());
					instanceRef.databaseMe().setLastCmnt2User(instanceRef.getLastCmnt2User());
					instanceRef.databaseMe().setLastcmnt2TaskId(instanceRef.getLastcmnt2TaskId());
				}
			}
		}
		return instanceRef;
	}
	@Autowired
	public InstanceViewContent instanceViewContent;


	@ServiceMethod(callByContent=true, when= MetaworksContext.WHEN_EDIT )
	public void save() throws RemoteException, ClassNotFoundException, Exception{

		ResultPayload rp = createResultPayload();

		processManager.saveWorkitem(getInstanceId() + (getExecutionScope() != null ? "@" + getExecutionScope():""), getTracingTag(), getTaskId().toString(), rp );
	}

	@ServiceMethod(when= MetaworksContext.WHEN_EDIT, target=ServiceMethod.TARGET_POPUP )
	public void delegate(){
		//not implemented.
	}

	private ResultPayload createResultPayload() throws Exception {
		ResultPayload rp = new ResultPayload();

		if(outputParameters!=null)
			for(ParameterValue pv : outputParameters){
				String variableTypeName = pv.getVariableType();
				//Class variableType = Thread.currentThread().getContextClassLoader().loadClass(variableTypeName);
				ProcessVariableValue processVariableValue = new ProcessVariableValue();
				processVariableValue.setName(pv.getVariableName());

				if(pv.isMultipleInput()) {
					ProcessVariableValueList pvvl =pv.getProcessVariableValueList();

					for (MetaworksElement me : pvvl.getElements()) {
						processVariableValue.setValue(me.getValue());
						processVariableValue.moveToAdd();
					}
				}else{
					processVariableValue.setValue(pv.getValue());
				}

				if(processVariableValue instanceof ITool){
					((ITool)processVariableValue).beforeComplete();
				}

				rp.setProcessVariableChange(new KeyedParameter(pv.getVariableName(), processVariableValue));
			}
		return rp;
	}

	@ServiceMethod(callByContent=true, when="compete")
	public Object[]  accept() throws Exception{
		ProcessInstance instance = processManager.getProcessInstance(instanceId.toString());

		HumanActivity humanActivity = (HumanActivity) instance.getProcessDefinition().getActivity(tracingTag);


		RoleMapping roleMapping = RoleMapping.create();
		roleMapping.setName(humanActivity.getRole().getName());
		roleMapping.setEndpoint(session.getEmployee().getEmpCode());

		String[] executedTaskIds = processManager.delegateWorkitem(this.getInstanceId(), this.getTracingTag(), roleMapping);
		processManager.applyChanges();

		// 변경된 액티비티 들만 찾기
		ArrayList<ProcessWorkItem> newlyAddedWorkItems = new ArrayList<ProcessWorkItem>();

		for(String taskId : executedTaskIds){
			ProcessWorkItem newlyAppendedWorkItem = new ProcessWorkItem();
			newlyAppendedWorkItem.setTaskId(new Long(taskId));
			newlyAppendedWorkItem.copyFrom(newlyAppendedWorkItem.databaseMe());

			newlyAddedWorkItems.add(newlyAppendedWorkItem);
		}


		ProcessWorkItem workItemMe = new ProcessWorkItem();
		workItemMe.setTaskId(this.getTaskId());
		workItemMe.copyFrom(workItemMe.databaseMe());
		workItemMe.setMetaworksContext(new MetaworksContext());


		//refreshes the instanceview so that the next workitem can be show up
		Instance inst = new Instance();
		inst.setInstId(new Long(getRootInstId()));
		inst.copyFrom(inst.databaseMe());

		InstanceViewThreadPanel panel = new InstanceViewThreadPanel();
		panel.getMetaworksContext().setHow("instanceList");
		panel.getMetaworksContext().setWhere("sns");
		panel.session = session;
		panel.load(this.getRootInstId().toString());

		this.sendPush(inst,newlyAddedWorkItems,workItemMe);

		return new Object[]{panel, new Remover(new ModalWindow(), true)};

	}
	@ServiceMethod(payload={"taskId", "replyTitle", "replyFieldName", "rootInstId", "instanceId"}, when=MetaworksContext.WHEN_EDIT, target=ServiceMethodContext.TARGET_APPEND)
	public ReplyOverlayCommentWorkItem comment() throws Exception{

		OverlayCommentOption overlayCommentOption = new OverlayCommentOption();
		overlayCommentOption.setParentTaskId(getTaskId());

		ReplyOverlayCommentWorkItem replyOverlayCommentWorkItem = new ReplyOverlayCommentWorkItem();
		replyOverlayCommentWorkItem.session = session;
		replyOverlayCommentWorkItem.processManager = processManager;
		replyOverlayCommentWorkItem.setOverlayCommentOption(overlayCommentOption);
		replyOverlayCommentWorkItem.setInstId(new Long(getInstanceId()));
		replyOverlayCommentWorkItem.setTitle(replyTitle);
		replyOverlayCommentWorkItem.setExt1(replyFieldName);
		replyOverlayCommentWorkItem.setPrtTskId(getTaskId());
		replyOverlayCommentWorkItem.setEndpoint(session.getUser().getUserId());
		replyOverlayCommentWorkItem.setRootInstId(this.getRootInstId());
		replyOverlayCommentWorkItem.add();

		replyOverlayCommentWorkItem.getMetaworksContext().setWhen("edit");
		return replyOverlayCommentWorkItem;
	}

	@Autowired
	transient public ProcessManagerRemote processManager;

	MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}

	protected Map<String, Object> makeMapForITool(HumanActivity humanActivity)
			throws Exception {
		Map<String, Object> mapForITool = new HashMap<String, Object>();

		mapForITool.put(ITool.ITOOL_INSTANCEID_KEY, getInstanceId());
		mapForITool.put(ITool.ITOOL_TRACINGTAG_KEY, getTracingTag());
		mapForITool.put(ITool.ITOOL_TASKID_KEY, getTaskId().toString());
		mapForITool.put(ITool.ITOOL_SESSION_KEY, session);
		mapForITool.put(ITool.ITOOL_PROCESS_MANAGER_KEY, processManager);
		mapForITool.put(ITool.ITOOL_ACTIVITY_EXT1_KEY, humanActivity.getExtValue1());
		mapForITool.put(ITool.ITOOL_ACTIVITY_EXT2_KEY, humanActivity.getExtValue2());
		mapForITool.put(ITool.ITOOL_ACTIVITY_EXT3_KEY, humanActivity.getExtValue3());
		mapForITool.put(ITool.ITOOL_ACTIVITY_EXT4_KEY, humanActivity.getExtValue4());
		mapForITool.put(ITool.ITOOL_ACTIVITY_EXT5_KEY, humanActivity.getExtValue5());
		mapForITool.put(ITool.ITOOL_ACTIVITY_EXT6_KEY, humanActivity.getExtValue6());
		mapForITool.put(ITool.ITOOL_ACTIVITY_EXT7_KEY, humanActivity.getExtValue7());
		mapForITool.put(ITool.ITOOL_ACTIVITY_EXT8_KEY, humanActivity.getExtValue8());
		mapForITool.put(ITool.ITOOL_ACTIVITY_EXT9_KEY, humanActivity.getExtValue9());
		mapForITool.put(ITool.ITOOL_ACTIVITY_EXT10_KEY, humanActivity.getExtValue10());



		return mapForITool;
	}

	private void loadMapForITool(Map<String, Object> map) {
		TransactionContext.getThreadLocalInstance().setSharedContext(
				ITool.ITOOL_MAP_KEY, map);
	}

	private void releaseMapForITool() {
		TransactionContext.getThreadLocalInstance().setSharedContext(
				ITool.ITOOL_MAP_KEY, null);
	}



	public static String[] executedActivityTaskIds(ProcessManagerRemote processManager, String instanceId) throws Exception {
		ProcessInstance instance = processManager.getProcessInstance(instanceId);

		return executedActivityTaskIds(instance);
	}

	public static String[] executedActivityTaskIds(ProcessInstance instance) throws Exception {
        List executedActivityCtxs = instance.getProcessTransactionContext().getExecutedActivityInstanceContextsInTransaction();

		List<String> newlyAddedWorkItems = new ArrayList<String>();
        if(executedActivityCtxs!=null && executedActivityCtxs.size() > 0){
            for(int i=executedActivityCtxs.size()-1; i>-1; i--){
                    ActivityInstanceContext lastActCtx = (ActivityInstanceContext)executedActivityCtxs.get(i);
                    Activity lastAct = lastActCtx.getActivity();

                    if(lastAct instanceof HumanActivity && lastActCtx.getInstance().isRunning(lastAct.getTracingTag())){
                        HumanActivity newlyStartedAct = (HumanActivity)lastAct;
						String[] taskIds = newlyStartedAct.getTaskIds(instance);
                        if( taskIds != null ){
							for(String taskId : taskIds){
	                            newlyAddedWorkItems.add(taskId);
							}
                        }
                    }

                    if(lastAct instanceof SubProcessActivity && lastActCtx.getInstance().isRunning(lastAct.getTracingTag())){
                    	Vector subProcess = ((SubProcessActivity)lastAct).getSubProcesses(instance);
                    	for(int indexOfSP=0; indexOfSP<subProcess.size(); indexOfSP++){
                    		ProcessInstance sp = (ProcessInstance) subProcess.elementAt(indexOfSP);
                    		String[] subprocessTaskIds = executedActivityTaskIds(sp);
                    		if( subprocessTaskIds != null ){
	                    		for(int j=0; j < subprocessTaskIds.length; j++){
	                    			newlyAddedWorkItems.add(subprocessTaskIds[j]);
	                    		}
                    		}
                	    }
                    }
            }
        }
        if( instance.isSubProcess() && (Activity.STATUS_STOPPED.equals(instance.getStatus()) || Activity.STATUS_COMPLETED.equals(instance.getStatus()))){
        	ProcessInstance mainp = instance.getMainProcessInstance();
        	String[] mainProcessTaskIds = executedActivityTaskIds(mainp);
    		if( mainProcessTaskIds != null ){
        		for(int j=0; j < mainProcessTaskIds.length; j++){
        			newlyAddedWorkItems.add(mainProcessTaskIds[j]);
        		}
    		}
        }

        return newlyAddedWorkItems.toArray(new String[newlyAddedWorkItems.size()]);
	}

	Boolean handleByJira;
		@Hidden
		public Boolean getHandleByJira() {
			return handleByJira;
		}
		public void setHandleByJira(Boolean handleByJira) {
			this.handleByJira = handleByJira;
		}
}
