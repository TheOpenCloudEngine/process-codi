package org.uengine.codi.mw3.model;

import org.metaworks.ToAppend;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;

@Face(ejsPath="dwr/metaworks/org/uengine/codi/mw3/model/OverlayCommentWorkItem.ejs")
public class OverlayCommentWorkItem extends WorkItem{
	
	final static String TYPE="ovryCmnt";
	
	public OverlayCommentWorkItem(){
		setType(TYPE);
	}
	
	@Hidden//(on=false)
	public OverlayCommentWorkItem comment() throws Exception {	
		return null;
	}
	
	public Object[] add() throws Exception {
		boolean isOwnReturn = false;
		if( WHEN_EDIT.equals(getMetaworksContext().getWhen())){
			isOwnReturn = true;
		}else{
			isOwnReturn = false;
		}

		WorkItem parentWorkItem = new WorkItem();
		parentWorkItem.setTaskId(getOverlayCommentOption().getParentTaskId());

		Instance instance = new Instance();
		instance.setInstId(parentWorkItem.databaseMe().getRootInstId());
		
		this.setRootInstId(instance.databaseMe().getRootInstId());
		
		this.setPrtTskId(getOverlayCommentOption().getParentTaskId());
		if( !"replyCmnt".equals(this.getType()) ){
			this.setExt1(getOverlayCommentOption().getX());
			this.setExt2(getOverlayCommentOption().getY());
		}
		
		/*
		 * fileWorkItem 은 GenericWorkItem 이 한번 감싸고, 다른 instId 를 가지고 따로 그려진다.
		 * grpTaskId를 가지고 genericWorkItem을 찾은 후에 해당 instId를 셋팅해 주어야 한다.
		 */
		/*
		if( WORKITEM_TYPE_FILE.equals(parentWorkItem.getType()) ){
			parentWorkItem.setType(WORKITEM_TYPE_GENERIC);
			IWorkItem workItemRef = parentWorkItem.findGenericWorkItem();
			this.setInstId(workItemRef.getInstId());
			// TODO 만약 fileWorkItem 에 붙지를 않고 genericWorkItem 에 붙여 놓고 싶다면 getOverlayCommentOption의 parentTaskId를 변경해야 한다.
		}
		*/
		
		Object[] returnObjects = super.add();
		if( !isOwnReturn ){
			returnObjects = new Object[]{new ToAppend(parentWorkItem, this)};
		}
		return returnObjects;
	}
}
