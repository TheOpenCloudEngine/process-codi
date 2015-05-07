package org.uengine.codi.mw3.knowledge;

import java.util.ArrayList;

import org.metaworks.annotation.Face;
import org.metaworks.annotation.ServiceMethod;

@Face(displayName="지식맵에서 검색")
public class MashupKMS extends MashupTool{

	@ServiceMethod(payload={"keyword", "targetNodeId"})
	public void search() throws Exception {
		setSearchResults(new ArrayList<SearchResult>());
		
		if( keyword != null){
			String[] subKeyword = keyword.split(" ");
			WfNode searchNode = new WfNode();
			String makingString = "";
			for( int j =0; j < subKeyword.length; j++ ){
				makingString += subKeyword[j];
				if(!( j == subKeyword.length -1 )){
					makingString += "|";
				}
			}
			searchNode.searchKMS( makingString );
			if( searchNode != null && searchNode.getChildNode() != null && searchNode.getChildNode().size() > 0){
				ArrayList<WfNode> nf = searchNode.getChildNode();
				for(int i = 0; i < nf.size(); i++){
					WfNode wfNode = nf.get(i);
					SearchResult searchResult = new SearchResult();
					searchResult.setTargetNodeId(getTargetNodeId());
					searchResult.setTitle(wfNode.getName());
					searchResult.setResultType("KMS");
					getSearchResults().add(searchResult);
				}
			}
			setSearchResults(searchResults);
		}
		
	}

}
