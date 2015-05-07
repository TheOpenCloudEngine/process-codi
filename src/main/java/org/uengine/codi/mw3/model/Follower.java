package org.uengine.codi.mw3.model;

import org.metaworks.EventContext;
import org.metaworks.MetaworksException;
import org.metaworks.Refresh;
import org.metaworks.Remover;
import org.metaworks.ServiceMethodContext;
import org.metaworks.ToEvent;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.dao.Database;
import org.metaworks.dao.TransactionContext;
import org.metaworks.dao.TransactionListener;
import org.metaworks.dwr.MetaworksRemoteService;
import org.uengine.codi.mw3.Login;
import org.uengine.codi.mw3.filter.AllSessionFilter;
import org.uengine.kernel.Role;

public class Follower extends Database<IFollower> implements IFollower {	
	
	@AutowiredFromClient
	public Session session;
	
	String parentType;
		public String getParentType() {
			return parentType;
		}
		public void setParentType(String parentType) {
			this.parentType = parentType;
		}

	String parentId;
		public String getParentId() {
			return parentId;
		}
		public void setParentId(String parentId) {
			this.parentId = parentId;
		}

	String endpoint;
		public String getEndpoint() {
			return endpoint;
		}
		public void setEndpoint(String endpoint) {
			this.endpoint = endpoint;
		}

	String resName;
		public String getResName() {
			return resName;
		}
		public void setResName(String resName) {
			this.resName = resName;
		}

	int assigntype;
		public int getAssigntype() {
			return assigntype;
		}
		public void setAssigntype(int assigntype) {
			this.assigntype = assigntype;
		}

	IUser user;
		public IUser getUser() {
			return user;
		}
		public void setUser(IUser user) {
			this.user = user;
			
			if(this.getUser() != null)
				this.setEndpoint(user.getUserId());
		}
	
	IDept dept;
		public IDept getDept() {
			return dept;
		}
		public void setDept(IDept dept) {
			this.dept = dept;
		}
		
	boolean enablePush;
		public boolean isEnablePush() {
			return enablePush;
		}
		public void setEnablePush(boolean enablePush) {
			this.enablePush = enablePush;
		}
	
	public Follower(){
		this.setEnablePush(true);
	}
	
	public IFollower find() throws Exception {
		throw new Exception("not defined exist method");
	}

	public void put() throws Exception {
		throw new Exception("not defined put method");
	}
	
	public void put(IUser user) throws Exception {
		this.setUser(user);
		this.setAssigntype(Role.ASSIGNTYPE_USER);
		this.put();
	}
	public void put(IDept dept) throws Exception {
		this.setDept(dept);
		this.setEndpoint(dept.getPartCode());
		this.setResName(dept.getPartName());
		this.setAssigntype(Role.ASSIGNTYPE_DEPT);
		this.put();
	}
	
	public void delegate() throws Exception {
		throw new Exception("not defined delegate method");
	}
	
	public void delegate(IUser user) throws Exception {
		this.setUser(user);
		this.setEndpoint(user.getUserId());
		this.setAssigntype(Role.ASSIGNTYPE_USER);
		this.delegate();
	}
	
	public void delegate(IDept dept) throws Exception {
		this.setDept(dept);
		this.setEndpoint(dept.getPartCode());
		this.setResName(dept.getPartName());
		this.setAssigntype(Role.ASSIGNTYPE_DEPT);
		this.delegate();
	}
	
	public IFollower findFollowers() throws Exception {
		throw new Exception("not defined findFollowers method");
	}

	public IContact findContacts(String keyword) throws Exception {
		throw new Exception("not defined findContacts(String keyword) method");
	}
	public IDept findDepts(String keyword) throws Exception {
		throw new Exception("not defined findContacts(String keyword) method");
	}

	@Override
	public Object[] detail() throws Exception {
		session.setClipboard(this);
		
		if("instance".equals(this.getParentType())){
			Instance instance = new Instance();
			instance.setInstId(Long.valueOf(this.getParentId()));
			instance.session = session;
			instance.copyFrom(instance.databaseMe());
			
			if( instance.getIsDeleted() ){
				throw new MetaworksException("$alreadyDeletedPost");
			}
			if(!instance.checkAuth()){
				throw new MetaworksException("$NotPermittedToWork");
			}
		}
		
		if(Role.ASSIGNTYPE_USER == this.getAssigntype()){
			User convertUser = new User();
			convertUser.copyFrom(this.getUser());
			convertUser.session =  session;
			
			return new Object[]{new Refresh(session), convertUser.detail()};
		}else if(Role.ASSIGNTYPE_DEPT == this.getAssigntype()){
			Dept convertDept = new Dept();
			convertDept.copyFrom(this.getDept());
			convertDept.session = session;
			
			return new Object[]{new Refresh(session), convertDept.detail()};
		}else
			return null;
	}
	
