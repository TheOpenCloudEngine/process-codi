package org.uengine.codi.mw3.knowledge;

import org.metaworks.annotation.Id;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.annotation.Table;
import org.metaworks.dao.IDAO;

@Table(name="bpm_topicmapping")
public interface ITopicMapping extends IDAO {
	 
	
		public String getTopicId();
		public void setTopicId(String topicId);
	
		@Id
		public Long getTopicMappingId();
		public void setTopicMappingId(Long topicMappingId);
		
		public String getUserId();
		public void setUserId(String userId);
		
		public String getUserName();
		public void setUserName(String userName);
		
		public int getAssigntype() ;
		public void setAssigntype(int assigntype) ;
}
