package org.uengine.codi.mw3.model;

import org.metaworks.ContextAware;
import org.metaworks.EventContext;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Available;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.ServiceMethod;

public class RolePanel implements ContextAware {
	
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
	IRole	role;
		public IRole getRole() {
			return role;
		}
		public void setRole(IRole role) {
			this.role = role;
		}
		
	SearchBox searchBox;
		@Face(options={"keyupSearch"}, values={"true"})
		@Available(condition="searchBox")
		public SearchBox getSearchBox() {
			return searchBox;
		}
		public void setSearchBox(SearchBox searchBox) {
			this.searchBox = searchBox;
		}	
	boolean picker;
		public boolean isPicker() {
			return picker;
		}
		public void setPicker(boolean picker) {
			this.picker = picker;
		}	
	MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}
	private String getKeyword(){
		String keyword = null;
		if(this.getSearchBox() != null)
			keyword = this.getSearchBox().getKeyword();
	
		return keyword;
	}
	
	public void load() throws Exception{
		IRole role = null;
		
		Role roleRef = new Role();
		roleRef.session = session;
		role = roleRef.findRoles(getKeyword());
		
		if(this.isPicker()){
			role.getMetaworksContext().setWhere(IUser.WHERE_PICKERLIST);
		}else{
		}
		
		setRole(role);
	}
	
	@ServiceMethod(callByContent=true, except="role", eventBinding=EventContext.EVENT_CHANGE)
	public void refresh() throws Exception{
		this.load();
	}

}
