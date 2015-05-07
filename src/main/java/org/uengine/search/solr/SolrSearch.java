package org.uengine.search.solr;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.uengine.kernel.GlobalContext;

public class SolrSearch {
	
	public final static String USE_SEARCH_ENGINE = GlobalContext.getPropertyString("searchEngine.use", "0");
	
	String keyword;
		public String getKeyword() {
			return keyword;
		}
		public void setKeyword(String keyword) {
			this.keyword = keyword;
		}
		
	String topicId;
		public String getTopicId() {
			return topicId;
		}
		public void setTopicId(String topicId) {
			this.topicId = topicId;
		}
		
	public SolrSearch(){
		
	}
	
	public String searchInstance() throws Exception{
		String returnStr = null;
		ArrayList<String> list = new ArrayList<String>(); 
		QueryResponse queryResponse = this.search();
		List<SolrWorkItem> beans = queryResponse.getBeans(SolrWorkItem.class);
		// 인스턴스 아이디가 중복되서 들어갈수 있으니 일단 리스트에 담아 놓고 loop를 한번 더 돌림
		if( beans != null ){
			for(SolrWorkItem bean : beans){
				list.add(bean.getInstanceid());
			}
			
			for(int i=0 ; i < list.size(); i++){
				String instanceId = list.get(i);
				if( i == 0){
					returnStr = instanceId;
				}else{
					returnStr += "," + instanceId;
				}
			}
		}
		
		return returnStr;
	}
	
	public QueryResponse search() throws Exception{
		SolrData solrData = new SolrData();
		solrData.init();
		SolrServer server = SolrData.solrServer;
		if( server == null ){
			return null;
		}
		if( this.getKeyword() == null ){
			return null;
		}
		
		String makedKeyword = "text:";
//		if(this.getTopicId() != null){
//			makedKeyword = "topicid:" + this.getTopicId() + " and (text:";
//		}
		
		String[] str = keyword.split(" ");
		for(int i=0; i < str.length; i++){
			if( i ==0 ){
				makedKeyword += "*" + str[i] + "*";
			}else{
				makedKeyword += " or text:*" + str[i] + "*";
			}
		}
//		if(this.getTopicId() != null){
//			makedKeyword += ")"; 
//		}
		System.out.println("makedKeyword = " + makedKeyword);
		SolrQuery query = new SolrQuery();
		query.setQuery(makedKeyword);
		query.setRows(1000);		// TODO 현재 검색을 두번날리는 시점이라... row를 주기가 애매함.. 근데 안주면 기본 10개 밖에 안나옴
		query.addSort("id", SolrQuery.ORDER.desc);
		query.setHighlight(true);
		
		QueryResponse queryResponse = server.query(query);
		return queryResponse;
	}

}
