package org.uengine.codi.mw3.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import org.metaworks.EventContext;
import org.metaworks.MetaworksContext;
import org.metaworks.MetaworksException;
import org.metaworks.Refresh;
import org.metaworks.Remover;
import org.metaworks.ServiceMethodContext;
import org.metaworks.ToAppend;
import org.metaworks.ToEvent;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.dao.DAOUtil;
import org.metaworks.dao.Database;
import org.metaworks.dao.IDAO;
import org.metaworks.dao.TransactionContext;
import org.metaworks.dwr.MetaworksRemoteService;
import org.metaworks.website.MetaworksFile;
import org.metaworks.widget.ModalWindow;
import org.oce.garuda.multitenancy.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.uengine.codi.CodiProcessDefinitionFactory;
import org.uengine.codi.mw3.Login;
import org.uengine.codi.mw3.filter.AllSessionFilter;
import org.uengine.codi.mw3.filter.OtherSessionFilter;
import org.uengine.codi.mw3.knowledge.KnowledgeTool;
import org.uengine.codi.mw3.knowledge.TopicNode;
import org.uengine.codi.mw3.knowledge.WfNode;
import org.uengine.kernel.EJBProcessInstance;
import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.RoleMapping;
import org.uengine.modeling.resource.Version;
import org.uengine.modeling.resource.VersionManager;
import org.uengine.processmanager.ProcessManagerBean;
import org.uengine.processmanager.ProcessManagerRemote;

public class ProcessMap extends Database<IProcessMap> implements IProcessMap {

    public final static String PROCESS = "process";

    public ProcessMap() {
        setIconFile(new MetaworksFile());
        setIconColor(new ProcessMapColor("배경선택"));
    }

    @Autowired
    public ProcessManagerRemote processManager;

    @AutowiredFromClient
    public Session session;

    @AutowiredFromClient
    public NewInstancePanel newInstancePanel;

    @AutowiredFromClient
    public ProcessMapList processMapList;

    String mapId;

