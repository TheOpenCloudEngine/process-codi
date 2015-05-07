package org.uengine.codi.mw3.model;

import java.util.ArrayList;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.ServiceMethod;
public class DocumentPanel implements ContextAware {

	String tenentId;
		public String getTenentId() {
			return tenentId;
		}
		public void setTenentId(String tenentId) {
			this.tenentId = tenentId;
		}

	MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}	
	
	IDocumentNode documentNode;
		public IDocumentNode getDocumentNode() {
			return documentNode;
		}
		public void setDocumentNode(IDocumentNode documentNode) {
			this.documentNode = documentNode;
		}
		
	public DocumentPanel(){
		setMetaworksContext(new MetaworksContext());
	}
	@AutowiredFromClient
	transient public Session session;
	

	@ServiceMethod
	public Object[] loadUnformed() throws Exception{

		String title = tenentId+"의 미분류 문서";
		//Object[] returnObject = Perspective.loadDocumentListPanel(session, "UnlabeledDocument", "Main", title);
		
		//return returnObject;
		return null;
	}

	public void load() throws Exception{
		DocumentNode node = new DocumentNode();
		node.session = session;
//		Node.setId(session.getCompany().getComCode());
		node.setCompanyId(session.getCompany().getComCode());
		
		documentNode = node.loadDocumentList();
		documentNode.setMetaworksContext(this.getMetaworksContext());
		documentNode.getMetaworksContext().setWhen("onlyView");
		setDocumentNode(documentNode);
	}
}
