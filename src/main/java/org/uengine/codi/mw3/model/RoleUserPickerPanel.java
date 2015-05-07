package org.uengine.codi.mw3.model;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;

public class RoleUserPickerPanel implements ContextAware {

	public RoleUserPickerPanel() {
		setMetaworksContext(new MetaworksContext());
		getMetaworksContext().setWhere(IUser.MW3_WHERE_ROLEUSER_PICKER);
	}
	
	public RoleUserPickerPanel(IUser loginUser) throws Exception{
		this();
		
		/*
		ContactListPanel contactListPanel = new ContactListPanel();		
		contactListPanel.getMetaworksContext().setWhen(IUser.MW3_WHERE_ROLEUSER_PICKER);
		contactListPanel.load(loginUser.getUserId());		
		
		setContactListPanel(contactListPanel);
		*/
	}
	
	ContactPanel contactPanel;
		public ContactPanel getContactPanel() {
			return contactPanel;
		}	
		public void setContactPanel(ContactPanel contactPanel) {
			this.contactPanel = contactPanel;
		}

	MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}
}
