package org.uengine.codi.mw3.model;

public class OverlayCommentReloadPanel {

	IWorkItem comments;
		public IWorkItem getComments() {
			return comments;
		}
		public void setComments(IWorkItem comments) {
			this.comments = comments;
		}
	
	public OverlayCommentReloadPanel(IWorkItem commnets){
		this.setComments(commnets);
	}
}
