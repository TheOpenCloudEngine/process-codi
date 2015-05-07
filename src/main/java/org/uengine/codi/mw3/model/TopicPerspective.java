package org.uengine.codi.mw3.model;

import org.metaworks.MetaworksContext;
import org.metaworks.website.MetaworksFile;
import org.uengine.codi.mw3.knowledge.ITopicNode;
import org.uengine.codi.mw3.knowledge.TopicNode;
import org.uengine.codi.mw3.knowledge.TopicPanel;
import org.uengine.codi.mw3.knowledge.TopicTitle;

public class TopicPerspective extends MoreViewPerspective {

	public TopicPerspective() throws Exception {
		this.setLabel("$Topic");
		this.setLoader(true);
	}
	
	@Override
	protected void loadChildren() throws Exception {
		super.loadChildren();
		
		ITopicNode topic = TopicNode.findTopic(session, this.isMore());
		
		int count = TopicNode.calcTopicCount(session);
		if(count > Integer.parseInt(TopicNode.DEFAULT_TOPIC_COUNT))
			this.setEnableMore(true);
		else
			this.setEnableMore(false);
		
		this.setChild(new TopicPanel(topic));
	}

	@Override
	public Popup popupAdd() throws Exception{
		TopicTitle topicTitle = new TopicTitle();
		topicTitle.setMetaworksContext(new MetaworksContext());
		topicTitle.getMetaworksContext().setWhen(MetaworksContext.WHEN_NEW);
		topicTitle.setLogoFile(new MetaworksFile());
		topicTitle.session = session;
		
		return new Popup(500, 200, topicTitle);
	}
}
