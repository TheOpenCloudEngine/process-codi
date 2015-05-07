package org.uengine.codi.mw3.knowledge;

import org.metaworks.component.HorizontalSplitBox;

public class BacklogTab extends HorizontalSplitBox {
	
	public BacklogTab(){
		this(null);		
	}
	
	public BacklogTab(String id){
		super(id);
	}
	
	public void load(){
		try{		
			BacklogPanel backlogPanel = new BacklogPanel();
			backlogPanel.load(this.getId());
			
			PlanPanel planPanel = new PlanPanel();
			planPanel.load(this.getId());
			
			this.setTop(backlogPanel);		
			this.setBottom(planPanel);		
			this.useSplitter(true);
			
		}catch(Exception e){
			e.printStackTrace();
		}			
	}
}
