package org.uengine.codi.mw3.model;

import java.util.ArrayList;
import java.util.Date;

import javax.sql.RowSet;

import org.metaworks.MetaworksContext;
import org.metaworks.Refresh;
import org.metaworks.Remover;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.Name;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.annotation.Test;
import org.metaworks.dwr.MetaworksRemoteService;
import org.metaworks.widget.ModalWindow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.uengine.codi.mw3.Login;
import org.uengine.codi.mw3.filter.AllSessionFilter;
import org.uengine.processmanager.ProcessManagerRemote;
import org.uengine.util.UEngineUtil;

public class InstanceView {

	@Autowired
	public ProcessManagerRemote processManager;	

	@AutowiredFromClient
	public Locale localeManager;

	@AutowiredFromClient(onDrop = true)
	public IInstance dropInstance;

	InstanceTooltip instanceAction;
		public InstanceTooltip getInstanceAction() {
			return instanceAction;
		}
		public void setInstanceAction(InstanceTooltip instanceAction) {
			this.instanceAction = instanceAction;
		}

	public InstanceView() {
	}		
		
	public void load(IInstance instance) throws Exception{

		InstanceTooltip instanceTooltip = MetaworksRemoteService.getComponent(InstanceTooltip.class);

		instanceTooltip.getMetaworksContext().setHow("action");		
		instanceTooltip.load(instance);
		
		this.setInstanceAction(instanceTooltip);
		
		setInstanceName(instance.getName());
		setInstanceId(instance.getInstId().toString());
		setStatus(instance.getStatus());
		setSecuopt(instance.getSecuopt());
		setDueDate(instance.getDueDate());
		
		Follower follower = new InstanceFollower();
		follower.setParentId(instance.getInstId().toString());
		
		Followers followers = new Followers(follower);
		followers.load();
		
		this.setFollowers(followers);
		
		// InstanceView 기본 설정
		this.loadDefault(instance);
		
		
		/*
		 * 인스턴스 읽음 표시
		 */
		/*
		Notification notificationType = new Notification();
		INotification notifictionsInSameInstance  = notificationType.sql("update bpm_noti set confirm=1 where instid = ?instid and userId=?userId");
		notifictionsInSameInstance.setConfirm(true);
		notifictionsInSameInstance.setInstId(inst.getInstId());
		notifictionsInSameInstance.setUserId(session.getUser().getUserId());
		notifictionsInSameInstance.update();
		*/
		
		/*
		 * TODO: 보안체크 부분 다른 부분에서 처리할 수 있도록
		if("1".equals(getSecuopt())){ //means secured conversation
			
			IUser followers = getFollowers().getFollowers();
			followers.beforeFirst();
			
			boolean iCanSee = false;
			while(followers.next()){
				if(session.getUser().getUserId().equals(followers.getUserId())){
					iCanSee = true;
					break;
				}
			}
			IDept deptFollower = getFollowers().getDeptFollowers();
			if( deptFollower.getImplementationObject().getCachedRows() != null ){
				deptFollower.beforeFirst();
				while( deptFollower.next() ){
					if(deptFollower.getPartCode().equals(session.getEmployee().getPartCode()) ){
						iCanSee = true;
						break;
					}
				}
				deptFollower.beforeFirst();
			}
			
			if(!iCanSee){
				throw new Exception("$NotPermittedToSee");
			}
			
			followers.beforeFirst();
		}
		*/
		
		//setInstanceViewThreadPanel(activityStream());

		setInstanceViewDetail(createInstanceViewDetail());
		
		//setInstanceViewThreadPanel(new Loader(instanceViewThreadPanel, "load"));
	}

	public InstanceViewDetail createInstanceViewDetail() {
		InstanceViewThreadPanel instanceViewThreadPanel = new InstanceViewThreadPanel();
		instanceViewThreadPanel.setInstanceId(this.getInstanceId());
		instanceViewThreadPanel.session = session;
		instanceViewThreadPanel.load();

		return (instanceViewThreadPanel);
	}
	
