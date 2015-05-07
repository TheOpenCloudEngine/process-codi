package org.uengine.codi.mw3.knowledge;

import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.ServiceMethod;
import org.uengine.codi.mw3.model.Session;

public class BrainStormToolbar {

	@AutowiredFromClient
	public Session session;
	
	String id;
		@Hidden
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}

	@Face(displayName="$Knowlege")
	@ServiceMethod(callByContent=true)
	public Object goKnowlege(){
		KnowlegeTab knowlegeTab = new KnowlegeTab(this.getId());
		knowlegeTab.load();
		
		return knowlegeTab;
	}
	
	@Face(displayName="$Goal")
	@ServiceMethod(callByContent=true)
	public Object goBacklog(){
		BacklogTab backlogTab = new BacklogTab(this.getId());
		backlogTab.load();
		
		return backlogTab;
	}
	
	@Face(displayName="$Plan")
	@ServiceMethod(callByContent=true)
	public Object goPlan(){
		PlanTab planTab = new PlanTab(this.getId());
		planTab.load();
		
		return planTab;
	}
	
	@Face(displayName="$Do")
	@ServiceMethod(callByContent=true)
	public Object goDo(){
		DoTab doTab = new DoTab(this.getId());
		doTab.session = session;
		doTab.load();
		
		return doTab;
	}
	
	@Face(displayName="$See")
	@ServiceMethod(callByContent=true)
	public Object goSee(){
		SeeTab seeTab = new SeeTab(this.getId());
		seeTab.load();
		
		return seeTab;
	}
}
