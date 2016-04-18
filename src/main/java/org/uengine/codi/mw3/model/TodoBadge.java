package org.uengine.codi.mw3.model;

import org.metaworks.EventContext;
import org.metaworks.MetaworksContext;
import org.metaworks.Refresh;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.ServiceMethod;

public class TodoBadge{
	
	int newItemCount;
		public int getNewItemCount() {
			return newItemCount;
		}
		public void setNewItemCount(int newItemCount) {
			this.newItemCount = newItemCount;
		}
		
	boolean loader;
		public boolean isLoader() {
			return loader;
		}
		public void setLoader(boolean loader) {
			this.loader = loader;
		}

	@AutowiredFromClient
	public Session session;

	public TodoBadge(){
		this.setLoader(true);
	}
	
	@ServiceMethod(eventBinding=EventContext.EVENT_CHANGE)
	public void refresh() throws Exception{
		
		
		/*PersonalPerspective personalPerspective = new PersonalPerspective();

		personalPerspective.session = session;
		personalPerspective.session.getMetaworksContext().setWhen("todoBadge");
		personalPerspective.loadInbox();
		personalPerspective.session.getMetaworksContext().setWhen(null);*/

		this.setLoader(false);

		if(session != null){

			setNewItemCount(Instance.countTodo(session));
		}
	}
	
	@ServiceMethod(target=ServiceMethodContext.TARGET_STICK, loader="org.uengine.codi.mw3.model.Popup")
	public Object[] showList() throws Exception{

		/*	
		PersonalPerspective personalPerspective = new PersonalPerspective();
		personalPerspective.session = session;
		return personalPerspective.loadInbox();
		 */		
		session.setLastPerspecteType("inbox");
		session.setLastSelectedItem(session.getEmployee().getEmpCode());
		session.setLastPerspecteMode("personal");
		session.setSearchKeyword(null);
		
		TodoBadgeInstanceList instList = new TodoBadgeInstanceList(session);
		instList.setMetaworksContext(new MetaworksContext());
		instList.getMetaworksContext().setWhen("TodoBadge");
		instList.setInstanceListType("instancelist");
		instList.load();
		
		Popup popup = new Popup();
		popup.setName("$TodoList");
		popup.setPanel(instList);
		
		this.refresh();
		
		return new Object[]{popup, new Refresh(this)};
	}
}
