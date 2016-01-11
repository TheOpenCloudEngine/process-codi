package org.uengine.codi.mw3.model;

import java.util.Date;

import javax.servlet.http.HttpSession;
import javax.sql.RowSet;

import org.metaworks.EventContext;
import org.metaworks.MetaworksContext;
import org.metaworks.Refresh;
import org.metaworks.Remover;
import org.metaworks.ServiceMethodContext;
import org.metaworks.ToEvent;
import org.metaworks.ToOpener;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.common.MetaworksUtil;
import org.metaworks.dao.Database;
import org.metaworks.dao.MetaworksDAO;
import org.metaworks.dao.TransactionContext;
import org.metaworks.dwr.MetaworksRemoteService;
import org.metaworks.website.MetaworksFile;
import org.metaworks.widget.Label;
import org.metaworks.widget.ModalWindow;
import org.springframework.beans.factory.annotation.Autowired;
import org.uengine.processmanager.ProcessManagerRemote;

public class User extends Database<IUser> implements IUser {

	@Autowired
	public ProcessManagerRemote processManager;

	@AutowiredFromClient
	public Session session;

	String name;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}

	String userId;
		public String getUserId() {
			return userId;
		}	
		public void setUserId(String userId) {
			this.userId = userId;
		}

	String network;
		public String getNetwork() {
			return network;
		}
		public void setNetwork(String network) {
			this.network = network;
		}
		
	String mood;
		public String getMood() {
			return mood;
		}
		public void setMood(String mood) {
			this.mood = mood;
		}
		
	int businessValue;
		public int getBusinessValue() {
			return businessValue;
		}
		public void setBusinessValue(int businessValue) {
			this.businessValue = businessValue;
		}
		
	int todoCount;
		public int getTodoCount() {
			return todoCount;
		}
		public void setTodoCount(int todoCount) {
			this.todoCount = todoCount;
		}
		
	boolean self;
		public boolean isSelf() {
			return self;
		}
		public void setSelf(boolean self) {
			this.self = self;
		}
	
	boolean friend;
		public boolean isFriend() {
			return friend;
		}
		public void setFriend(boolean friend) {
			this.friend = friend;
		}

	boolean admin;
		public boolean isAdmin() {
			return admin;
		}
		public void setAdmin(boolean admin) {
			this.admin = admin;
		}
		
	String email;
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		
	boolean anotherTenant;
		@Hidden
		public boolean isAnotherTenant() {
			return anotherTenant;
		}
		public void setAnotherTenant(boolean anotherTenant) {
			this.anotherTenant = anotherTenant;
		}
	
	boolean deleted;
		public boolean isDeleted() {
			return deleted;
		}
		public void setDeleted(boolean deleted) {
			this.deleted = deleted;
		}
		
	UserNicknameChanger userNicknameChanger;
		public UserNicknameChanger getUserNicknameChanger() {
			return userNicknameChanger;
		}
		public void setUserNicknameChanger(UserNicknameChanger userNicknameChanger) {
			this.userNicknameChanger = userNicknameChanger;
		}
		
	@Override
	public Object[] pickUp() throws Exception {
		this.getMetaworksContext().setHow(HOW_PICKER);
		this.getMetaworksContext().setWhere(WHERE_EVER);
		this.getMetaworksContext().setWhen(WHEN_EDIT);
		
		return new Object[]{new ToOpener(this), new ToEvent(ServiceMethodContext.TARGET_OPENER, EventContext.EVENT_CHANGE), new ToEvent(ServiceMethodContext.TARGET_SELF, EventContext.EVENT_CLOSE)};
	}
	
	public static User fromHttpSession(){
		HttpSession session = TransactionContext.getThreadLocalInstance().getRequest().getSession();
		
		User login = new User();
		
		if(session!=null){
			login.setUserId((String) session.getAttribute("userId"));
			
			return login;
		}
		
		return null;
	
	}

	@Override
	public IEmployee loadEmployee() throws Exception{
		Employee emp = new Employee();
		emp.setEmpCode(getUserId());
		
		return emp.databaseMe();
	}
	
	public void load() throws Exception {
		boolean isSelf = (session!=null && session.getUser()!=null && this.getUserId()!=null && this.getUserId().equals(session.getUser().getUserId()));
		boolean isFriend = false;
		boolean isAnotherTenant = false;
		
		Employee emp = new Employee();
		emp.setEmpCode(this.getUserId());
		emp.copyFrom(emp.findMe());
		
		// reload data
		this.setName(this.getName());
		this.setMood(emp.getMood());
		this.setEmail(emp.getEmail());
		this.setDeleted("1".equals(emp.getIsDeleted()));
		
		this.setSelf(isSelf);
		
		if(!isSelf){
			IContact findContact = Contact.findContactsWithFriendId(session.getUser(), this.getUserId());
			if(findContact != null)
				isFriend = true;
		}
		
		if(emp.getGlobalCom() == null){
			if(!Employee.extractTenantName(this.getEmail()).equals(Employee.extractTenantName(session.getEmployee().getEmail())))
				isAnotherTenant = true;
		}else{				
			if(session!=null && session.getEmployee()!=null && !emp.getGlobalCom().equals(session.getEmployee().getGlobalCom()))
				isAnotherTenant = true;
		}
		
		this.setFriend(isFriend);
		this.setAdmin(session.getEmployee().getIsAdmin());
		this.setAnotherTenant(isAnotherTenant);
	}
	
	@Override
	public Popup detail() throws Exception {

		if(getMetaworksContext()!=null && MetaworksContext.WHEN_EDIT.equals(getMetaworksContext().getWhen()) || MetaworksContext.WHEN_NEW.equals(getMetaworksContext().getWhen())){
			popupPicker();

			return null;

		}

		int with = 435;
		int height = 279;
		
		this.load();
		if(!this.getUserId().equals(session.getUser().getUserId())){
			Contact contact = new Contact();
			IContact findContact = contact.findContactsWithFriendId(session.getUser(), this.getUserId());
			if(findContact != null){
				userNicknameChanger = new UserNicknameChanger();
				userNicknameChanger.setUserId(this.getUserId());
				userNicknameChanger.setUserNickname(findContact.getFriend().getName());
				userNicknameChanger.session = session;
				userNicknameChanger.getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);
			}
		}
		//this.getMetaworksContext().setWhen(this.getMetaworksContext().getWhere());
		this.getMetaworksContext().setHow(HOW_INFO);
		
