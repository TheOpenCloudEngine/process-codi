package org.uengine.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.uengine.kernel.GlobalContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by MisakaMikoto on 2015. 8. 28..
 */
public class SolrServerManager {
    public SolrServerManager() {
    }

    public List<String> searchWorkListIds(String keyword, String page, int searchCount) {
        String solrURL = GlobalContext.getPropertyString("solr.server.url");
        String targetTable = GlobalContext.getPropertyString("solr.server.base.table");

        HttpSolrServer httpSolrServer = new HttpSolrServer(solrURL + targetTable);

        SolrQuery solrQuery = new SolrQuery(keyword);

        if(page == null) {
            solrQuery.setStart(0);

        } else {
            int pageCount = Integer.parseInt(page);
            solrQuery.setStart(pageCount * searchCount);
        }

        solrQuery.setRows(searchCount);
        //solrQuery.addSort("id", SolrQuery.ORDER.asc); // order by

        QueryResponse queryResponse = null;
        int totalCount = 0;
        List<String> idList = new ArrayList<String>();

        try {
            queryResponse = httpSolrServer.query(solrQuery);
            SolrDocumentList resultList = queryResponse.getResults();

            for(SolrDocument solrDocument : resultList) {
                idList.add(solrDocument.getFieldValue("id").toString());
            }

        } catch (SolrServerException e) {
            e.printStackTrace();
        }

        return idList;
    }
}
