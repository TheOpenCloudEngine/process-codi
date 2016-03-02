package org.uengine.codi.mw3.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import javax.servlet.http.HttpSession;

import org.directwebremoting.WebContextFactory;
import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.*;
import org.metaworks.dao.TransactionContext;
import org.metaworks.widget.ModalPanel;
import org.metaworks.widget.ModalWindow;
import org.uengine.codi.mw3.Login;
import org.uengine.codi.mw3.StartCodi;
import org.uengine.codi.util.CodiHttpClient;
import org.uengine.sso.BaseAuthenticate;
import org.uengine.sso.CasAuthenticate;

@AutowiredFromClient
public class Session implements ContextAware{

	static Hashtable<String, ArrayList> messagesToUsers = new Hashtable<String, ArrayList>();

	public Session() {
		MetaworksContext metaworkscontext = new MetaworksContext();
		metaworkscontext.setWhen(MetaworksContext.WHEN_VIEW);

		setMetaworksContext(metaworkscontext);
	}

	IUser user;
		public IUser getUser() {
			return user;
		}
		public void setUser(IUser user) {
			this.user = user;
		}

	String defId;
		public String getDefId() {
			return defId;
		}
		public void setDefId(String defId) {
			this.defId = defId;
		}

	String ux;
		public String getUx() {
			return ux;
		}
		public void setUx(String ux) {
			this.ux = ux;
		}

	boolean guidedTour;
		@Hidden
		public boolean isGuidedTour() {
			return guidedTour;
		}
		public void setGuidedTour(boolean guidedTour) {
			this.guidedTour = guidedTour;
		}

	Object clipboard;
	@Hidden
		public Object getClipboard() {
			return clipboard;
		}
		public void setClipboard(Object clipboard) {
			this.clipboard = clipboard;
		}

	IEmployee employee;
		@Available(when = MetaworksContext.WHEN_VIEW)
		@NonEditable
		public IEmployee getEmployee() {
			if(this.employee != null) {
				this.employee.getMetaworksContext().setWhere("inSession");
			}
			return employee;
		}

		public void setEmployee(IEmployee employee) {
			this.employee = employee;
		}

	IDept dept;
		@Hidden
		@NonEditable
		public IDept getDept() {
			return dept;
		}

		public void setDept(IDept dept) {
			this.dept = dept;
		}

	ICompany company;
		@Hidden
		@NonEditable
		public ICompany getCompany() {
			return company;
		}

		public void setCompany(ICompany company) {
			this.company = company;
		}

	private MetaworksContext context;
		public MetaworksContext getMetaworksContext() {
			return this.context;
		}

		public void setMetaworksContext(MetaworksContext paramMetaworksContext) {
			this.context = paramMetaworksContext;
		}

	String lastPerspecteMode;
		@Hidden
		public String getLastPerspecteMode() {
			return lastPerspecteMode;
		}
		public void setLastPerspecteMode(String lastPerspecteMode) {
			this.lastPerspecteMode = lastPerspecteMode;
		}

	String lastPerspecteType;
		@Hidden
		public String getLastPerspecteType() {
			return lastPerspecteType;
		}

		public void setLastPerspecteType(String lastPerspecteType) {
			this.lastPerspecteType = lastPerspecteType;
		}

	String lastSelectedItem;
		@Hidden
		public String getLastSelectedItem() {
			return lastSelectedItem;
		}
		public void setLastSelectedItem(String lastSelectedItem) {
			this.lastSelectedItem = lastSelectedItem;
		}

	String lastInstanceId;
		@Hidden
		public String getLastInstanceId() {
			return lastInstanceId;
		}
		public void setLastInstanceId(String lastInstanceId) {
			this.lastInstanceId = lastInstanceId;
		}

	String windowTitle;
		@Hidden
		public String getWindowTitle() {
			return windowTitle;
		}
		public void setWindowTitle(String windowTitle) {
			this.windowTitle = windowTitle;
		}

	IRole role;
		@Hidden
		public IRole getRole() {
			return role;
		}
		public void setRole(IRole role) {
			this.role = role;
		}

	int todoListCount;
		public int getTodoListCount(){
			return todoListCount;
		}
		public void setTodoListCount(int todoListCount){
			this.todoListCount = todoListCount;
		}


	String searchKeyword;
		@Hidden
		public String getSearchKeyword() {
			return searchKeyword;
		}
		public void setSearchKeyword(String searchKeyword) {
			this.searchKeyword = searchKeyword;
		}

