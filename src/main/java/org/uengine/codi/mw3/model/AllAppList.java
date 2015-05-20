package org.uengine.codi.mw3.model;

import org.metaworks.*;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.ServiceMethod;
import org.uengine.codi.mw3.marketplace.AppMapping;
import org.uengine.codi.mw3.marketplace.IAppMapping;
import org.uengine.codi.mw3.widget.IFrame;

import java.util.ArrayList;

/**
 * Created by ho.lim on 2015-05-14.
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

    public AllAppList(){
        myAppsList = new ArrayList<AppMapping>();
    }
    public void load() throws Exception{
        AppMapping myapps = new AppMapping();

        myapps.setComCode(session.getCompany().getComCode());
        myapps.setIsDeleted(false);
        myapps.session = session;

        //전체 리스트 나오게
        IAppMapping getAppsList = myapps.findMyApps(0);

        while(getAppsList.next()){

            AppMapping app = new AppMapping();

            app.setAppId(getAppsList.getAppId());
            app.setAppName(getAppsList.getAppName());
            app.setComCode(getAppsList.getComCode());
            app.setIsDeleted(getAppsList.getIsDeleted());
            app.setLogoFile(getAppsList.getLogoFile());
            app.setUrl(getAppsList.getUrl());
            app.setAppType(getAppsList.getAppType());

            app.setMetaworksContext(new MetaworksContext());
            app.getMetaworksContext().setWhere("oce_dashboard");

            myAppsList.add(app);
        }
    }

    @ServiceMethod(target= ServiceMethodContext.TARGET_APPEND)
    public Object[] goSNS() throws Exception {
        SNS sns = new SNS(session);
        return new Object[]{new Refresh(sns), new Refresh(sns.loadTopCenterPanel(session)), new ToEvent(ServiceMethodContext.TARGET_SELF, EventContext.EVENT_CLOSE)};
    }


}
