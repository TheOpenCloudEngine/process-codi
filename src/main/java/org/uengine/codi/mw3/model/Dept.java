package org.uengine.codi.mw3.model;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.directwebremoting.Browser;
import org.directwebremoting.ScriptSessions;
import org.metaworks.EventContext;
import org.metaworks.MetaworksContext;
import org.metaworks.MetaworksException;
import org.metaworks.Refresh;
import org.metaworks.Remover;
import org.metaworks.ServiceMethodContext;
import org.metaworks.ToAppend;
import org.metaworks.ToEvent;
import org.metaworks.ToOpener;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.dao.Database;
import org.metaworks.dao.TransactionContext;
import org.metaworks.dao.UniqueKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.uengine.codi.mw3.Login;
import org.uengine.codi.mw3.admin.AdminEastPanel;
import org.uengine.processmanager.ProcessManagerRemote;

public class Dept extends Database<IDept> implements IDept {
	String partCode;
	String partName;
	String parentPartCode;
	String isDeleted;
	String description;
	
	public Dept(String partCode){
		this.partCode = partCode;
	}
	
	public Dept(){
		setIsDeleted("0");
		
		setChildren(new DeptList());
		setDeptEmployee(new EmployeeList());
	}
	
	public String getPartCode() {
		return partCode;
	}

	public void setPartCode(String partCode) {
		this.partCode = partCode;
	}

	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public String getParent_PartCode() {
		return parentPartCode;
	}

