package org.uengine.codi.mw3.knowledge;

import org.metaworks.component.HorizontalSplitBox;

public class KnowlegeTab  extends HorizontalSplitBox {

	public KnowlegeTab() {
		this(null);
	}
	
	public KnowlegeTab(String id){
		super(id);
	}
	
	public void load(){
		try {
			KnowlegePanel knowlegePanel = new KnowlegePanel();
			knowlegePanel.load(this.getId());

			BacklogPanel backlogPanel = new BacklogPanel();
			backlogPanel.load(this.getId());

			this.setTop(knowlegePanel);
			this.setBottom(backlogPanel);
			this.useSplitter(true);

		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}
