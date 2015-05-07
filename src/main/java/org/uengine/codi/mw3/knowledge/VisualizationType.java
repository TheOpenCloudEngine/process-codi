package org.uengine.codi.mw3.knowledge;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.Refresh;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.Range;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dwr.MetaworksRemoteService;
import org.uengine.codi.mw3.Login;
import org.uengine.codi.mw3.filter.AllSessionFilter;
import org.uengine.codi.mw3.model.Session;

public class VisualizationType implements ContextAware{

	@AutowiredFromClient
	public Session session;

	public VisualizationType() {
	}
	

	public VisualizationType(WfNode wfNode) {
		setMetaworksContext(new MetaworksContext());
		getMetaworksContext().setWhen("edit");
		setNodeId(wfNode.getId());
	}
	
	String visType;
	@Face(displayName="가시화 방식")
	@Range(options={"개조식", "마인드맵", "표", "UML", "퀴즈", "프레젠테이션"},
			values={"bullet","mindmap","table", "uml", "quiz", "pt"})

		public String getVisType() {
			return visType;
		}
	
		public void setVisType(String visType) {
			this.visType = visType;
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
	@Face(displayName="OK")
	public void save() throws Exception{
		WfNode theNode = new WfNode();
		theNode.setId(getNodeId());
		theNode.databaseMe().setVisType(getVisType());
		
		//MetaworksRemoteService.pushClientObjects(new Object[]{new Refresh(theNode.databaseMe())});
		MetaworksRemoteService.pushClientObjectsFiltered(
				new AllSessionFilter(Login.getSessionIdWithCompany(session.getEmployee().getGlobalCom())),
				new Object[]{new Refresh(theNode.databaseMe())});
	}
	
	MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
	
	
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}
	
	
}
