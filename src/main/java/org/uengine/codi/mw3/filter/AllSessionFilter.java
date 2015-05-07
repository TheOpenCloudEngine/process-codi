package org.uengine.codi.mw3.filter;

import org.directwebremoting.ScriptSession;
import org.directwebremoting.ScriptSessionFilter;

import java.util.HashMap;

public class AllSessionFilter implements ScriptSessionFilter {

	HashMap<String, String> filteredSession;
		public HashMap<String, String> getFilteredSession() {
			return filteredSession;
		}
		public void setFilteredSession(HashMap<String, String> filteredSession) {
			this.filteredSession = filteredSession;
		}

	public AllSessionFilter(HashMap<String, String> allSession){
		this.filteredSession = allSession;
	}
	
	@Override
	public boolean match(ScriptSession scriptsession) {
//		System.out.println("filteredSession : " + filteredSession.keySet().toString());
//		System.out.println("match : " + filteredSession.containsValue(scriptsession.getId()));
//		System.out.println(filteredSession.get("TEST"));
//		System.out.println(scriptsession.getId());
		if(filteredSession != null && filteredSession.containsValue(scriptsession.getId()))
			return true;
		else
			return false;
	}

}
