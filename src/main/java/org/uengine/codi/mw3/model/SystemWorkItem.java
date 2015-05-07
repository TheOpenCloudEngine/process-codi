package org.uengine.codi.mw3.model;

import org.uengine.kernel.GlobalContext;


public class SystemWorkItem extends CommentWorkItem{

	String systemMessage;
		public String getSystemMessage() {
			return systemMessage;
		}
		public void setSystemMessage(String systemMessage) {
			this.systemMessage = systemMessage;
		}
		
	public SystemWorkItem(){
		setType(WORKITEM_TYPE_SYSTEM);
	}

	@Override
	public Object[] add() throws Exception {
		System.out.println(this.getSystemMessage());
		Session codiSession = new Session();
		
		Instance instance = new Instance();
		instance.setInstId(this.getInstId());
		
		IEmployee emp = new Employee();
		emp.setEmpCode(GlobalContext.getPropertyString("codi.user.id", "0"));
		emp.setEmpName(GlobalContext.getPropertyString("codi.user.name", "CODI"));
		emp.setGlobalCom(instance.databaseMe().getInitComCd());
		
		//코디계정 - 노티워크아이템발행
		codiSession.setUser(emp.getUser());
		//노티발행을 하게 한 유저
		codiSession.setEmployee(session.getEmployee());
		
		this.setTitle(this.getSystemMessage());
		this.session = codiSession;
		
		super.add();
		
		return null;
	}
}