	/*
	 * 2013-01-21 jinwon
	 * InstanceViewThreadPanel 로 대체
	 * 
	@ServiceMethod(inContextMenu=true, callByContent=true, needToConfirm=true, target="popup", mouseBinding="drop")
	public Object[] drop() throws Exception{
		Object clipboard = session.getClipboard();
		if(clipboard instanceof IWfNode){
			WfNode draggedNode = (WfNode) clipboard;
			
			//setting the first workItem as wfNode referencer
			GenericWorkItem genericWI = new GenericWorkItem();
			
			genericWI.processManager = processManager;
			genericWI.session = session;
			genericWI.setTitle("Attaching Knowledge");//parent.getName());
			GenericWorkItemHandler genericWIH = new GenericWorkItemHandler();
			KnowledgeTool knolTool = new KnowledgeTool();
			knolTool.setNodeId(draggedNode.getId());
			genericWIH.setTool(knolTool);
			
			genericWI.setGenericWorkItemHandler(genericWIH);
			genericWI.setInstId(new Long(getInstanceId()));
			// TODO attach thread 
			return genericWI.add();
//		}else{
//		
//			Instance instance = new Instance();
//			instance.session = session;
//			instance.processManager = processManager;
//			instance.setInstId(new Long(getInstanceId()));
//			return instance.paste();
		}
		return null;
	}
	*/
	
	
	String status;
		public String getStatus() {
			return status;
		}
	
		public void setStatus(String status) {
			this.status = status;
		}

	InstanceNameChanger instanceNameChanger;
		public InstanceNameChanger getInstanceNameChanger() {
			return instanceNameChanger;
		}
		public void setInstanceNameChanger(InstanceNameChanger instanceNameChanger) {
			this.instanceNameChanger = instanceNameChanger;
		}
		
	InstanceSecurityConfigurer instanceSecurityConfigurer;
		public InstanceSecurityConfigurer getInstanceSecurityConfigurer() {
			return instanceSecurityConfigurer;
		}
		public void setInstanceSecurityConfigurer(
				InstanceSecurityConfigurer instanceSecurityConfigurer) {
			this.instanceSecurityConfigurer = instanceSecurityConfigurer;
		}

	String folderId;
		public String getFolderId() {
			return folderId;
		}
		public void setFolderId(String folderId) {
			this.folderId = folderId;
		}

	String instanceName;
		@Name
		public String getInstanceName() {
			return instanceName;
		}
		public void setInstanceName(String instanceName) {
			this.instanceName = instanceName;
		}
	
	String secuopt;
		public String getSecuopt() {
			return secuopt;
		}	
		public void setSecuopt(String secuopt) {
			this.secuopt = secuopt;
		}
	Date dueDate;
		public Date getDueDate() {
			return dueDate;
		}
		public void setDueDate(Date dueDate) {
			this.dueDate = dueDate;
		}
		
	@AutowiredFromClient
	public Session session;

	Long taskId;
		public Long getTaskId() {
			return taskId;
		}
		public void setTaskId(Long taskId) {
			this.taskId = taskId;
		}
	protected void loadDefault(IInstance inst) throws Exception{
		newItem = new CommentWorkItem();
		newItem.setInstId(new Long(getInstanceId()));
		newItem.setTaskId(new Long(-1));
		newItem.getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);
		
		//Session session = (Session) TransactionContext.getThreadLocalInstance().getSharedContext("codi_session");

		if(session==null){
			session = new Session();
			session.user = User.fromHttpSession();
		}
		
		newItem.setWriter(session.user);
		
		//ProcessInstanceMonitor flowChart = new ProcessInstanceMonitor();
		//flowChart.setInstanceId(instanceId);
		//setProcessInstanceMonitor(flowChart);
		
