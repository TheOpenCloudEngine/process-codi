package org.uengine.codi.mw3.model;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.directwebremoting.Browser;
import org.directwebremoting.ScriptSessions;
import org.metaworks.Forward;
import org.metaworks.MetaworksContext;
import org.metaworks.MetaworksException;
import org.metaworks.Refresh;
import org.metaworks.Remover;
import org.metaworks.ServiceMethodContext;
import org.metaworks.ToOpener;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.component.SelectBox;
import org.metaworks.dao.DAOFactory;
import org.metaworks.dao.Database;
import org.metaworks.dao.KeyGeneratorDAO;
import org.metaworks.dao.TransactionContext;
import org.metaworks.website.MetaworksFile;
import org.metaworks.widget.ModalWindow;
import org.springframework.beans.factory.annotation.Autowired;
import org.uengine.cloud.saasfier.TenantURL;
import org.uengine.codi.mw3.Login;
import org.uengine.codi.mw3.StartCodi;
import org.uengine.codi.mw3.admin.TopPanel;
import org.uengine.codi.mw3.common.MainPanel;
import org.uengine.codi.mw3.knowledge.TopicMapping;
import org.uengine.codi.mw3.knowledge.WfNode;
import org.uengine.processmanager.ProcessManagerRemote;

public class Employee extends Database<IEmployee> implements IEmployee {

	@Autowired
	public ProcessManagerRemote processManager;

	@AutowiredFromClient
	public Locale localeManager;
	
	/*
	IUser user;	
		public IUser getUser() {
			return user;
		}
		public void setUser(IUser user) {
			this.user = user;
		}
	*/
	String authKey;
		public String getAuthKey() {
			return authKey;
		}
		public void setAuthKey(String authKey) {
			this.authKey = authKey;
		}

	boolean validEmail;
		public boolean isValidEmail() {
			return validEmail;
		}
		public void setValidEmail(boolean validEmail) {
			this.validEmail = validEmail;
		}	

	String empCode;
		public String getEmpCode() {
			return empCode;
		}
		public void setEmpCode(String empCode) {
			this.empCode = empCode;
		}
		
	String empName;
		public String getEmpName() {
			return empName;
		}
	
		public void setEmpName(String empName) {
			this.empName = empName;
		}
		
	String facebookId;
		public String getFacebookId() {
			return facebookId;
		}
		public void setFacebookId(String facebookId) {
			this.facebookId = facebookId;
		}

	IDept dept;
		public IDept getDept() {
			return dept;
		}
		public void setDept(IDept dept) {
			this.dept = dept;
		}

	String partCode;
		public String getPartCode() {
			return partCode;
		}	
		public void setPartCode(String partCode) {
			this.partCode = partCode;
		}
	
	String mood;
		public String getMood() {
			return mood;
		}
		public void setMood(String mood) {
			this.mood = mood;
		}
		
	String preferMob;
		public String getPreferMob() {
			return preferMob;
		}
		public void setPreferMob(String preferMob) {
			this.preferMob = preferMob;
		}

	String preferUX;
		public String getPreferUX() {
			return preferUX;
		}
		public void setPreferUX(String preferUX) {
			this.preferUX = preferUX;
		}

