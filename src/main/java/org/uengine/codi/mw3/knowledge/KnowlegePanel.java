package org.uengine.codi.mw3.knowledge;

import org.metaworks.annotation.Face;

@Face(displayName="$Knowlege", ejsPath="dwr/metaworks/org/uengine/codi/mw3/knowledge/WfPanel.ejs")
public class KnowlegePanel extends WfPanel {
	public final static String ID = "knowlege";
	
	public KnowlegePanel(){
		this(ID);
	}
	
	public KnowlegePanel(String id){
		this.setFirst(false);
		this.setId(id);
	}
	
	@Override
	public WfNode makeNewNode(){
		WfNode node;
		
		if(BacklogPanel.ID.equals(this.getId()))
			node = new BacklogNode();
		else if(PlanPanel.ID.equals(this.getId()))
			node = new PlanNode();
		else
			node = new KnowlegeNode();
		
		node.setType(this.getId());
		
		if(session != null)
			node.setAuthorId(session.getUser().getUserId());
		
		return node;
	}
}
