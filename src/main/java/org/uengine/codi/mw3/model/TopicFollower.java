package org.uengine.codi.mw3.model;

import org.metaworks.EventContext;
import org.metaworks.ToEvent;
import org.metaworks.dwr.MetaworksRemoteService;
import org.uengine.codi.mw3.Login;
import org.uengine.codi.mw3.filter.AllSessionFilter;
import org.uengine.codi.mw3.knowledge.TopicMapping;
import org.uengine.kernel.Role;

public class TopicFollower extends Follower {

	public TopicFollower(){
		this.setParentType(TYPE_TOPIC);
	}

	@Override
	public void put() throws Exception {
		TopicMapping tm = new TopicMapping();
		tm.setTopicId(this.getParentId());
		tm.setAssigntype(this.getAssigntype());
		if(Role.ASSIGNTYPE_USER == this.getAssigntype()){
			tm.setUserId(user.getUserId());
			tm.setUserName(user.getName());
		}else if(Role.ASSIGNTYPE_DEPT == this.getAssigntype()){
			tm.setUserId(dept.getPartCode());
			tm.setUserName(dept.getPartName());
		}
		
		// not exist save topicmapping
		if( !tm.findByUser().next() ){
			tm.saveMe();
			this.addPushListener();
		}
	}

	@Override
	public void delegate() throws Exception {
		TopicMapping tm = new TopicMapping();
		tm.setTopicId(this.getParentId());
		if(Role.ASSIGNTYPE_USER == this.getAssigntype()){
			tm.setUserId(user.getUserId());
		}else if(Role.ASSIGNTYPE_DEPT == this.getAssigntype()){
			tm.setUserId(dept.getPartCode());
		}
		
		tm.removeMe();
		this.push();
	}
	
	@Override
	public IFollower findFollowers() throws Exception {
		TopicMapping tm = new TopicMapping();
		tm.setTopicId(this.getParentId());

		IFollower follower = tm.findFollowers();
		follower.getMetaworksContext().setWhere(WHERE_FOLLOWER);
		
		return follower;
	}
	
	@Override
	public AddFollowerPanel makeFollowerPanel(Session session, Follower follower) throws Exception{
		AddFollowerPanel addFollowerPanel = new AddFollowerPanel(session, this);
		addFollowerPanel.loadDept(session, follower);
		return addFollowerPanel;
	}
	
	@Override
	public IContact findContacts(String keyword) throws Exception {
		return Contact.findContactsForTopic(this.getParentId(), session.getUser(), keyword);
	}

	@Override
	public IDept findDepts(String keyword) throws Exception {
		Dept dept = new Dept();
		dept.setGlobalCom(session.getEmployee().getGlobalCom());
		
		return dept.findDeptForTopic(this.getParentId(), keyword);
	}
	
	@Override
	public void push() throws Exception {
		super.push();
		if(this.isEnablePush()){
			if(Role.ASSIGNTYPE_USER == this.getAssigntype()){
				MetaworksRemoteService.pushTargetClientObjects(
						Login.getSessionIdWithUserId(user.getUserId()),
						new Object[]{new ToEvent(new TopicPerspective(), EventContext.EVENT_CHANGE)});
			}else if(Role.ASSIGNTYPE_DEPT == this.getAssigntype()){
				MetaworksRemoteService.pushClientObjectsFiltered(
						new AllSessionFilter(Login.getSessionIdWithDept(dept.getPartCode())),
						new Object[]{new ToEvent(new TopicPerspective(), EventContext.EVENT_CHANGE)});
			}
		}
		
	}
}
