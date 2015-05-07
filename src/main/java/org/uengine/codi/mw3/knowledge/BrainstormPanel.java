package org.uengine.codi.mw3.knowledge;

import org.metaworks.annotation.Face;
import org.metaworks.component.HorizontalSplitBox;

@Face(ejsPath="dwr/metaworks/genericfaces/CleanObjectFace.ejs")
public class BrainstormPanel {

	Object content;
		public Object getContent() {
			return content;
		}
		public void setContent(Object content) {
			this.content = content;
		}

	public BrainstormPanel(String id){
		BrainStormToolbar toolbar = new BrainStormToolbar();
		toolbar.setId(id);
		
		HorizontalSplitBox hSplitBox = new HorizontalSplitBox();
		hSplitBox.setTop(toolbar);
		hSplitBox.setBottom(toolbar.goKnowlege());
		hSplitBox.setFixHeight(21);
		
		this.setContent(hSplitBox);
	}
	

}
