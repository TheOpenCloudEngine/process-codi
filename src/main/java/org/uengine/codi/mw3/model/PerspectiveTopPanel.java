package org.uengine.codi.mw3.model;

import org.metaworks.Refresh;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.ServiceMethod;

public class PerspectiveTopPanel {

	@AutowiredFromClient
	public Session session;
	
	SearchBox searchBox;		
		@Face(options={"keyupSearch"}, values={"true"})
		public SearchBox getSearchBox() {
			return searchBox;
		}
		public void setSearchBox(SearchBox searchBox) {
			this.searchBox = searchBox;
		}

	public PerspectiveTopPanel(){
		InstanceSearchBox searchBox = new InstanceSearchBox();
		searchBox.setKeyUpSearch(true);
		searchBox.setKeyEntetSearch(true);
		setSearchBox(searchBox);
	}
	
	@ServiceMethod(target=ServiceMethodContext.TARGET_POPUP)
	public Object newInstance() throws Exception{
		NewInstancePanel newInstancePanel =  new NewInstancePanel();
		newInstancePanel.load(session);
		
		NewInstanceWindow window = new NewInstanceWindow(newInstancePanel);
		
		if(SNS.isPhone()){
			Popup popup = new Popup(window.getPanel());
			popup.setName(window.getTitle());
			
			return popup;
		}else{
			return new Refresh(window, true);
		}
	}

}
