package org.uengine.codi.mw3.model;

import org.metaworks.annotation.Face;

@Face(ejsPath="dwr/metaworks/genericfaces/CleanObjectFace.ejs", options={"disableHeight"}, values={"true"})
public class ContactList {
	
	IContact contacts;	
		public IContact getContacts() {
			return contacts;
		}	
		public void setContacts(IContact contacts) {
			this.contacts = contacts;			
		}		
}