    public String getMapId() {
        return mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

    String defId;

    public String getDefId() {
        return defId;
    }

    public void setDefId(String defId) {
        this.defId = defId;
    }

    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    int no;

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    String comCode;

    public String getComCode() {
        return comCode;
    }

    public void setComCode(String comCode) {
        this.comCode = comCode;
    }

    MetaworksFile iconFile;

    public MetaworksFile getIconFile() {
        return iconFile;
    }

    public void setIconFile(MetaworksFile iconFile) {
        this.iconFile = iconFile;
    }

    ProcessMapColor iconColor;

    public ProcessMapColor getIconColor() {
        return iconColor;
    }

    public void setIconColor(ProcessMapColor iconColor) {
        this.iconColor = iconColor;
    }

    RoleMappingPanel roleMappingPanel;

    public RoleMappingPanel getRoleMappingPanel() {
        return roleMappingPanel;
    }

    public void setRoleMappingPanel(RoleMappingPanel roleMappingPanel) {
        this.roleMappingPanel = roleMappingPanel;
    }

    String cmPhrase;

    public String getCmPhrase() {
        return cmPhrase;
    }

    public void setCmPhrase(String cmPhrase) {
        this.cmPhrase = cmPhrase;
    }

    String cmTrgr;

    public String getCmTrgr() {
        return cmTrgr;
    }

    public void setCmTrgr(String cmTrgr) {
        this.cmTrgr = cmTrgr;
    }

    boolean isScheduled;

    public boolean getIsScheduled() {
        return isScheduled;
    }

    public void setIsScheduled(boolean isScheduled) {
        this.isScheduled = isScheduled;
    }

    public void createRoleDef() throws Exception {
        org.uengine.kernel.ProcessDefinition definition = processManager.getProcessDefinition(defId);
        for (org.uengine.kernel.Role role : definition.getRoles()) {
            RoleMappingDefinition roleMappingDefinition = new RoleMappingDefinition();
            roleMappingDefinition.setRoleDefId(TenantContext.getThreadLocalInstance().getTenantId() + "." + defId + "." + role.getName());

            if ("Initiator".equals(role.getName())) {
                continue;
            }
            User user = new User();
            user.setUserId(role.getName());
            roleMappingDefinition.setDefId(defId);
            roleMappingDefinition.setRoleName(role.getName());
            roleMappingDefinition.setComCode(TenantContext.getThreadLocalInstance().getTenantId());
            roleMappingDefinition.setMappedRoleCode(role.getName());
            roleMappingDefinition.setRoleDefType(RoleMappingDefinition.ROLE_DEF_TYPE_ROLE);

            roleMappingDefinition.createDatabaseMe();
        }
    }

    public void createMe() throws Exception {
        createDatabaseMe();
        flushDatabaseMe();
    }

    public Object[] save() throws Exception {

        if (!"simulator".equals(this.getMetaworksContext().getWhere())) {
            this.saveMe();

            ProcessMapList processMapList = new ProcessMapList();
            processMapList.load(session);
        }

        if ("simulator".equals(this.getMetaworksContext().getWhere()) || "run".equals(this.getMetaworksContext().getHow())) {

            return this.initiate(); //new Object[]{new Remover(new Popup()), new Refresh(processMapList)};
        } else
            return new Object[]{new ToEvent(ServiceMethodContext.TARGET_SELF, EventContext.EVENT_CLOSE)};

        //new Refresh(processMapList),
    }

    public Object[] remove() throws Exception {
//		// 스케쥴 테이블에 데이터가 있는지 확인하여 지움
//		String sql = "DELETE FROM SCHEDULE_TABLE WHERE defId = ?defId AND GLOBALCOM = ?globalcom";
//        IDAO dao = Database.sql(IDAO.class, sql);
//        dao.set("defId"	, this.getDefId());
//        dao.set("globalcom"	, this.getComCode());
//        dao.update();
//
        deleteDatabaseMe();

        ProcessMapList processMapList = new ProcessMapList();
        processMapList.load(session);

        return new Object[]{processMapList, new Remover(new Popup())};
    }

    public void saveMe() throws Exception {
        if (getIconFile().getDeletedPath() != null)
            getIconFile().remove();

        if (getIconFile().getFileTransfer() != null && !getIconFile().getFileTransfer().getFilename().isEmpty())
            getIconFile().upload();

        setComCode(session.getCompany().getComCode());

        getRoleMappingPanel().save();

        syncToDatabaseMe();
        flushDatabaseMe();
    }

    public Popup modify() throws Exception {
        getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);

        this.setRoleMappingPanel(new RoleMappingPanel(processManager, this.getDefId(), session));

        Popup popup = new Popup(580, 600);
        popup.setPanel(this);
        popup.setName("Process Information");

        if ("run".equals(this.getMetaworksContext().getHow())) {
            popup.setHeight(210);
            popup.setName("담당자 지정");
        }

        return popup;
    }

    public Remover close() throws Exception {
        Popup popup = new Popup();
        popup.setName("프로세스 정보");
        return new Remover(popup);
    }

