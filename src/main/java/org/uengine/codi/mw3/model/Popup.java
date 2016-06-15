package org.uengine.codi.mw3.model;

import javax.validation.Valid;

import org.metaworks.ContextAware;
import org.metaworks.EventContext;
import org.metaworks.MetaworksContext;
import org.metaworks.Remover;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.ServiceMethod;

public class Popup implements ContextAware {
	
	public Popup(){
		this(400,300, null);
		setSticky(true);
	}

	public Popup(Object panel){		
		this(400,300, panel);
	}
	
	public Popup(int width, int height){
		this(width,height, null);
	}	
	
	public Popup(int width, int height, Object panel){
		setWidth(width);
		setHeight(height);
		setPanel(panel);
		setAnimate(true);
	}	

	Object panel;
		@Valid
		public Object getPanel() {
			return panel;
		}
		public void setPanel(Object panel) {
			this.panel = panel;
		}		
		
	MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}	
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}		

	String name;		
		@Id
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
	int width;	
		public int getWidth() {
			return width;
		}
		public void setWidth(int width) {
			this.width = width;
		}

	int height;
		public int getHeight() {
			return height;
		}
		public void setHeight(int height) {
			this.height = height;
		}
	
	boolean animate;
		public boolean isAnimate() {
			return animate;
		}
		public void setAnimate(boolean animate) {
			this.animate = animate;
		}

	boolean sticky;
		public boolean isSticky() {
			return sticky;
		}
		public void setSticky(boolean sticky) {
			this.sticky = sticky;
		}



	@ServiceMethod(eventBinding=EventContext.EVENT_CLOSE)
	public Object close() {
		return new Remover(ServiceMethodContext.TARGET_SELF);
	}

}


