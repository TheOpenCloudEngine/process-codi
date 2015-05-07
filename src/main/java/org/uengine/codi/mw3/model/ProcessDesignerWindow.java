package org.uengine.codi.mw3.model;

import org.metaworks.MetaworksContext;
import org.metaworks.annotation.Face;
import org.springframework.beans.factory.annotation.Autowired;

@Face(ejsPath = "genericfaces/Window.ejs", displayName = "ProcessDesigner Window", options = { "hideLabels" }, values = { "true" })
public class ProcessDesignerWindow extends ContentWindow {
	

	@Autowired
	public ProcessDesignerContentPanel processDesignerContentPanel;	
	@Autowired
		public ProcessDesignerContentPanel getProcessDesignerContentPanel() {
			return processDesignerContentPanel;
		}
		public void setProcessDesignerContentPanel(ProcessDesignerContentPanel processDesignerContentPanel){
			this.processDesignerContentPanel = processDesignerContentPanel;
		}

	public void load(String defId) throws Exception {
		//processDesignerContentPanel = new ProcessDesignerContentPanel();
		processDesignerContentPanel.load(defId);
	}
	
	public void newProcessDefinition(String parentFolder) throws Exception {
		processDesignerContentPanel = new ProcessDesignerContentPanel();
		processDesignerContentPanel.newProcessDefinition(parentFolder);
		processDesignerContentPanel.setMetaworksContext(new MetaworksContext());
		processDesignerContentPanel.getMetaworksContext().setWhen("edit");
		
	}

}