    public boolean confirmExist() {
        try {
            if (databaseMe() == null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return true;
        }

    }

    public static IProcessMap loadList(Session session) throws Exception {

        DAOUtil daoUtil = new DAOUtil();

        StringBuffer sb = new StringBuffer();
        sb.append("SELECT *");
        sb.append("  FROM processMap where comCode=?comCode");
        sb.append(" ORDER BY " + daoUtil.replaceReservedKeyword("no"));

        IProcessMap processMap = (IProcessMap) sql(ProcessMap.class, sb.toString());
        processMap.setComCode(session.getCompany().getComCode());
        processMap.select();

        return processMap;

    }

    @Autowired
    public InstanceViewContent instanceView;


    public String initializeProcess() throws Exception {

        // 프로세스 케쉬 초기화
        ProcessManagerBean pmb = (ProcessManagerBean) processManager;
        CodiProcessDefinitionFactory.getInstance(pmb.getTransactionContext()).removeFromCache(this.getDefId());

        String instId = processManager.initializeProcess(VersionManager.getProductionResourcePath("codi", this.getDefId()));

        RoleMapping rm = RoleMapping.create();
        if (session.getUser() != null) {
            rm.setName("Initiator");
            rm.setEndpoint(session.getUser().getUserId());

            processManager.putRoleMapping(instId, rm);
        }

        processManager.setLoggedRoleMapping(rm);

        return instId;
    }

    public void executeProcess(String instId) throws Exception {
        processManager.executeProcess(instId);
        processManager.applyChanges();
    }

    public Object[] initiate() throws Exception {

		/* 프로세스를 시작할때, 사용자가 지정이 안되어 있으면 팝업을 띄우는 부분 현재 안쓰여서 주석 처리 - 14.2.12 김형국
		roleMappingPanel = new RoleMappingPanel(processManager, this.getDefId(), session);
		ArrayList<IRoleMappingDefinition> roleDefList = roleMappingPanel.getRoleMappingDefinitions();
		for(IRoleMappingDefinition roleDef : roleDefList){
			if( "Initiator".equals(roleDef.getRoleName())){
				continue;
			}
			if( roleDef.getMappedUser().getUserId() == null ){
				this.getMetaworksContext().setHow("run");
				
				return new Object[]{this.modify()};
			}
		}
		*/

        // InstanceViewContent instanceView// = new InstanceViewContent();
        // 프로세스 실행
        if (getMetaworksContext() != null && "simulator".equals(getMetaworksContext().getWhere())) {
            TransactionContext.getThreadLocalInstance().setSharedContext("isDevelopmentTime", new Boolean(true));
        }

        String instId = processManager.initializeProcess(VersionManager.getProductionResourcePath("codi", this.getDefId()));


        String title = null;
        if (newInstancePanel != null && newInstancePanel.getNewInstantiator().getTitle() != null)
            title = newInstancePanel.getNewInstantiator().getTitle() + (this.getName() != null ? "-" + this.getName() : "");

        Instance instanceRef = new Instance();
        instanceRef.setInstId(new Long(instId));

        if (newInstancePanel != null) {
//			instanceRef.databaseMe().setSecuopt("" + newInstancePanel.getSecurityLevel());

            if (newInstancePanel.getKnowledgeNodeId() != null) {
                processManager.executeProcess(instId);
                processManager.applyChanges();

                InstanceViewContent rootInstanceView = instanceView;// = new InstanceViewContent();
                rootInstanceView.session = session;
                rootInstanceView.load(instanceRef);


                WfNode parent = new WfNode();
                parent.load(newInstancePanel.getKnowledgeNodeId());

                instanceView.instanceView.instanceNameChanger.setInstanceName(parent.getName());
                instanceView.instanceView.instanceNameChanger.change();


//				WfNode child = new WfNode();		
//				child.setName(instanceView.instanceName);
//				child.setLinkedInstId(Long.parseLong(instId));
//				parent.addChildNode(child);
//				child.createMe();

                parent.setLinkedInstId(Long.parseLong(instId));
                parent.save();

                //setting the first workItem as wfNode referencer
                GenericWorkItem genericWI = new GenericWorkItem();

                genericWI.processManager = processManager;
                genericWI.session = session;
                genericWI.setTitle("지식조각을 첨부합니다");//parent.getName());
                GenericWorkItemHandler genericWIH = new GenericWorkItemHandler();
                KnowledgeTool knolTool = new KnowledgeTool();
                knolTool.setNodeId(parent.getId());
                genericWIH.setTool(knolTool);

                genericWI.setGenericWorkItemHandler(genericWIH);
                genericWI.setInstId(new Long(instId));
                genericWI.add();

                WorkItem newItem = new CommentWorkItem();
                newItem.setInstId(new Long(instanceView.getInstanceView().getInstanceId()));
                newItem.setTaskId(new Long(instanceView.getInstanceView().getInstanceId()));
                newItem.getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);

                instanceView.getInstanceView().setNewItem(newItem);

                instanceRef.databaseMe().setSecuopt("" + newInstancePanel.getSecurityLevel());
                instanceRef.flushDatabaseMe();

                return new Object[]{instanceView, parent};

            }


        } else if (processMapList != null && processMapList.getParentInstanceId() != null) { //need to attach new instance to the parent instance
            Instance instance = new Instance();
            instance.setInstId(Long.valueOf(this.instanceView.getInstanceView().getInstanceId()));
            instance.session = session;
            instance.copyFrom(instance.databaseMe());

            if (!instance.checkAuth()) {
                throw new MetaworksException("$NotPermittedToWork");
            }
            if (instance.getIsDeleted()) {
                throw new MetaworksException("$alreadyDeletedPost");
            }

            ProcessInstance instanceObject = processManager.getProcessInstance(instId);

            //IInstance instance = instanceRef.databaseMe();

            EJBProcessInstance ejbParentInstance = (EJBProcessInstance) instanceObject;
            ejbParentInstance.getProcessInstanceDAO().setRootInstId(new Long(processMapList.getParentInstanceId()));

            Instance rootInstanceRef = new Instance();
            rootInstanceRef.setInstId(processMapList.getParentInstanceId());

            roleMappingPanel = new RoleMappingPanel(processManager, this.getDefId(), session);
            roleMappingPanel.putRoleMappings(processManager, processMapList.getParentInstanceId().toString());
            processManager.executeProcess(instId);

            String[] executedTaskIds = WorkItemHandler.executedActivityTaskIds(instanceObject);

            processManager.applyChanges();

            afterInstantiation(instanceRef);

            if ("sns".equals(session.getEmployee().getPreferUX())) {
                InstanceViewThreadPanel panel = new InstanceViewThreadPanel();
                panel.getMetaworksContext().setHow("instanceList");
                panel.getMetaworksContext().setWhere("sns");
                panel.session = session;
                panel.load(processMapList.getParentInstanceId().toString());
                return new Object[]{panel, new Remover(new Popup(), true)};
            } else {
				/*
				 * 2013/12/03 cjw
				 * 서브로 프로세스 실행시에 실행된 워크아이템만 부분 갱신되게 수정
				 */
                InstanceViewThreadPanel instanceViewThreadPanel = new InstanceViewThreadPanel();
                instanceViewThreadPanel.setInstanceId(processMapList.getParentInstanceId().toString());

                ArrayList<WorkItem> newlyAddedWorkItems = new ArrayList<WorkItem>();

                for (String taskId : executedTaskIds) {
                    ProcessWorkItem newlyAppendedWorkItem = new ProcessWorkItem();
                    newlyAppendedWorkItem.setTaskId(new Long(taskId));
                    newlyAppendedWorkItem.copyFrom(newlyAppendedWorkItem.databaseMe());
                    newlyAppendedWorkItem.setMetaworksContext(new MetaworksContext());
                    newlyAddedWorkItems.add(newlyAppendedWorkItem);
                }


                Instance parentInstance = new Instance();
                parentInstance.setInstId(processMapList.getParentInstanceId());
                IInstance copyOfInstance = parentInstance.databaseMe();
                parentInstance.copyFrom(copyOfInstance);
                parentInstance.setMetaworksContext(new MetaworksContext());

                // 주제 제목 설정
                if (copyOfInstance.getTopicId() != null) {
                    TopicNode topic = new TopicNode();
                    topic.setId(copyOfInstance.getTopicId());
                    topic.copyFrom(topic.databaseMe());
                    copyOfInstance.setTopicName(topic.getName());
                }
                HashMap<String, String> notiUsers = this.processNoti(parentInstance);
                notiUsers.putAll(Login.getSessionIdWithCompany(session.getEmployee().getGlobalCom()));

                MetaworksRemoteService.pushTargetClientObjects(Login.getSessionIdWithUserId(session.getUser().getUserId()), new Object[]{new InstanceListener(parentInstance)});

                // 본인 이외에 다른 사용자에게 push
                // 새로 추가되는 workItem이 있는 경우 - 1. 새로추가된 workItem은 append를 한다
//						wt.setInstId(processMapList.getParentInstanceId());
                MetaworksRemoteService.pushClientObjectsFiltered(
                        new OtherSessionFilter(notiUsers, session.getUser().getUserId().toUpperCase()),
                        new Object[]{new ToAppend(instanceViewThreadPanel, newlyAddedWorkItems)});
                // 2. 인스턴스 리스트를 위로 올린다.
                MetaworksRemoteService.pushClientObjectsFiltered(
                        new OtherSessionFilter(notiUsers, session.getUser().getUserId().toUpperCase()),
                        new Object[]{new InstanceListener(parentInstance)});

                Followers followers = instanceView.getInstanceView().getFollowers();
                followers.load();

                return new Object[]{new ToEvent(ServiceMethodContext.TARGET_OPENER, EventContext.EVENT_CLOSE), new ToAppend(instanceViewThreadPanel, newlyAddedWorkItems), new Refresh(followers)};
            }
        }


        //set the role mappings the administrator set.
		
		/*
		roleMappingPanel = new RoleMappingPanel(processManager, this.getDefId(), session);
		ArrayList<IRoleMappingDefinition> roleDefList = roleMappingPanel.getRoleMappingDefinitions();
		for(IRoleMappingDefinition roleDef : roleDefList){
			if( "Initiator".equals(roleDef.getRoleName())){
				continue;
			}
			if( roleDef.getMappedUser().getUserId() == null ){
				this.getMetaworksContext().setHow("run");
				
				return new Object[]{this.modify()};
				*/
		
				/*
				// user가 매핑이 안되어 있다면 유저를 매핑하라고 메시지를 띄우고 프로세스가 진행이 안됨
				String systemAdmin = GlobalContext.getPropertyString("codi.user.id","0");
				IEmployee correntUser = session.getEmployee();
				Employee emp = new Employee();
				emp.copyFrom(correntUser);
				IEmployee adminUser = emp.findCompanyAdmin();
				if( adminUser != null ){
					int i =0;
					while(adminUser.next() ){
						if( i == 0 ){
							systemAdmin = adminUser.getEmpName();
						}else{
							systemAdmin += " , " + adminUser.getEmpName();
						}
						i++;
					}
				}
				StringBuffer buffer = new StringBuffer();
				buffer.append("<b>현재 프로세스에 역할자가 매핑이 되어 있지 않습니다.<br/>");
				buffer.append(" <span style='color:blue'>" + systemAdmin + "</span> (관리자) 에게 문의하여 주세요.</b> <br/><br/>");
				buffer.append(" <span style='color:blue'><b>* 매핑 가이드 </b></span><br/>");
				buffer.append(" 관리자 Login > 프로세스앱 마우스 오버 > 톱니바퀴 클릭 > 친구선택     ");
				 
				
				ErrorPage message = new ErrorPage();
				message.setErrorMessage(buffer.toString());
				
				ModalWindow errorMessage = new ModalWindow();
				errorMessage.setTitle("");
				errorMessage.setWidth(450);
				errorMessage.setHeight(200);
				errorMessage.setPanel(message);
				
				return new Object[]{errorMessage};
				
//				throw new MetaworksException("역할 매핑이 안되어 있어요. 관리자에게 문의해 주세요.");
				*/
		
		/*
			}
		}
		*/


        roleMappingPanel.putRoleMappings(processManager, instId);

        processManager.executeProcess(instId);
        processManager.applyChanges();
        //end

        afterInstantiation(instanceRef);

        if ("sns".equals(session.getEmployee().getPreferUX())) {
            InstanceListPanel instanceListPanel = new InstanceListPanel(session); //should return instanceListPanel not the instanceList only since there're one or more instanceList object in the client-side
            instanceListPanel.getInstanceList().load();

            return new Object[]{new Refresh(instanceListPanel), new Remover(new Popup())};
        } else {
            IInstance copyOfInstance = ((Instance) instanceRef).databaseMe();
            instanceView.session = session;
            instanceView.load(copyOfInstance);

            if (processMapList != null && processMapList.getTitle() != null) {
                instanceView.getInstanceView().getInstanceNameChanger().setInstanceName(title);
                instanceView.getInstanceView().getInstanceNameChanger().change();
                instanceView.getInstanceView().setInstanceName(title);
            }

            copyOfInstance.getMetaworksContext().setWhen("blinking");
            copyOfInstance.getMetaworksContext().setWhere(Instance.WHERE_INSTANCELIST);

            // 주제 제목 설정
            if (copyOfInstance.getTopicId() != null) {
                TopicNode topic = new TopicNode();
                topic.setId(copyOfInstance.getTopicId());
                topic.copyFrom(topic.databaseMe());
                copyOfInstance.setTopicName(topic.getName());
            }

            HashMap<String, String> notiUsers = this.processNoti(copyOfInstance);

            //2014.11.25 ik
            if (Login.getSessionIdWithCompany(session.getEmployee().getGlobalCom()) != null) {
                notiUsers.putAll(Login.getSessionIdWithCompany(session.getEmployee().getGlobalCom()));
            }

            TodoBadge todoBadge = new TodoBadge();
            todoBadge.loader = true;
            // 자기자신의 인스턴스를 변경하고, todo count 를 refresh
            MetaworksRemoteService.pushTargetClientObjects(Login.getSessionIdWithUserId(session.getUser().getUserId()), new Object[]{new InstanceListener(copyOfInstance), new Refresh(todoBadge, true)});

            MetaworksRemoteService.pushClientObjectsFiltered(
                    new OtherSessionFilter(notiUsers, session.getUser().getUserId()),
                    new Object[]{new InstanceListener(InstanceListener.COMMAND_APPEND, copyOfInstance)});

            Notification notification = new Notification();
            notification.session = session;
            notiUsers = notification.findInstanceNotiUser(instanceRef.getInstId().toString());

            // follower 될 사용자의 todo count 를 refresh
            MetaworksRemoteService.pushClientObjectsFiltered(
                    new OtherSessionFilter(notiUsers, session.getUser().getUserId()),
                    new Object[]{new Refresh(todoBadge, true)});

            if ("simulator".equals(this.getMetaworksContext().getWhere())) {
                return new Object[]{new ToEvent(ServiceMethodContext.TARGET_OPENER, EventContext.EVENT_CLOSE), new ModalWindow(new org.metaworks.widget.Label("<h4>Successfully initiated a simulation instance. <br>Please go to workspace to work with the practice instance.</h4>"), 500, 200, "Simulator")};
            }

            return new Object[]{new ToEvent(ServiceMethodContext.TARGET_OPENER, EventContext.EVENT_CLOSE), new Refresh(instanceView), new Remover(new ModalWindow())};
        }
    }

