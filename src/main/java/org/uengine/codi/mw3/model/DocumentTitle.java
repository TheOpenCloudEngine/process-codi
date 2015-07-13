package org.uengine.codi.mw3.model;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.Refresh;
import org.metaworks.Remover;
import org.metaworks.ServiceMethodContext;
import org.metaworks.ToAppend;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Available;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dao.DAOFactory;
import org.metaworks.dao.KeyGeneratorDAO;
import org.metaworks.dao.TransactionContext;
import org.metaworks.website.MetaworksFile;
import org.metaworks.widget.ModalWindow;
import org.uengine.codi.mw3.knowledge.TopicMapping;
import org.uengine.codi.mw3.knowledge.TopicPanel;



@Face(ejsPath="dwr/metaworks/genericfaces/FormFace.ejs", options={"fieldOrder"}, values={"name,description,documentSecuopt,logoFile"})
public class DocumentTitle implements ContextAware{
	
	@AutowiredFromClient
	transient public Session session;
	
	public DocumentTitle(){
		setMetaworksContext(new MetaworksContext());
	}
	
	MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}
	
	MetaworksFile logoFile;
		@Available(when={MetaworksContext.WHEN_NEW, MetaworksContext.WHEN_EDIT})
		public MetaworksFile getLogoFile() {
			return logoFile;
		}
		public void setLogoFile(MetaworksFile logoFile) {
			this.logoFile = logoFile;
		}	
		
	String id;
		@Hidden
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		
	String name;
		@Face(displayName="$Name")
		public String getName() {
				return name;
		}
		public void setName(String name) {
			this.name = name;
		}	
		
	String parentId;
		@Hidden
		public String getParentId() {
			return parentId;
		}
		public void setParentId(String parentId) {
			this.parentId = parentId;
		}
		
	String description;
		@Face(displayName="$description")
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		
	Date startDate;
		public Date getStartDate() {
			return startDate;
		}
		public void setStartDate(Date startDate) {
			this.startDate = startDate;
		}	
		
	boolean documentSecuopt;		
		@Face(displayName="$topicSecuopt")
		@Available(when={MetaworksContext.WHEN_NEW, MetaworksContext.WHEN_EDIT})
		public boolean isDocumentSecuopt() {
			return documentSecuopt;
		}
		public void setDocumentSecuopt(boolean documentSecuopt) {
			this.documentSecuopt = documentSecuopt;
		}

	public void saveMain() throws Exception{
		DocumentNode node = new DocumentNode();
		node.setId(node.makeId());
		node.setName(this.getName());
		node.setDescription(this.getDescription());
		node.setType(DocumentNode.TYPE_DOC);
		node.setSecuopt(documentSecuopt ? "1" : "0");
		node.setStartDate(Calendar.getInstance().getTime());		
//		node.setParentId("Main");	
		node.setParentId("tid_"+session.getCompany().getComCode());	
		node.setAuthorId(session.getUser().getUserId());		
		node.setCompanyId(session.getCompany().getComCode());
		node.getMetaworksContext().setWhen(this.getMetaworksContext().getWhen());
		
		if(this.getLogoFile().getUploadedPath() != null && this.getLogoFile().getFilename() != null){
			node.setUrl(this.getLogoFile().getUploadedPath());
			node.setThumbnail(this.getLogoFile().getFilename());
		}
		
		node.createDatabaseMe();
		node.flushDatabaseMe();
		
		this.setId(node.getId());
		
		TopicMapping tm = new TopicMapping();
		tm.setTopicId(node.getId());
		tm.setUserId(session.getUser().getUserId());
		tm.setUserName(session.getUser().getName());
		tm.setAssigntype(0);
		
		Map options = new HashMap();
		options.put("useTableNameHeader", "false");
		options.put("onlySequenceTable", "true");
		KeyGeneratorDAO kg = DAOFactory.getInstance(TransactionContext.getThreadLocalInstance()).createKeyGenerator("bpm_topicmapping", options);
		kg.select();
		kg.next();
		
		Number number = kg.getKeyNumber();
		
		tm.setTopicMappingId(number.longValue());
		tm.createDatabaseMe();
		tm.flushDatabaseMe();
		
	}
	DocumentNode node;
		public DocumentNode getNode() {
			return node;
		}
		public void setNode(DocumentNode node) {
			this.node = node;
		}
	public void saveSub() throws Exception{
		DocumentNode node = new DocumentNode();
		node.setId(node.getId());
		node.setName(this.getName());
		node.setDescription(this.getDescription());
		node.setType(DocumentNode.TYPE_DOC);
		node.setSecuopt(documentSecuopt ? "1" : "0");
		node.setStartDate(Calendar.getInstance().getTime());
		node.setParentId(this.getId());	
		node.setAuthorId(session.getUser().getUserId());		
		node.setCompanyId(session.getCompany().getComCode());
		node.getMetaworksContext().setWhen(this.getMetaworksContext().getWhen());
		if(this.getLogoFile().getUploadedPath() != null && this.getLogoFile().getFilename() != null){
			node.setUrl(this.getLogoFile().getUploadedPath());
			node.setThumbnail(this.getLogoFile().getFilename());
		}
		
		node.createMe();
		
		
		this.setId(node.getId());
		
		
		TopicMapping tm = new TopicMapping();
		tm.setTopicId(node.getId());
		tm.setUserId(session.getUser().getUserId());
		tm.setUserName(session.getUser().getName());
		tm.setAssigntype(0);
		
		Map options = new HashMap();
		options.put("useTableNameHeader", "false");
		options.put("onlySequenceTable", "true");
		KeyGeneratorDAO kg = DAOFactory.getInstance(TransactionContext.getThreadLocalInstance()).createKeyGenerator("bpm_topicmapping", options);
		kg.select();
		kg.next();
		
		Number number = kg.getKeyNumber();
		
		tm.setTopicMappingId(number.longValue());
		tm.createDatabaseMe();
	}
	
	@Face(displayName="$Save")
	@Available(when={MetaworksContext.WHEN_NEW})
	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_APPEND)
	public Object[] save() throws Exception{
		
		if(this.getLogoFile().getFileTransfer() != null &&
				this.getLogoFile().getFilename() != null && 
				this.getLogoFile().getFilename().length() > 0){			
			this.getLogoFile().upload();
		}
		
		if(MetaworksContext.WHEN_NEW.equals(this.getMetaworksContext().getWhen()) && 
				"sub".equals(this.getMetaworksContext().getHow())){
			
			this.saveSub();
			DocumentNode documentNode = new DocumentNode();
			documentNode.setId(this.getId());
			
			return new Object[]{new ToAppend(new DocumentPanel(), documentNode), new Refresh(this), new Remover(new ModalWindow())};
		}else if(MetaworksContext.WHEN_NEW.equals(this.getMetaworksContext().getWhen())){
			
			this.saveMain();
			
			
			
			DocumentNode documentNode = new DocumentNode();
			documentNode.setId(this.getId());
			documentNode.setName(this.getName());
			documentNode.session = session;
//			return new Object[]{new ToAppend(new DocumentPanel(), documentNode), new Refresh(this), new Remover(new ModalWindow())};
			
			Object[] returnObj = documentNode.loadDocument();
			Object[] returnObject = new Object[ returnObj.length + 2];
			for (int i = 0; i < returnObj.length; i++) {
				if( returnObj[i] instanceof InstanceListPanel){
					returnObject[i] = new Refresh(returnObj[i]);
				}else{
					returnObject[i] = new Refresh(returnObj[i]);
				}			
			}
			
//			DocumentPanel documentPanel = new DocumentPanel();
//			documentPanel.session = session;
//			documentPanel.load();
//			returnObject[returnObj.length ] = new Refresh(documentPanel);
			
			returnObject[returnObj.length ] = new ToAppend(new DocumentPanel(), documentNode);
			returnObject[returnObj.length + 1] = new Remover(new ModalWindow());
			return returnObject;

			
		}
		
		return null;
	}
	@Face(displayName="$Save")
	@Available(when={MetaworksContext.WHEN_EDIT})
	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_APPEND)
	public Object[] modify() throws Exception{
		
		if(this.getLogoFile().getFileTransfer() != null &&
				this.getLogoFile().getFilename() != null && 
				this.getLogoFile().getFilename().length() > 0){			
			this.getLogoFile().upload();
		}
		
		DocumentNode node = new DocumentNode();
		node.setId(this.getId());
		
		node.copyFrom(node.databaseMe());
		
		if(this.getLogoFile().getUploadedPath() != null && this.getLogoFile().getFilename() != null){
			node.setUrl(this.getLogoFile().getUploadedPath());
			node.setThumbnail(this.getLogoFile().getFilename());
		}
		
		node.setName(this.getName());
		node.setDescription(this.getDescription());
		node.setSecuopt(documentSecuopt ? "1" : "0");
		node.saveMe();
		return new Object[]{ new Refresh(node), new Remover(new ModalWindow())};
	}
	
}
