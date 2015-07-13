package org.uengine.codi.mw3.model;

import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Face;
import org.metaworks.widget.Window;

@Face(ejsPath="genericfaces/Window.ejs", options={"hideLabels", "minimize", "hideTitleBar"}, values={"true", "true", "true"})

public class ListWindow extends Window {
	
	public ListWindow(){}
	
	public ListWindow(Object panel){
		this.setPanel(panel);
	}
	
	public ListWindow(Session session) throws Exception {
		if( session.getLastPerspecteType() == null ){
			session.setLastPerspecteType("allICanSee");
		}

		this.setPanel((Perspective.loadInstanceListPanel(session, session.getLastPerspecteMode(), session.getLastPerspecteType(), session.getLastSelectedItem())[1]));
		
		/*
		if(instanceListPanel.isPreloaded()){
			PersonalPerspective personalPerspective = new PersonalPerspective();
			personalPerspective.session = session;
			this.setPanel((InstanceListPanel) personalPerspective.loadAllICanSee()[1]);
		}else{
			this.setPanel(instanceListPanel);	
		}
		*/
	}
		
	@AutowiredFromClient
	public Session session;
}
