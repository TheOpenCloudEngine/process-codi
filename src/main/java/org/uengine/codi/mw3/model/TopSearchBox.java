package org.uengine.codi.mw3.model;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.ServiceMethod;
import org.uengine.solr.SolrServerManager;

import java.util.List;
import java.util.Map;

/**
 * Created by MisakaMikoto on 2015. 9. 7..
 */
public class TopSearchBox implements ContextAware {
    public TopSearchBox(){
        setMetaworksContext(new MetaworksContext());

    }

    @AutowiredFromClient
    public Session session;

    MetaworksContext metaworksContext;
    public MetaworksContext getMetaworksContext() {
        return metaworksContext;
    }
    public void setMetaworksContext(MetaworksContext metaworksContext) {
        this.metaworksContext = metaworksContext;
    }

    String keyword;
    public String getKeyword() {
        return keyword;
    }
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @ServiceMethod(callByContent=true, target= ServiceMethodContext.TARGET_STICK, loader="org.uengine.codi.mw3.model.Popup")
    public Object search() throws Exception {
        session.setTopSearchKeyword(this.getKeyword());

        SolrServerManager solrServerManager = new SolrServerManager();
        //List<String> idList = solrServerManager.searchWorkListIds(this.getKeyword(), null, InstanceList.PAGE_CNT);

        TopSearchInstanceList topSearchInstanceList = new TopSearchInstanceList();
        //topSearchInstanceList = Perspective.loadInstanceList(session, session.getLastPerspecteMode(), session.getLastPerspecteType(), session.getLastSelectedItem());

        Popup popup = new Popup();
        popup.setName("Search");
        popup.setPanel(topSearchInstanceList);

        return popup;
    }

}
