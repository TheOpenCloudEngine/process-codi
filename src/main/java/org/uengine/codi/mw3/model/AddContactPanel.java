package org.uengine.codi.mw3.model;

import org.metaworks.MetaworksContext;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Order;

@Face(ejsPath="dwr/metaworks/genericfaces/Tab.ejs")
public class AddContactPanel {
	
	public AddContactPanel(){
		
	}
	
	public AddContactPanel(Session session) throws Exception {
		UserPanel userPanel = new UserPanel();
		userPanel.session = session;
		userPanel.load();
		this.setUserPanel(userPanel);
		
		Invitation invitation = new Invitation();
		invitation.setMetaworksContext(new MetaworksContext());
		invitation.getMetaworksContext().setHow("invite");
		setInviteByEmail(invitation);
	}
	
	UserPanel userPanel;
		@Order(value=1)
		@Face(displayName="$LocalContact")
		public UserPanel getUserPanel() {
			return userPanel;
		}
		public void setUserPanel(UserPanel userPanel) {
			this.userPanel = userPanel;
		}

	Invitation inviteByEmail;
		@Order(value=2)
		@Face(displayName="$InviteByEmail")
		public Invitation getInviteByEmail() {
			return inviteByEmail;
		}
		public void setInviteByEmail(Invitation inviteByEmail) {
			this.inviteByEmail = inviteByEmail;
		}
}
