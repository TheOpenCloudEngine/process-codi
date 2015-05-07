package org.uengine.codi.mw3.model;

import org.metaworks.ContextAware;
import org.metaworks.EventContext;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Available;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.ServiceMethod;

public class DeptPanel implements ContextAware {

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
		
	IDept dept;
		public IDept getDept() {
			return dept;
		}
		public void setDept(IDept dept) {
			this.dept = dept;
		}
		
	Follower follower;
		public Follower getFollower() {
			return follower;
		}
		public void setFollower(Follower follower) {
			this.follower = follower;
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
		@Available(condition="searchBox")
		public SearchBox getSearchBox() {
			return searchBox;
		}
		public void setSearchBox(SearchBox searchBox) {
			this.searchBox = searchBox;
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
	public void loadWithCheckFollowedDept(Session session) throws Exception{
		IDept dept = null;
		if( this.getFollower() != null ){
			this.getFollower().session = session;
			dept = this.getFollower().findDepts(this.getKeyword());
		}else{
			Dept deptRef = new Dept(); 
			dept = deptRef.findTreeByGlobalCom(session.getCompany().getComCode(), this.getKeyword());
		}
		
		if(this.isPicker()){
			dept.getMetaworksContext().setWhere(IUser.WHERE_PICKERLIST);
		}else{
			dept.getMetaworksContext().setHow("addFollower");
			dept.getMetaworksContext().setWhere(User.WHERE_ADDFOLLOWER);
		}
		
		setDept(dept);
		
	}
	
	@ServiceMethod(callByContent=true, except="dept", eventBinding=EventContext.EVENT_CHANGE)
	public void refresh() throws Exception{
		this.loadWithCheckFollowedDept(session);
	}
}
