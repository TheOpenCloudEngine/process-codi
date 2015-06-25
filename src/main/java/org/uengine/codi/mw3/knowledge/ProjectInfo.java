package org.uengine.codi.mw3.knowledge;

import org.uengine.codi.mw3.model.Session;
import org.uengine.codi.mw3.model.TopicInfo;

/**
 * Created by soo on 2015. 6. 18..
 */
public class ProjectInfo extends TopicInfo {

    public ProjectInfo(){
    }

    public ProjectInfo(Session session, String topicId, String topicName) throws Exception {
        super(session, topicId);
        this.setTitle(topicName);
    }
}
