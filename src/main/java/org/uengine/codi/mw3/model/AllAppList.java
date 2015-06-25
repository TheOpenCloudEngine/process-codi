package org.uengine.codi.mw3.model;

import org.metaworks.EventContext;
import org.metaworks.Refresh;
import org.metaworks.ServiceMethodContext;
import org.metaworks.ToEvent;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.ServiceMethod;
import org.uengine.codi.mw3.admin.TopPanel;

/**
 * Created by ho.lim on 2015-05-20.
 */
public class AllAppList {
    @AutowiredFromClient
    public Session session;

    @AutowiredFromClient
    public TopPanel topPanel;

    public void load() throws Exception{

    }

    @ServiceMethod(target= ServiceMethodContext.TARGET_APPEND)
    public Object[] goSNS() throws Exception {
        SNS sns = new SNS(session);
        topPanel.setTopCenterTitle("$AppList.SNS");
        return new Object[]{new Refresh(sns), new Refresh(topPanel),
                new ToEvent(ServiceMethodContext.TARGET_SELF, EventContext.EVENT_CLOSE)};
    }
}
