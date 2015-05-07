package org.uengine.codi.mw3.knowledge;

import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Face;
import org.uengine.codi.mw3.model.IUser;
import org.uengine.codi.mw3.model.Session;

@Face(ejsPath="genericfaces/Window.ejs", 
      displayName="Knowledge", 
      options={"hideLabels"}, 
      values={"true"})
public class WfWindow {
	
	public WfWindow(){
		
	}
	public WfWindow(Session session) throws Exception {
		WfPanel panel = new WfPanel();
		panel.session = session;
		panel.load(session.getCompany().getComCode());
		setWfPanel(panel);
	}
	public WfWindow(IUser user) throws Exception {
		WfPanel panel = new WfPanel();
		panel.session = session;
		panel.load("-1");//user.getUserId());
		
		setWfPanel(panel);
	}
	
	WfPanel wfPanel;
		public WfPanel getWfPanel() {
			return wfPanel;
		}
		public void setWfPanel(WfPanel wfPanel) {
			this.wfPanel = wfPanel;
		}
		
		
	@AutowiredFromClient
	public Session session;
	
}
