package org.uengine.codi.mw3.model;

import org.metaworks.MetaworksContext;
import org.metaworks.annotation.Available;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;

@Face(ejsPath="genericfaces/Tab.ejs"  )
public class FollowerSelectTab {

	String id;
		@Hidden
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}

	ContactPanel contactPanel;
		@Face(displayName="$FollowerFriends")
		@Available(condition="contactPanel")
		public ContactPanel getContactPanel() {
			return contactPanel;
		}
		public void setContactPanel(ContactPanel contactPanel) {
			this.contactPanel = contactPanel;
		}
		
	OrganizationTree deptTree;
		@Face(displayName="$Organization")
		//@Available(condition="deptTree")
		public OrganizationTree getDeptTree() {
			return deptTree;
		}
		public void setDeptTree(OrganizationTree deptTree) {
			this.deptTree = deptTree;
		}

	MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}
		
	public FollowerSelectTab() throws Exception{
		this(null);
	}
	
	public FollowerSelectTab(String id) throws Exception{
		this.setId(id);
	}
	
	public void loadContactPanel(Session session, String how) throws Exception {
		ContactPanel contactPanel = new ContactPanel();
		contactPanel.session = session;
		contactPanel.setId(this.getId());
		contactPanel.load();
		
		this.setContactPanel(contactPanel);
	}
	
	public void loadDeptTree(Session session) throws Exception {
		OrganizationTree deptTree = new OrganizationTree(session);
		deptTree.setId("dept");
		deptTree.setHiddenEmployee(true);
		deptTree.setHiddenCheckBoxFolder(true);
		deptTree.load();		
		
		this.setDeptTree(deptTree);
	}
}
