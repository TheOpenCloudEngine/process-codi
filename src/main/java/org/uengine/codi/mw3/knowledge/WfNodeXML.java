package org.uengine.codi.mw3.knowledge;

import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.example.ide.SourceCode;
import org.uengine.kernel.GlobalContext;

public class WfNodeXML {

	SourceCode xml;
	
		public SourceCode getXml() {
			return xml;
		}
	
		public void setXml(SourceCode xml) {
			this.xml = xml;
		}
		
	String nodeId;
	@Hidden
		public String getNodeId() {
			return nodeId;
		}
	
		public void setNodeId(String nodeId) {
			this.nodeId = nodeId;
		}
		
	@ServiceMethod(callByContent=true)
	public IWfNode ok() throws Exception{
		IWfNode wfNode = (IWfNode) GlobalContext.deserialize(getXml().getCode(), Object.class);
//		WfNode 
		
		return wfNode;
	}
}
