package org.uengine.codi.mw3.model;

import java.io.Serializable;

import org.metaworks.MetaworksContext;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Id;
import org.uengine.codi.ITool;

public class DocumentTool implements ITool, Serializable{

	@AutowiredFromClient
	transient public Session session;

	String instId;
	@Id
		public String getInstId() {
			return instId;
		}
		public void setInstId(String instId) {
			this.instId = instId;
		}
		
	transient IWorkItem workitem;
		public IWorkItem getWorkitem() {
			return workitem;
		}
		public void setWorkitem(IWorkItem workitem) {
			this.workitem = workitem;
		}
		
	@Override
	public void onLoad() {
		try {
			IWorkItem workitem = new WorkItem();
			this.workitem = new WorkItem();
			this.workitem = workitem.loadMajorVersionFile(instId);
			this.workitem.getMetaworksContext().setHow(MetaworksContext.HOW_MINIMISED);
			
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
