package org.uengine.codi.mw3.knowledge;

import org.metaworks.annotation.Face;

@Face(displayName="Hints", options={"hideEditBtn", "hideLabels"}, values={"true", "true"})
public class WorkflowyPanel {
	
	public WorkflowyPanel(Workflowy workflowy){
		setWorkflowy(workflowy);
	}
	Workflowy workflowy;
		public Workflowy getWorkflowy() {
			return workflowy;
		}
		public void setWorkflowy(Workflowy workflowy) {
			this.workflowy = workflowy;
		}
}
