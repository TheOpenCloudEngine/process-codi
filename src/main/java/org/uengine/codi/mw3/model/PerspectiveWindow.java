package org.uengine.codi.mw3.model;

import org.metaworks.annotation.Face;
import org.metaworks.widget.Window;
import org.uengine.kernel.GlobalContext;

@Face(ejsPath="dwr/metaworks/genericfaces/Window.ejs",  
options={"hideLabels", "minimize", "color", "hideTitleBar", "hideGuideBox"}, 
values={"true", "true", "blue", "true", "true"})
public class PerspectiveWindow extends Window {
	
	public PerspectiveWindow() throws Exception {
		this(null);		
	}
	
	public PerspectiveWindow(Session session) throws Exception {
		
		String clsName = GlobalContext.getPropertyString("perspectivepanel.class","org.uengine.codi.mw3.model.PerspectivePanel");
		PerspectivePanel perspectivePanel = (PerspectivePanel)GlobalContext.loadClass(clsName).newInstance();
		perspectivePanel.load(session);
		setPanel(perspectivePanel);
	}

}
