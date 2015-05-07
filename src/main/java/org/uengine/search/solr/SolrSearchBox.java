package org.uengine.search.solr;

import org.apache.solr.client.solrj.SolrServer;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.widget.ModalWindow;
import org.uengine.codi.mw3.model.SearchBox;

public class SolrSearchBox extends SearchBox {
	
	public SolrSearchBox(){
		super();
	}
	
	static SolrServer solrServer;
	
	SolrWorkItem solrFileItem;
		public SolrWorkItem getSolrFileItem() {
			return solrFileItem;
		}
		public void setSolrFileItem(SolrWorkItem solrFileItem) {
			this.solrFileItem = solrFileItem;
		}

	@ServiceMethod(callByContent=true , target=ServiceMethodContext.TARGET_POPUP)
	public Object[] search() throws Exception{
		
		ModalWindow modal = new ModalWindow();
		
		return new Object[]{modal};
	}
}
