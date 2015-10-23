package org.uengine.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.Group;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.metaworks.MetaworksContext;
import org.uengine.codi.mw3.model.IInstance;
import org.uengine.codi.mw3.model.IUser;
import org.uengine.codi.mw3.model.Instance;
import org.uengine.codi.mw3.model.User;
import org.uengine.kernel.GlobalContext;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by MisakaMikoto on 2015. 8. 28..
 */
public class SolrServerManager {
    // instance and user field
    public final static String INITIATOR_ID = "initep";
    public final static String INITIATOR_NAME = "initrsnm";
    public final static String CURRENT_USER_ID = "currep";
    public final static String CURRENT_USER_NAME = "currrsnm";
    public final static String LAST_CMNT_USER_ID = "lastCmntep";
    public final static String LAST_CMNT_USER_NAME = "lastCmntrsnm";
    public final static String LAST_CMNT_2_USER_ID = "lastCmnt2ep";
    public final static String LAST_CMNT_2_USER_NAME = "lastCmnt2rsnm";

    // search field (see the solr schema.xml index = true field)
    public final static String TITLE = "title";
    public final static String CONTENT = "content";
    public final static String NAME = "name";
    public final static String INITRSNM = "initrsnm";
    public final static String CURRRSNM = "currrsnm";
    public final static String LASTCMNT = "lastcmnt2";
    public final static String LASTCMNT2 = "lastcmnt2";
    public final static String FILETEXT = "filetext";

    public final static String SET = "set";
    public final static String INSTANCE_ID = "instid";


    // setting group info
    public final static String AND = "and";
    public final static String OR = "or";

    public SolrServerManager() {
    }

    Hashtable<String, String> userMapping;
    public Hashtable<String, String> getUserMapping() {
        return userMapping;
    }
    public void setUserMapping(Hashtable<String, String> userMapping) {
        this.userMapping = userMapping;
    }

    public ArrayList<IInstance> search(String keyword, int page, int count) {
        HttpSolrServer httpSolrServer = this.createHttpSolrServer();
        SolrQuery solrQuery = this.createQuery(keyword, page, count);

        QueryResponse queryResponse = null;
        ArrayList<IInstance> instanceContents = new ArrayList<IInstance>();

        // reflect iinstance
        Class instanceClass = IInstance.class;
        Method[] instanceClassMethods = instanceClass.getMethods();

        this.initUserHashTable();

        try {
            queryResponse = httpSolrServer.query(solrQuery);
            List<Group> groupList = queryResponse.getGroupResponse().getValues().get(0).getValues();

            for(Group group : groupList) {
                SolrDocumentList results = group.getResult();

                for (SolrDocument solrDocument : results) {
                    // one solrDocument == Instance
                    IInstance instance = new Instance();

                    for (String solrField : solrDocument.getFieldNames()) {
                        // find User Information
                        // see the iinstance's IUser field
                        instance = this.mappingUser(instance, solrDocument, solrField);
                        this.invokeObject(instance, instanceClassMethods, solrDocument, solrField);

                        this.initUserHashTable();
                    }
                    instance.setMetaworksContext(new MetaworksContext());
                    instance.getMetaworksContext().setWhere(IInstance.WHERE_INSTANCELIST);
                    instanceContents.add(instance);
                }
            }

        } catch (SolrServerException e) {
            e.printStackTrace();

        }
        return instanceContents;
    }

    private HttpSolrServer createHttpSolrServer() {
        String solrURL = GlobalContext.getPropertyString("solr.server.url");
        String targetTable = GlobalContext.getPropertyString("solr.server.base.table");

        HttpSolrServer httpSolrServer = new HttpSolrServer(solrURL + targetTable);

        return httpSolrServer;
    }