//		String when = this.getMetaworksContext().getWhen();
//		
//		if(when != null && when.equals(Followers.CONTEXT_WHERE_INFOLLOWERS)){
//			String instId = session.getLastInstanceId();
//			
//			RoleMapping roleMapping = new RoleMapping(new Long(instId), RoleMapping.ROLEMAPPING_FOLLOWER_ROLENAME_FREFIX + getName(), getUserId());
//			if(!roleMapping.confirmFollower()){
//				this.getMetaworksContext().setWhen("participant");
//			}
//		}
		
		
		// TODO: User 가 로드되고 나서 나중에 로드 되게 수정해야함
		Employee employee = new Employee();
		employee.setEmpCode(getUserId());
		setMood(employee.databaseMe().getMood());
		setName(employee.databaseMe().getEmpName());
		
		//선택된 유저의 business value를 보인다.
		int myBV = getBV(getUserId());
		setBusinessValue(myBV);
		
		//선택된 유저가 해야 할 일의 개수를 보인다.
		int todoCount = Instance.countTodo(getUserId(), employee.databaseMe().getPartCode());
		setTodoCount(todoCount);

		Popup popup = new Popup(with, height);
		popup.setPanel(this);
		popup.setAnimate(false);

		MetaworksRemoteService.wrapReturn(popup);

		return popup;
	}
	
	@Override
	public void chat() throws Exception{

	}

	public void addContact() throws Exception {
		Contact contact = new Contact();
		contact.session = session;
		contact.setFriend(this);
		contact.setUserId(session.getUser().getUserId());
		contact.put();
	}
	
	public Object[] addContactForInfo() throws Exception {
		this.addContact();
		this.load();
		
		return new Object[]{new ToEvent(new ContactPerspective(), EventContext.EVENT_CHANGE), new Refresh(this, false, true)};
		
		/*
		ContactList contactList = new ContactList();
		contactList.getMetaworksContext().setWhen(ContactListPanel.CONTACT);
		
		if(this.getNetwork() != null && this.getNetwork().equals("fb")){
			contactList.getMetaworksContext().setWhere(ContactList.FACEBOOK);
			
			Employee employee = new Employee();
			employee.setEmpCode(this.getUserId());
			employee.setEmpName(this.getName());
			employee.setPassword("");
			employee.setIsAdmin(false);
			employee.setEmail(this.getUserId()+"@uengine.org");
			employee.setGlobalCom("uengine.org");
			employee.setLocale("ko");
			employee.setPreferUX("wave");
			employee.setPreferMob("auto");
			employee.setApproved(true);
			employee.createDatabaseMe();
			
		}else{
			contactList.getMetaworksContext().setWhere(ContactList.LOCAL);
		}
		contactList.load(session.getUser().getUserId());		
		
		return new Refresh(contactList);
		*/
	}	
	
	@Override
	public Object[] addContactForList() throws Exception {
		this.addContact();
		
		return new Object[]{new ToEvent(new ContactPerspective(), EventContext.EVENT_CHANGE), new Remover(ServiceMethodContext.TARGET_SELF)};
	}
	
	@Override
	public Object[] removeContact() throws Exception {
		Contact contact = new Contact();
		contact.setFriend(this);
		contact.setUserId(session.getUser().getUserId());
		contact.delegate();
		
		this.load();
		
		return new Object[]{new ToEvent(new ContactPerspective(), EventContext.EVENT_CHANGE), new Refresh(this, false, true)};
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
		this.getMetaworksContext().setWhere(WHERE_ADDFOLLOWER);
		
		// refresh self
		return new Object[]{new Refresh(this, false, true)};
	}
	
	public Object[] removeFollower() throws Exception {
		
		// use clipboard for removeFollower
		Object clipboard = session.getClipboard();
		
		if(clipboard instanceof Follower){
			Follower follower = (Follower)clipboard;
			follower.session = session;
			follower.delegate(this);
		}
		this.getMetaworksContext().setWhere(WHERE_REMOVEFOLLOWER);
		
		// refresh self
		return new Object[]{new Refresh(this, false, true)};
	}
	
	@Override
	public void changeMood() throws Exception {

		try{
			Employee employee = new Employee();
			employee.setEmpCode(getUserId());
			employee.databaseMe().setMood(getMood());
			setNetwork("local");
		}catch(Exception e){
		//	e.printStackTrace();
		}
		
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getName();
	}

	@Override
	public Session drag() {
		session.setClipboard(this);
		return session;
	}
	
	@Override
	public void drop() {
		Object clipboard = session.getClipboard();
		if(clipboard instanceof IUser){
			
			IUser user = (IUser) clipboard;
			
			try {
				if(!user.getUserId().equals(getUserId())){
					//copyFrom(user);
					//setUserId(user.getUserId());
					copyFrom(user);
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}	
	
	}

	@Override
	public void addAsAdmin() throws Exception {
		if(session.getEmployee().getIsAdmin()){
			
			Employee employeeType = new Employee();
			employeeType.setEmpCode(getUserId());
			employeeType.databaseMe().setIsAdmin(true);
			
		}else
			throw new Exception("$AdminOnly");
	}
	
	@Override
	public Object[] showWall() throws Exception{
		
		session.setLastSelectedItem(getUserId());
		
		PersonalPerspective personalPerspective = new PersonalPerspective();
		personalPerspective.session = session;
		
		Locale locale = new Locale(session);
		locale.load();
		
		String title = locale.getString("$Contact") + " - " + getName();
		session.setWindowTitle(title);
		
		return new Object[]{MetaworksUtil.makeRefreshObjectArray(personalPerspective.load(Perspective.TYPE_STARTEDBYME))};
	}
	
		
	@Override
	public Object[] showSchedule() throws Exception{
		
		session.setLastSelectedItem(getUserId());
		
		PersonalPerspective personalPerspective = new PersonalPerspective();
		personalPerspective.session = session;
		
		Locale locale = new Locale(session);
		locale.load();
		
		String title = locale.getString("$Contact") + " - " + getName();
		session.setWindowTitle(title);
		
		Object [] returnObject = MetaworksUtil.makeRefreshObjectArray(personalPerspective.load(Perspective.TYPE_CALENDAR));
		returnObject = MetaworksUtil.putObjectArray(returnObject, new ToEvent(ServiceMethodContext.TARGET_SELF, EventContext.EVENT_CLOSE));
		
		return returnObject;
	}
	
	@ServiceMethod()
	public int getBV(String empCode) throws Exception{
		
		int totalBV = 0;
		
		String sql = "select sum(businessvalue) totalBV from inst_emp_perf where empcode=?empCode";
		
		IInstanceEmployeePerformance getBV = (IInstanceEmployeePerformance) Database.sql(IInstanceEmployeePerformance.class, sql);
		getBV.setEmpCode(empCode);
		getBV.select();
		
		RowSet rowset = getBV.getImplementationObject().getRowSet();
		
		if(rowset.next()){
			totalBV = rowset.getInt("totalBV");
		}
		
		return totalBV;
		
	}
	
	public void approvedSubscribe() throws Exception {
		if(!session.getEmployee().getIsAdmin()){
			throw new Exception("$AdminOnly");
		}
		
		Employee employee = new Employee();
		employee.setEmpCode(this.getUserId());
		employee.databaseMe().setApproved(true);
	}
	
	@Override
	public IUser findByDept(Dept dept) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("select emptable.empcode as userId, emptable.empname as name  from ");
		sb.append("emptable LEFT OUTER JOIN PARTTABLE on emptable.partcode=PARTTABLE.partcode ");
		if (dept.getPartCode() != null) {
			sb.append("where emptable.partcode=?partCode ");
		} else {
			sb.append("where emptable.partcode is null ");
		}
		sb.append("and emptable.globalcom=?globalCom ");
		IUser deptEmployee = sql(sb.toString());
		if (dept.getPartCode() != null) {
			deptEmployee.set("partCode" , dept.getPartCode());
		}
		deptEmployee.set("globalCom" , dept.getGlobalCom());
		deptEmployee.select();
		deptEmployee.setMetaworksContext(this.getMetaworksContext());
		
		return deptEmployee;
	}
	
	public Instance createWorkItem() throws Exception{
		
		Employee representiveMailEmp = new Employee();
		representiveMailEmp.setEmpCode("0");
		
		
		IEmployee repMailEmp = representiveMailEmp.findMe();
		
		User theFirstWriter;
		theFirstWriter = new User();
		theFirstWriter.setName(repMailEmp.getEmpName());
		
		SystemWorkItem comment = new SystemWorkItem();
		comment.processManager = processManager;
		comment.getMetaworksContext().setWhen(MetaworksContext.WHEN_NEW);
		
		comment.setWriter(theFirstWriter);
		comment.setTitle("주제 : " + " 이 생성되었습니다.");
		
		comment.session = session;
		comment.add();
		
		Instance instance = new Instance();
		instance.setInstId(comment.getInstId());
		instance.copyFrom(instance.databaseMe());
		
		return instance;
	}
	
	public static void updateRecentItem(Session session, IUser user) throws Exception {
		//선택한 유저 recentItem에 add
		RecentItem recentItem = new RecentItem();
		recentItem.setEmpCode(session.getEmployee().getEmpCode());
		recentItem.setItemId(user.getUserId());
		recentItem.setItemType(RecentItem.TYPE_FRIEND);
		recentItem.setUpdateDate(new Date());
		
		recentItem.add();
	}
	
	public static IUser findUsers(Session session, String keyword) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT e.empcode userId, e.empname name, CONCAT(p.partname,IF(!isnull(p.partname) and length(trim(p.partname))>0 and !isnull(e.jikname) and length(trim(e.jikname))>0, ', ', ''), e.jikname) mood");
		sb.append("  FROM emptable e");
		sb.append("  LEFT JOIN parttable p");
		sb.append("         ON e.partcode=p.partcode");
		sb.append(" WHERE e.empcode!=?userid");
		sb.append("   AND e.isdeleted=?isdeleted");
		sb.append("   AND e.approved=?approved");
		sb.append("   AND e.globalcom=?globalcom");
		sb.append("   AND NOT EXISTS");
		sb.append("    (SELECT 1");
		sb.append("       FROM contact c");
		sb.append("      WHERE c.userid=?userid");
		sb.append("        AND c.friendid=e.empcode");
		sb.append("    )");
		
		if(keyword != null && keyword.trim().length() > 0)
			sb.append("   AND e.empname LIKE ?empname");
		
		
		IUser dao = (IUser)MetaworksDAO.createDAOImpl(TransactionContext.getThreadLocalInstance(),
	   			 sb.toString(), 
	   			 IUser.class);

		dao.setUserId(session.getUser().getUserId());		
		dao.set("empname", "%" + keyword + "%");
		dao.set("isdeleted", "0");
		dao.set("approved", "1");
		dao.set("globalcom", session.getEmployee().getGlobalCom());
		dao.select();

		return dao;
	}
	
	public Object[] delUser() throws Exception {
		
		IEmployee emp = new Employee();
		emp.setEmpCode(getUserId());
		Employee employee = (Employee)emp.findMe();	
		
		employee.databaseMe().setIsDeleted("1");		
			
		return new Object[]{new ToEvent(ServiceMethodContext.TARGET_SELF, EventContext.EVENT_CLOSE)};
	}
	
	public void popupPicker() throws Exception {
		ModalWindow popup = new ModalWindow();
		
		ContactPanel contactPanel = new ContactPanel();
		contactPanel.session = session;
		contactPanel.setPicker(true);
		contactPanel.load();

		popup.setPanel(contactPanel);
		popup.setTitle("AddFollowerPanel");
		//popup.setPanel(new Label("Test"));
		
		MetaworksRemoteService.wrapReturn(popup);
	}
	
	public Object[] popupProfile() throws Exception {
		
		Employee employee = new Employee();
		employee.setEmpCode(this.getUserId());
		employee.copyFrom(employee.findMe());
		
		return new Object[]{new ModalWindow(employee.editEmployeeInfo(), 657, 430, "$EditProfile"), new ToEvent(ServiceMethodContext.TARGET_SELF, EventContext.EVENT_CLOSE)};
	}
	
	public Object[] popupConfigNoti() throws Exception {
		Employee employee = new Employee();
		employee.setEmpCode(this.getUserId());
		employee.copyFrom(employee.findMe());

		return new Object[]{new ModalWindow(employee.editNotiSetting(), 700, 550, "$NotiSetting"), new ToEvent(ServiceMethodContext.TARGET_SELF, EventContext.EVENT_CLOSE)};		
	}

	public Object[] popupConfigCompany() throws Exception {
		Company company = new Company();
		company.setComCode(session.getEmployee().getGlobalCom());
		company.copyFrom(company.findByCode());
		company.getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);
		company.setLogo(new MetaworksFile());

		ModalWindow modalWindow = new ModalWindow();
		modalWindow.setPanel(company);
		modalWindow.setWidth(400);
		modalWindow.setHeight(235);
		modalWindow.setTitle("관리자");

		return new Object[]{modalWindow, new ToEvent(ServiceMethodContext.TARGET_OPENER, EventContext.EVENT_CLOSE)};
	}
	
	public Object[] logout() throws Exception {
		return new Object[]{new ToEvent(ServiceMethodContext.TARGET_SELF, EventContext.EVENT_CLOSE), new Refresh(session.logout())};		
	}

	public void modePicker(){
		this.getMetaworksContext().setHow(HOW_PICKER);
	}
}



/*if(user.addFollowerPanel!=null && "userPicker".equals(user.addFollowerPanel.getMetaworksContext().getWhen())){
	user.getMetaworksContext().setWhen("edit");
	user.getMetaworksContext().setHow("picker");
	
	return new Object[]{new Remover(new Popup()), new ToOpener(user)};
	
}else
else if("addPicker".equals(user.getMetaworksContext().getWhen())){
			getMetaworksContext().setWhen("edit");
			getMetaworksContext().setHow("picker");
			
			return new Object[]{new Remover(new Popup()), new ToOpener(this)};			
		}
 *
 *
 */

