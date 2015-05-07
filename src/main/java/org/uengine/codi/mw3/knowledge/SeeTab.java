package org.uengine.codi.mw3.knowledge;

import org.metaworks.component.HorizontalSplitBox;

public class SeeTab extends HorizontalSplitBox {
	
	public SeeTab(){
		this(null);		
	}
	
	public SeeTab(String id){
		super(id);
	}
	
	public void load(){
		try{		
			SeeFilter filter = new SeeFilter();
			filter.setId(this.getId());
			filter.setNodeType(BacklogPanel.ID);
			filter.setChartType(GoogleChart.CHARTTYPE_PIECHART);
			
			SeePanel panel = new SeePanel();
			panel.setFilter(filter);
			panel.load();
			
			this.setTop(filter);		
			this.setBottom(panel);		
			this.setFixHeight(35);
			
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
}