    public HashMap<String, String> processNoti(IInstance instanceRef) throws Exception {
        /**
         *  === noti push 부분 ===
         */
        HashMap<String, String> notiUsers = new HashMap<String, String>();
        Notification notification = new Notification();
        notification.session = session;
        notiUsers = notification.findInstanceNotiUser(instanceRef.getInstId().toString());
        if (instanceRef.getTopicId() != null) {
            HashMap<String, String> topicNotiUsers = notification.findTopicNotiUser(instanceRef.getTopicId());
            Iterator<String> iterator = topicNotiUsers.keySet().iterator();
            while (iterator.hasNext()) {
                String followerUserId = (String) iterator.next();
                notiUsers.put(followerUserId, topicNotiUsers.get(followerUserId));
            }
        }

        // noti 저장
        Iterator<String> iterator = notiUsers.keySet().iterator();
        while (iterator.hasNext()) {
            String followerUserId = (String) iterator.next();
            Notification noti = new Notification();
            INotiSetting notiSetting = new NotiSetting();
            INotiSetting findResult = notiSetting.findByUserId(followerUserId);
            if (!findResult.next() || findResult.isWriteInstance()) {
                noti.setNotiId(System.currentTimeMillis()); //TODO: why generated is hard to use
                noti.setUserId(followerUserId);
                noti.setActorId(session.getUser().getUserId());
                noti.setConfirm(false);
                noti.setInputDate(Calendar.getInstance().getTime());
                noti.setInstId(instanceRef.getInstId());
                noti.setActAbstract(session.getUser().getName() + " completed workItem : " + instanceRef.getName());
                noti.add(instanceRef);
            }
        }

        MetaworksRemoteService.pushTargetScriptFiltered(new AllSessionFilter(notiUsers),
                "mw3.getAutowiredObject('" + NotificationBadge.class.getName() + "').refresh",
                new Object[]{});

        return notiUsers;
    }