	String password;
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}

	String confirmPassword;
		public String getConfirmPassword() {
			return confirmPassword;
		}
		public void setConfirmPassword(String confirmPassword) {
			this.confirmPassword = confirmPassword;
		}
	
	boolean isAdmin;
		public boolean getIsAdmin() {
			return isAdmin;
		}
		public void setIsAdmin(boolean value) {
			this.isAdmin = value;
		}

	String jikName;
		public String getJikName() {
			return jikName;
		}
		public void setJikName(String jikName) {
			this.jikName = jikName;
		}

	transient String partName;
		public String getPartName() {
			return partName;
		}
		public void setPartName(String partName) {
			this.partName = partName;
		}

	String globalCom;
		public String getGlobalCom() {
			return globalCom;
		}
		public void setGlobalCom(String globalCom) {
			this.globalCom = globalCom;
		}
	
	String isDeleted;
		public String getIsDeleted() {
			return this.isDeleted;
		}
		public void setIsDeleted(String deleted) {
			this.isDeleted = deleted;
		}

	String mobileNo;
		public String getMobileNo() {
			return mobileNo;
		}
		public void setMobileNo(String mobileNo) {
			this.mobileNo = mobileNo;
		}

	String email;
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}

	String locale;
		public String getLocale() {
			return locale;
		}
		public void setLocale(String locale) {
			this.locale = locale;
		}

	PortraitImageFile imageFile;		
		public PortraitImageFile getImageFile() {
			return imageFile;
		}
		public void setImageFile(PortraitImageFile imageFile) {
			this.imageFile = imageFile;
		}

	boolean approved;
		public boolean isApproved() {
			return approved;
		}
		public void setApproved(boolean approved) {
			this.approved = approved;
		}
		
	boolean guest;
		public boolean isGuest() {
			return guest;
		}
		public void setGuest(boolean guest) {
			this.guest = guest;
		}
	
		
	boolean mailNoti;
		public boolean isMailNoti() {
			return mailNoti;
		}
		public void setMailNoti(boolean mailNoti) {
			this.mailNoti = mailNoti;
		}
		
	String inviteUser;
		public String getInviteUser() {
			return inviteUser;
		}
		public void setInviteUser(String inviteUser) {
			this.inviteUser = inviteUser;
		}
	
	@Override
	public IEmployee load() throws Exception {
		String errorMessage = null;
		if (getEmail() != null) {
			IEmployee emp = (IEmployee) findByEmail();
			
				if(emp.getIsDeleted() != null && emp.getIsDeleted().equals("1")) {
					errorMessage = "<font color=blue>There's no such ID. Please subscribe.</font>";
					
					}
				if(getPassword().equals(emp.getPassword())) {
						Employee employee = new Employee();
						employee.copyFrom(emp);
						emp = employee.findMe();				
		
						getMetaworksContext().setWhen(MetaworksContext.WHEN_VIEW);
						return emp;				
				}else {
						errorMessage = "<font color=blue>Wrong User or Password! forgot?</font>";
					}
		} else {
			errorMessage = "<font color=blue>There's no such ID. Please subscribe.</font>";
		}
		
		throw new Exception(errorMessage);		
	}

	@Override
	public IEmployee findMe() throws Exception {
		Employee employee = new Employee();
		
		StringBuffer sb = new StringBuffer();
		sb.append("select emptable.*, PARTTABLE.PARTNAME from ");
		sb.append("emptable LEFT OUTER JOIN PARTTABLE on emptable.partcode=PARTTABLE.partcode ");
		sb.append("where emptable.empcode=?empcode ");
		

		IEmployee findEmployee = (IEmployee) sql(sb.toString());
		findEmployee.set("empcode", this.getEmpCode());
		findEmployee.select();
		if (findEmployee.next()) {
			employee.copyFrom(findEmployee);
			employee.getDept().getMetaworksContext().setHow("picker");
			/*
			Dept dept = new Dept();
			
			dept.getMetaworksContext().setHow("info");
			dept.setPartCode(employee.getDept().getPartCode());
			dept.setPartName(employee.getDept().getPartName());
			
			employee.setDept(dept);
			*/
		}
		return employee;

	}
	@Override
	public IEmployee findMeByEmpName() throws Exception {
		
		StringBuffer sb = new StringBuffer();
		sb.append("select emptable.*, PARTTABLE.PARTNAME from ");
		sb.append("emptable LEFT OUTER JOIN PARTTABLE on emptable.partcode=PARTTABLE.partcode ");
		sb.append("where emptable.EMPNAME like ?empName ");
		
		IEmployee findEmployee = (IEmployee) sql(sb.toString());
		findEmployee.set("empName", "%" + getEmpName() + "%" );
		findEmployee.select();
		findEmployee.setMetaworksContext(this.getMetaworksContext());
		
		return findEmployee;
	}
	@Override
	public IEmployee findByDept(Dept dept) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("select emptable.*, PARTTABLE.PARTNAME from ");
		sb.append("emptable LEFT OUTER JOIN PARTTABLE on emptable.partcode=PARTTABLE.partcode ");
		if (dept.getPartCode() != null) {
			sb.append("where emptable.partcode=?partCode ");
		} else {
			sb.append("where emptable.partcode is null ");
		}
		sb.append("and emptable.globalcom=?globalCom ");
		IEmployee deptEmployee = sql(sb.toString());
		if (dept.getPartCode() != null) {
			deptEmployee.setPartCode(dept.getPartCode());
		}
		deptEmployee.setGlobalCom(dept.getGlobalCom());
		deptEmployee.select();
		deptEmployee.setMetaworksContext(this.getMetaworksContext());
		
		return deptEmployee;
	}

	@Override
	public IEmployee findByDeptOther(String empCode) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("select * from emptable e");
		sb.append(" where e.globalcom=?globalCom");
		sb.append(" and e.empcode not in");
		sb.append(" (select c.friendid as empcode from contact c");
		sb.append(" where c.userId=?empCode)");
		sb.append(" and e.empcode!=?empCode");
				
		IEmployee deptEmployee = sql(sb.toString());
		deptEmployee.setGlobalCom(this.getGlobalCom());
		deptEmployee.setEmpCode(empCode);
		deptEmployee.select();
		deptEmployee.setMetaworksContext(this.getMetaworksContext());
		
		return deptEmployee;
	}
	
	
	@Override
	public IEmployee findByRole(Role role) throws Exception {
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT a.*");
		sb.append("  FROM emptable a, roleUserTable b");
		sb.append(" WHERE a.empCode = b.empCode");
		sb.append("   AND a.isDeleted=?isDeleted");
		sb.append("   AND b.roleCode=?roleCode");
		
		IEmployee employee = sql(sb.toString());
		employee.setIsDeleted("0");
		employee.set("roleCode", role.getRoleCode());
		employee.select();
		employee.setMetaworksContext(this.getMetaworksContext());
		
		return employee;
	}
	
	@Override
	public IEmployee findByGlobalCom(String GlobalCom) throws Exception {
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * ");
		sb.append("FROM emptable ");
		sb.append("WHERE globalCom=?GlobalCom");
		
		IEmployee employee = sql(sb.toString());
		employee.set("GlobalCom", GlobalCom);
		employee.select();
		employee.setMetaworksContext(this.getMetaworksContext());
		
		return employee;
	}
	
	
	@AutowiredFromClient
	public Session session;

	@Override
	public Object[] loadOrganization() throws Exception {
		return Perspective.loadInstanceListPanel(session, "organization", getEmpCode(), "사원 : " + this.getEmpName() + "(" + this.getJikName() +")");
	}

	@Override
	public Object[] pickup() throws Exception {
		
		// TODO add Remover to return values
		IEmployee selectedEmployee = findMe();
		selectedEmployee.getMetaworksContext().setWhere("pickerCaller");
		return new Object[] {new ToOpener(selectedEmployee), new Remover(new Popup())};
	}
	
	@Override
	public Popup openPicker() {
		EmployeePicker employeePicker = new EmployeePicker(session.getCompany().getComCode());
		employeePicker.getMetaworksContext().setWhere("picker");
		return new Popup(employeePicker);
	}

	public IEmployee editEmployeeInfo() throws Exception {
		IEmployee employee = findMe();
		
		employee.getMetaworksContext().setHow("detail");
		employee.getMetaworksContext().setWhen(WHEN_EDIT);
		employee.getMetaworksContext().setWhere("inDetailWindow");

		employee.setImageFile(new PortraitImageFile());
		employee.getImageFile().getMetaworksContext().setWhen(WHEN_EDIT);
		
		return employee;
	}	
	
	public INotiSetting editNotiSetting() throws Exception {
		NotiSetting notiSetting = new NotiSetting();
		notiSetting.getMetaworksContext().setWhen(WHEN_EDIT);
		INotiSetting result = notiSetting.findByUserId(this.getEmpCode());
		if( result.next() ){
			notiSetting.copyFrom(result);
			SelectBox selectBox = new SelectBox();
			selectBox.add("OnDate", "1");
			selectBox.add("DayBefore", "2");
			selectBox.setSelected(notiSetting.getDefaultNotiTime());
			notiSetting.setSelectTime(selectBox);
		}else{
			notiSetting.setUserId(this.getEmpCode());
		}
		return notiSetting;
	}
	
	@Override
	public void saveMe() throws Exception {
		if(this.getGlobalCom() == null){
			String comAlias = Employee.extractTenantName(this.getEmail());
			boolean isAdmin = false;
			
			Company company = new Company();
			company.setAlias(comAlias);
			
			ICompany findCompany = company.findByAlias();
			if(findCompany == null){
				isAdmin = true;
				
				// not yet sign up tenant
				try {
					company.setComCode(company.createNewId());
					company.setComName(comAlias);

					findCompany = company.createDatabaseMe();
				} catch (Exception e) {
					e.printStackTrace();
					throw new MetaworksException(e.getMessage());
				}

				String tenantId = findCompany.getComCode();
				
				this.setGlobalCom(tenantId);
				
				addBasicTopics();
				addBasicProcess();
			}else{
				this.setGlobalCom(findCompany.getComCode());
			}
			
		}
		
		/*// 초대 받아 가입 할경우 서로 친구 추가됨.
		if(this.getInviteUser() != null){
			Contact newContact = new Contact();
			newContact.setUserId(this.getEmpCode());
			
			Employee recommender = new Employee();
			recommender.setEmpCode(this.getInviteUser());
			recommender.copyFrom(recommender.findMe());
			
			IUser newUser_ = new User();
			newUser_.setName(recommender.getEmpName());
			newUser_.setUserId(recommender.getEmpCode());
			newUser_.setNetwork("local");
			newContact.setFriendId(newUser_.getUserId());
			newContact.setFriend(newUser_);
			newContact.createDatabaseMe();
			
			newContact = new Contact();
			newContact.setUserId(recommender.getEmpCode());
			
			IUser me_ = new User();
			me_.setName(this.getEmpName());
			me_.setUserId(this.getEmpCode());
			me_.setNetwork("local");
			newContact.setFriendId(me_.getUserId());
			newContact.setFriend(me_);
			newContact.createDatabaseMe();
			
//			recommender.setInviteUser(null);
//			recommender.syncToDatabaseMe();
			
			this.setInviteUser(null);
		}*/
			
			
		this.setApproved(true);
		this.setMailNoti(this.isMailNoti());
		
//		if( localeManager.getLanguage() != null ){
//			this.setLocale(localeManager.getLanguage());
//		}
		
		// 한국어 만 지원하므로 강제 셋팅
		this.setLocale("ko");
		
		// TODO: 부서 딕셔너리 처리 필		
		if(getImageFile()!=null && getImageFile().getFileTransfer()!=null && getImageFile().getFileTransfer().getFilename()!=null && !"".equals(getImageFile().getFileTransfer().getFilename())){
			if( getImageFile().getFileTransfer().getMimeType() != null  && !getImageFile().getFileTransfer().getMimeType().startsWith("image")){
				throw new MetaworksException("$OnlyImageFileCanUpload");
			}else{
				getImageFile().setEmpCode(this.getEmpCode());
				getImageFile().upload();
			}
		}
		
		if(WHEN_NEW.equals(this.getMetaworksContext().getWhen())){
			this.setEmpCode(this.createNewId());
			createDatabaseMe();
		}else
			syncToDatabaseMe();
		
		flushDatabaseMe();
		
		NotiSetting notiSetting = new NotiSetting();
		notiSetting.setId(this.createNewNotiSettingId());
		notiSetting.setUserId(this.getEmpCode());
		notiSetting.setWriteInstance(true);
		
		notiSetting.createDatabaseMe();
		
		if(session != null){
			session.setEmployee(this);
			session.fillUserInfoToHttpSession();
		}
	}
	
	public void addBasicTopics() throws Exception{
		
		String topics[]={"영업","도서","연락처"};
		
		
		for(int i=0; i<topics.length; i++){
			WfNode wfNode = new WfNode();
						
			wfNode.setName(topics[i]);
			wfNode.setType("topic");
			wfNode.setSecuopt("0");
			wfNode.setParentId(this.getGlobalCom());	
			wfNode.setAuthorId(this.getEmpCode());		
			wfNode.setCompanyId(this.getGlobalCom());
			wfNode.createMe();
			
			TopicMapping tm = new TopicMapping();
			tm.setTopicId(wfNode.getId());
			tm.setUserId(this.getEmpCode());
			tm.setUserName(this.getEmpName());
			tm.getMetaworksContext().setWhen(this.getMetaworksContext().getWhen());
			
			tm.saveMe();
		}
	}
	
	public void addBasicProcess() throws Exception{
	
		String[] defId={
				    "buisinesstrip/process/buisinesstrip.process", 
				    "contact/process/contactregistration.process", 
				    "holiday/process/applholiday.process", 
				    "purchasing/process/purchasingreq.process", 
				    "sales/process/sales.process", 
				    "troubleticket/process/troubleticket.process",
//				    "spreadSheet/SpreadSheet.process",
//				    "mindmap/MindMap.process",
//				    "ganttchart/GanttChart.process",
//				    "devgoods/process/devgoods.process"
				    };
				  
		String[] name={
				    "출장", 
				    "연락처", 
				    "휴가", 
				    "구매", 
				    "영업", 
				    "클레임",
//				    "스프레드시트",
//				    "마인드맵",
//				    "간트차트",
//				    "문서"
				    };
		
		String basePath = "default/process/";
		String[] upLoadedPath={
				basePath + "buisinesstrip.png",
				basePath + "contact.png",
				basePath + "holiday.png",
				basePath + "purchasing.png",
				basePath + "sales.png",
				basePath + "troubleticket.png",
//				basePath + "spreadSheet.png",
//				basePath + "mindmap.png",
//				basePath + "ganttchart.png",
//				basePath + "devgoods.png",
		};
				  
		for(int i=0; i<defId.length; i++){
			IProcessMap processMap = new ProcessMap();
			processMap.setMapId(this.getGlobalCom()+"."+defId[i]);
			processMap.setDefId(defId[i]);
			processMap.setName(name[i]);
			processMap.setComCode(this.getGlobalCom());
			processMap.setNo(i);
			processMap.setIconFile(new MetaworksFile());
			processMap.getIconFile().setUploadedPath(upLoadedPath[i]);
			
			processMap.createMe();
			
		}
	}
	
	public void notiToCompany() throws Exception{
		Notification noti = new Notification();
		INotiSetting notiSetting = new NotiSetting();
		Instance instance = new Instance();
		noti.session = session;
		instance = this.createWorkItem("join");
		
		Employee employee = new Employee();
		employee.setEmpCode(session.getEmployee().getEmpCode());
		employee.copyFrom(employee.databaseMe());
		IEmployee findResult = employee.findByGlobalCom(employee.getGlobalCom());
		INotiSetting findNotiSetting;
		
		while(findResult.next()){
			findNotiSetting = notiSetting.findByUserId(findResult.getEmpCode());
			if(findNotiSetting.next()){
				if(findNotiSetting.isModiUser()){
					noti.setNotiId(System.currentTimeMillis()); //TODO: why generated is hard to use
					noti.setUserId(findResult.getEmpCode());
					noti.setActorId(session.getEmployee().getEmpName());
					noti.setConfirm(false);
					noti.setInstId(instance.getInstId());
					noti.setInputDate(Calendar.getInstance().getTime());
					noti.setActAbstract(session.getUser().getName() + "님이 가입하셨습니다.");
		
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
	
	public void notiToFriend() throws Exception{
		Notification noti = new Notification();
		INotiSetting notiSetting = new NotiSetting();
		Instance instance = new Instance();
		noti.session = session;
		instance = this.createWorkItem("addFriend");
		
		INotiSetting findNotiSetting = notiSetting.findByUserId(this.getEmpCode());
		if(findNotiSetting.next()){
			if(findNotiSetting.isModiUser()){
				noti.setNotiId(System.currentTimeMillis()); //TODO: why generated is hard to use
				noti.setUserId(this.getEmpCode());
				noti.setActorId(session.getEmployee().getEmpName());
				noti.setConfirm(false);
				noti.setInstId(instance.getInstId());
				noti.setInputDate(Calendar.getInstance().getTime());
				noti.setActAbstract(session.getUser().getName() + "님이 " + this.getEmpName() + "님을 친구로 추가 하셨습니다.");
				
				noti.add(instance);
			
				String followerSessionId = Login.getSessionIdWithUserId(this.getEmpCode());
				
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
	
	public Instance createWorkItem(String type) throws Exception{
//		Employee representiveMailEmp = new Employee();
//		representiveMailEmp.setEmpCode("0");
//		
//		
//		IEmployee repMailEmp = representiveMailEmp.findMe();
//		
//		User theFirstWriter;
//		theFirstWriter = new User();
//		theFirstWriter.setName(repMailEmp.getEmpName());
		
		SystemWorkItem comment = new SystemWorkItem();
		comment.processManager = processManager;
		comment.getMetaworksContext().setWhen(MetaworksContext.WHEN_NEW);
		
//		comment.setWriter(theFirstWriter);
		if("join".equals(type)){
			comment.setSystemMessage(this.getEmpName() + "님이 가입 하셨습니다.");
		}
		else if("addFriend".equals(type)){
			comment.setSystemMessage(session.getUser().getName() + "님이 " + this.getEmpName() + "님을 친구로 추가 하셨습니다.");
		}
		comment.session = session;
		
		comment.add();
		
		Instance instance = new Instance();
		instance.setInstId(comment.getInstId());
		instance.copyFrom(instance.databaseMe());
		
		return instance;
	}
	
	/*
	@Override
	public boolean saveMe() throws Exception {
		if (getMetaworksContext().getWhen().startsWith(MetaworksContext.WHEN_NEW)) {
			checkRegistered();

			this.setIsDeleted("0");
			this.setApproved(true);
			
			if(this.getGlobalCom()==null){
				this.setGlobalCom("uengine.org");
			}else{
				
				Company company = new Company();
				company.setComCode(this.getGlobalCom());

				try{
					company.databaseMe();					
				}catch(Exception e){		
					company.createDatabaseMe();
					company.syncToDatabaseMe();
					
					this.setIsAdmin(true);
					this.setApproved(true);
					
					this.createCodi();
				}
			}
			
			createDatabaseMe();
		} else {
			this.setPartCode(this.getDept().getPartCode());
			// if(getMetaworksContext().getWhen().equals(MetaworksContext.WHEN_EDIT))
			syncToDatabaseMe();
		}
		flushDatabaseMe();
		
		//codi user 추가 시 이행하도록 이동
		//getMetaworksContext().setWhen(MetaworksContext.WHEN_VIEW);
		
		if(getImageFile()!=null && getImageFile().getFileTransfer()!=null && getImageFile().getFileTransfer().getFilename()!=null){
			getImageFile().setEmpCode(this.getEmpCode());
			getImageFile().upload();
		}
		
		return true;
	}
	*/
	
	/*
	@Override
	public boolean createCodi() throws Exception {
		
//		if (getMetaworksContext().getWhen().startsWith(MetaworksContext.WHEN_NEW)) {
			
			Employee emp = new Employee();
			emp.setEmpCode(GlobalContext.getPropertyString("codi.user.id", "0"));
			emp.setEmpName(GlobalContext.getPropertyString("codi.user.name", "CODI"));
			emp.setGlobalCom(this.getGlobalCom());
			emp.setLocale(this.getLocale());
			emp.setApproved(true);
			
			if(emp.findMe().getEmpCode() != null && emp.findMe().getEmpCode().equals(GlobalContext.getPropertyString("codi.user.id")))
				return false;
			
			emp.createDatabaseMe();
			
			this.createCodiThumNail(emp.getEmpCode());
//		}
		
//		getMetaworksContext().setWhen(MetaworksContext.WHEN_VIEW);
		
		
		return true;
	}
	
	@Override
	public void createCodiThumNail(String target) throws Exception {
		
		String inputPath = GlobalContext.getPropertyString("fastcat.server.host") + ":" + GlobalContext.getPropertyString("fastcat.server.port") 
							+ GlobalContext.WEB_CONTEXT_ROOT + "/images/portrait/codi.jpg";
		
		String outputPath = GlobalContext.getPropertyString("filesystem.path") + "/portrait/" + target + ".jpg";
		
		URL url = new URL(inputPath);
		
		try {
			InputStream fis = url.openStream();
			FileOutputStream fos = new FileOutputStream(outputPath);
			
			int bytesRead = 0;
			
			byte[] buffer = new byte[1024];   
			while ((bytesRead = fis.read(buffer, 0, 1024)) != -1) {
				fos.write(buffer, 0, bytesRead);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	*/
	
	@ServiceMethod(callByContent=true, validate=true, target=ServiceMethodContext.TARGET_SELF)
	public Object activate() throws MetaworksException{

		IEmployee findEmployee = null;
		
		try {
			findEmployee = findByKey();
		} catch (Exception e) {
			e.printStackTrace();
			
			throw new MetaworksException(e.getMessage());
		}
		
		if(findEmployee == null)
			throw new MetaworksException("wrong access");
		
		if(findEmployee.isApproved()){
			Login login = new Login();
			login.setEmail(findEmployee.getEmail());
			
			return login;
		}
		
		if(!findEmployee.getAuthKey().equals(this.getAuthKey()))
			throw new MetaworksException("not match activation code");

		if(findEmployee.getInviteUser() != null)
			this.setInviteUser(findEmployee.getInviteUser());
	
		findEmployee.getMetaworksContext().setWhere("step1");
		findEmployee.getMetaworksContext().setHow("signUp");
		findEmployee.getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);			
		findEmployee.setImageFile(new PortraitImageFile());
		findEmployee.getImageFile().getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);
		
		
		//defaultUX, defaultMob 값 설정
		String defaultUX = "wave";
		String defaultMob = "auto";
				
		findEmployee.setPreferUX(defaultUX);
		findEmployee.setPreferMob(defaultMob);
		findEmployee.setPassword(null);
		return findEmployee;
	}
	
	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_SELF)
	public Object[] applyForAddContact() throws Exception{
		
		
		IEmployee findEmployee = null;
		
		try {
			findEmployee = findByKey();
		} catch (Exception e) {
			e.printStackTrace();
			
			throw new MetaworksException(e.getMessage());
		}
		
		if(findEmployee == null)
			throw new MetaworksException("wrong access");
		
		
		
		//초대 받는사람 - newContact
		//초대 한 사람  - recommender
		if(findEmployee.getInviteUser() != null){
			
			Contact newContact = new Contact();
			newContact.setUserId(findEmployee.getEmpCode()); 
			Employee recommender = new Employee();
			recommender.setEmpCode(findEmployee.getInviteUser());
			recommender.copyFrom(recommender.findMe());
			
			IUser newUser_ = new User();
			newUser_.setName(recommender.getEmpName());
			newUser_.setUserId(recommender.getEmpCode());
			newUser_.setNetwork("local");
			newContact.setFriendId(newUser_.getUserId());
			newContact.setFriend(newUser_);
			newContact.createDatabaseMe();
			
			newContact = new Contact();
			newContact.setUserId(recommender.getEmpCode());
			
			IUser me_ = new User();
			me_.setName(findEmployee.getEmpName());
			me_.setUserId(findEmployee.getEmpCode());
			me_.setNetwork("local");
			newContact.setFriendId(me_.getUserId());
			newContact.setFriend(me_);
			newContact.createDatabaseMe();
			
			findEmployee.setInviteUser(null);
			Employee saveEmp = new Employee();
			saveEmp.copyFrom(findEmployee);
			saveEmp.syncToDatabaseMe();
			
//			return saveEmp.forward();
			
			Login login = new Login();
			login.setEmail(saveEmp.getEmail());
			login.setPassword(saveEmp.getPassword());
			return login.login();
		}
		
		
		return null;
		
	}
	
	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_SELF)
	public Object forgotPassword() throws MetaworksException{
		System.out.println("authKey= "+ getAuthKey());
		
		Employee employee = new Employee();
		employee.setAuthKey(this.getAuthKey());
		IEmployee findEmployee = null;
		
		try {
			findEmployee = employee.findByKey();
		} catch (Exception e) {
			e.printStackTrace();
			
			throw new MetaworksException(e.getMessage());
		}
		
		if(findEmployee == null)
			throw new MetaworksException("wrong access");
		if(!findEmployee.getAuthKey().equals(this.getAuthKey()))
			throw new MetaworksException("not match activation code");

		findEmployee.getMetaworksContext().setWhere("forgotpassword");
		findEmployee.getMetaworksContext().setHow("signUp");
		findEmployee.getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);		
		findEmployee.setPassword(null);
		return findEmployee;
	}
	
	@Override
	public Object[] closeEmployeeInfo() throws Exception{
		
		return new Object[]{new Remover(new ModalWindow(), true)};
	}
	
	@Override
	public Object[] saveEmployeeInfo() throws Exception {	
		
		this.saveMe();
		
		if(session != null && session.getEmployee().getEmpCode().equals(getEmpCode())) {
			session.setEmployee(findMe());
			
			SNS sns = new SNS(session);
			TopPanel topPanel = new TopPanel(session);
			topPanel.setTopCenterPanel(sns.loadTopCenterPanel(session));
			
			MainPanel mainPanel = new MainPanel(topPanel, sns);
			
//			return new Object[] {new Refresh(new EmployeeInfo(this)), new Refresh(session)};
			return new Object[] {new Remover(new ModalWindow(), true), new Refresh(mainPanel) };
		}
		
		if(this.getIsAdmin() == false && !this.isApproved()){
			Session session = new Session();
			session.setEmployee(this);
			session.fillSession();
			session.setGuidedTour(true);
			
			CommentWorkItem newComment = new CommentWorkItem();
			newComment.processManager = processManager;
			newComment.session = session;

			newComment.setTitle(localeManager.getResourceBundle().getProperty("RequestJoinApprovedMessage"));
			newComment.save();
			
			processManager.applyChanges();
		}
		
		

		this.notiToCompany();
		
		ModalWindow modalWindow = new ModalWindow();
		modalWindow.getMetaworksContext().setWhen(MetaworksContext.WHEN_VIEW);
		modalWindow.setWidth(300);
		modalWindow.setHeight(150);
		
		modalWindow.setTitle("$JoinCompleteTitle");
		modalWindow.setPanel(localeManager.getString("$JoinCompleteMessage"));
		modalWindow.getButtons().put("$Confirm", null);
		modalWindow.getCallback().put("$Confirm", "forward");
		
		return new Object[] {modalWindow, new Refresh(this)};
	}
	
	public void checkRegistered() throws Exception {
		boolean alreadyRegistered = false;
		try{
			
//				if(this.getEmpCode()==null){
			this.setEmpCode(this.getEmail());
//				}
			
			this.databaseMe(); 
			
			alreadyRegistered = true;
		}catch(Exception e){}

		if(alreadyRegistered)	
			throw new Exception("$AlreadyExistingUser");
	}
	
	
	@Override
	public void addTopicUser() throws Exception {
		System.out.println(getEmpName());
	}
	
	@Override
	public void checkEmpCode() throws Exception {
			
		IEmployee employee = this.findMe();
		
		if(employee.getEmpCode() != null)
			throw new Exception("이미 존재하는 empCode 입니다.");
	}

	public boolean checkValidEmail() throws Exception{
		int posAt = this.getEmail().indexOf("@");
		
		if(this.getEmail().length() != 0 && posAt > -1 ) {
			String[] splitEmail = this.getEmail().split("@", -1);
			
			if(splitEmail[0].length() != 0 && splitEmail[1].length() != 0){
				return true;
			}
		}
		return false;
	}

	@Override
	public String checkId() throws Exception {
	
		String valid = "invalid";

		if(checkValidEmail()){
			this.setEmpCode(this.getEmail());
			IEmployee employee = this.findMe();
		
			if(employee.getEmpCode() == null)
				valid = "valid";
			else
				valid = "duplicate";
		}
		return valid;
	}

	@Override
	public Object[] subscribeStep1() throws Exception {
		
		
		Locale locale = new Locale();
		locale.setLanguage(this.getLocale());
		locale.load();
		
		int posAt = this.getEmpCode().indexOf("@");
		if(posAt > -1){
			String domain = this.getEmpCode().substring(posAt +1);
			if(domain.indexOf("'") > -1) throw new Exception("email address is invalid");
			
			IEmployee exisingUserWhoInTheSameDomain = sql("select * from emptable where email like '%" + domain + "'");
			exisingUserWhoInTheSameDomain.select();
			
			if(exisingUserWhoInTheSameDomain.next()){
				String globalCom = exisingUserWhoInTheSameDomain.getGlobalCom();
				this.setGlobalCom(globalCom);
			}else{
				this.setGlobalCom(domain);
			}
		}
		
		getMetaworksContext().setWhen("new2");

		return new Object[]{locale, this};
	}
	@Override
	public Object subscribeStep2() throws Exception {
		if(!this.checkValidEmail()){
			throw new Exception("Is that email is already subscribed");
		}

		
		if(!getPassword().equals(getConfirmPassword())){
			throw new Exception("Re-entered password doesn't match");
		}

		getMetaworksContext().setWhen("new3");

		return this;
	}
	@Override
	public Object subscribeStep3() throws Exception {
		
		Locale locale = new Locale();
		this.setLocale(localeManager.getLanguage());
		locale.setLanguage(localeManager.getLanguage());
		locale.load();
		
		getMetaworksContext().setHow("detail");
		getMetaworksContext().setWhen("edit");
		getMetaworksContext().setWhere("admin");
		
		this.setImageFile(new PortraitImageFile());
		this.getImageFile().getMetaworksContext().setWhen(WHEN_EDIT);
		
		return new Object[]{locale, this};
	}
	
	@Override
	public Object[] unsubscribe() throws Exception {
		
		if(session.getEmployee().getEmpCode().equals(this.getEmpCode()) || session.employee.getIsAdmin()) {
			
			Employee employee = new Employee();
			employee.setEmpCode(this.empCode);		
			employee.databaseMe().setIsDeleted("1");		
			
			if(session.getEmployee().getEmpCode().equals(this.getEmpCode()))
				return new Object[]{session.logout(), new Remover(new ModalWindow())};
			else
				return new Object[]{new Remover(employee , true)};
		}
		else
			throw new Exception("관리자나 본인이 아니면 탈퇴할 수 없습니다");		
	}	
	
	@Override
	public Session drag() throws Exception {
		session.setClipboard(this);
		return session;
	}
	
	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_APPEND)
	public Object[] forward() throws Exception {
		Session session = new Session();
		session.setEmployee(this);
		session.fillSession();
		session.setGuidedTour(true);

		Login login = new Login();
		login.storeIntoServerSession(session);
		
		ModalWindow removeWindow = new ModalWindow();
		removeWindow.setId("subscribe");
		
		return new Object[]{new Remover(removeWindow, true), new ToOpener(new MainPanel(new Main(session)))};		
		//return new Object[]{new Remover(removeWindow, true), new Remover(new ModalWindow()), new ToOpener(new MainPanel(new Main(session)))};		
	}
	
	public IEmployee findByEmail() throws Exception {
		
		Employee employee = null;
		
		StringBuffer sb = new StringBuffer();
		sb.append("select emptable.*, PARTTABLE.PARTNAME");
		sb.append("  from emptable LEFT OUTER JOIN PARTTABLE on emptable.partcode=PARTTABLE.partcode ");
		sb.append(" where emptable.email=?email ");
		sb.append("   and emptable.isdeleted = 0");

		IEmployee findEmployee = (IEmployee) sql(sb.toString());
		findEmployee.setEmail(this.getEmail());
		findEmployee.select();
		if (findEmployee.next()) {
			employee = new Employee();
			employee.copyFrom(findEmployee);
			employee.getDept().getMetaworksContext().setHow("picker");
		}
		return employee;
	}
	
	public IEmployee findForLogin(){
		StringBuffer sb = new StringBuffer();
		sb.append("select empcode, password, globalcom");
		sb.append("  from emptable");
		sb.append(" where email=?email ");
		sb.append("   and isdeleted = 0");
		
		IEmployee dao = null;
		
		try {
			dao = sql(sb.toString());
			dao.setEmail(this.getEmail());
			dao.select();
			
			if(!dao.next())
				dao = null;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return dao; 
	}
	
	public IEmployee findByKey(){
		
		StringBuffer sb = new StringBuffer();
		sb.append("select * from ");
		sb.append("emptable ");
		sb.append("where authKey=?authKey ");
		
		IEmployee dao = null;
		
		try {
			dao = sql(sb.toString());
			dao.setAuthKey(this.getAuthKey());
			dao.select();
			
			if(!dao.next())
				dao = null;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return dao; 
	}
	
	
	public static String extractTenantName(String email){
		
		String str = email.substring(email.indexOf("@")+1);

		return str.substring(0, str.indexOf("."));
		
		
	}
	
	public String createNewId() throws Exception{
		Map options = new HashMap();
		options.put("useTableNameHeader", "false");
		options.put("onlySequenceTable", "true");
		
		KeyGeneratorDAO kg = DAOFactory.getInstance(TransactionContext.getThreadLocalInstance()).createKeyGenerator("emptable", options);
		kg.select();
		kg.next();
		
		Number number = kg.getKeyNumber();
		
		return number.toString();
	}
	
	public void prevStep(){
		this.getImageFile().setFileTransfer(null);
		
		int step = Integer.parseInt(this.getMetaworksContext().getWhere().substring(4));
		this.getMetaworksContext().setWhere("step" + String.valueOf(step-1));
	}
	
	public void nextStep(){
		this.getImageFile().setFileTransfer(null);
		
		int step = Integer.parseInt(this.getMetaworksContext().getWhere().substring(4));
		
		this.getMetaworksContext().setWhere("step" + String.valueOf(step+1));
	}
	
	@Override
	public Forward finish() throws Exception {
		
		session = new Session();
		
		this.saveMe();
				
		return new Forward(TenantURL.getURL());
	}

	public int createNewNotiSettingId() throws Exception{
		Map options = new HashMap();
		options.put("useTableNameHeader", "false");
		options.put("onlySequenceTable", "true");
		
		KeyGeneratorDAO kg = DAOFactory.getInstance(TransactionContext.getThreadLocalInstance()).createKeyGenerator("notisetting", options);
		kg.select();
		kg.next();
		
		Number number = kg.getKeyNumber();
		
		return number.intValue();
	}
	
	public IEmployee findCompanyAdmin() throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("select * from ");
		sb.append("emptable ");
		sb.append("where isAdmin=?isAdmin ");
		sb.append("and globalCom=?globalCom ");
		
		IEmployee dao = sql(sb.toString());
//		dao.set("isAdmin", "1");
		dao.setIsAdmin(true);
		dao.setGlobalCom(this.getGlobalCom());
		dao.select();
		
		return dao; 
	}

	public static IEmployee getSystemUser() throws Exception{
		
		Employee find = new Employee();
		
		// TODO: System Empcode setting
		find.setEmpCode("0");
		
		return find.findMe();
		
	}
	
	public IUser getUser(){
		User user = new User();
		user.session = session;
		user.setUserId(this.getEmpCode());
		user.setName(this.getEmpName());
		
		return user;
	}
	
	public Object facebookSSO() throws Exception {
		IEmployee findEmp = this.findForLogin();
		
		if(findEmp == null){
			this.getMetaworksContext().setWhen(WHEN_NEW);
			this.saveMe();
		}
		
		Session session = new Session();
		session.setEmployee(findEmp);
		session.fillUserInfoToHttpSession();
		
		return new StartCodi(session);
	}
	

}