	@Override
	public Object[] addFollower() throws Exception {
		this.put();
		
		return new Object[]{ new ToEvent(ServiceMethodContext.TARGET_OPENER, EventContext.EVENT_CHANGE), new Remover(ServiceMethodContext.TARGET_SELF)};	
	}
	
	@Override
	public Object[] removeFollower() throws Exception {
		this.delegate();
		
		return new Object[]{ new ToEvent(ServiceMethodContext.TARGET_OPENER, EventContext.EVENT_CHANGE) };
	}

	@Override
	public Object[] popupAddFollower() throws Exception {
		Popup popup = new Popup(400,400);
		popup.setWidth(400);
		popup.setHeight(400);

		// copy to follower for addFollower
		session.setClipboard(this);
		
		popup.setPanel(this.makeFollowerPanel(session, this));

		return new Object[]{ new Refresh(session), popup }; 
	}
	
	public AddFollowerPanel makeFollowerPanel(Session session, Follower follower) throws Exception{
		AddFollowerPanel addFollowerPanel = new AddFollowerPanel(session, this);
		return addFollowerPanel;
	}
	
	public void addPushListener(){
		/*
		 * event 순서 문제 해결
		 * 
		 * desc : flushDatabaseMe() 에서 beforeCommit 이벤트를 발생 시켜 create 및 update 를 실행하는데
		 * push 가 create 및 update 보다 더 빨리 실행 되여 발생하여 afterCommit 시  push 되도록 수정
		 */
		final Follower pushFollower = this;
		
		TransactionListener flusher = new TransactionListener(){
			public void beforeCommit(TransactionContext tx) throws Exception {
				// TODO Auto-generated method stub
			}

			public void beforeRollback(TransactionContext tx) throws Exception {
				// TODO Auto-generated method stub
			}

			public void afterCommit(TransactionContext tx) throws Exception {
				// TODO Auto-generated method stub
				System.out.println("afterCommit");
				pushFollower.push();
			}

			public void afterRollback(TransactionContext tx) throws Exception {
				// TODO Auto-generated method stub
			}
		};

		TransactionContext tx = TransactionContext.getThreadLocalInstance();
		tx.addTransactionListener(flusher);
	}
	
	public void push() throws Exception {
		
		if(this.isEnablePush()){
			
			// TODO 타 회사의 사용자도 리프레쉬가 되어야 한다.
			// 본인 이외에 다른 사용자에게 push
			MetaworksRemoteService.pushClientObjectsFiltered(
					new AllSessionFilter(Login.getSessionIdWithCompany(session.getEmployee().getGlobalCom())),
					new Object[]{new ToEvent(new Followers(this), EventContext.EVENT_CHANGE , true)});

			final Object[] returnObject = new Object[]{new ToEvent(new TodoBadge(), EventContext.EVENT_CHANGE)};
			
 			if(this.getAssigntype() == Role.ASSIGNTYPE_USER)
				MetaworksRemoteService.pushTargetClientObjects(Login.getSessionIdWithUserId(this.getEndpoint()), returnObject);
			else
				MetaworksRemoteService.pushClientObjectsFiltered(new AllSessionFilter(Login.getSessionIdWithDept(this.getEndpoint())), returnObject);
			
			// TODO 다른 테넌트일 경우는?? 체크하는 로직이 필요할듯
			// user 에 회사정보가 없어서 체크를 할 수가 없음
			// - 부서는 다른 회사를 넣을수가 없으니 빼야함
//			if(Role.ASSIGNTYPE_USER == this.getAssigntype()){
//				MetaworksRemoteService.pushTargetClientObjects(
//						Login.getSessionIdWithUserId(user.getUserId()),
//						new Object[]{new ToEvent(new TopicPerspective(), EventContext.EVENT_CHANGE)});
//			}
		}

	}

}
