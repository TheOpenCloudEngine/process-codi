package org.uengine.codi.mw3.model;

import org.metaworks.annotation.Hidden;

public class GenericWorkItem extends WorkItem{
	
	
	public GenericWorkItem(){
		setType(WORKITEM_TYPE_GENERIC);
	}

	@Hidden(on=false)
	public GenericWorkItemHandler getGenericWorkItemHandler() {
		// TODO Auto-generated method stub
		return super.getGenericWorkItemHandler();
	}
	
	@Override
	public Object[] add() throws Exception {
		// TODO : ormapping 순서 문제로 this.content 값이 들어가 null 이 되는 현상 근본적 해결 필요			
		this.setContent(org.uengine.kernel.GlobalContext.serialize(this.getGenericWorkItemHandler().getTool(), Object.class));
		
		
		return super.add();
	}

}
