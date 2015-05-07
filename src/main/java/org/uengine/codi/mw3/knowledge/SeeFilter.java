package org.uengine.codi.mw3.knowledge;

import org.metaworks.annotation.Range;
import org.metaworks.annotation.ServiceMethod;

public class SeeFilter {
	
	String id;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}

	String nodeType;
		@Range(options={"backlog", "plan"}, values={"backlog", "plan"})		
		public String getNodeType() {
			return nodeType;
		}
		public void setNodeType(String nodeType) {
			this.nodeType = nodeType;
		}

	String chartType;
		@Range(options={"pie", "line", "column"}, values={"pie", "line", "column"})
		public String getChartType() {
			return chartType;
		}
		public void setChartType(String chartType) {
			this.chartType = chartType;
		}
		
	@ServiceMethod(callByContent=true)
	public Object applyNode(){
		SeePanel panel = new SeePanel();
		panel.setFilter(this);
		panel.load();
		
		return panel;
	}
	
	@ServiceMethod(callByContent=true)
	public Object applyChart(){
		return null;
	}

}
