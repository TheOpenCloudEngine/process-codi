package org.uengine.codi.mw3.model;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.AutowiredToClient;
import org.metaworks.annotation.Face;
import org.uengine.solr.SolrServerManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by uengine on 2015. 9. 7..
 */

public class SearchList implements ContextAware{
    public final static int PAGE_CNT = 15;
    public final static int PAGE_CNT_MOBILE = 5;
    public final static int PAGE_CNT_DASHBOARD = 3;

    @AutowiredToClient
    public Session session;

    MetaworksContext metaworksContext;
    public MetaworksContext getMetaworksContext() {
        return metaworksContext;
    }
    public void setMetaworksContext(MetaworksContext metaworksContext) {
        this.metaworksContext = metaworksContext;
    }

    IInstance instances;
    public IInstance getInstances() {
        return instances;
    }
    public void setInstances(IInstance instances) {
        this.instances = instances;
    }

    public void load(String keyword) {
        //SolrServerManager solrServerManager = new SolrServerManager();
        //searchDataList = solrServerManager.searchWorklist(keyword);
    }
}
