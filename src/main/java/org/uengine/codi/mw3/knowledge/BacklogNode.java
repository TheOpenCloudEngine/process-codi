package org.uengine.codi.mw3.knowledge;

import org.metaworks.ToAppend;
import org.metaworks.annotation.Face;

@Face(ejsPath="dwr/metaworks/org/uengine/codi/mw3/knowledge/IWfNode.ejs",
      ejsPathMappingByContext={"{how: 'backlog', face: 'dwr/metaworks/genericfaces/FormFace.ejs'}"})
public class BacklogNode extends KnowlegeNode {
	
	@Override
	public Object[] drop() throws Exception {
		Object clipboard = session.getClipboard();
		
		if(clipboard instanceof BacklogNode){
			this.setDragNode((WfNode)clipboard);
			
			return this.move();
		}else if(clipboard instanceof KnowlegeNode){
			WfNode node = (WfNode)clipboard;
			node.setLoadDepth(-1);
			node.setFirst(false);
			node.setChildNode(node.loadChildren());
			
			WfNode copyNode = node.copy(this.getId(), this.makeNewNode());
			
			if(this.getRootId() == this.getId()){
				return new Object[]{new ToAppend(new PlanPanel(), copyNode)};
			}else{
				return new Object[]{new ToAppend(this, copyNode)};
			}
		}else{
			return null;
		}
	}
}
