package org.uengine.codi.mw3.model;

import org.metaworks.annotation.Face;

@Face(ejsPath="dwr/metaworks/genericfaces/Window.ejs",
	  displayName="New InstancePanel",
	  options={"hideLabels", "maximize", "hideTitleBar"},
	  values={"true", "true", "true"})
public class NewInstanceWindow extends ContentWindow {
	public NewInstanceWindow(){
		
	}
	public NewInstanceWindow(Object panel){
		super(panel);
	}
}
