package org.uengine.codi.mw3.knowledge;

import org.metaworks.component.HorizontalSplitBox;

public class PlanTab extends HorizontalSplitBox {
	
	public PlanTab(){
		this(null);		
	}
	
	public PlanTab(String id){
		super(id);
	}
	
	public void load(){
		try{		
			PlanPanel planPanel = new PlanPanel();
			planPanel.load(this.getId());
			
			this.setTop(planPanel);		
			this.setBottom(new InstancePanel(PlanPanel.ID));		
			this.useSplitter(true);
			
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
}
