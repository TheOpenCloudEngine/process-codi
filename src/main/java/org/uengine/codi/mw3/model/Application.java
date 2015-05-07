package org.uengine.codi.mw3.model;

public class Application {
	
	public Session session;
	
	String topCenterPanelType;
		public String getTopCenterPanelType() {
			return topCenterPanelType;
		}
		public void setTopCenterPanelType(String topCenterPanelType) {
			this.topCenterPanelType = topCenterPanelType;
		}

	Feedback feedback;
		public Feedback getFeedback() {
			return feedback;
		}
		public void setFeedback(Feedback feedback) {
			this.feedback = feedback;
		}
		
	public Application(){
		this.setTopCenterPanelType(TopCenterPanel.HOW_TRAY);
		this.setFeedback(new Feedback());
	}
	
	public TopCenterPanel loadTopCenterPanel(Session session) throws Exception {
		TopCenterPanel contentTopPanel = new TopCenterPanel(this.getTopCenterPanelType());
		contentTopPanel.session = session;
		contentTopPanel.load();
		
		return contentTopPanel; 
	}
}
