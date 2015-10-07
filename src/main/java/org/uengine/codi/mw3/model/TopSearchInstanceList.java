package org.uengine.codi.mw3.model;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.AutowiredToClient;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dao.MetaworksDAO;

public class TopSearchInstanceList implements ContextAware{

    int page;
    @Id
    @Hidden
    public int getPage() {
        return page;
    }
    public void setPage(int page) {
        this.page = page;
    }

    MetaworksContext metaworksContext;
    public MetaworksContext getMetaworksContext() {
        return metaworksContext;
    }
    public void setMetaworksContext(MetaworksContext metaworksContext) {
        this.metaworksContext = metaworksContext;
    }
    ITopSearchInstance instances;
    public ITopSearchInstance getInstances() {
        return instances;
    }
    public void setInstances(ITopSearchInstance instances) {
        this.instances = instances;
    }

    TopSearchInstanceList moreInstanceList;
    public TopSearchInstanceList getMoreInstanceList() {
        return moreInstanceList;
    }
    public void setMoreInstanceList(TopSearchInstanceList moreInstanceList) {
        this.moreInstanceList = moreInstanceList;
    }

    Navigation navigation;
    public Navigation getNavigation() {
        return navigation;
    }
    public void setNavigation(Navigation navigation) {
        this.navigation = navigation;
    }

    @AutowiredToClient
    public Session session;

    public TopSearchInstanceList(){
        this(null);
    }

    public TopSearchInstanceList(Session session){
        this.session = session;

        this.setNavigation(new Navigation(session));
        this.setPage(1);
    }

    public TopSearchInstanceList load() throws Exception {
        return load(this.getNavigation());
    }

    public TopSearchInstanceList load(Navigation navigation) throws Exception {
        int count =  ("phone".equals(navigation.getMedia())?InstanceList.PAGE_CNT_MOBILE:InstanceList.PAGE_CNT);

        IInstance instanceContents = Instance.load(navigation, getPage()-1, count);

        ITopSearchInstance topSearchInstance = (ITopSearchInstance)MetaworksDAO.createDAOImpl(ITodoBadgeInstance.class);

        if( instanceContents.size() > 0 ){
            while(instanceContents.next()){
                topSearchInstance.moveToInsertRow();
                topSearchInstance.getImplementationObject().copyFrom(instanceContents);
            }
            topSearchInstance.setMetaworksContext(new MetaworksContext());
            topSearchInstance.getMetaworksContext().setWhere(IInstance.WHERE_INSTANCELIST);
            this.setInstances(topSearchInstance);
        }else{
            this.setInstances(null);
        }


        // setting moreInstanceList
        if( instanceContents.size() >= InstanceList.PAGE_CNT){
            setMoreInstanceList(new TopSearchInstanceList());
            getMoreInstanceList().setNavigation(navigation);
            getMoreInstanceList().setPage(getPage()+1);
        }
        return this;

    }
    @ServiceMethod(callByContent=true, except={"instances"})
    public void more() throws Exception {
        load(this.getNavigation());
    }
}
