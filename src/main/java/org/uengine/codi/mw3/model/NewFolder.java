package org.uengine.codi.mw3.model;

import java.io.File;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.Remover;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.widget.ModalWindow;
import org.springframework.beans.factory.annotation.Autowired;
import org.uengine.codi.mw3.CodiClassLoader;
import org.uengine.processmanager.ProcessManagerRemote;

public class NewFolder implements ContextAware{
	
	public NewFolder(){
		setMetaworksContext(new MetaworksContext());
		getMetaworksContext().setWhen("edit");
	}
	
	MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}

	String folderName;
	
		public String getFolderName() {
			return folderName;
		}
	
		public void setFolderName(String folderName) {
			this.folderName = folderName;
		}

	String parentFolderDefId;
	@Hidden
		public String getParentFolderDefId() {
			return parentFolderDefId;
		}	
		public void setParentFolderDefId(String parentFolderDefId) {
			this.parentFolderDefId = parentFolderDefId;
		}

	@AutowiredFromClient
	public Session session;
		
	@ServiceMethod(callByContent = true)
	public Object[] create() throws Exception{
		/*
		String parentId = getParentFolderDefId();
		processManager.addFolder(getFolderName(), parentId);
		processManager.applyChanges();
				
		String resourceBase = CodiClassLoader.getMyClassLoader().sourceCodeBase() + "/";

		
		File file = new File(resourceBase + "/" + getParentFolderDefId() + "/" + getFolderName());
		file.mkdirs();
		
		if(parentId != null){
			ProcessDefinition parentForRefresh = new ProcessDefinition();
			parentForRefresh.setDefId(new Long(parentId));		
			parentForRefresh.drillDown();
			parentForRefresh.setName(parentForRefresh.databaseMe().getName());
			parentForRefresh.setObjType("folder");
			
			return new Object[]{parentForRefresh, this};
		}else{
			ResourcePanel resourcePanel = new ResourcePanel(session.getUser());
			
			return new Object[]{resourcePanel, this};
		}
		*/
		
		
		String resourceBase = CodiClassLoader.getMyClassLoader().getCodebase() + "/";

		
		File file = new File(resourceBase + "/" + getParentFolderDefId() + "/" + getFolderName());
		file.mkdirs();
		
		String parentName = getParentFolderDefId();
		
		if(parentName.lastIndexOf("/") != -1){
			parentName = parentName.substring(parentName.lastIndexOf("/") + 1);
		}
		
		ResourceFile parent = new ResourceFile();
		
		parent.setName(parentName);
		parent.setFolder(true);
		parent.getMetaworksContext().setWhere("design");
		parent.getMetaworksContext().setWhen("design");

		parent.setAlias(getParentFolderDefId());
		parent.drillDown();
			
		return new Object[]{parent, new Remover(new ModalWindow())};
	}
	
	@Autowired
	public ProcessManagerRemote processManager;
}
