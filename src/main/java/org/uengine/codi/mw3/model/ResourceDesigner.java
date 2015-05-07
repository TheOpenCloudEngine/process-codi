package org.uengine.codi.mw3.model;

import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.ServiceMethod;

public abstract class ResourceDesigner {
	
	String alias;
		@Id
		@Hidden
		public String getAlias() {
			return alias;
		}
	
		public void setAlias(String alias) {
			this.alias = alias;
		}

	@ServiceMethod
	@Hidden
	abstract public void load();

}
