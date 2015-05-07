package org.uengine.codi.mw3.model;

import java.rmi.RemoteException;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.ServiceMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.uengine.processmanager.ProcessManagerRemote;

@Component
public class ProcessDesignerContentPanel extends ContentWindow implements ContextAware {
	
	public void newProcessDefinition(String parentFolder) throws Exception{
		setParentFolder(parentFolder);
	}

	public void load(String defId) throws Exception{
		String defVerId = processManager.getProcessDefinitionProductionVersion(defId);
		
	    

		setChartHTML("chartHTML");
		setDefVerId(defVerId);
		setDefId(defId);
		
	//	setDefinitionString(processManager.getProcessDefinition(defVerId, "UTF-8"));
		
	//	processManager.remove();
	}
	
	String chartHTML;
		public String getChartHTML() {
			return chartHTML;
		}
		public void setChartHTML(String chartHTML) {
			this.chartHTML = chartHTML;
		}
		
	String parentFolder;
		public String getParentFolder() {
			return parentFolder;
		}
		public void setParentFolder(String parentFolder) {
			this.parentFolder = parentFolder;
		}
		
	String defId;
		public String getDefId() {
			return defId;
		}
		public void setDefId(String defId) {
			this.defId = defId;
		}
	
	String defVerId;
		public String getDefVerId() {
			return defVerId;
		}
		public void setDefVerId(String defVerId) {
			this.defVerId = defVerId;
		}

	String definitionString;
	@Face(ejsPath="genericfaces/richText.ejs")
		public String getDefinitionString() {
			return definitionString;
		}
		public void setDefinitionString(String definitionString) {
			this.definitionString = definitionString;
		}
		
	String name;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	
	String description;
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}

	@ServiceMethod(callByContent=true)
	public void save() throws RemoteException{
		defId = processManager.addProcessDefinition(name, 0, description, false, definitionString, parentFolder, defId, "process", name, null);

	}
	
	

	@Autowired
	public ProcessManagerRemote processManager;

	
	MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}

}
