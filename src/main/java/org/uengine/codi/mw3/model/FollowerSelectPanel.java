package org.uengine.codi.mw3.model;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.component.HorizontalSplitBox;

public class FollowerSelectPanel extends HorizontalSplitBox implements ContextAware{
	MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}
		
	public FollowerSelectPanel(){
		setMetaworksContext(new MetaworksContext());
	}
	public void load(Session session, String type) throws Exception{
		
		/*
		FollowerSelectTab followerSelectTab = new FollowerSelectTab(session);
		
		OrganizationTree deptTree = followerSelectTab.getDeptTree();
				 
		deptTree.setId("dept");
		deptTree.setHiddenEmployee(true);
		deptTree.load();
		
		
		ContactListPanel contactListPanel = followerSelectTab.getContactListPanel();
		contactListPanel.getMetaworksContext().setHow("followerCheck");
		contactListPanel.setId(type);
		contactListPanel.load(session.getUser().getUserId());
		contactListPanel.getLocalContactList().getMetaworksContext().setWhen(type);		
		//contactListPanel.getSocialContactList().getMetaworksContext().setWhen(type);
				
		FollowerSelectCommand followerSelectCommand = new FollowerSelectCommand();
		followerSelectCommand.setFollowerType(type);
		
		this.setTop(followerSelectTab);
		this.setBottom(followerSelectCommand);
		this.setFixHeight(50);
		this.setAlign(FollowerSelectPanel.ALIGN_BOTTOM);
		*/
	}
}
