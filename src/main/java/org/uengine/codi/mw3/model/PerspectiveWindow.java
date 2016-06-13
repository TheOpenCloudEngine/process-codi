package org.uengine.codi.mw3.model;

import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;
import org.metaworks.dwr.MetaworksRemoteService;
import org.metaworks.widget.Window;
import org.uengine.kernel.GlobalContext;

@Face(ejsPath="dwr/metaworks/genericfaces/Window.ejs", options={"hideLabels", "minimize", "color", "hideTitleBar", "hideGuideBox"}, values={"true", "true", "blue", "true", "true"})
public class PerspectiveWindow extends Window {
	
	public PerspectiveWindow() throws Exception {
		this(null);		
	}
	
	public PerspectiveWindow(Session session) throws Exception {
		
		PerspectivePanel perspectivePanel = MetaworksRemoteService.getComponent(PerspectivePanel.class);
		perspectivePanel.load(session);
		setPanel(perspectivePanel);
		setPerspectivePanel(perspectivePanel);
	}

	PerspectivePanel perspectivePanel;
	@Hidden
		public PerspectivePanel getPerspectivePanel() {
			return perspectivePanel;
		}

		public void setPerspectivePanel(PerspectivePanel perspectivePanel) {
			this.perspectivePanel = perspectivePanel;
		}


}
