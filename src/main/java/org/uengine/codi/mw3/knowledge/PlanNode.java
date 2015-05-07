package org.uengine.codi.mw3.knowledge;

import java.util.Date;

import org.metaworks.MetaworksContext;
import org.metaworks.ToAppend;
import org.metaworks.annotation.Available;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;
import org.uengine.codi.mw3.model.NewInstancePanel;

@Face(ejsPath="dwr/metaworks/org/uengine/codi/mw3/knowledge/IWfNode.ejs",
	  ejsPathMappingByContext={"{how: 'plan', face: 'dwr/metaworks/genericfaces/FormFace.ejs'}"})
public class PlanNode extends KnowlegeNode {

	@Override
	@Hidden(on=false)
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	@Override
	@Hidden(on=false)
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Override
	public Object[] drop() throws Exception {
		Object clipboard = session.getClipboard();
		
		if(clipboard instanceof PlanNode){
			this.setDragNode((WfNode)clipboard);
			
			return this.move();
		}else if(clipboard instanceof BacklogNode){		
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
	
	@Override
	@Hidden(on=false)
	@Available(when={MetaworksContext.WHEN_VIEW})
	public Object newProcessInstance() throws Exception{
		NewInstancePanel newInstancePanel =  new NewInstancePanel();
		newInstancePanel.setKnowledgeNodeId(this.getId());
		newInstancePanel.session = session;
		newInstancePanel.load(session);
		newInstancePanel.getNewInstantiator().setTitle(getName());
		
		InstancePanel panel = new InstancePanel(PlanPanel.ID);
		panel.setContent(newInstancePanel);
		
		return panel;
	}
}
