package org.uengine.codi.mw3.model;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.Id;

public class RoleList implements ContextAware {
	
	public RoleList() {
		setMetaworksContext(new MetaworksContext());
	}
	
	String id;
		@Id
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		
	IRole Role;
		public IRole getRole() {
			return Role;
		}
		public void setRole(IRole Role) {
			this.Role = Role;
		}
		
	MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}
	
}