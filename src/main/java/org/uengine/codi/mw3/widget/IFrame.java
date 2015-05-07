package org.uengine.codi.mw3.widget;


public class IFrame {
	
	public IFrame(){}
	
	public IFrame(String url){
		setSrc(url);
	}
	
	String src;

		public String getSrc() {
			return src;
		}
	
		public void setSrc(String src) {
			this.src = src;
		}
		
	int width;
		
		public int getWidth() {
			return width;
		}
	
		public void setWidth(int width) {
			this.width = width;
		}

	int height;

		public int getHeight() {
			return height;
		}
	
		public void setHeight(int height) {
			this.height = height;
		}
}
