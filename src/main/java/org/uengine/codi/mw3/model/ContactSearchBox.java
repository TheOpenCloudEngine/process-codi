package org.uengine.codi.mw3.model;

import org.metaworks.EventContext;
import org.metaworks.ServiceMethodContext;
import org.metaworks.ToEvent;
import org.metaworks.annotation.ServiceMethod;

public class ContactSearchBox extends SearchBox{

	public ContactSearchBox(){	
		
	}

	@ServiceMethod(callByContent=true)
	public Object[] search() throws Exception{		
		return new Object[]{ new ToEvent(ServiceMethodContext.TARGET_SELF, EventContext.EVENT_CHANGE) };
	}
}
