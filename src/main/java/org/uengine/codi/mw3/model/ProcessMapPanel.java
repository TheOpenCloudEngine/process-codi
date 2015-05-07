package org.uengine.codi.mw3.model;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.ServiceMethod;


public class ProcessMapPanel implements ContextAware {

	public ProcessMapPanel() {
		setMetaworksContext(new MetaworksContext());
	}
	
	public void load(Session session) throws Exception {
		ProcessMapList processMapList = new ProcessMapList();
		processMapList.setMetaworksContext(this.getMetaworksContext());
		processMapList.load(session);
		
		setProcessMapList(processMapList);
	}
	
	ProcessMapList processMapList;
		public ProcessMapList getProcessMapList() {
			return processMapList;
		}
		public void setProcessMapList(ProcessMapList processMapList) {
			this.processMapList = processMapList;
		}
		
	MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}
	
}		
