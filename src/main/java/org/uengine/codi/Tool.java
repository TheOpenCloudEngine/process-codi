package org.uengine.codi;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;

public class Tool implements ITool, ContextAware {
	
	public Tool(){
		setMetaworksContext(new MetaworksContext());
	}
	
	
	public void onLoad(){}
	
	public void beforeComplete(){}
	
	public void afterComplete(){}

	
	MetaworksContext metaworksContext;
	
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
	
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}

}