    public Object[] processListFilter() throws Exception {
        InstanceListPanel instanceListPanel = Perspective.loadInstanceList(session, Perspective.MODE_PROCESS, Perspective.TYPE_NEWSFEED, this.getDefId());

        Locale locale = new Locale(session);
        locale.load();

        String title = locale.getString("$Process") + " - " + this.getName();
        session.setWindowTitle(title);

        return new Object[]{session, new ListPanel(instanceListPanel, new ProcessInfo(session))};
    }

    protected void afterInstantiation(Instance instanceRef) throws Exception {
        if (session != null && session.getEmployee() != null) {
            ((Instance) instanceRef).databaseMe().setInitiator(session.user);
            ((Instance) instanceRef).databaseMe().setInitComCd(session.getEmployee().getGlobalCom());
        }
        if (session != null && "topic".equalsIgnoreCase(session.getLastPerspecteMode())) {
            ((Instance) instanceRef).databaseMe().setTopicId(session.getLastSelectedItem());
        }
        if (newInstancePanel != null) {
            if (newInstancePanel.getSecurityLevel().getSelected() == null) {
                newInstancePanel.getSecurityLevel().setSelected("0");
            }
            ((Instance) instanceRef).databaseMe().setSecuopt("" + newInstancePanel.getSecurityLevel().getSelected());
            //((Instance)instanceRef).flushDatabaseMe();
        }
		
		/*
		if(newInstancePanel.dueDate != null){
			((Instance)instanceRef).databaseMe().setDueDate(newInstancePanel.getDueDate());
		}
		*/

        ((Instance) instanceRef).databaseMe().setName(this.getName() + " " + instanceRef.getInstId().toString());

        ((Instance) instanceRef).flushDatabaseMe();
    }

    public Object startWithRoleMapping() throws Exception {
        return _run();
    }


    public Object simulate() throws Exception {
        getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);
        getMetaworksContext().setWhere("simulator");

        return _run();
    }

    private Object _run() throws Exception {
        getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);
        getMetaworksContext().setHow("run");

        this.setRoleMappingPanel(new RoleMappingPanel(processManager, this.getDefId(), session));

        Popup popup = new Popup(580, 270);
        popup.setPanel(this);
        popup.setName("담당자 지정");

        MetaworksRemoteService.wrapReturn(popup);

        return this;
    }

}


