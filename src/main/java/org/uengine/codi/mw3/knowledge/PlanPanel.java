package org.uengine.codi.mw3.knowledge;

import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.ServiceMethod;

@Face(displayName="$Plan", ejsPath="dwr/metaworks/org/uengine/codi/mw3/knowledge/WfPanel.ejs")
public class PlanPanel extends KnowlegePanel {

	public final static String ID = "plan";
		
	public PlanPanel(){
		super(ID);
	}
	
	@ServiceMethod(payload="rootNodeId", mouseBinding="drop", target=ServiceMethodContext.TARGET_APPEND)
	public Object[] drop() throws Exception {
		Object clipboard = session.getClipboard();
		
		if(clipboard instanceof BacklogNode){
			WfNode node = this.makeRootNode();
			node.setRootId(this.getRootNodeId());
			node.session = this.session;
			
			return node.drop();
		}else{
			return null;
		}
	}	
}
