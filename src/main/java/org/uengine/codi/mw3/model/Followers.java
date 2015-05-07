package org.uengine.codi.mw3.model;

import org.metaworks.ContextAware;
import org.metaworks.EventContext;
import org.metaworks.MetaworksContext;
import org.metaworks.MetaworksException;
import org.metaworks.ServiceMethodContext;
import org.metaworks.ToEvent;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.ServiceMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.uengine.processmanager.ProcessManagerRemote;

public class Followers implements ContextAware {
	static final String CONTEXT_WHERE_INFOLLOWERS = "followers";
	static final String CONTEXT_WHERE_DEPTFOLLOWERS = "deptFollower";
	static final String CONTEXT_WHERE_ROLEFOLLOWERS = "roleFollowers";
	
	static final String ADD_INSTANCEFOLLOWERS = "addInstanceFollower";
	static final String ADD_TOPICFOLLOWERS = "addTopicFollower";
	static final String ADD_ROLEFOLLOWERS = "addRoleFollower";
	static final String ADD_DEPTFOLLOWERS = "addDeptFollower";
	static final String ADD_DOCUMENTFOLLOWERS = "addDocumentFollower";
	static final String ADD_ETCFOLLOWERS = "addEtcFollower";
	
	static final String ADD_NOTI = "add";
	static final String REMOVE_NOTI = "remove";
	

	String id;
		@Id
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		
	IFollower followers;
		@Face(ejsPath="genericfaces/ArrayFace.ejs", options={"alignment"}, values={"horizontal"})
		public IFollower getFollowers() {
			return followers;
		}
		public void setFollowers(IFollower followers) {
			this.followers = followers;
		}
		
	Follower follower;
		public Follower getFollower() {
			return follower;
		}
		public void setFollower(Follower follower) {
			this.follower = follower;
		}

	transient MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}
		
	public Followers(){
		setMetaworksContext(new MetaworksContext());
	}
	
	public Followers(Follower follower){
		this.setFollower(follower);
		
		this.setId(follower.getParentType() + "_" + follower.getParentId());
	}
	
	public void load() throws Exception {
		if(this.getFollower() == null)
			throw new Exception("follower is null");
		
		this.setFollowers(this.getFollower().findFollowers());
	}
	
	public void addFollower(IUser user) throws Exception {
		
	}

	public void addFollower(DeptTreeNode dept) throws Exception {
		
	}
	
	public void removeFollower(IUser user) throws Exception {
		
	}
	
	public void removeFollower(DeptTreeNode dept) throws Exception {
		
	}
	
	public boolean existFollower(String endpoint, int assignType) throws Exception {
		
		boolean isExist = false;
		
		IFollower followers = this.getFollowers();
		followers.beforeFirst();
		while(followers.next()){
			if(endpoint.equals(followers.getEndpoint()) && assignType == followers.getAssigntype()){
				isExist = true;
				
				break;
			}
		}
		followers.beforeFirst();
		
		return isExist;
	}
	
	@ServiceMethod(callByContent=true, except="followers", target=ServiceMethodContext.TARGET_STICK)
	public Object[] popupAddFollowers() throws Exception {
		
		if(this.getFollower() == null)
			throw new Exception("follower is null");

		this.getFollower().session = session;
		
		return new Object[]{this.getFollower().popupAddFollower()};
	}
	
	@ServiceMethod(callByContent=true, except="followers", target=ServiceMethodContext.TARGET_APPEND, mouseBinding=EventContext.EVENT_DROP)
	public Object drop() throws Exception{
		
		if("instance".equals(this.getFollower().getParentType())){
			Instance instance = new Instance();
			instance.setInstId(Long.valueOf(this.getFollower().getParentId()));
			instance.session = session;
			instance.copyFrom(instance.databaseMe());
			
			if(!instance.checkAuth()){
				throw new MetaworksException("$NotPermittedToWork");
			}
			if( instance.getIsDeleted() ){
				throw new MetaworksException("$alreadyDeletedPost");
			}
		}
		Object clipboard = session.getClipboard();
		if(clipboard instanceof IUser){
			User newFollowUser = (User)clipboard;
		
			this.getFollower().session = session;
			this.getFollower().put(newFollowUser);
		}
		
		return null;
	}
	
	@Hidden
	@ServiceMethod(callByContent=true, except="followers", eventBinding=EventContext.EVENT_CHANGE, bindingHidden=true)
	public void refresh() throws Exception {
		this.load();
	}
	
	@AutowiredFromClient
	public Session session;
	
	@Autowired
	public ProcessManagerRemote processManager;
	
	@AutowiredFromClient(onDrop=true)
	public IUser user;
}
