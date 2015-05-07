package org.uengine.codi.mw3.common;

public class FacebookComments {

	public FacebookComments(String defId){
		setUrl(defId);		
	}
	
	String url;
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
}
