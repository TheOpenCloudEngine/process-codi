package org.uengine.codi.mw3.view;

import org.metaworks.annotation.Order;
import org.uengine.codi.mw3.model.NewInstancePanel;

public class NewInstanceContentListPanel extends ContentListPanel {

	@Order(2)
	@Override
	public Object getContent() {
		return content;
	}
	
	NewInstancePanel newInstancePanel;
	@Order(value=1)
	public NewInstancePanel getNewInstancePanel() {
		return newInstancePanel;
	}
	public void setNewInstancePanel(NewInstancePanel newInstancePanel) {
		this.newInstancePanel = newInstancePanel;
	}
	
}
