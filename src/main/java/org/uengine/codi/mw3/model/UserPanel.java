package org.uengine.codi.mw3.model;

import org.metaworks.EventContext;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Available;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.ServiceMethod;

public class UserPanel {
	
	@AutowiredFromClient
	public Session session;
	
	String id;
		@Id
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		
	int count;
		public int getCount() {
			return count;
		}
		public void setCount(int count) {
			this.count = count;
		}

	SearchBox searchBox;
		@Face(options={"keyupSearch"}, values={"true"})
		public SearchBox getSearchBox() {
			return searchBox;
		}
		public void setSearchBox(SearchBox searchBox) {
			this.searchBox = searchBox;
		}
		
	IUser list;
		public IUser getList() {
			return list;
		}
		public void setList(IUser list) {
			this.list = list;
		}

	Invitation invitation;
		@Available(condition="typeof count == 'undefined' || count == 0")
		public Invitation getInvitation() {
			return invitation;
		}
		public void setInvitation(Invitation invitation) {
			this.invitation = invitation;
		}
		
	public UserPanel(){
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
		IUser users = User.findUsers(session, this.getKeyword());
		users.getMetaworksContext().setWhere(IUser.WHERE_ADDCONTACT);
		
		this.setList(users);
		this.setCount(users.size());
	}

	@ServiceMethod(callByContent=true, except="list", eventBinding=EventContext.EVENT_CHANGE)
	public void refresh() throws Exception{
		this.load();
	}
}
