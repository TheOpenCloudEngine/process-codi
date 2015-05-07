package org.uengine.codi.mw3.model;

import org.metaworks.annotation.Hidden;


public class ProcessPerspective extends CollapsePerspective {
	
	public ProcessPerspective(){
		setLabel("$Process");
	}
	
	protected void loadChildren() throws Exception {
		IProcessMap processMap = ProcessMap.loadList(session);
		processMap.getMetaworksContext().setWhen("filter");
		
		System.out.println("processMap.size() : " + processMap.size());
				
		setChild(processMap);
	}

	protected void unloadChildren() throws Exception {
		setChild(null);
	}
	
	IProcessMap processMapList;
		public IProcessMap getProcessMapList() {
			return processMapList;
		}	
		public void setProcessMapList(IProcessMap processMapList) {
			this.processMapList = processMapList;
		}
		
	@Override
	@Hidden
	public Popup popupAdd() throws Exception{
		return null;
	}

}
