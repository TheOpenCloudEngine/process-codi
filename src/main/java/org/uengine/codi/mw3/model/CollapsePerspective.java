package org.uengine.codi.mw3.model;

import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.Available;
import org.metaworks.annotation.ServiceMethod;

public class CollapsePerspective extends Perspective {

	boolean selected;
		public boolean isSelected() {
			return selected;
		}
		public void setSelected(boolean selected) {
			this.selected = selected;
		}
		
	boolean enableSelect;
		public boolean isEnableSelect() {
			return enableSelect;
		}
		public void setEnableSelect(boolean enableSelect) {
			this.enableSelect = enableSelect;
		}
	
	public CollapsePerspective(){
		super();
	}
	
	@Available(condition="typeof enableSelect != 'undefined' && enableSelect")
	@ServiceMethod(callByContent=true, except="child", mouseBinding=ServiceMethodContext.MOUSEBINDING_LEFTCLICK, bindingFor="label")
	public Object[] select() throws Exception {
		setSelected(!isSelected()); // toggle
		if (isSelected()) {
			loadChildren();
		} else {
			unloadChildren();
		}
		return new Object[] { this };
	}
		
	protected void unloadChildren() throws Exception {
		// TODO Override and unload children when perspective deselected
	}
	
}
