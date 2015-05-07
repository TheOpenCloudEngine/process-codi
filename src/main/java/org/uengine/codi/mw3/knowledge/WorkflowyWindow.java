package org.uengine.codi.mw3.knowledge;

import org.metaworks.annotation.Face;
import org.uengine.codi.mw3.model.IUser;

@Face(ejsPath="genericfaces/Window.ejs", 
      displayName="Knowledge Mode", 
      options={"hideLabels"}, 
      values={"true"})
public class WorkflowyWindow {
	
	public WorkflowyWindow(IUser user) throws Exception {	
		setWorkflowy(new Workflowy(user));
	}
	
	Workflowy workflowy;
		public Workflowy getWorkflowy() {
			return workflowy;
		}
		public void setWorkflowy(Workflowy workflowy) {
			this.workflowy = workflowy;
		}
}
