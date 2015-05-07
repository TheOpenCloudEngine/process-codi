package org.uengine.codi.mw3.knowledge;

import java.io.Serializable;

import org.metaworks.MetaworksContext;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Hidden;
import org.uengine.codi.ITool;
import org.uengine.codi.mw3.model.Session;

public class KnowledgeTool implements ITool, Serializable{
	
	String nodeId; 
		public String getNodeId() {
			return nodeId;
		}
		public void setNodeId(String nodeId) {
			this.nodeId = nodeId;
		}

	//very important in serializable object
//	transient WfPanel wfPanel;
//		public WfPanel getWfPanel() {
//			return wfPanel;
//		}
//		public void setWfPanel(WfPanel wfPanel) {
//			this.wfPanel = wfPanel;
//		}
	transient WfNode wfNode;
		public WfNode getWfNode() {
			return wfNode;
		}
		public void setWfNode(WfNode wfNode) {
			this.wfNode = wfNode;
		}
	@Override
	public void onLoad() {
		try {
			wfNode = new WfNode();
			wfNode.setMetaworksContext(new MetaworksContext());
			wfNode.getMetaworksContext().setWhen("onlyView");
			wfNode.session = session;
			wfNode.setLoadDepth(2);
			wfNode.load(nodeId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void beforeComplete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterComplete() {
		// TODO Auto-generated method stub
		
	}
	
	
	@AutowiredFromClient
	public Session session;

}