		if(inst.getDefId() == null){
			/*
			crowdSourcer = new CrowdSourcer();
			crowdSourcer.setInstanceId(getInstanceId());
			crowdSourcer.setFollowers(this.followers);
			crowdSourcer.setMessage(getInstanceName());
			
			
			if(instance.getProperty("", "facebook_postIds") != null){
				String[] postIds = (String[])instance.getProperty("", "facebook_postIds");
				crowdSourcer.setPostIds(postIds);			
			}
			
			boolean isOpen = false;
			if(instance.getProperty("", "is_open") != null){
				isOpen = ((String)instance.getProperty("", "is_open")).equals("open") ? true : false;
			}
			
			System.out.println("isOpen :" + isOpen);
			crowdSourcer.setOpen(isOpen);
		
			//threadPosting = new PostingsWorkItem();
			externalFeedback = new ArrayList<FacebookFeedback>();
			if(instance.getProperty("", "facebook_postIds") != null){x1
				String[] postIds = (String[]) instance.getProperty("", "facebook_postIds");
				
				for(int i=0; i<postIds.length; i++){
					FacebookFeedback postingWorkItem = new FacebookFeedback();
					postingWorkItem.setPostId(postIds[i]);
					//postingWorkItem.setType("posting");
					
					externalFeedback.add(postingWorkItem);
				}			
			}	
			*/
			//ProcessInstance instance = processManager.getProcessInstance(getInstanceId());			
			//eventTriggerPanel = new EventTriggerPanel(instance);
		}
		
		instanceNameChanger = new InstanceNameChanger();
		instanceNameChanger.setInstanceId(this.getInstanceId());
		instanceNameChanger.setInstanceName(this.getInstanceName());
		if(session!= null && session.getEmployee().getEmpCode().equals(inst.getInitEp())){
			instanceNameChanger.session = session;
			instanceNameChanger.getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);
		}
		
		setInstanceSecurityConfigurer(new InstanceSecurityConfigurer());
		getInstanceSecurityConfigurer().setInstanceId(inst.getInstId().toString());

	}
	
	Long rootInstId;
		public Long getRootInstId() {
			return rootInstId;
		}
		public void setRootInstId(Long rootInstId) {
			this.rootInstId = rootInstId;
		}
	ArrayList<FacebookFeedback> externalFeedback;
		public ArrayList<FacebookFeedback> getExternalFeedback() {
			return externalFeedback;
		}
	
		public void setExternalFeedback(ArrayList<FacebookFeedback> externalFeedback) {
			this.externalFeedback = externalFeedback;
		}


	String instanceId;
		@Id
		public String getInstanceId() {
			return instanceId;
		}
		public void setInstanceId(String instanceId) {
			this.instanceId = instanceId;
		}

	IWorkItem newItem;
		public IWorkItem getNewItem() {
			return newItem;
		}
		public void setNewItem(IWorkItem newItem) {
			this.newItem = newItem;
		}
		
//	NewInstancePanel newSubInstancePanel;
//		public NewInstancePanel getNewSubInstancePanel() {
//			return newSubInstancePanel;
//		}
//		public void setNewSubInstancePanel(NewInstancePanel newSubInstancePanel) {
//			this.newSubInstancePanel = newSubInstancePanel;
//		}
	
//	IWorkItem thread;
//		public IWorkItem getThread() {
//			return thread;
//		}
//		public void setThread(IWorkItem thread) {
//			this.thread = thread;
//		}	

	InstanceViewDetail instanceViewDetail;
		public InstanceViewDetail getInstanceViewDetail() {
			return instanceViewDetail;
		}
		public void setInstanceViewDetail(InstanceViewDetail instanceViewDetail) {
			this.instanceViewDetail = instanceViewDetail;
		}



	Object documentContentView;
		public Object getDocumentContentView() {
			return documentContentView;
		}
		public void setDocumentContentView(Object documentContentView) {
			this.documentContentView = documentContentView;
		}
		
