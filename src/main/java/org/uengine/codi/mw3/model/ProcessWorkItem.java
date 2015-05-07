package org.uengine.codi.mw3.model;

public class ProcessWorkItem extends WorkItem{
	
	public ProcessWorkItem(){
		setType("");
	}
	
	@Override
	public void loadContents() throws Exception {
		setContentLoaded(true);
		
		this.detail();
	}

}
