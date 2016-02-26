package org.uengine.codi.mw3.model;

import org.metaworks.annotation.Hidden;
import org.metaworks.dao.MetaworksDAO;
import org.uengine.kernel.GlobalContext;

public class UpcommingTodoPerspective extends CollapsePerspective {
	
	public UpcommingTodoPerspective() throws Exception {
		this.setLabel("$UpcomingTodo");
		this.setLoader(true);
		this.setSelected(true);
		this.setIcon("arrow-up");
	}
	
	@Override
	protected void loadChildren() throws Exception {
		super.loadChildren();
		
		Navigation navigation = new Navigation(session);
		navigation.setPerspectiveType(Perspective.TYPE_COMMINGTODO);
			
		IInstance instanceContents = Instance.load(navigation, 0, Integer.parseInt(GlobalContext.getPropertyString("commingtodo.list.count", "2")));
		
		ICommingTodoIInstance commingTodoIInstanceRef = (ICommingTodoIInstance)MetaworksDAO.createDAOImpl(ICommingTodoIInstance.class);
		if( instanceContents.size() > 0 ){
			while(instanceContents.next()){
				commingTodoIInstanceRef.moveToInsertRow();
				commingTodoIInstanceRef.getImplementationObject().copyFrom(instanceContents);
			}
			commingTodoIInstanceRef.getMetaworksContext().setHow(Perspective.TYPE_COMMINGTODO);
			this.setChild(commingTodoIInstanceRef);	
		}else{
			this.setChild(null);
		}
		
	}
	
	@Override
	@Hidden
	public Popup popupAdd() throws Exception{
		return null;
	}
	
}