//	IWorkItem threadPosting;
//		public IWorkItem getThreadPosting() {
//			return threadPosting;
//		}
//		public void setThreadPosting(IWorkItem threadPosting) {
//			this.threadPosting = threadPosting;
//		}


	EventTriggerPanel eventTriggerPanel;
		public EventTriggerPanel getEventTriggerPanel() {
			return eventTriggerPanel;
		}
		public void setEventTriggerPanel(EventTriggerPanel eventTrigger) {
			this.eventTriggerPanel = eventTrigger;
		}
		
	Followers followers;
		public Followers getFollowers() {
			return followers;
		}
		public void setFollowers(Followers followers) {
			this.followers = followers;
		}
	
	/*
	CrowdSourcer crowdSourcer;			
		public CrowdSourcer getCrowdSourcer() {
			return crowdSourcer;
		}
	
		public void setCrowdSourcer(CrowdSourcer crowdSourcer) {
			this.crowdSourcer = crowdSourcer;
		}
	*/
		
	//TODO: Please remove this method after Fake
	@ServiceMethod 
	@Test(scenario="first", starter=true, instruction="댓글로 메모, 파일등을 입력할 수 있고 기타 동적인 업무를 추가할 수 있습니다. 여기서 기타활동을 클릭하십시오.", next="autowiredObject.org.uengine.codi.mw3.model.IProcessMap.initiate()")
	public void newActivity() {}
	
	
	@ServiceMethod(target="popup", loader="auto")
	public Popup schedule() throws Exception{
		Instance instance = new Instance();
		instance.setInstId(new Long(getInstanceId()));
		 
		if(!instance.databaseMe().getInitEp().equals(session.getUser().getUserId())  && !(session.getEmployee()!=null && session.getEmployee().getIsAdmin())){
			throw new Exception("$OnlyInitiatorCanSetInstanceInfo");
		}

		InstanceDueSetter ids = new InstanceDueSetter();
		ids.setInstId(new Long(getInstanceId()));
		ids.setDueDate(instance.databaseMe().getDueDate());
		ids.setBenefit(instance.databaseMe().getBenefit());
		ids.setPenalty(instance.databaseMe().getPenalty());
		ids.setEffort(instance.databaseMe().getEffort());
		ids.setOnlyInitiatorCanComplete(instance.databaseMe().isInitCmpl());
		ids.setProgress(instance.databaseMe().getProgress());
		if("sns".equals(session.getEmployee().getPreferUX()) ){
			ids.getMetaworksContext().setHow("instanceList");
			ids.getMetaworksContext().setWhere("sns");
		}
		ids.getMetaworksContext().setWhen("edit");
		
		/*return new Popup(350,200,ids);*/
		return new Popup(400,300,ids);
	}
	
	@ServiceMethod(payload={"instanceId"}, needToConfirm=true, target=ServiceMethodContext.TARGET_APPEND)
	public Object[] remove() throws Exception{

		Instance instance = new Instance();
		instance.setInstId(new Long(getInstanceId()));
		 
		IInstance instanceRef = instance.databaseMe();
		
		if(!instanceRef.getInitEp().equals(session.getUser().getUserId())  && !(session.getEmployee()!=null && session.getEmployee().getIsAdmin())){
			throw new Exception("$OnlyInitiatorCanDeleteTheInstance");
		}
		
		processManager.stopProcessInstance(instanceId);

		instanceRef.setIsDeleted(true);
		instance.flushDatabaseMe();
		
		/* 내가 할일 카운트 다시 계산 */
		if(instanceRef.getDefId() != null || (instanceRef.getDefId() == null && instanceRef.getDueDate() != null)){
			TodoBadge todoBadge = new TodoBadge();
			todoBadge.session = session;
			todoBadge.refresh();

			MetaworksRemoteService.pushTargetClientObjects(Login.getSessionIdWithUserId(session.getUser().getUserId()),
					new Object[]{new Refresh(todoBadge)});			
		}
		
		if(!"sns".equals(session.getEmployee().getPreferUX())){
			NewInstancePanel instancePanel = new NewInstancePanel();
			instancePanel.load(session);
			
			return new Object[]{new Remover(instance), new Refresh(new ContentWindow(instancePanel))};
		}else{
			return new Object[]{new Remover(instance)};
		}
	}
	
	@ServiceMethod(payload={"instanceId", "status"})
	public void complete() throws Exception{

		//processManager.stopProcessInstance(instanceId);
		
		Instance instance = new Instance();
		instance.setInstId(new Long(getInstanceId()));
		instance.copyFrom(instance.databaseMe());
				
		if(instance.isInitCmpl() && !session.getUser().getUserId().equals(instance.getInitEp())){
			throw new Exception("$OnlyInitiatorCanComplete");
		}
		
		String tobe = null;
		String title = null;
		
		if(getStatus().equals("Completed")){
			tobe = "Running";
			title = localeManager.getResourceBundle().getProperty("CancleCompleted");
		}else{
			tobe = "Completed";
			title = localeManager.getResourceBundle().getProperty("CompletedDate");
		}
			
		//if schedule changed
		CommentWorkItem workItem = new CommentWorkItem();
		workItem.getMetaworksContext().setHow("changeSchedule");
		workItem.session = session;
		workItem.processManager = processManager;
		
		workItem.setInstId(new Long(getInstanceId()));
		workItem.setTitle(title);
		workItem.add();
		workItem.flushDatabaseMe();
		
		// instance update flush
		instance.databaseMe().setStatus(tobe);

		this.load(instance);
		this.setStatus(tobe);

		//MetaworksRemoteService.pushClientObjects(new Object[]{new InstanceListener(InstanceListener.COMMAND_REFRESH, instance)});
		MetaworksRemoteService.pushClientObjectsFiltered(
				new AllSessionFilter(Login.getSessionIdWithCompany(session.getEmployee().getGlobalCom())),
				new Object[]{new InstanceListener(InstanceListener.COMMAND_REFRESH, instance.databaseMe())});

		/* 내가 할일 카운트 다시 계산 */
		if(instance.getDefId() != null || (instance.getDefId() == null && instance.getDueDate() != null)){
			TodoBadge todoBadge = new TodoBadge();
			todoBadge.session = session;
			todoBadge.refresh();

			MetaworksRemoteService.pushTargetClientObjects(Login.getSessionIdWithUserId(session.getUser().getUserId()),
					new Object[]{new Refresh(todoBadge), new WorkItemListener(workItem)});			
		}
		
		//inst_emp_perf 테이블에 성과정보 저장 insert
		int businessValue = instance.getBenefit() + instance.getPenalty();
		
		if(tobe.equals("Running")){
			deleteBV();
		}else if (tobe.equals("Completed")){
			insertBV(businessValue);
		}
	}
	
	private void insertBV(int businessValue) throws Exception{
		IRoleMapping allFollower = RoleMapping.allFollower(Long.parseLong(instanceId));
		RowSet rowset = allFollower.getImplementationObject().getRowSet();
		
		InstanceEmployeePerformance bizVal = new InstanceEmployeePerformance();
		
		if(allFollower.size() > 0){
			int eachBV = businessValue/ allFollower.size();

			
			while(rowset.next()){
				bizVal.setInstId(rowset.getLong("instId"));
				bizVal.setEmpCode(rowset.getString("endPoint"));
				bizVal.setBusinessValue(eachBV);
				
				bizVal.createDatabaseMe();
			}

		}
		
	}
		
	private void deleteBV() throws Exception{
		InstanceEmployeePerformance bizVal = new InstanceEmployeePerformance();
		
		bizVal.setInstId(Long.parseLong(instanceId));
		bizVal.deleteDatabaseMe();
		
	}
	
	


	
	@ServiceMethod(target=ServiceMethodContext.TARGET_POPUP)
	public Popup newSubInstancePanel() throws Exception{
		
//		NewInstancePanel newSubInstancePanel = new NewInstancePanel();
//		newSubInstancePanel.setParentInstanceId(getInstanceId());
//		
//		newSubInstancePanel.load();

		//Good example :   customizing for specific usage - removing some parts
//		newSubInstancePanel.setUnstructuredProcessInstanceStarter(null);
		
		
//		ProcessMapPanel processMapPanel;
//		processMapPanel = new ProcessMapPanel();		
//		processMapPanel.setMetaworksContext(this.getMetaworksContext());
//		processMapPanel.load();

		
		ProcessMapList processMapList = new ProcessMapList();
		processMapList.load(session);
		processMapList.setParentInstanceId(new Long(getInstanceId()));
		
		Popup popup = new Popup();
		popup.setPanel(processMapList);
		
		return popup;
		
	}

	@ServiceMethod
	public InstanceViewThreadPanel activityStream() throws Exception{
		InstanceViewThreadPanel instanceViewThreadPanel = new InstanceViewThreadPanel();
		instanceViewThreadPanel.session = session;
		instanceViewThreadPanel.load(instanceId);
		
		return instanceViewThreadPanel;
	}
	
	transient MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}
}
