package org.uengine.codi.mw3.model;


public class ListPanel {
	
	PerspectiveInfo perspectiveInfo;
		public PerspectiveInfo getPerspectiveInfo() {
			return perspectiveInfo;
		}
		public void setPerspectiveInfo(PerspectiveInfo perspectiveInfo) {
			this.perspectiveInfo = perspectiveInfo;
		}
	
	InstanceListPanel instanceListPanel;
		public InstanceListPanel getInstanceListPanel() {
			return instanceListPanel;
		}
		public void setInstanceListPanel(InstanceListPanel instanceListPanel) {
			this.instanceListPanel = instanceListPanel;
		}
		
	public ListPanel() throws Exception{
	}
	
	public ListPanel(InstanceListPanel instanceListPanel, PerspectiveInfo perspectiveInfo) throws Exception{		
		this.setInstanceListPanel(instanceListPanel);
		this.setPerspectiveInfo(perspectiveInfo);
	}
}
