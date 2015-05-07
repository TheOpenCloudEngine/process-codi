package org.uengine.codi.mw3.knowledge;

import java.util.ArrayList;

public class WfPanel_pt extends WfPanel{
	
	ArrayList<IWfNode> pathForPt;
		public ArrayList<IWfNode> getPathForPt() {
			return pathForPt;
		}
		public void setPathForPt(ArrayList<IWfNode> pathForPt) {
			this.pathForPt = pathForPt;
		}
		
	String pathHtmlForPt;
		public String getPathHtmlForPt() {
			return pathHtmlForPt;
		}
		public void setPathHtmlForPt(String pathHtmlForPt) {
			this.pathHtmlForPt = pathHtmlForPt;
		}
	
	String wfNodeHtmlForPt;	
		public String getWfNodeHtmlForPt() {
			return wfNodeHtmlForPt;
		}
		public void setWfNodeHtmlForPt(String wfNodeHtmlForPt) {
			this.wfNodeHtmlForPt = wfNodeHtmlForPt;
		}
	
}
