package org.uengine.codi.mw3.model;

import org.metaworks.EventContext;
import org.metaworks.ToEvent;
import org.metaworks.common.MetaworksUtil;


public class TopicInfo extends GroupInfo{

	public final static String HTML = "html";

	public TopicInfo(){
		
	}
	
	public TopicInfo(Session session, String topicId) throws Exception {
		super(session, topicId);
	}
	
	@Override
	public Object[] remove() throws Exception {
		return MetaworksUtil.putObjectArray(MetaworksUtil.makeRefreshObjectArray(super.remove()), new ToEvent(new TopicPerspective(), EventContext.EVENT_CHANGE));
	}	
}
