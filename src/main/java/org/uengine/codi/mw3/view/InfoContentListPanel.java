package org.uengine.codi.mw3.view;

import org.metaworks.annotation.Order;
import org.uengine.codi.mw3.model.NewInstancePanel;
import org.uengine.codi.mw3.model.PerspectiveInfo;

public class InfoContentListPanel extends NewInstanceContentListPanel {

	@Order(value=3)
	@Override
	public Object getContent() {
		return content;
	}
	
	@Order(value=2)
	@Override
	public NewInstancePanel getNewInstancePanel() {
		return newInstancePanel;
	}
	
	PerspectiveInfo perspectiveInfo;
	@Order(value=1)
	public PerspectiveInfo getPerspectiveInfo() {
		return perspectiveInfo;
	}
	public void setPerspectiveInfo(PerspectiveInfo perspectiveInfo) {
		this.perspectiveInfo = perspectiveInfo;
	}
	
}
