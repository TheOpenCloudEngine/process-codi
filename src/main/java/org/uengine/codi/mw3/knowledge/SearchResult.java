package org.uengine.codi.mw3.knowledge;

import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.ImagePath;
import org.metaworks.annotation.Name;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.annotation.Validator;
import org.metaworks.annotation.ValidatorContext;
import org.metaworks.annotation.ValidatorSet;
import org.uengine.codi.mw3.model.Session;

@Face(options="hideEditBtn", values="true")
public class SearchResult {
	
	String title;
	String description;
	String thumbnail;
	String url;
	String targetNodeId;
	String resultType;

	public String getTargetNodeId() {
		return targetNodeId;
	}
	public void setTargetNodeId(String targetNodeId) {
		this.targetNodeId = targetNodeId;
	}

	@Face(displayName="")
	@Hidden
	@Name
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	@Hidden
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@ImagePath
	@Face(displayName="&nbsp;")
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	
	@Hidden
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	@Hidden
	public String getResultType() {
		return resultType;
	}
	public void setResultType(String resultType) {
		this.resultType = resultType;
	}
	
	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_APPEND)
	@Face(displayName="선택")
	public Object[] choose() throws Exception{
		if( resultType != null && resultType.equals("KMS")){
			// 지식맵은 선택시에 wfNode 로의 add 가 이루어 지지 않는다.
			return null;
		}else{
			WfNode wfNode = new WfNode();
			wfNode.load(targetNodeId);
			wfNode.session = session;
			wfNode.copyFrom(wfNode.databaseMe());
			if( resultType != null && resultType.equals("slideshare")){
				wfNode.setNameNext(description);
				wfNode.setTypeNext("slideshare");	
			}else if( resultType != null && resultType.equals("WIKI")){
				// 주의 : 같은 변수에 서로 다른 목적으로 사용하는 변수를 넣었으니 조심해서 사용하자
				wfNode.setNameNext(title);	//	제일 상위 제목 
				wfNode.setThumbnailNext(description);	// xml 형식으로 넘어오니 객체화 시킨후에 저장
				wfNode.setTypeNext("wiki");	
			}else if( resultType != null && resultType.equals("LMS")){
				wfNode.setNameNext(title);
				wfNode.setUrlNext(url);
				wfNode.setThumbnailNext(thumbnail);
				wfNode.setTypeNext("LMS");	
			}else{
				wfNode.setNameNext(title);
				wfNode.setTypeNext("text");	
			}
			
			return wfNode.addMashup();
		}
	}
	
	@AutowiredFromClient
	public Session session;
}