	public void setParent_PartCode(String parentPartCode) {
		this.parentPartCode = parentPartCode;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

//	@Override
//	public String getComCode() {
//		// TODO Auto-generated method stub
//		return this.comCode;
//	}
//
//	@Override
//	public void setComCode(String comCode) {
//		this.comCode = comCode;
//	}
	
	String globalCom;
	public String getGlobalCom() {
		return globalCom;
	}

	public void setGlobalCom(String globalCom){
		this.globalCom = globalCom;
	}

	PortraitImageFile logoFile;
		public PortraitImageFile getLogoFile() {
			return logoFile;
		}
		public void setLogoFile(PortraitImageFile logoFile) {
			this.logoFile = logoFile;
		}

	boolean selected;
		public boolean getSelected() throws Exception {
			return selected;
		}
		public void setSelected(boolean value) throws Exception {
			selected = value;
		}
	boolean followed;
		public boolean isFollowed() {
			return followed;
		}
		public void setFollowed(boolean followed) {
			this.followed = followed;
		}
		
	String deptPath;
		public String getDeptPath() {
			return deptPath;
		}
		public void setDeptPath(String deptPath) {
			this.deptPath = deptPath;
		}

	DeptList children;
		public DeptList getChildren() {
			return children;
		}
		public void setChildren(DeptList children) {
			this.children = children;
		}

	EmployeeList deptEmployee;

	@Override
	public EmployeeList getDeptEmployee() {
		return deptEmployee;
	}

	@Override
	public void setDeptEmployee(EmployeeList deptEmployee) {
		this.deptEmployee = deptEmployee;
	}
	
	@Override
	public IDept load() throws Exception {
		if (getPartCode() != null) {
			IDept dept = (IDept) databaseMe();
			return dept;
		}
		return null;
	}

	
	public IDept findByCode() throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("select * from partTable ");
		sb.append("where partcode=?partcode ");
		sb.append("and isDeleted=0");
		
		IDept dao = null;
		
		try {
			dao = sql(sb.toString());
			dao.setPartCode(this.getPartCode());
			dao.select();
			
			if(!dao.next())
				dao = null;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return dao;
	}
	
	public IDept findByName() throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("select * from partTable ");
		sb.append("where partName=?partName ");
		sb.append("and globalCom=?globalCom ");
		sb.append("and isDeleted='0'");
		
		IDept dao = null;
		
		try {
			dao = sql(sb.toString());
			dao.setGlobalCom(session.getEmployee().getGlobalCom());
			dao.setPartName(this.getPartName());
			dao.select();
			
			if(!dao.next())
				dao = null;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return dao;
	}
	
	@Override
	public IDept findByGlobalCom() throws Exception {
		String sql = "select * from parttable where globalcom=?globalCom and parent_partCode is null and isDeleted='0' ";
		IDept deptList = sql(sql);
		deptList.setGlobalCom(this.getGlobalCom());
		deptList.select();
		deptList.setMetaworksContext(this.getMetaworksContext());
		
		return deptList;
	}

	@Override
	public IDept findTreeByGlobalCom(String globalCom) throws Exception {
		return findTreeByGlobalCom(globalCom, null);
	}
	public IDept findTreeByGlobalCom(String globalCom,String keyword) throws Exception {
		String sql = "select * from parttable where globalcom=?globalCom and isDeleted='0' ";
		
		if(keyword != null && keyword.trim().length() > 0)
			sql += "   AND partName LIKE ?partName";
		
		IDept deptList = sql(sql);
		deptList.setGlobalCom(globalCom);
		deptList.setPartName("%" + keyword + "%");
		deptList.select();
		deptList.setMetaworksContext(new MetaworksContext());
		deptList.getMetaworksContext().setWhen(MetaworksContext.WHEN_VIEW);
		if (deptList.size() == 0) {
			deptList = new Dept();
			deptList.setMetaworksContext(new MetaworksContext());
			deptList.getMetaworksContext().setWhen(MetaworksContext.WHEN_NEW);
		}
		return deptList;
	}
	
	public IDept findDeptForInstance(String instanceId, String keyword) throws Exception {
		
		StringBuffer sb = new StringBuffer();
		sb.append("select parttable.* , if(cnt > 0 , 1, 0 ) as followed, GetDeptPathAncestry(partcode, '->') as deptPath from ( ");
		
		sb.append("select pt.*  ");
		sb.append(" , (select count('x') from bpm_rolemapping rm   ");
		sb.append("    where pt.partcode = rm.endpoint ");
		sb.append("    and rm.rootinstid=?instanceId ");
		sb.append("   ) ");
		sb.append("   as cnt  ");
		sb.append("from parttable pt ");
		sb.append("where pt.globalcom=?globalCom ");
		sb.append("  and pt.isDeleted='0' ");
//		if(getPartCode() != null) {
//			sb.append("and pt.parent_partcode=?parent_PartCode ");
//		} else {
//			sb.append("and pt.parent_partcode is null ");
//		}
		if(keyword != null && keyword.trim().length() > 0)
			sb.append("   AND pt.partname LIKE ?deptname");
		
		sb.append("  ) parttable ");
		
		
		IDept childDeptList = sql(sb.toString());
		childDeptList.setGlobalCom(globalCom);
		childDeptList.setParent_PartCode(this.getParent_PartCode());
		childDeptList.set("instanceId", instanceId);
		childDeptList.set("deptname", "%" + keyword + "%");
		childDeptList.select();
		
		return childDeptList;
	}
	public IDept findDeptForTopic(String topicId, String keyword) throws Exception {
		
		StringBuffer sb = new StringBuffer();
		sb.append("select parttable.* , if(cnt > 0 , 1, 0 ) as followed , GetDeptPathAncestry(partcode, '->') as deptPath from ( ");
		
		sb.append("select pt.*  ");
		sb.append(" , (select count('x') from bpm_topicmapping tm   ");
		sb.append("    where tm.topicId=?topicId ");
		sb.append("    and tm.userid=pt.partcode ");
		sb.append("   ) ");
		sb.append("   as cnt  ");
		sb.append("from parttable pt ");
		sb.append("where pt.globalcom=?globalCom ");
		sb.append("  and pt.isDeleted='0' ");
//		if(getPartCode() != null) {
//			sb.append("and pt.parent_partcode=?parent_PartCode ");
//		} else {
//			sb.append("and pt.parent_partcode is null ");
//		}
		
		if(keyword != null && keyword.trim().length() > 0)
			sb.append("   AND pt.partname LIKE ?deptname");
		
		sb.append("  ) parttable ");
		
		
		IDept childDeptList = sql(sb.toString());
		childDeptList.setGlobalCom(globalCom);
		childDeptList.setParent_PartCode(this.getParent_PartCode());
		childDeptList.set("topicId", topicId);
		childDeptList.set("deptname", "%" + keyword + "%");
		childDeptList.select();
		
		return childDeptList;
	}

	@Override
	public IDept findChildren() throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("select * from parttable ");
		sb.append("where globalcom=?globalCom ");
		sb.append("  and isDeleted='0' ");
		
		if(getPartCode() != null) {
			sb.append("and parent_partcode=?parent_PartCode ");
		} else {
			sb.append("and parent_partcode is null ");
		}
		IDept childDeptList = sql(sb.toString());
		childDeptList.setGlobalCom(getGlobalCom());
		
		if(getPartCode() != null) {
			childDeptList.setParent_PartCode(getPartCode());
		}
		childDeptList.select();
		childDeptList.setMetaworksContext(this.getMetaworksContext());
		
		System.out.println(sb.toString());
		
		return childDeptList;
	}
	
