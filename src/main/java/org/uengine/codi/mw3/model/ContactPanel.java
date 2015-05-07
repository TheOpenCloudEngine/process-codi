package org.uengine.codi.mw3.model;

import org.metaworks.ContextAware;
import org.metaworks.EventContext;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Available;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.ServiceMethod;

public class ContactPanel implements ContextAware {
	
	@AutowiredFromClient
	public Session session;
	
	MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}

	String id;
		@Id
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}

	Follower follower;
		public Follower getFollower() {
			return follower;
		}
		public void setFollower(Follower follower) {
			this.follower = follower;
		}

	int count;
		public int getCount() {
			return count;
		}
		public void setCount(int count) {
			this.count = count;
		}

	boolean picker;
		public boolean isPicker() {
			return picker;
		}
		public void setPicker(boolean picker) {
			this.picker = picker;
		}

	SearchBox searchBox;
		@Face(options={"keyupSearch"}, values={"true"})
		public SearchBox getSearchBox() {
			return searchBox;
		}
		public void setSearchBox(SearchBox searchBox) {
			this.searchBox = searchBox;
		}
		
	IContact list;
		public IContact getList() {
			return list;
		}
		public void setList(IContact list) {
			this.list = list;
		}

	Invitation invitation;
		@Available(condition="(typeof count == 'undefined' || count == 0) && (typeof picker == 'undefined' || !picker)")
		public Invitation getInvitation() {
			return invitation;
		}
		public void setInvitation(Invitation invitation) {
			this.invitation = invitation;
		}
		
	public ContactPanel(){
		MetaworksContext metaworksContext = new MetaworksContext();
		this.setMetaworksContext(metaworksContext);
		
		SearchBox searchBox = new SearchBox(); 
		this.setSearchBox(searchBox);

		Invitation invitation = new Invitation();
		invitation.getMetaworksContext().setHow("invite");
		
		this.setInvitation(invitation);	
	}
	
	private String getKeyword(){
		String keyword = null;
		if(this.getSearchBox() != null)
			keyword = this.getSearchBox().getKeyword();

		return keyword;
	}
			
	public void load() throws Exception{
		
		IContact contacts = null;
		if(this.getFollower() != null){
			this.getFollower().session = session;
			
			contacts = this.getFollower().findContacts(this.getKeyword());
			contacts.getMetaworksContext().setWhere(IUser.WHERE_ADDFOLLOWER);
		}else{
			contacts = Contact.findContacts(session.getUser(), true, this.getKeyword());
		}
		
		if(this.isPicker())
			contacts.getMetaworksContext().setWhere(IUser.WHERE_PICKERLIST);
		
		this.setList(contacts);
		this.setCount(contacts.size());
	}

	@ServiceMethod(callByContent=true, except="list", eventBinding=EventContext.EVENT_CHANGE)
	public void refresh() throws Exception{
		this.load();
	}
}
