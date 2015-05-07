package org.uengine.codi.mw3.knowledge;

import org.metaworks.component.HorizontalSplitBox;
import org.uengine.codi.mw3.model.PersonalPerspective;
import org.uengine.codi.mw3.model.Session;

public class DoTab extends HorizontalSplitBox {
	
	public Session session;
	
	public DoTab(){
		this(null);		
	}
	
	public DoTab(String id){
		super(id);
	}
	
	public void load(){
		try{		
			PersonalPerspective personalPerspective = new PersonalPerspective();
			
			this.setTop("searchbox");		
			//this.setBottom(personalPerspective.loadInstanceListPanel(session, "inbox", null)[1]);		
			this.setFixHeight(-1);
			
		}catch(Exception e){
			e.printStackTrace();
		}			
	}
}
