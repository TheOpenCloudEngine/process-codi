package org.uengine.codi.mw3.model;

import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.ServiceMethod;

public class ContactPerspective extends MoreViewPerspective {
	
	public ContactPerspective() throws Exception {
		this.setLabel("$Contact");
		this.setLoader(true);
		this.setEnableMore(true);
	}
		
	@Override
	protected void loadChildren() throws Exception {
		super.loadChildren();

			IContact contact = Contact.findContacts(session.getUser(), this.isMore());
			contact.getMetaworksContext().setWhere(IUser.WHERE_FRIENDS);

			int count = Contact.calcFriendCount(session.getUser());
			if (count > Integer.parseInt(Contact.DEFAULT_TOPIC_COUNT))
				this.setEnableMore(true);
			else
				this.setEnableMore(false);

			this.setChild(contact);
	}
	
	@Override
	public Popup popupAdd() throws Exception{
		Popup popup = new Popup();
		popup.setPanel(new AddContactPanel(session));
		
		return popup;
	}
	
	@ServiceMethod(target=ServiceMethodContext.TARGET_NONE)
	public IContact loadAllContact() throws Exception {
		return Contact.findContacts(session.getUser(), true);
	}
}
