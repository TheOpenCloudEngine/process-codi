package org.uengine.codi.mw3.knowledge;

import java.io.Serializable;

import org.metaworks.annotation.Hidden;
import org.uengine.codi.ITool;

public class WorkflowyTool implements ITool, Serializable{
	
	String instanceId;
	@Hidden
		public String getInstanceId() {
			return instanceId;
		}
		public void setInstanceId(String instanceId) {
			this.instanceId = instanceId;
		}

	Workflowy workflowy;
		public Workflowy getWorkflowy() {
			return workflowy;
		}
		public void setWorkflowy(Workflowy workflowy) {
			this.workflowy = workflowy;
		}

	@Override
	public void onLoad() {
		try {
			setWorkflowy(new Workflowy("pi_" + getInstanceId(), "edit"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void beforeComplete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterComplete() {
		// TODO Auto-generated method stub
		
	}

}
