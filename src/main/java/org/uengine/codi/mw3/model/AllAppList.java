package org.uengine.codi.mw3.model;

import org.metaworks.EventContext;
import org.metaworks.Refresh;
import org.metaworks.ServiceMethodContext;
import org.metaworks.ToEvent;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.ServiceMethod;
import org.uengine.codi.mw3.admin.TopPanel;
import org.uengine.codi.mw3.marketplace.AppMapping;

import java.util.ArrayList;

/**
 * Created by ho.lim on 2015-05-20.
 */
public class AllAppList {
    @AutowiredFromClient
    public Session session;

    ArrayList<AppMapping> myAppsList;
    public ArrayList<AppMapping> getMyAppsList() {
        return myAppsList;
    }
    public void setMyAppsList(ArrayList<AppMapping> myAppsList) {
        this.myAppsList = myAppsList;
    }

    public void load() throws Exception{

    }

    @ServiceMethod(target= ServiceMethodContext.TARGET_APPEND)
    public Object[] goSNS() throws Exception {
        SNS sns = new SNS(session);
        return new Object[]{new Refresh(sns), new Refresh(sns.loadTopCenterPanel(session)),
                new ToEvent(ServiceMethodContext.TARGET_SELF, EventContext.EVENT_CLOSE)};
    }
}
