package org.uengine.codi.mw3.model;

import org.metaworks.Remover;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dao.Database;
import org.springframework.beans.factory.annotation.Autowired;
import org.uengine.kernel.EJBProcessInstance;
import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.RoleMapping;
import org.uengine.processmanager.ProcessManagerRemote;

public class ProcessDefinition extends Database<IProcessDefinition> implements IProcessDefinition{
	
	String author;
		public String getAuthor() {
			return author;
		}
		public void setAuthor(String author) {
			this.author = author;
		}

	Long defId;
		public Long getDefId() {
			return defId;
		}
		public void setDefId(Long defId) {
			this.defId = defId;
		}
	
	String description;
		public String getDescription() {
			return description;
		}
		
		public void setDescription(String description) {
			this.description = description;
		}
		

	boolean isFolder;
		public boolean getIsFolder() {
			return isFolder;
		}
		public void setIsFolder(boolean isFolder) {
			this.isFolder = isFolder;
		}
	
	boolean isAdhoc;



	String parentFolder;
		public String getParentFolder() {
			return parentFolder;
		}
	
		public void setParentFolder(String parentFolder) {
			this.parentFolder = parentFolder;
		}

	int prodVer;
	
	String objType;



	Long prodVerId;

	String name;

	boolean isDeleted;

	boolean isVisible;
	
	String alias;

	Long superDefId;

	String comCode;
	
	





	public int getProdVer() {
		return prodVer;
	}

	public void setProdVer(int prodVer) {
		this.prodVer = prodVer;
	}

	public String getObjType() {
		return objType;
	}

	public void setObjType(String objType) {
		this.objType = objType;
	}

	public Long getProdVerId() {
		return prodVerId;
	}

	public void setProdVerId(Long prodVerId) {
		this.prodVerId = prodVerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Long getSuperDefId() {
		return superDefId;
	}

	public void setSuperDefId(Long superDefId) {
		this.superDefId = superDefId;
	}

	public String getComCode() {
		return comCode;
	}

	public void setComCode(String comCode) {
		this.comCode = comCode;
	}
	
	public boolean getIsAdhoc() {
		return isAdhoc;
	}

	public void setIsAdhoc(boolean isAdhoc) {
		this.isAdhoc = isAdhoc;
	}

	public boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public boolean getIsVisible() {
		return isVisible;
	}

	public void setIsVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public IProcessDefinition findAll() throws Exception{
		
		String whereClause = "";
		if(getMetaworksContext()!=null && "newInstance".equals(getMetaworksContext().getWhen()))
			whereClause = " and (objType='process' or objType='folder')";
		
		IProcessDefinition procDefs = sql("select * from bpm_procdef where isdeleted=0 and parentfolder=?parentfolder" + whereClause);// where comcode=?comcode");
		//procDefs.s  //TODO: need to narrow by comcode; comcode may be obtained by MetaworksContext 'who' => multi-tenancy issue
		procDefs.setParentFolder(getParentFolder());
		procDefs.select();
		
		return procDefs;
	}
	
	public void drillDown() throws Exception{
		
		if(childs!=null) childs = null;
		else{
		//if(getIsFolder()){ //this will not tell right value since it would be called by key object not the full object. it would be set by the ejs face will not create "drillDown" button.
			ProcessDefinition child = new ProcessDefinition();
			child.setParentFolder(getDefId().toString());
			setIsFolder(true);
			child.setMetaworksContext(getMetaworksContext());
			
			setChilds(child.findAll()); //this lets drill down
			getChilds().setMetaworksContext(getMetaworksContext());
		//}
		}
	}

	@Autowired
	public InstanceViewContent instanceViewContent;
	
	@Override
	public Object[] initiate() throws Exception {
		
		String prodVerId = processManager.getProcessDefinitionProductionVersion(getDefId().toString());
		String instId = processManager.initializeProcess(prodVerId);
		
		RoleMapping rm = RoleMapping.create();
		if(session.getUser() != null)
			rm.setEndpoint(session.getUser().getUserId());
		
		processManager.setLoggedRoleMapping(rm);
		
		Instance instanceRef = new Instance();
		instanceRef.setInstId(new Long(instId));
		
		

		if(newInstancePanel!=null){
			
			if(newInstancePanel.getKnowledgeNodeId() != null){

				processManager.executeProcess(instId);
				processManager.applyChanges();

				InstanceViewContent instanceView = instanceViewContent;// = new InstanceViewContent();
				instanceView.load(instanceRef);

				
				
				/*
				WorkflowyNode parent = new WorkflowyNode(newInstancePanel.getKnowledgeNodeId());
				parent.load();
				
				WorkflowyNode child = new WorkflowyNode(WorkflowyNode.makeId());
				child.setName(instanceView.instanceName);
				child.setLinkedInstId(instId);
				parent.addChildNode(child);
				
				child.save();
				*/
			

				return new Object[]{instanceView};
				
				
			}else if(newInstancePanel.getParentInstanceId() != null){ //need to attach new instance to the parent instance
				ProcessInstance instanceObject = processManager.getProcessInstance(instId);
				
				//IInstance instance = instanceRef.databaseMe();
			
				EJBProcessInstance ejbParentInstance = (EJBProcessInstance)instanceObject;
				ejbParentInstance.getProcessInstanceDAO().setRootInstId(new Long(newInstancePanel.getParentInstanceId()));
				
				Instance rootInstanceRef = new Instance();
				rootInstanceRef.setInstId(new Long(newInstancePanel.getParentInstanceId()));
				
				processManager.executeProcess(instId);
				processManager.applyChanges();
			
				InstanceViewContent rootInstanceView = instanceViewContent;// = new InstanceViewContent();
				rootInstanceView.load(rootInstanceRef);
				
				

				return new Object[]{rootInstanceView, new Remover(new Popup())};
				
			}

		}

		
		processManager.executeProcess(instId);
		processManager.applyChanges();
	

		InstanceViewContent instanceView = instanceViewContent;// = new InstanceViewContent();
		instanceView.load(instanceRef);

		InstanceListPanel instanceList = new InstanceListPanel(session); //should return instanceListPanel not the instanceList only since there're one or more instanceList object in the client-side
		//instanceList.load(session.login, session.navigation);
		return new Object[]{instanceView, instanceList};

		
	}
	
	@AutowiredFromClient
	public NewInstancePanel newInstancePanel;
	


	@Override
	public ContentWindow design() throws Exception {

		return null;
	}
	
	@ServiceMethod(target=ServiceMethodContext.TARGET_POPUP)
	public Popup contextMenu(){
		Popup contextMenuPopup = new Popup();
		contextMenuPopup.setName("");
		
		ProcessDefinitionContextMenu contextMenu = new ProcessDefinitionContextMenu();
		contextMenu.setDefId(getDefId().toString());
		
		contextMenuPopup.setPanel(contextMenu);
		
		return contextMenuPopup;
	}


	
	
	@AutowiredFromClient
	public Session session;
	
	
	@Autowired
	public ProcessManagerRemote processManager;

	IProcessDefinition childs;
		public IProcessDefinition getChilds() {
			return childs;
		}
		public void setChilds(IProcessDefinition childs) {
			this.childs = childs;
		}
		

}