	@Override
	public IDept findRootDeptByGlobalCom(String globalCom) throws Exception{
		setGlobalCom(globalCom);
		setPartName(globalCom);
		setPartCode(null);
		drillDown();
		return this;
	}

	@Override
	public void drillDown() throws Exception {
		setSelected(!getSelected());
		
		if (!selected) {
			DeptList deptList = new DeptList();
			deptList.setId(this.getPartCode());
			
			setChildren(deptList);
			
//			EmployeeList employeeList = new EmployeeList();
//			employeeList.setId(this.getPartCode());
//			
//			setDeptEmployee(employeeList);
		} else {
			DeptList deptList = new DeptList();
			deptList.setMetaworksContext(this.getMetaworksContext());
			deptList.setId(this.getPartCode());
			deptList.setDept(this.findChildren());			
			setChildren(deptList);			
			
//			if(!("deptPicker".equals(this.getMetaworksContext().getWhere()))){
//				IEmployee employee = new Employee();
//				employee.setMetaworksContext(this.getMetaworksContext());
//				employee.getMetaworksContext().setHow("tree");
//				if( "addContact".equals(this.getMetaworksContext().getWhere()) ){
//					employee.getMetaworksContext().setWhere("addContact");
//				}else{
//					employee.getMetaworksContext().setWhere("navigator");
//				}
//				
//				EmployeeList employeeList = new EmployeeList();			
//				employeeList.setMetaworksContext(this.getMetaworksContext());
//				employeeList.setId(this.getPartCode());
//				employeeList.setEmployee(employee.findByDept(this));
//				
//				setDeptEmployee(employeeList);
//			}
		}
	}
		
	public Object[] loadDeptList() throws Exception {
		InstanceListPanel instanceListPanel = Perspective.loadInstanceList(session, Perspective.MODE_DEPT, Perspective.TYPE_NEWSFEED, getPartCode());
		
		Locale locale = new Locale(session);
		locale.load();

		String title = locale.getString("$Dept") + " - " + getPartName();
		session.setWindowTitle(title);
		
		DeptInfo deptInfo = new DeptInfo(session, Perspective.TYPE_NEWSFEED);
		
		return new Object[]{session,  new ListPanel(instanceListPanel, deptInfo)};
	}

	@Override
	public Popup editDeptInfo() throws Exception {
		Dept dept = new Dept();
		dept.setPartCode(this.getPartCode());
		dept.copyFrom(dept.databaseMe());
		
		dept.getMetaworksContext().setWhere("admin");
		dept.getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);
		dept.setLogoFile(new PortraitImageFile());
		
		Popup popup = new Popup();
		popup.setPanel(dept);
		
