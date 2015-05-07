package org.uengine.codi.mw3.model;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.MetaworksException;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.Name;
import org.metaworks.annotation.ServiceMethod;
import org.springframework.beans.factory.annotation.Autowired;

public class TrayItem implements ContextAware {
	
	public TrayItem(){
		setMetaworksContext(new MetaworksContext());
	}
	
	String title;
	@Name
		public String getTitle() {
			return title;
		}
	
		public void setTitle(String title) {
			this.title = title;
		}
	
	String instId;
	@Id
		public String getInstId() {
			return instId;
		}
	
		public void setInstId(String instId) {
			this.instId = instId;
		}
		
	transient InstanceView instance;
		public InstanceView getInstance() {
			return instance;
		}
		public void setInstance(InstanceView instance) {
			this.instance = instance;
		}
		
	MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}

	@ServiceMethod(inContextMenu=true, payload={"title", "instId"})
	public void slideDown() throws Exception{
		getMetaworksContext().setHow("down");
		
		Instance instance = new Instance();
		instance.setInstId(new Long(getInstId()));
		instance.session = session;
		instance.copyFrom(instance.databaseMe());
		
		if(!instance.checkAuth()){
			throw new MetaworksException("$NotPermittedToWork");
		}
		if( instance.getIsDeleted() ){
			throw new MetaworksException("$alreadyDeletedPost");
		}	
		
		
		instanceView.session = session;
		instanceView.load(instance);
		
//		instanceView.getInstanceViewThreadPanel().getThread().getMetaworksContext().setHow("instance");
//		instanceView.getInstanceViewThreadPanel().getNewItem().getMetaworksContext().setHow("instance");
		
		
		setInstance(instanceView);
	}
	
	@ServiceMethod(inContextMenu=true, payload={"title", "instId"})
	public void slideUp() throws Exception{
		getMetaworksContext().setHow("up");
	}
		
	@ServiceMethod(inContextMenu=true, payload={"instId"})
	public Tray close() throws Exception{
		tray.getTrayItems().remove(this);
		tray.session = session;
		tray.save();
		
		return tray;
	}
	
	@ServiceMethod(inContextMenu=true)
	public ContentWindow maximize() throws Exception{
		
		Instance instance = new Instance();
		instance.setInstId(new Long(getInstId()));
		instance.session = session;
		instance.copyFrom(instance.databaseMe());
		
		if(!instance.checkAuth()){
			throw new MetaworksException("$NotPermittedToWork");
		}
		if( instance.getIsDeleted() ){
			throw new MetaworksException("$alreadyDeletedPost");
		}
		instanceViewContent.session = session;
		instanceViewContent.load(instance.databaseMe());
		
		return instanceViewContent;
	}
	
	

	@Override
	public boolean equals(Object obj) {
		try{
			return ((TrayItem)obj).getInstId().equals(this.getInstId());
			
		}catch(Exception e){}
		
		return false;
	}

	@Autowired
	public InstanceView instanceView;
	
	@Autowired
	public InstanceViewContent instanceViewContent;
	
	@AutowiredFromClient
	public Session session;

	@AutowiredFromClient
	public Tray tray;
}
