package org.uengine.codi.mw3.model;

import org.metaworks.annotation.Available;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Order;

@Face(ejsPath="dwr/metaworks/genericfaces/Tab.ejs")
public class AddFollowerPanel {
	
	ContactPanel contactPanel;
		@Order(value=1)
		@Face(displayName="$FollowerFriends")
		@Available(condition="contactPanel")
		public ContactPanel getContactPanel() {
			return contactPanel;
		}
		public void setContactPanel(ContactPanel contactPanel) {
			this.contactPanel = contactPanel;
		}
		
	DeptPanel deptPanel;
		@Order(value=2)
		@Face(displayName="$Organization")
		@Available(condition="deptPanel")
		public DeptPanel getDeptPanel() {
			return deptPanel;
		}
		public void setDeptPanel(DeptPanel deptPanel) {
			this.deptPanel = deptPanel;
		}
		
	public AddFollowerPanel(Session session, Follower follower) throws Exception {
		ContactPanel contactPanel = new ContactPanel();
		contactPanel.session = session;
		contactPanel.setFollower(follower);
		contactPanel.load();
		contactPanel.getMetaworksContext().setWhere(User.WHERE_ADDFOLLOWER);
		
		this.setContactPanel(contactPanel);
	}
	
	public void loadDept(Session session, Follower follower) throws Exception{

		DeptPanel deptList = new DeptPanel();
		deptList.setId("/ROOT/");
		deptList.setFollower(follower);
		deptList.setSearchBox(new SearchBox());
		deptList.loadWithCheckFollowedDept(session);
		
		this.setDeptPanel(deptList);
	}
}
