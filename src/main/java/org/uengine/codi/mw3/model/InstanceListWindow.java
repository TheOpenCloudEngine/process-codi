package org.uengine.codi.mw3.model;

import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Face;
import org.metaworks.widget.Window;

@Face(ejsPath="genericfaces/Window.ejs",  
      options={"hideLabels", "minimize"}, 
      values={"true", "true"})

public class InstanceListWindow extends Window {
	
	public InstanceListWindow(){}
	
	public InstanceListWindow(Object panel){
		this.setPanel(panel);
	}
	
	public InstanceListWindow(Session session) throws Exception {
		this.session = session;
		
		InstanceListPanel instanceListPanel = new InstanceListPanel(session);
		instanceListPanel.setPreloaded(true);
		
		if( session.getLastPerspecteType() == null ){
			session.setLastPerspecteType("allICanSee");
		}

		//this.setPanel((Perspective.loadInstanceListPanel(session, session.getLastPerspecteType(), session.getLastSelectedItem())[1]));
		
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
