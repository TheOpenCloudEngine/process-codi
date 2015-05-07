package org.uengine.codi.mw3.knowledge;

import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.Id;

@Face(ejsPath="dwr/metaworks/genericfaces/CleanObjectFace.ejs")
public class InstancePanel {
	
	String id;
		@Id
		@Hidden
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		
	Object content;
		public Object getContent() {
			return content;
		}
		public void setContent(Object content) {
			this.content = content;
		}
		
	public InstancePanel(){
		this(null);
	}
	
	public InstancePanel(String id){
		this.setId(id);
	}
	
}