    private SolrQuery createQuery(String keyword, int page, int count) {
        String changeWildCardKeyword = keyword + "*";

        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery(TITLE + ":" + changeWildCardKeyword + " " + OR + " " +
                    CONTENT + ":" + changeWildCardKeyword + " " + OR + " " +
                    NAME + ":" + changeWildCardKeyword + " " + OR + " " +
                    INITRSNM + ":" + changeWildCardKeyword + " " + OR + " " +
                    CURRRSNM + ":" + changeWildCardKeyword + " " + OR + " " +
                    LASTCMNT + ":" + changeWildCardKeyword + " " + OR + " " +
                    LASTCMNT2 + ":" + changeWildCardKeyword + " " + OR + " " +
                    FILETEXT + ":" + changeWildCardKeyword);
        solrQuery.set("group", true);
        solrQuery.set("group.field", INSTANCE_ID);

        // page
        if(page == 0) {
            solrQuery.setStart(0);

        } else {
            solrQuery.setStart(page * count);
        }

        solrQuery.setRows(count);

        return solrQuery;
    }

    private void invokeObject(Object object, Method[] methods, SolrDocument solrDocument, String solrField) {
        for(Method method : methods) {
            if(method.getName().toLowerCase().startsWith(SET)) {
                String parseMethodName = method.getName().substring(SET.length(), method.getName().length());

                if(parseMethodName.toLowerCase().equals(solrField)) {
                    try {
                        method.invoke(object, solrDocument.getFieldValue(solrField));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private IInstance mappingUser(IInstance instance, SolrDocument solrDocument, String solrField) {
        if(this.getUserMapping().containsKey(solrField)) {
            Enumeration<String> keys = this.getUserMapping().keys();

            while(keys.hasMoreElements()) {
                if("".equals(this.getUserMapping().get(keys.nextElement()))) {
                    this.getUserMapping().put(keys.nextElement(), solrDocument.getFieldValue(solrField).toString());
                }
            }

            if(this.getUserMapping().get(INITIATOR_ID) != null && this.getUserMapping().get(INITIATOR_NAME) != null) {
                IUser user = new User();
                user.setUserId(this.getUserMapping().get(INITIATOR_ID));
                user.setName(this.getUserMapping().get(INITIATOR_NAME));

                instance.setInitiator(user);
            }

            if(this.getUserMapping().get(CURRENT_USER_ID) != null && this.getUserMapping().get(CURRENT_USER_NAME) != null) {
                IUser user = new User();
                user.setUserId(this.getUserMapping().get(CURRENT_USER_ID));
                user.setName(this.getUserMapping().get(CURRENT_USER_NAME));

                instance.setCurrentUser(user);
            }

            if(this.getUserMapping().get(LAST_CMNT_USER_ID) != null && this.getUserMapping().get(LAST_CMNT_USER_NAME) != null) {
                IUser user = new User();
                user.setUserId(this.getUserMapping().get(LAST_CMNT_USER_ID));
                user.setName(this.getUserMapping().get(LAST_CMNT_USER_NAME));

                instance.setLastCmntUser(user);
            }

            if(this.getUserMapping().get(LAST_CMNT_2_USER_ID) != null && this.getUserMapping().get(LAST_CMNT_2_USER_NAME) != null) {
                IUser user = new User();
                user.setUserId(this.getUserMapping().get(LAST_CMNT_2_USER_ID));
                user.setName(this.getUserMapping().get(LAST_CMNT_2_USER_NAME));

                instance.setLastCmnt2User(user);
            }
        }
        return instance;
    }

    private void initUserHashTable() {
        this.setUserMapping(new Hashtable<String, String>());

        this.getUserMapping().put(INITIATOR_ID, "");
        this.getUserMapping().put(INITIATOR_NAME, "");

        this.getUserMapping().put(CURRENT_USER_ID, "");
        this.getUserMapping().put(CURRENT_USER_NAME, "");

        this.getUserMapping().put(LAST_CMNT_USER_ID, "");
        this.getUserMapping().put(LAST_CMNT_USER_NAME, "");

        this.getUserMapping().put(LAST_CMNT_2_USER_ID, "");
        this.getUserMapping().put(LAST_CMNT_2_USER_NAME, "");
    }

}
