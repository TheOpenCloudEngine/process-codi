package org.uengine.codi.mw3.filter;

import java.util.HashMap;

public class OtherSessionFilter extends AllSessionFilter {

	public OtherSessionFilter(HashMap<String, String> allSession, String excludeKey){
		super(allSession);
		
		if(filteredSession != null)
			filteredSession.remove(excludeKey);
	}
}
