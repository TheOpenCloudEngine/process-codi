package org.uengine.codi.mw3.knowledge;

import java.util.ArrayList;

import org.metaworks.component.HorizontalSplitBox;

public class SeePanel extends HorizontalSplitBox {
	
	public final static String ID = "see";
	
	SeeFilter filter;
		public SeeFilter getFilter() {
			return filter;
		}
		public void setFilter(SeeFilter filter) {
			this.filter = filter;
		}

	public SeePanel(){
		super(ID);
	}
	
	public void load(){
		WfPanel panel = null;
		if(BacklogPanel.ID.equals(this.getFilter().getNodeType()))
			panel = new BacklogPanel();
		else
			panel = new PlanPanel();
		
		try{
			panel.load(filter.getId(), "read");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		ArrayList<Object[]> data = new ArrayList<Object[]>();
		if(GoogleChart.CHARTTYPE_PIECHART.equals(this.getFilter().getChartType())){
			data.add(new Object[] { "Task", "Hours per Day'"});
			data.add(new Object[] { "Work", 11});
			data.add(new Object[] { "Eat", 2});
			data.add(new Object[] { "Commute", 2});
			data.add(new Object[] { "Watch TV", 2});
			data.add(new Object[] { "Sleep", 7});
		}else{
			data.add(new Object[] { "Year", "Sales","Expenses"});
			data.add(new Object[] { "2004", 1000, 400});
			data.add(new Object[] { "2005", 1170, 460});
			data.add(new Object[] { "2006", 660, 1120});
			data.add(new Object[] { "2007", 1030, 540});
		}
		
		GoogleChart googleChart = new GoogleChart();
		googleChart.setType(this.getFilter().getChartType());
		googleChart.setData(data);
		
		
		this.setTop(panel);
		this.setBottom(googleChart);
		this.useSplitter(true);
	}
}
