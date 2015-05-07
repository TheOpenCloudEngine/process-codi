package org.uengine.codi.mw3.model;

import org.metaworks.annotation.Face;

@Face(ejsPath="genericfaces/MobileWindow.ejs",
	  options={"hideLabels"}, 
       values={"true"})
public class MobileWindow {
	
	public MobileWindow(){
	}
	
	public MobileWindow(Object panel){
		setPanel(panel);
	}
	
	Object panel;
		public Object getPanel() {
			return panel;
		}
		public void setPanel(Object panel) {
			this.panel = panel;
		}	
		
	String url;
		public String getUrl() {
			return url;
		}
	
		public void setUrl(String url) {
			this.url = url;
		}
		
	String type;
		public String getType() {
			return type;
		}
	
		public void setType(String type) {
			this.type = type;
		}
		

}
