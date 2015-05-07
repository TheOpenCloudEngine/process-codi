package org.uengine.codi.mw3.model;

import java.util.Date;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.AutowiredToClient;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;
import org.metaworks.dao.MetaworksDAO;
import org.metaworks.dao.TransactionContext;
import org.metaworks.widget.Choice;
import org.uengine.codi.mw3.knowledge.ITopicNode;


public class NewInstancePanel implements ContextAware {
	
	public NewInstancePanel(){
		setMetaworksContext(new MetaworksContext());
		getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);
	}
	
	@AutowiredFromClient
	public Session session;
	
	String title;
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}

	WorkItem newInstantiator;
		public WorkItem getNewInstantiator() {
			return newInstantiator;
		}
	
		public void setNewInstantiator(WorkItem newInstantiator) {
			this.newInstantiator = newInstantiator;
		}
		
	Choice securityLevel;
		@Face(ejsPath="dwr/metaworks/org/uengine/codi/mw3/model/SecurityLevelRadioButton.ejs")
		public Choice getSecurityLevel() {
			return securityLevel;
		}
		public void setSecurityLevel(Choice securityLevel) {
			this.securityLevel = securityLevel;
		}

	InstanceBackground instanceBackground; 
		public InstanceBackground getInstanceBackground() {
			return instanceBackground;
		}
	
		public void setInstanceBackground(InstanceBackground instanceBackground) {
			this.instanceBackground = instanceBackground;
		}
		
	ProcessMapPanel processMapPanel;
		public ProcessMapPanel getProcessMapPanel() {
			return processMapPanel;
		}
		public void setProcessMapPanel(ProcessMapPanel processMapPanel) {
			this.processMapPanel = processMapPanel;
		}	
		
	String topicNodeId;
		public String getTopicNodeId() {
			return topicNodeId;
		}
		public void setTopicNodeId(String topicNodeId) {
			this.topicNodeId = topicNodeId;
		}

	String knowledgeNodeId;
		@Hidden
		public String getKnowledgeNodeId() {
			return knowledgeNodeId;
		}
		public void setKnowledgeNodeId(String knowledgeNodeId) {
			this.knowledgeNodeId = knowledgeNodeId;
		}
		
	String parentInstanceId;
		@Hidden
		public String getParentInstanceId() {
			return parentInstanceId;
		}
		public void setParentInstanceId(String parentInstanceId) {
			this.parentInstanceId = parentInstanceId;
		}
		
	MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}	
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}
	
		
	public void load(Session session) throws Exception{

		String title = "새로쓰기";

		if(Perspective.MODE_TOPIC.equals(session.getLastPerspecteMode()) ||
				   Perspective.MODE_PROJECT.equals(session.getLastPerspecteMode())){
			this.setTopicNodeId(session.getLastSelectedItem());
			title = "'" + session.getWindowTitle() + "' 에 새로쓰기";
		}
		setTitle(title);

		if("UnlabeledDocument".equals(session.getLastPerspecteType())){
			newInstantiator = new DocWorkItem();
			newInstantiator.setFolderId(getTopicNodeId());
			newInstantiator.setType(IWorkItem.WORKITEM_TYPE_FILE);
		}else{
			newInstantiator = new CommentWorkItem();
		}
		
		User writer = new User();
		writer.copyFrom(session.getUser());
		writer.setMetaworksContext(new MetaworksContext());
		
		newInstantiator.session = session;
		newInstantiator.setWriter(writer);
	
		processMapPanel = new ProcessMapPanel();		
		processMapPanel.setMetaworksContext(this.getMetaworksContext());
		processMapPanel.load(session);
		
		Choice securityLevel = new Choice(); 
		securityLevel.add("$Privacy.Normal","0");
		securityLevel.add("$Privacy.OnlyFollowers","1");
		setSecurityLevel(securityLevel);
	
		/*
		 * default security level
		 * 
		 * 1. level 정보
		 *  - 0 : 모두 공개
		 *  - 1 : 팔로워만 공개
		 *  - 3 : topic 맴버 및 팔로워 만 공개
		 *  
		 * 2. topic 일 경우 
		 *  - 1 : 공개 토픽 
		 *  - 3 : 비공개 토픽
		 */
		
		/*
		if("topic".equals(session.getLastPerspecteType())){
			StringBuffer sb = new StringBuffer();
			sb.append("select * from bpm_knol knol");
			sb.append(" where 	knol.type = ?type");
			sb.append(" and 		knol.id = ?topicId ");
			
			ITopicNode dao = (ITopicNode)MetaworksDAO.createDAOImpl(TransactionContext.getThreadLocalInstance(), sb.toString(), ITopicNode.class); 
			dao.set("type", "topic");
			dao.set("topicId", session.getLastSelectedItem());
			dao.select();
			if( dao.next() ){
				if( dao.getSecuopt() != null && dao.getSecuopt().equals("1") ){	// 비공개토픽
					securityLevel.add("$Privacy.Topic","3");
					securityLevel.setSelected("3");
				}else{	// 공개 토픽
					securityLevel.setSelected("0");
				}
			}
			
		}else if("document".equals(session.getLastPerspecteType())){
			StringBuffer sb = new StringBuffer();
			sb.append("select * from bpm_knol knol");
			sb.append(" where 	knol.type = ?type");
			sb.append(" and 		knol.id = ?topicId ");
			
			ITopicNode dao = (ITopicNode)MetaworksDAO.createDAOImpl(TransactionContext.getThreadLocalInstance(), sb.toString(), ITopicNode.class); 
			dao.set("type", "doc");
			dao.set("topicId", session.getLastSelectedItem());
			dao.select();
			if( dao.next() ){
				if( dao.getSecuopt() != null && dao.getSecuopt().equals("1") ){	// 비공개토픽
					securityLevel.add("$Privacy.Topic","3");
					securityLevel.setSelected("3");
				}else{	// 공개 토픽
					securityLevel.setSelected("0");
				}
			}
			
		}else{
			securityLevel.setSelected("0");
		}
		
		setSecurityLevel(securityLevel);
		*/
	}
}
