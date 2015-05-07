package org.uengine.codi.mw3.model;

import org.metaworks.MetaworksContext;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.website.MetaworksFile;
import org.uengine.codi.mw3.knowledge.TopicNode;
import org.uengine.codi.mw3.knowledge.TopicTitle;


public class GroupInfo extends FollowerPerspectiveInfo{
	public final static int MODIFY_POPUP_HEIGHT = 200;
	public final static int MODIFY_POPUP_WIDTH = 500;
	
	String secuopt;
		public String getSecuopt() {
			return secuopt;
		}
		public void setSecuopt(String secuopt) {
			this.secuopt = secuopt;
		}
		
	public GroupInfo(){
		
	}
	
	public GroupInfo(Session session, String topicId) throws Exception {
		super(session, Perspective.TYPE_NEWSFEED);
		
		this.setId(topicId);
		this.load();
	}
	
	@Override
	public void load() throws Exception {
		TopicFollower follower = new TopicFollower();
		follower.setParentId(this.getId());
		
		this.setFollower(follower);
		
		super.load();
	}
	
	@Override
	public Popup modify() throws Exception {
		TopicNode topicNode = new TopicNode();
		topicNode.setId(this.getId());
		topicNode.copyFrom(topicNode.databaseMe());
		
		TopicTitle topicTitle = new TopicTitle();
		topicTitle.setTopicId(this.getId());
		topicTitle.setTopicTitle(topicNode.getName());
		topicTitle.setTopicSecuopt("0".equals(topicNode.getSecuopt()) ? false : true );
		topicTitle.setLogoFile(new MetaworksFile());
		topicTitle.setMetaworksContext(new MetaworksContext());
		topicTitle.getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);
		topicTitle.session = session;
		
		return new Popup(MODIFY_POPUP_WIDTH, MODIFY_POPUP_HEIGHT, topicTitle);
	}
	
	@ServiceMethod(callByContent=true, except="followers", target=ServiceMethodContext.TARGET_APPEND, needToConfirm=true)
	public Object[] remove() throws Exception {
		TopicNode deletedNode = new TopicNode();
		deletedNode.setId(this.getId());
		deletedNode.copyFrom(deletedNode.databaseMe());
		deletedNode.deleteDatabaseMe();
		
		return super.remove();
	}
	
}
