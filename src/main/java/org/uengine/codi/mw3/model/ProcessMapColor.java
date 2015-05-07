package org.uengine.codi.mw3.model;

public class ProcessMapColor {

	public ProcessMapColor(){

	}
	
	public ProcessMapColor(String title){
		setTitle(title);
	}
	
	public String title;	
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		
	public String value;
		public String getValue() {
			return value;
		}	
		public void setValue(String value) {
			this.value = value;
		}

}