		return popup;		
	}

	@AutowiredFromClient
	public Session session;

	@Override
	public Object[] saveDeptInfo() throws Exception {

		this.getMetaworksContext().setHow("tree");
		
		if(this.getLogoFile().getFileTransfer() != null &&
				this.getLogoFile().getFilename() != null && 
				this.getLogoFile().getFilename().length() > 0){			

			if( this.getLogoFile().getFileTransfer().getMimeType() != null  && !this.getLogoFile().getFileTransfer().getMimeType().startsWith("image")){
				throw new MetaworksException("$OnlyImageFileCanUpload");
			}else{
				this.getLogoFile().setEmpCode("dept_"+this.getPartCode());
				this.getLogoFile().upload();
			}
			
		}
		
		if (getMetaworksContext().getWhen().equals(MetaworksContext.WHEN_NEW)) {
			//그룹 중복 검사
			IDept findDept = this.findByName();
			if(findDept != null)
				throw new Exception("$DuplicateName");
			
			this.getMetaworksContext().setWhen(MetaworksContext.WHEN_VIEW);
			
			this.setGlobalCom(session.getCompany().getComCode());
			this.setIsDeleted("0");		
			
			Map options = new HashMap();
			options.put("onlySequenceTable", true);
			
			Long genKey = UniqueKeyGenerator.issueKey("PartTable", options, TransactionContext.getThreadLocalInstance());
			
			this.setPartCode(genKey.toString());
			
			createDatabaseMe();
			flushDatabaseMe();
			
			//deptFollow에 dept생성자 추가.
			Employee emp = new Employee();
			emp.setEmpCode(session.getEmployee().getEmpCode());
			emp.copyFrom(emp.databaseMe());
			emp.setPartCode(this.getPartCode());
			emp.syncToDatabaseMe();
			emp.flushDatabaseMe();
			
			
			InstanceListPanel instanceListPanel = Perspective.loadInstanceList(session, Perspective.MODE_DEPT, Perspective.TYPE_NEWSFEED, getPartCode());

			Locale locale = new Locale(session);
			locale.load();
			String title = locale.getString("$Dept") + " : " + this.getPartName(); 
			
			session.setWindowTitle(title);
			
			return new Object[]{new Refresh(session), 
					 			new Refresh(new ListPanel(instanceListPanel, new DeptInfo(session, Perspective.TYPE_NEWSFEED))),  
					 			new ToEvent(new DeptPerspective(), EventContext.EVENT_CHANGE),
					 			new ToEvent(ServiceMethodContext.TARGET_SELF, EventContext.EVENT_CLOSE)};
			
			
			//returnObject = MetaworksUtil.putObjectArray(returnObject, new ToEvent(new DeptPerspective(), EventContext.EVENT_CHANGE), false);
			//returnObject = MetaworksUtil.putObjectArray(returnObject, new ToEvent(ServiceMethodContext.TARGET_SELF, EventContext.EVENT_CLOSE), false);
			
			//returnObject[returnObj.length+1] = new Remover(new Popup());
			//return returnObject;

//			return null;
			
			//drillDown
			
			//return new Object[] { returnValueDept, resultSaveDeptPanel };
			
			//returnValueDept = findRefreshableParentDept();
		} else {
			this.getMetaworksContext().setWhen(MetaworksContext.WHEN_VIEW);

			Locale locale = new Locale(session);
			locale.load();
			String title = locale.getString("$Dept") + " : " + this.getPartName(); 
			
			session.setWindowTitle(title);
			
			DeptInfo deptInfo = new DeptInfo(session, Perspective.TYPE_NEWSFEED);
			
			syncToDatabaseMe();
			flushDatabaseMe();
			
			return new Object[]{new Refresh(session), 
								new Refresh(deptInfo), 
								new ToEvent(new DeptPerspective(), EventContext.EVENT_CHANGE), 
								new ToEvent(ServiceMethodContext.TARGET_SELF, EventContext.EVENT_CLOSE)};
		}
		//}
		// TODO execute drillDown()
		// returnValueDept.drillDown();
		
		//return new Object[] { returnValueDept, resultSaveDeptPanel };
	}
	
	public void notiToCompany() throws Exception{
		Notification noti = new Notification();
		INotiSetting notiSetting = new NotiSetting();
		Instance instance = new Instance();
		noti.session = session;
		instance = this.createWorkItem();
		
		Employee employee = new Employee();
		employee.setEmpCode(session.getEmployee().getEmpCode());
		employee.copyFrom(employee.databaseMe());
		IEmployee findResult = employee.findByGlobalCom(employee.getGlobalCom());
		INotiSetting findNotiSetting;
		Employee codi = new Employee();
		codi.setEmpCode("0");
		
		while(findResult.next()){
			findNotiSetting = notiSetting.findByUserId(findResult.getEmpCode());
			if(findNotiSetting.next()){
				if(findNotiSetting.isModiOrgan()){
					noti.setNotiId(System.currentTimeMillis()); //TODO: why generated is hard to use
					noti.setUserId(findResult.getEmpCode());
					noti.setActorId(session.getEmployee().getEmpName());
					noti.setConfirm(false);
					noti.setInstId(instance.getInstId());
					noti.setInputDate(Calendar.getInstance().getTime());
					if(MetaworksContext.WHEN_VIEW.equals(this.getMetaworksContext().getWhen())){
						noti.setActAbstract(session.getUser().getName() + "님이 부서 " + this.getPartName() + "를 생성하였습니다.");
					}
					else if(MetaworksContext.WHEN_EDIT.equals(this.getMetaworksContext().getWhen())){
						noti.setActAbstract(session.getUser().getName() + "님이  " + this.getPartName() + " 부서를 제거하였습니다.");
					}
					
		
					//워크아이템에서 노티를 추가할때와 동일한 로직을 수행하도록 변경
			//			noti.createDatabaseMe();
			//			noti.flushDatabaseMe();
					
					noti.add(instance);
				
					String followerSessionId = Login.getSessionIdWithUserId(employee.getEmpCode());
					
					try{
						//NEW WAY IS GOOD
						Browser.withSession(followerSessionId, new Runnable(){
			
							@Override
							public void run() {
								//refresh notification badge
								ScriptSessions.addFunctionCall("mw3.getAutowiredObject('" + NotificationBadge.class.getName() + "').refresh", new Object[]{});
							}
							
						});
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		}				
	}
	
	public Instance createWorkItem() throws Exception{
		Employee representiveMailEmp = new Employee();
		representiveMailEmp.setEmpCode("0");
		
		
		IEmployee repMailEmp = representiveMailEmp.findMe();
		
		
		SystemWorkItem comment = new SystemWorkItem();
		comment.processManager = processManager;
		comment.getMetaworksContext().setWhen(MetaworksContext.WHEN_NEW);
		
		User theFirstWriter;
		theFirstWriter = new User();
		theFirstWriter.setName(repMailEmp.getEmpName());
		
		comment.setWriter(theFirstWriter);
		if(MetaworksContext.WHEN_VIEW.equals(this.getMetaworksContext().getWhen())){
			comment.setSystemMessage(session.getUser().getName() + "님이 부서 " + this.getPartName() + "를 생성하였습니다.");
		}
		else if(MetaworksContext.WHEN_EDIT.equals(this.getMetaworksContext().getWhen())){
			comment.setSystemMessage(session.getUser().getName() + "님이  " + this.getPartName() + " 부서를 제거하였습니다.");
		}
		
		comment.setStartDate(new Date());
		
		comment.session = session;
		comment.add();
		
		Instance instance = new Instance();
		instance.setInstId(comment.getInstId());
		instance.copyFrom(instance.databaseMe());
		
		return instance;
	}

	private IDept findRefreshableParentDept() throws Exception {
		IDept returnValueDept;
		Dept parentDept = new Dept();
		if (getParent_PartCode() != null) {
			parentDept.setPartCode(getParent_PartCode());
			returnValueDept = parentDept.databaseMe();
		} else {
			returnValueDept = parentDept.findRootDeptByGlobalCom(getGlobalCom());
		}
		returnValueDept.setMetaworksContext(getMetaworksContext());
		return returnValueDept;
	}

	@Override
	public Object[] deleteDept() throws Exception {
		Dept dept = new Dept();
		dept.setPartCode(this.getPartCode());		
		dept.copyFrom(dept.databaseMe());
		dept.setIsDeleted("1");
		this.setPartName(dept.getPartName());
		
/*		if( (this.getChildren() != null && this.getChildren().size() > 0) || (this.getDeptEmployee() != null && this.getDeptEmployee().size() > 0))
			throw new Exception("�섏쐞 �몃뱶媛�議댁옱�섎㈃ ��젣�����놁뒿�덈떎.");*/
				
		dept.syncToDatabaseMe();
		dept.flushDatabaseMe();
		
		this.getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);
		
		//this.notiToCompany();
		
		return new Object[]{new Refresh(new InstanceListPanel()), new Remover(dept)};		
	}

	
	@Override
	public Object[] restoreDept() throws Exception {
		setIsDeleted("0");
		return saveDeleteFlag();
	}

	private Object[] saveDeleteFlag() throws Exception {
		syncToDatabaseMe();
		IDept returnValueDept = findRefreshableParentDept();

		AdminEastPanel resultSaveDeptPanel = new AdminEastPanel();
		resultSaveDeptPanel.setContent("save successfully.");
		return new Object[] { returnValueDept, resultSaveDeptPanel };
	}


	@Override
	public Object addNewChildDept() throws Exception {
		IDept newDept = new Dept();
		newDept.setParent_PartCode(getPartCode());
		newDept.getMetaworksContext().setWhen(MetaworksContext.WHEN_NEW);
		newDept.getMetaworksContext().setWhere("admin");
		newDept.setLogoFile(new PortraitImageFile());
		
		Popup popup = new Popup();
		popup.setPanel(newDept);
		
		return popup;
	}



	public Object[] subscribe() throws Exception {
		
		Employee employee = new Employee();
		employee.setEmpCode(session.getEmployee().getEmpCode());
		employee.copyFrom(employee.databaseMe());
		
		if(employee.getPartCode() != null && employee.getPartCode().equals(this.getPartCode())){
			return null;
		}else{
			String prevPartCode = employee.getPartCode();
								
			employee.setPartCode(this.getPartCode());
			employee.syncToDatabaseMe();
			employee.flushDatabaseMe();
			employee.setMetaworksContext(this.getMetaworksContext());
			employee.getMetaworksContext().setHow("tree");
			
			EmployeeList employeeList = new EmployeeList();
			employeeList.setId(this.getPartCode());

			Dept findDept = null;
			try{
				findDept = new Dept();			
				findDept.setPartCode(prevPartCode);			
				findDept.copyFrom(findDept.databaseMe());
				findDept.setMetaworksContext(this.getMetaworksContext());
				findDept.drillDown();
			}catch(Exception e){}

			if(findDept!=null){				
				return new Object[]{new Refresh(findDept), new Refresh(session), new ToAppend(employeeList, employee)};
			}else{
				return new Object[]{new Refresh(session), new ToAppend(employeeList, employee)};
				
			}
		}
		
	}
	
	@Override
	public Object[] addFollower() throws Exception {
		// use clipboard for addFollower
		Object clipboard = session.getClipboard();
		
		if(clipboard instanceof Follower){
			Follower follower = (Follower)clipboard;
			follower.session = session;
			follower.put(this);
		}
		this.setFollowed(true);
		
		// refresh self
		return new Object[]{new Refresh(this, false, true)};
	}
	@Override
	public Object[] removeFollower() throws Exception {
		
		// use clipboard for removeFollower
		Object clipboard = session.getClipboard();
		
		if(clipboard instanceof Follower){
			Follower follower = (Follower)clipboard;
			follower.session = session;
			follower.delegate(this);
		}
		this.setFollowed(false);
		// change context for remover 'removeFollower' button
		this.getMetaworksContext().setWhere(WHERE_EVER);
		
		// refresh self
		return new Object[]{new Refresh(this, false, true)};
	}
	
	@Override
	public Popup detail() throws Exception {
		int with = 435;
		int height = 275;
		
		this.load();
		this.getMetaworksContext().setHow(IUser.HOW_INFO);

		Popup popup = new Popup(with, height);
		popup.setPanel(this);

		return popup;
	}
	@Override
	public Popup openPicker() throws Exception {
		DeptPicker deptPicker = new DeptPicker(session.getCompany().getComCode());
		deptPicker.load();
		
		return new Popup(deptPicker);
	}
	
	@Override
	public Object[] pickup() throws Exception {
		Dept dept = new Dept();
		dept.setPartCode(this.getPartCode());
		dept.copyFrom(dept.databaseMe());
		
		dept.getMetaworksContext().setHow("picker");
		dept.getMetaworksContext().setWhen("edit");
		
		return new Object[] {new ToOpener(dept), new ToEvent(ServiceMethodContext.TARGET_OPENER, EventContext.EVENT_CHANGE), new ToEvent(ServiceMethodContext.TARGET_SELF, EventContext.EVENT_CLOSE)};
	}
	
	@Override
	public Object[] drop() throws Exception {
		Object clipboard = session.getClipboard();
			IEmployee employeeInClipboard = (IEmployee) clipboard;
			
			Employee locatorForEmployeeInClipboard = new Employee();
			locatorForEmployeeInClipboard.setEmpCode(employeeInClipboard.getEmpCode());
			
			locatorForEmployeeInClipboard.databaseMe().setPartCode(this.getPartCode());
			locatorForEmployeeInClipboard.flushDatabaseMe();
			
			return new Object[]{new Remover(employeeInClipboard)};
	}
	
	public IFollower findFollowers() throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("select emptable.empcode as endpoint, emptable.empname as resname, emptable.partcode parentId, '" + Follower.TYPE_DEPT + "' parentType");
		sql.append("  from emptable LEFT OUTER JOIN PARTTABLE");
		sql.append("    on emptable.partcode=PARTTABLE.partcode ");
		sql.append(" where emptable.partcode=?partCode ");
		
		IFollower follower = (IFollower) Database.sql(IFollower.class, sql.toString());
		follower.set("partCode", this.getPartCode());
		follower.select();	
		
		return follower;
	}
	
	@Autowired
	public ProcessManagerRemote processManager;
}
