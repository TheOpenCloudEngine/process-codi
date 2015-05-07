package org.uengine.codi.mw3.common;

import org.metaworks.annotation.Available;
import org.springframework.stereotype.Component;
import org.uengine.codi.mw3.admin.TopPanel;
import org.uengine.codi.mw3.model.PerspectivePanel;

@Component
public class MainPanel {
	 
	PerspectivePanel perspectivePanel;
	@Available(condition="perspectivePanel")
		public PerspectivePanel getPerspectivePanel() {
			return perspectivePanel;
		}
		public void setPerspectivePanel(PerspectivePanel perspectivePanel) {
			this.perspectivePanel = perspectivePanel;
		}


	TopPanel topPanel;
		public TopPanel getTopPanel() {
			return topPanel;
		}
		public void setTopPanel(TopPanel topPanel) {
			this.topPanel = topPanel;
		}
	

	Object appPanel;
		public Object getAppPanel() {
			return appPanel;
		}
		public void setAppPanel(Object appPanel) {
			this.appPanel = appPanel;
		}
		
	public MainPanel(){
		
	}
	
	public MainPanel(Object appPanel){
		this(null, appPanel);
	}

	public MainPanel(TopPanel topPanel, Object appPanel){
		setTopPanel(topPanel);
		setAppPanel(appPanel);
	}
}
