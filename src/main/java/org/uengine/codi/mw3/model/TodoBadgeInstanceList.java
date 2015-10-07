package org.uengine.codi.mw3.model;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.AutowiredToClient;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dao.MetaworksDAO;

public class TodoBadgeInstanceList implements ContextAware{

	int page;
		@Id
		@Hidden		
		public int getPage() {
			return page;
		}
		public void setPage(int page) {
			this.page = page;
		}
	
	MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}
	ITodoBadgeInstance instances;
		public ITodoBadgeInstance getInstances() {
			return instances;
		}
		public void setInstances(ITodoBadgeInstance instances) {
			this.instances = instances;
		}

	TodoBadgeInstanceList moreInstanceList;
		public TodoBadgeInstanceList getMoreInstanceList() {
			return moreInstanceList;
		}
		public void setMoreInstanceList(TodoBadgeInstanceList moreInstanceList) {
			this.moreInstanceList = moreInstanceList;
		}
	Navigation navigation;
		public Navigation getNavigation() {
			return navigation;
		}
		public void setNavigation(Navigation navigation) {
			this.navigation = navigation;
		}

	String instanceListType;
		public String getInstanceListType() {
			return instanceListType;
		}
		public void setInstanceListType(String instanceListType) {
			this.instanceListType = instanceListType;
		}

	@AutowiredToClient
	public Session session;
	
	public TodoBadgeInstanceList(){
		this(null);
	}
	
	public TodoBadgeInstanceList(Session session){
		this.session = session;
		
		this.setNavigation(new Navigation(session));
		this.setPage(1);
	}
		
	public TodoBadgeInstanceList load() throws Exception {
		return load(this.getNavigation());
	}
	
	public TodoBadgeInstanceList load(Navigation navigation) throws Exception {
		int count =  ("phone".equals(navigation.getMedia())?InstanceList.PAGE_CNT_MOBILE:InstanceList.PAGE_CNT);
		
		IInstance instanceContents = Instance.load(navigation, getPage()-1, count);
		
		ITodoBadgeInstance todoBadgeIInstanceRef = (ITodoBadgeInstance)MetaworksDAO.createDAOImpl(ITodoBadgeInstance.class);
		
		if( instanceContents.size() > 0 ){
			while(instanceContents.next()){
				todoBadgeIInstanceRef.moveToInsertRow();
				todoBadgeIInstanceRef.getImplementationObject().copyFrom(instanceContents);
			}
			todoBadgeIInstanceRef.setMetaworksContext(new MetaworksContext());
			todoBadgeIInstanceRef.getMetaworksContext().setWhere(IInstance.WHERE_INSTANCELIST);
			this.setInstances(todoBadgeIInstanceRef);
		}else{
			this.setInstances(null);
		}
		
		
		// setting moreInstanceList
		if( instanceContents.size() >= InstanceList.PAGE_CNT){
			setMoreInstanceList(new TodoBadgeInstanceList());
			getMoreInstanceList().setNavigation(navigation);
			getMoreInstanceList().setPage(getPage()+1);
		}
		return this;
		
	}

	@ServiceMethod(callByContent=true, except={"instances"})
	public void more() throws Exception {
		load(this.getNavigation());
	}
}
