package org.uengine.codi.mw3.knowledge;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.ServiceMethod;
import org.uengine.codi.mw3.model.IUser;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class Workflowy implements ContextAware {
	
	public Workflowy(){
		
	}
	
	public Workflowy(IUser user) throws Exception {
		this(user.getUserId(), MetaworksContext.WHEN_VIEW);
	}
	
	public Workflowy(String rootNodeId, String context) throws Exception {
		setMetaworksContext(new MetaworksContext());
		getMetaworksContext().setWhen(context);
		
		//setUser(user);
		
		node = new WorkflowyNode(rootNodeId);
		node.setName("");				
		node.getMetaworksContext().setWhen(getMetaworksContext().getWhen());
		node.getMetaworksContext().setWhere("ROOT");
		
		if(getMetaworksContext().getWhen().equals(MetaworksContext.WHEN_VIEW)){
			node.load();
			
			if(node.getChildNode().size() == 0){
				WorkflowyNode newNode = new WorkflowyNode(WorkflowyNode.makeId());
				newNode.setParentNode(node);
				
				node.getChildNode().add(newNode);
				node.getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);
			}
		}
		
		setNode(node);
	}
	
//	IUser user;	
//		public IUser getUser() {
//			return user;
//		}	
//		public void setUser(IUser user) {
//			this.user = user;
//		}

	String keyword;	
		public String getKeyword() {
			return keyword;
		}	
		public void setKeyword(String keyword) {
			this.keyword = keyword;
		}

	WorkflowyNode node;
		public WorkflowyNode getNode() {
			return node;
		}
		public void setNode(WorkflowyNode node) {
			this.node = node;
		}

	MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}	
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}		
		
	@ServiceMethod(callByContent=true)
	public void search() throws Exception {

		Mongo mongo = null;
		DB db = null;
		DBCollection coll = null;
		DBCursor cur = null;
		BasicDBObject find = null;
		BasicDBObject sort = null;
		
		DBObject result = null;
		
		try{
			mongo = new Mongo();
			
			db = mongo.getDB("knowledge");
			
			coll = db.getCollection("workflowy");
			
			find = new BasicDBObject();
			sort = new BasicDBObject();
						
			System.out.println("keyword");
			System.out.println(keyword);
			
			// 자식 불러오기
		    find.clear();
		    find.append("name",  java.util.regex.Pattern.compile(keyword));
		    
		    cur = coll.find(find).sort(sort);

	        while(cur.hasNext()) {
	        	result = cur.next();
	        	
	        	String id = String.valueOf(result.get("id"));
	        	String name = (String)result.get("name");
	        	
	        	WorkflowyNode node = new WorkflowyNode(id);
	        	node.setParentNode(getNode());
	        	node.setName(name);
	        	node.load();
	        	node.getMetaworksContext().setWhen(getMetaworksContext().getWhen());
	        	getNode().addChildNode(node);
	        	
	        	System.out.println(node);
	        	
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
}
