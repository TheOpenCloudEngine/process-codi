package org.uengine.codi.mw3.model;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.Refresh;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.Name;
import org.metaworks.annotation.Range;
import org.metaworks.annotation.ServiceMethod;


public class UserNicknameChanger implements ContextAware{

	public UserNicknameChanger() {
		this.setMetaworksContext(new MetaworksContext());
		this.getMetaworksContext().setWhen(metaworksContext.WHEN_EDIT);
	}
	
	public MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}

	@AutowiredFromClient
	public Session session;
	
	
	String userNickname;
		@Name
		@Range(size=50)
		public String getUserNickname() {
			return userNickname;
		}
		public void setUserNickname(String userNickname) {
			this.userNickname = userNickname;
		}
		

	String userId;
		@Id
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		

	//good example for Refresh
	@ServiceMethod(callByContent=true, target="popup")
	public Refresh change() throws Exception{
		Contact contact = new Contact();
		IContact findContact = contact.findContactsWithFriendId(session.getUser(), this.getUserId());
		
		if(!this.getUserNickname().equals(findContact.getFriend().getName())){
			contact.copyFrom(findContact);
			contact.getFriend().setName(this.getUserNickname());
			contact.editFriendName();
			return new Refresh(contact);
		}else{
			return null;
		}
	}
	
}