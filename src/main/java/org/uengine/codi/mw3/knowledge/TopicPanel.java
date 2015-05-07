package org.uengine.codi.mw3.knowledge;

public class TopicPanel {
	
	ITopicNode topicNode;
		public ITopicNode getTopicNode() {
			return topicNode;
		}
		public void setTopicNode(ITopicNode topicNode) {
			this.topicNode = topicNode;
		}
		
	public TopicPanel(){
		
	}
	
	public TopicPanel(ITopicNode topic){
		this.setTopicNode(topic);
	}
}
