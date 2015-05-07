package org.uengine.search.solr;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.metaworks.website.MetaworksFile;
import org.uengine.codi.mw3.model.Instance;
import org.uengine.codi.mw3.model.WorkItem;
import org.uengine.kernel.GlobalContext;

public class SolrData {
	
	static SolrServer solrServer;
	
	public boolean init(){
		if(!"1".equals(SolrSearch.USE_SEARCH_ENGINE)){
			return false;
		}
		if( solrServer == null ){
			solrServer = new HttpSolrServer("http://localhost:8983/solr/");
		}
		return true;
	}
	public SolrData(){
	}

	protected void insertText(WorkItem workItem, Instance instance){
		try {
			SolrWorkItem solrWorkItem = new SolrWorkItem();
			solrWorkItem.setId(workItem.getTaskId()+"");
			solrWorkItem.setInstanceid(workItem.getInstId()+"");
			solrWorkItem.setTaskid(workItem.getTaskId()+"");
			solrWorkItem.setTitle(new String[]{workItem.getTitle()});
			solrWorkItem.setWorkitemtype(workItem.getType());
			solrWorkItem.setContents(new String[]{workItem.getContent()});
			solrWorkItem.setTopicid(instance.getTopicId());
			
			solrServer.addBean(solrWorkItem);
			solrServer.commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void insertFile(WorkItem workItem, Instance instance) throws Exception{
		MetaworksFile file = workItem.getFile();
		String fileName = file.overrideUploadPathPrefix() + "/" + file.getUploadedPath();
		String contentType = file.getMimeType();
		File uploadFile = new File(fileName);
		
		ContentStreamUpdateRequest req = new ContentStreamUpdateRequest("/update/extract");
		
		req.addFile(uploadFile , contentType);
		req.setParam("literal.id", workItem.getTaskId()+"");
		req.setParam("literal.name", file.getFilename());
		req.setParam("literal.uploadpath", fileName);
		req.setParam("literal.workitemtype", workItem.getType());
		req.setParam("literal.instanceid", workItem.getInstId()+"");	// 중간에 대문자 인식 못함
		req.setParam("literal.taskid", workItem.getTaskId()+"");
		req.setParam("literal.title", workItem.getTitle());
		req.setParam("literal.writer", workItem.getWriter().getName());
		req.setParam("literal.topicid", instance.getTopicId());
		
		req.setAction(ContentStreamUpdateRequest.ACTION.COMMIT, true, true);
		req.process(solrServer);
	}
	
	public void insertWorkItem(WorkItem workItem, Instance instance) throws Exception{
		if( this.init() ){
			String type = workItem.getType();
			System.out.println("type = " + type);
			if( workItem.WORKITEM_TYPE_FILE.equals(type)){
				insertFile(workItem , instance);
			}else{
				insertText(workItem , instance);
			}
		}
	}
	
	public void codiFileSystemIndexing() throws Exception{
		if( this.init() ){
			SolrQuery query = new SolrQuery();
			query.setQuery("*:*");
			query.setRows(1000);
			QueryResponse queryResponse = solrServer.query(query);
			Iterator<SolrDocument> iter = queryResponse.getResults().iterator();
	
		    while (iter.hasNext()) {
		      SolrDocument resultDoc = iter.next();
		      Object obj = resultDoc.getFieldValue("workitemtype");
				if( "file".equals((String)obj) ){
					ArrayList content = (ArrayList)resultDoc.getFieldValue("content");
					if( content != null && content.size() >0){
						String fileName = GlobalContext.getPropertyString("filesystem.path", "") + content.get(0) ;
						File uploadFile = new File(fileName);
						if( uploadFile.exists() ){
							ArrayList content_type = (ArrayList)resultDoc.getFieldValue("content_type");						
							String contentType = content_type != null && content_type.size()>0? (String)content_type.get(0) : "" ;
							
							ContentStreamUpdateRequest req = new ContentStreamUpdateRequest("/update/extract");
							
							req.addFile(uploadFile , contentType);
							req.setParam("literal.id", (String)resultDoc.getFieldValue("id"));
							req.setParam("literal.name", (String)resultDoc.getFieldValue("name"));
							req.setParam("literal.uploadpath", (String)resultDoc.getFieldValue("name"));
							req.setParam("literal.workitemtype", (String)resultDoc.getFieldValue("workitemtype"));
							req.setParam("literal.instanceid", (String)resultDoc.getFieldValue("instanceid"));	// 중간에 대문자 인식 못함
							req.setParam("literal.taskid", (String)resultDoc.getFieldValue("id"));
							ArrayList title = (ArrayList)resultDoc.getFieldValue("title");
							req.setParam("literal.title", title != null && title.size()>0? (String)title.get(0) : "" );
							req.setParam("literal.writer", (String)resultDoc.getFieldValue("writer"));
							req.setParam("literal.topicid", (String)resultDoc.getFieldValue("topicid"));
							
							req.setAction(ContentStreamUpdateRequest.ACTION.COMMIT, true, true);
							req.process(solrServer);
						}
					}
				}
			}
		}
	}
}
