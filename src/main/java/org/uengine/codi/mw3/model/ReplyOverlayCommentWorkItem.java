package org.uengine.codi.mw3.model;

import org.metaworks.annotation.Face;

@Face(ejsPath="dwr/metaworks/org/uengine/codi/mw3/model/ReplyOverlayCommentWorkItem.ejs")
public class ReplyOverlayCommentWorkItem  extends OverlayCommentWorkItem{

	final static String TYPE="replyCmnt";
	
	public ReplyOverlayCommentWorkItem(){
		setType(TYPE);
	}
}
