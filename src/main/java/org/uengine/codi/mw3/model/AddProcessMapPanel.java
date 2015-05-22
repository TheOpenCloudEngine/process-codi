package org.uengine.codi.mw3.model;

import org.metaworks.MetaworksContext;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;
import org.oce.garuda.multitenancy.TenantContext;
import org.uengine.codi.mw3.CodiClassLoader;

@Face(ejsPath="dwr/metaworks/genericfaces/Tab.ejs")
public class AddProcessMapPanel {

	ResourceFile processDefinitions;
		@Face(displayName="$FileResource")
		public ResourceFile getProcessDefinitions() {
			return processDefinitions;
		}
		public void setProcessDefinitions(ResourceFile processDefinitions) {
			this.processDefinitions = processDefinitions;
		}

	@AutowiredFromClient
	public Session session;
		
	public void load() throws Exception{
		
		ResourceFile processDefinitions = new ResourceFile();
		
		processDefinitions.setMetaworksContext(new MetaworksContext());	
		processDefinitions.getMetaworksContext().setWhen("appendProcessMap");

		processDefinitions.setFolder(true);
		processDefinitions.setAlias(CodiClassLoader.getProjectPathOfTenant(CodiClassLoader.getCodiId()));
		processDefinitions.setName("/");
		processDefinitions.setuEngineAlias("");
		processDefinitions.session = session;
		processDefinitions.drillDown();
		
		this.setProcessDefinitions(processDefinitions);
		
		// TODO: 작업해야함
		
//		MetadataProperty metadataTree = new MetadataProperty();
//		metadataTree.setMetaworksContext(new MetaworksContext());
//		metadataTree.getMetaworksContext().setHow("appendProcessMap");
//		metadataTree.getMetaworksContext().setWhen(MetaworksContext.WHEN_VIEW);
//		
//		metadataTree.setName("process");
//		metadataTree.setId("process");
//		metadataTree.setType("folder");
//		metadataTree.setChild(new ArrayList<MetadataProperty>());
//		
//
//		// load metadata
//		String sourceCodeBase = CodiClassLoader.mySourceCodeBase();
//		String metadataFileName = "uengine.metadata";
//		String metadataFilePath = sourceCodeBase + File.separatorChar + metadataFileName;
//		MetadataXML metadataXML = new MetadataXML();
//		metadataXML = metadataXML.loadWithPath(metadataFilePath);
//
//		if( metadataXML != null ){
//			for(MetadataProperty metadataProperty : metadataXML.getProperties()){
//				if(MetadataProperty.PROCESS_PROP.equals(metadataProperty.getType())){
//					metadataProperty.setMetaworksContext(new MetaworksContext());
//					metadataProperty.getMetaworksContext().setHow("appendProcessMap");
//					
//					metadataTree.getChild().add(metadataProperty);
//				}
//			}
//		}
//		
//		this.setMetadataTree(metadataTree);
	}
	
}
