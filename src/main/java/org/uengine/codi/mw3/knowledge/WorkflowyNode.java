package org.uengine.codi.mw3.knowledge;

import java.net.UnknownHostException;
import java.util.ArrayList;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Available;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.ServiceMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.uengine.codi.mw3.model.ContentWindow;
import org.uengine.codi.mw3.model.Instance;
import org.uengine.codi.mw3.model.InstanceViewContent;
import org.uengine.codi.mw3.model.NewInstancePanel;
import org.uengine.codi.mw3.model.NewInstanceWindow;
import org.uengine.codi.mw3.model.ProcessDefinition;
import org.uengine.codi.mw3.model.Session;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class WorkflowyNode implements ContextAware {
	
	public WorkflowyNode() throws Exception {
		this("");
	}
	
	public WorkflowyNode(String nodeId) throws Exception {
		setMetaworksContext(new MetaworksContext());
		
		setId(nodeId);
		setName("");
		
		childNode = new ArrayList<WorkflowyNode>();
		
		setId(nodeId);
	}	
	
	String id;
		@Id
		public String getId() {
			return id;
		}	
		public void setId(String id) {
			this.id = id;
		}

	String name;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
	String linkedInstId;	
		public String getLinkedInstId() {
			return linkedInstId;
		}
		public void setLinkedInstId(String linkedInstanceId) {
			this.linkedInstId = linkedInstanceId;
		}

	String nameNext;
		public String getNameNext() {
			return nameNext;
		}
		public void setNameNext(String nameNext) {
			this.nameNext = nameNext;
		}
		
	WorkflowyNode parentNode;	
		public WorkflowyNode getParentNode() {
			return parentNode;
		}
		public void setParentNode(WorkflowyNode parentNode) {
			this.parentNode = parentNode;
		}	
		
	WorkflowyNode dragNode;
		public WorkflowyNode getDragNode() {
			return dragNode;
		}
		public void setDragNode(WorkflowyNode dragNode) {
			this.dragNode = dragNode;
		}
		
	ArrayList<WorkflowyNode> childNode;
		@Face(ejsPath="genericfaces/ArrayFace.ejs", options={"hideAddBtn"}, values={"true"})
		public ArrayList<WorkflowyNode> getChildNode() {
			return childNode;
		}
		public void setChildNode(ArrayList<WorkflowyNode> childNode) {
			this.childNode = childNode;
		}

	MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
	
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}
	
	public void addChildNode(WorkflowyNode newNode){
		addChildNode(childNode.size(), newNode);	
	}
	
	private void addChildNode(int index, WorkflowyNode newNode){
		childNode.add(index, newNode);
		newNode.setParentNode(this);
	}
	
	private void removeChildNode(int index){
		childNode.remove(index);
	}
	
	private WorkflowyNode getNode(WorkflowyNode findNode){
		
		WorkflowyNode resultNode = null;
		
		if(getId().equals(findNode.getId())){
			resultNode = this;
		}else{
			for(int i =0; i<getChildNode().size(); i++){
				resultNode = getChildNode().get(i).getNode(findNode);
				
				if(resultNode != null)
					break;				
			}
		}
		
		return resultNode;
			
	}
	
	private int getNodeIndex(){
		return getParentNode().getChildNode().indexOf(this);
	}
	
	@ServiceMethod(callByContent=true)
	@Hidden
	public WorkflowyNode add() throws Exception {
		
		if(getParentNode().getParentNode() != null && getName().length() == 0 && getNameNext().length() == 0)
			return outdent();

		boolean isFocusingNewNode = true;
		boolean isSaveThis = false;
		
		WorkflowyNode resultNode = new WorkflowyNode();
		WorkflowyNode focusNode = new WorkflowyNode();
		
		// 부모로 부터 현재 노드의 위치를 구한다.
		int nodeIndex = getNodeIndex();	
		
		WorkflowyNode newNode = new WorkflowyNode(WorkflowyNode.makeId());
		newNode.setParentNode(getParentNode());
		
		if(getNameNext().length() == 0){
			if(getChildNode().size()>0){			
				newNode.setParentNode(this);
				addChildNode(0, newNode);
				
				isSaveThis = true;
			}else{											
				getParentNode().addChildNode(nodeIndex+1, newNode);								
			}
		}else{
			newNode.setName(getName());
			getParentNode().addChildNode(nodeIndex, newNode);
			
			setName(getNameNext());
			
			isFocusingNewNode = false;
		}
		
		// DB 에 노드 저장
		// 마지막이 아닐 경우에 수정된 노드 부터 하위노드 모두 저장
		if(isSaveThis){
			save();
			saveChild();
		}else{
			resultNode = getParentNode();
			resultNode.saveChild(nodeIndex);
		}
		
		// DB 에서 다시 하위 노드 읽기
		resultNode.load();
		
		// 포커스 노드 선택
		if(isFocusingNewNode)
			focusNode = resultNode.getNode(newNode);		
		else
			focusNode = resultNode.getNode(this);
		
		focusNode.getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);
		focusNode.getMetaworksContext().setHow("add");
		
		return resultNode;

	}
	
	@ServiceMethod(callByContent=true)
	@Hidden
	public WorkflowyNode remove() throws Exception {
		
		// 부모로 부터 현재 노드의 위치를 구한다.
		int nodeIndex = getNodeIndex();
		
		WorkflowyNode resultNode = null;
		WorkflowyNode targetNode = null;
		WorkflowyNode focusNode = null;		
		
		if(nodeIndex == 0){			
			if(getChildNode().size() > 0){	// 처리X - 첫번째 노드, 자식 존재
				return null;
			}else{				
				if(getNameNext().length() > 0){	// 처리X - 첫번째 노드, 자식 미존재, 붙일 content 존재
					System.out.println("null-------------");
					setName(getName() + getNameNext());						
					save();
					
					getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);
					getMetaworksContext().setHow("remove");					
					
					return getParentNode();	
				}else{	// 처리O - 첫번째 노드, 자식 미존재, 붙일 content 미존재 -> 현재 노드 제거, 부모 노드 포커싱
					getParentNode().removeChildNode(nodeIndex);
					getParentNode().getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);
					getParentNode().getMetaworksContext().setHow("remove");
					
					// 삭제
					delete();
					
					return getParentNode();					
				}
			}
		} else {
			if(getChildNode().size() > 0){				
				if(getParentNode().getChildNode().get(nodeIndex-1).getChildNode().size() > 0){ // 처리X - 두번째 노드 이상, 자식 존재, 이전 노드의 자식 존재
					return null;
				}else {	// 처리O - 두번째 노드 이상, 자식 존재, 이전 노드의 자식 미존재 -> 현재 노드 제거, 현재 노드에 자식들을 이전 노드로 변경, 이전 노드 포커싱
					// 윗노드 삭제 삭제
					WorkflowyNode deleteNode = getParentNode().getChildNode().get(nodeIndex-1);
					deleteNode.delete();
					
					setName(deleteNode.getName());
					save();
					
					resultNode =  getParentNode();
					
					targetNode = this;
				}					
			}else{				
				if(getParentNode().getChildNode().get(nodeIndex-1).getChildNode().size() > 0 && getNameNext().length() > 0){ // 처리X - 두번째 노드 이상, 자식 미존재, 이전노드 자식노드 존재 and 붙일 content 존재
					return null;
				}else{	// 처리O - 두번째 노드 이상, 자식 미존재, 이전노드 자식노드 미존재 or 붙일 content 미존재 -> 현재 노드 제거, 이전 노드 또는 이전 노드 의 마지막 자식노드에 선택 and 붙임					
					// 삭제
					delete();
					
					targetNode = getParentNode().getChildNode().get(nodeIndex-1);
					
					while (targetNode.getChildNode().size() > 0)					
						targetNode = targetNode.getChildNode().get(targetNode.getChildNode().size()-1);
						
					targetNode.setName(targetNode.getName()+getNameNext());
					targetNode.setNameNext(getNameNext());

					// 저장
					targetNode.save();
										
					resultNode = targetNode.getParentNode();										
				}				
			}
		}
	
		resultNode.load();
		
		focusNode = resultNode.getNode(targetNode);
		focusNode.setNameNext(targetNode.getNameNext());
		focusNode.getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);
		focusNode.getMetaworksContext().setHow("remove");
	
		return resultNode;
	}
	
	@ServiceMethod(callByContent=true)
	@Hidden
	public WorkflowyNode indent() throws Exception {
		
		int nodeIndex = getNodeIndex();		
		if(nodeIndex == 0){
			return null;			
		}
		
		// 부모 변경 처리
		setParentNode(getParentNode().getChildNode().get(nodeIndex-1));
		getParentNode().addChildNode(this);
		
		// 저장
		save();		
		
				
		WorkflowyNode resultNode = getParentNode().getParentNode();
		
		// DB 에서 다시 하위 노드 읽기
		resultNode.load();
		// 포커스 노드 선택
		WorkflowyNode focusNode = resultNode.getNode(this);
		
		focusNode.getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);
		focusNode.getMetaworksContext().setHow("indent");
		
		return resultNode;
	}
	
	@ServiceMethod(callByContent=true)
	@Hidden
	public WorkflowyNode outdent() throws Exception {
		if(getParentNode().getParentNode() == null){
			return null;
		}
		
		// 부모의 부모로 부터 현재 노드의 부모 위치를 구한다.
		int nodeIndex = getParentNode().getNodeIndex();
					
		// 부모에서 현재 노드를 제거한다.
		getParentNode().removeChildNode(getNodeIndex());
		
		// 현재 노드의 부모를 부모의 부모로 변경한다.
		setParentNode(getParentNode().getParentNode());
		
		// 변경된 부모의 자식노드에 현재 노드를 추가한다. (이전에 구한 부모 위치) 
		getParentNode().addChildNode(nodeIndex+1, this);
		
		WorkflowyNode resultNode = getParentNode();
		resultNode.saveChild(nodeIndex+1);

		// DB 에서 다시 하위 노드 읽기
		resultNode.load();		
		// 포커스 노드 선택
		WorkflowyNode focusNode = resultNode.getNode(this);
		
		focusNode.setMetaworksContext(new MetaworksContext());
		focusNode.getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);
		focusNode.getMetaworksContext().setHow("outdent");
				
		return resultNode;
	}
	
	
	@ServiceMethod(callByContent=true)
	@Hidden
	public WorkflowyNode move() throws Exception {
		
		if(this.equals(getDragNode())){
			getDragNode().getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);
			getDragNode().getMetaworksContext().setHow("drag");

			return this;
		}				
		
		WorkflowyNode resultNode = null;
		WorkflowyNode focusNode = null;		
		
		int dragIndex = getDragNode().getNodeIndex();
		
		// 자식 존재
		// 자식 맨 위에 붙임
		if(getChildNode().size() > 0){
			
			
			// 드래그된 노드의 부모에서 제거 드래그노드 제거
			getDragNode().getParentNode().removeChildNode(dragIndex);
			getDragNode().setParentNode(this);
			
			addChildNode(0, getDragNode());
			
			resultNode = this;
			
		// 자식 없음
		// 다음 노드에 붙임	
		} else {
			int nodeIndex = getNodeIndex();
			
			getDragNode().getParentNode().removeChildNode(dragIndex);
						
			if(getParentNode().equals(getDragNode().getParentNode())){
				if(nodeIndex > dragIndex)
					nodeIndex = nodeIndex - 1;
			}			
			
			getDragNode().setParentNode(getParentNode());
			getParentNode().addChildNode(nodeIndex+1, getDragNode());
			
			resultNode = getParentNode();
			
		}

		resultNode.saveChild();
		resultNode.load();
		
		focusNode = resultNode.getNode(getDragNode());
		focusNode.getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);
		focusNode.getMetaworksContext().setHow("drag");
		
		return resultNode;
	}	

	public void saveChild() throws Exception {
		saveChild(0);
	}
	
	public void saveChild(int startPos) throws Exception {
		saveChild(startPos, getChildNode().size()-1);
	}
	
	public void saveChild(int startPos, int endPos) throws Exception {
		for(int i=startPos; i<=endPos; i++){
			if(i < getChildNode().size()){
				WorkflowyNode childNode = getChildNode().get(i);
				
				childNode.save();				
			}
		}
	}
	
	public void save() throws Exception {

		Mongo mongo = null;
		DB db = null;
		DBCollection coll = null;
		DBCursor cur = null;
		BasicDBObject find = null;
		BasicDBObject node = null;
		
		try{
			mongo = new Mongo();
			
			db = mongo.getDB("knowledge");				
							
			find = new BasicDBObject();
			find.put("id", getId());
			
			coll = db.getCollection("workflowy");
			cur = coll.find(find);
			
			node = new BasicDBObject();
			node.put("id", getId());
			node.put("name", getName());
			node.put("linkedInstId", getLinkedInstId());

			node.put("parentId", getParentNode().getId());
			node.put("index", getParentNode().getChildNode().indexOf(this));
			
			if(cur.hasNext()){
				coll.update(find, node);
			}else{
				coll.insert(node);
			}
			
			/*
			setChanged(false);
			
			for(int i=0; i<getChildNode().size(); i++){
				WorkflowyNode childNode = getChildNode().get(i);
				
				if(childNode.isChanged())
					childNode.save();
					
			}
			*/
		}finally{
			if(node != null)
				node.clear();
			
			if(find != null)
				find.clear();

			if(cur != null)
				cur.close();
						
			if(coll != null)
				coll = null;
						
			if(db != null)
				db.cleanCursors(false);
			
			if(mongo != null)
				mongo.close();			
		}
	}
	
	@ServiceMethod(callByContent=true)
	public void delete() throws UnknownHostException, MongoException {
		
		Mongo mongo = null;
		DB db = null;
		DBCollection coll = null;
		BasicDBObject find = null;
		
		try {
			mongo = new Mongo();
			
			db = mongo.getDB("knowledge");
			
			coll = db.getCollection("workflowy");
			
			find = new BasicDBObject();
			find.put("id", getId());
			
			coll.remove(find);
			
		} finally {			
			if(find != null)
				find.clear();

			if(coll != null)
				coll = null;
						
			if(db != null)
				db.cleanCursors(false);
			
			if(mongo != null)
				mongo.close();
		}
		
	}
	
	public void load() throws Exception {
		
		Mongo mongo = null;
		DB db = null;
		DBCollection coll = null;
		DBCursor cur = null;
		BasicDBObject find = null;
		BasicDBObject sort = null;
		
		DBObject result = null;
		
		getChildNode().clear();
		
		try{					
			mongo = new Mongo();
			
			db = mongo.getDB("knowledge");
			
			coll = db.getCollection("workflowy");
			
			find = new BasicDBObject();
			sort = new BasicDBObject();
			
			// 자신 설정하기 현재는 비활성화
			/*
			if(isChanged()){				
				find.append("id", getId());	
				
				cur = coll.find(find);
				
		        while(cur.hasNext()) {
		        	result = cur.next();
		        	
		        	setName((String)result.get("name"));
		        }
	        					
			}
			*/
						
			// 자식 불러오기
		    find.clear();
		    find.append("parentId", getId());
		    
		    sort.clear();
		    sort.append("index", 0);
		    
		    cur = coll.find(find).sort(sort);

	        while(cur.hasNext()) {
	        	result = cur.next();
	        	
	        	String id = (String)result.get("id");
	        	String name = (String)result.get("name");
	        	String linkedInstId = (String)result.get("linkedInstId");
	        	
	        	WorkflowyNode node = new WorkflowyNode(id);
	        	node.setParentNode(this);
	        	node.setName(name);
	        	node.setLinkedInstId(linkedInstId);
	        	node.load();
	        	node.getMetaworksContext().setWhen(getMetaworksContext().getWhen());
	        	
	        	addChildNode(node);
	        }
	        
		}finally{
			if(result != null)
				result = null;
			
			if(find != null)
				find.clear();
						
			if(sort != null)
				sort.clear();

			if(cur != null)
				cur.close();

			if(coll != null)
				coll = null;
						
			if(db != null)
				db.cleanCursors(false);
			
			if(mongo != null)
				mongo.close();			
			
		}		
	}
	
	@ServiceMethod(callByContent=true)
	@Available(when={MetaworksContext.WHEN_VIEW, MetaworksContext.WHEN_EDIT})
	public WorkflowyNode newNode() throws Exception {		
		WorkflowyNode newNode = new WorkflowyNode();		

		newNode.setParentNode(this);
		addChildNode(newNode);
		
		return this;
	}	
	
	
	@AutowiredFromClient
	public Session session;
	
	@ServiceMethod(inContextMenu=true, keyBinding="Ctrl+N")
	public ContentWindow newProcessInstance() throws Exception{
		NewInstancePanel newInstancePanel =  new NewInstancePanel();
		newInstancePanel.setKnowledgeNodeId(id);
		newInstancePanel.load(session);
		
		return new NewInstanceWindow(newInstancePanel);
	}
	
	@ServiceMethod(inContextMenu=true, keyBinding="Ctrl+D")
	public ContentWindow newDocument() throws Exception{
//		NewInstancePanel newInstancePanel =  new NewInstancePanel();
//		newInstancePanel.setKnowledgeNodeId(id);
//		
//		return newInstancePanel;
		
		//ProcessDefinition processDefinition = new ProcessDefinition();
		processDefinition.setDefId(new Long(150));
		return (ContentWindow) processDefinition.initiate()[0];
		
	}
	
	
	@Autowired ProcessDefinition processDefinition;
	
	@ServiceMethod(callByContent=true)
	public ContentWindow linkInstance() throws Exception{
		Instance instance = new Instance();
		instance.setInstId(new Long(getLinkedInstId()));
		
		instanceViewContent.load(instance);
		
		return instanceViewContent;

	}
	
	@Autowired
	public InstanceViewContent  instanceViewContent;
	
	public static String makeId() throws UnknownHostException, MongoException {

		Mongo mongo = null;
		DB db = null;
		DBCollection coll = null;
		BasicDBObject find = null;
		BasicDBObject insert = null;
		
		BasicDBObject update = null;
		BasicDBObject inc = null;
		
		DBObject result = null;
		
		try {
			mongo = new Mongo();
			
			db = mongo.getDB("knowledge");
			
			coll = db.getCollection("seq");
						
		    find  = new BasicDBObject();
		    find.append("_id", "node_seq");
		    
		    if(!coll.find(find).hasNext()){
		    	insert = new BasicDBObject();
		    	insert.put("_id", "node_seq");
		    	insert.put("seq", 1);
		    	
		    	coll.insert(insert);
		    }
		    
			update = new BasicDBObject();
			inc = new BasicDBObject();
			
			inc.put("seq", 1);
			update.put("$inc", inc);
			
			result = coll.findAndModify(find, update);
			
			String seq = String.valueOf(result.get("seq"));		    
		    
			return seq;
			
		}finally{
			if(result != null)
				result = null;
			
			if(update != null)
				update.clear();

			if(inc != null)
				inc.clear();
			
			if(insert != null)
				insert.clear();
			
			if(find != null)
				find.clear();
						
			if(coll != null)
				coll = null;
						
			if(db != null)
				db.cleanCursors(false);
			
			if(mongo != null)
				mongo.close();
		}
			
	}
			
}