	String topSearchKeyword;
		@Hidden
		public String getTopSearchKeyword() {
			return topSearchKeyword;
		}
		public void setTopSearchKeyword(String topSearchKeyword) {
			this.topSearchKeyword = topSearchKeyword;
		}

	String accessToken;
		@Hidden
		public String getAccessToken() {
			return accessToken;
		}
		public void setAccessToken(String accessToken) {
			this.accessToken = accessToken;
		}

	@ServiceMethod(callByContent=true)
	public StartCodi logout() throws Exception {

		if("1".equals(StartCodi.USE_CAS)){
			String strTgt = TransactionContext.getThreadLocalInstance().getRequest().getSession().getAttribute("SSO-TGT").toString();

			BaseAuthenticate baseAuth = new CasAuthenticate();
			baseAuth.destorytoken(strTgt);
		}

        return new StartCodi(this, "logout");
	}


	@ServiceMethod(target=ServiceMethodContext.TARGET_NONE)
	public Object heartbeat(){
		//nothing to do
		String sessionId = WebContextFactory.get().getScriptSession().getId();
		//System.out.println("heartbeat:" + sessionId);

		if(messagesToUsers.containsKey(sessionId)){
			ArrayList messages = messagesToUsers.get(sessionId);
			messagesToUsers.remove(sessionId);

			return messages;
		}

		return null;
	}

	public static void pushMessage(String userId, Object message){
		String sessionId = Login.getSessionIdWithUserId(userId);

		if(sessionId==null) return;

		ArrayList messages = null;
		if( !messagesToUsers.containsKey(sessionId)){
			messages = new ArrayList();
			messagesToUsers.put(sessionId, messages);
		}else{
			messages = messagesToUsers.get(sessionId);
		}

		messages.add(message);
	}

	// when need HttpSession
	public void fillUserInfoToHttpSession(){
		HttpSession httpSession = TransactionContext.getThreadLocalInstance().getRequest().getSession();
		httpSession.setAttribute("loggedUserId", getEmployee().getEmpCode());
		httpSession.setAttribute("tenantId", getEmployee().getGlobalCom());
		httpSession.setAttribute("projectId", null);

	}

	public void removeUserInfoFromHttpSession(){
		HttpSession httpSession = TransactionContext.getThreadLocalInstance().getRequest().getSession();
		httpSession.invalidate();
	}

	@ServiceMethod(callByContent=true, target="popup")
	public ModalWindow manager() throws Exception{
		return new ModalWindow(new ManagerPanel(this), 600, 500);
	}

	public void fillSession() throws Exception {
		if (this.getEmployee() != null
				&& this.getEmployee().getGlobalCom() != null) {

			IUser user = new User();
			user.getMetaworksContext().setWhere("local");
			user.setUserId(this.getEmployee().getEmpCode());
			user.setName(this.getEmployee().getEmpName());

			this.setUser(user);


			if (this.getEmployee().getPartCode() != null) {
				try{
					IDept dept = new Dept();
					dept.setPartCode(this.getEmployee().getPartCode());
					this.setDept(dept.load());
				}catch(Throwable e){

				}
			}

			if (this.getEmployee().getGlobalCom() != null) {
				try{
					ICompany company = new Company();
					company.setComCode(this.getEmployee().getGlobalCom());
					this.setCompany(company.load());
				}catch(Throwable e){

				}
			}
		} else {
			throw new Exception(
					"There is no Company info in user info.");
		}
	}

    private String jiraComCode;
        public String getJiraComCode() {
            return jiraComCode;
        }

        public void setJiraComCode(String jiraComCode) {
            this.jiraComCode = jiraComCode;
        }

    private String jiraEmpCode;
        public String getJiraEmpCode() {
            return jiraEmpCode;
        }

        public void setJiraEmpCode(String jiraEmpCode) {
            this.jiraEmpCode = jiraEmpCode;
        }

    @ServiceMethod(callByContent=true)
	public void jiraLogin() throws Exception{
		Employee employee = new Employee();
        IEmployee iEmployee = employee.findByEmpCode(jiraEmpCode);

        Company company = new Company();
        company.setComCode(jiraComCode);
        ICompany iCompany = company.findByCode();

        this.setEmployee(iEmployee);
        this.fillSession();
    }

}
