package org.uengine.codi.mw3.knowledge;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.metaworks.MetaworksContext;
import org.metaworks.Refresh;
import org.metaworks.Remover;
import org.metaworks.ToNext;
import org.metaworks.ToOpener;
import org.metaworks.ToPrev;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.dao.DAOUtil;
import org.metaworks.dao.Database;
import org.metaworks.dao.TransactionContext;
import org.metaworks.dao.UniqueKeyGenerator;
import org.metaworks.example.ide.SourceCode;
import org.metaworks.website.MetaworksFile;
import org.metaworks.widget.ModalWindow;
import org.springframework.beans.factory.annotation.Autowired;
import org.uengine.codi.mw3.model.ContentWindow;
import org.uengine.codi.mw3.model.Employee;
import org.uengine.codi.mw3.model.IInstance;
import org.uengine.codi.mw3.model.IUser;
import org.uengine.codi.mw3.model.Instance;
import org.uengine.codi.mw3.model.InstanceDrag;
import org.uengine.codi.mw3.model.InstanceView;
import org.uengine.codi.mw3.model.InstanceViewContent;
import org.uengine.codi.mw3.model.NewInstancePanel;
import org.uengine.codi.mw3.model.NewInstanceWindow;
import org.uengine.codi.mw3.model.Popup;
import org.uengine.codi.mw3.model.ProcessDefinition;
import org.uengine.codi.mw3.model.Session;
import org.uengine.codi.mw3.model.UnstructuredProcessInstanceStarter;
import org.uengine.codi.mw3.model.WorkItem;
import org.uengine.kernel.GlobalContext;
import org.uengine.processmanager.ProcessManagerRemote;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;

@XStreamAlias("node")
public class WfNode extends Database<IWfNode> implements IWfNode {
	
	static Hashtable<Integer, ArrayList<String>> nodeListeners = new Hashtable<Integer, ArrayList<String>>();
	public final static int LOAD_DEPTH = 3;
	
	@XStreamOmitField
	MetaworksContext metaworksContext;
	
	@Autowired
	ProcessManagerRemote connectionFactory;

//	@XStreamOmitField
	MetaworksFile logoFile;
		public MetaworksFile getLogoFile() {
			return logoFile;
		}
		public void setLogoFile(MetaworksFile logoFile) {
			this.logoFile = logoFile;
		}

	@XStreamAsAttribute
	String id;
		public String getId() {
			return id;
		}	
		public void setId(String id) {
			this.id = id;
		}	
				
	@XStreamAsAttribute
	String type;		
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		
	@XStreamAsAttribute
	String refId;
		public String getRefId() {
			return refId;
		}
		public void setRefId(String refId) {
			this.refId = refId;
		}

	@XStreamAsAttribute
	String visType;
		public String getVisType() {
			return visType;
		}
		public void setVisType(String visType) {
			this.visType = visType;
		}

	@XStreamAsAttribute
	String connType;
		
		public String getConnType() {
			return connType;
		}
		public void setConnType(String connType) {
			this.connType = connType;
		}

	@XStreamAlias("text")
	String name;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
	String authorId;
		public String getAuthorId() {
			return authorId;
		}
		public void setAuthorId(String authorId) {
			this.authorId = authorId;
		}
		
	@XStreamOmitField
	IUser author;
		public IUser getAuthor() {
			return author;
		}
		public void setAuthor(IUser author) {
			this.author = author;
		}

	@XStreamOmitField
	String nameNext;
		public String getNameNext() {
			return nameNext;
		}
		public void setNameNext(String nameNext) {
			this.nameNext = nameNext;
		}
		
	@XStreamOmitField
	String typeNext;
	
		public String getTypeNext() {
			return typeNext;
		}
		public void setTypeNext(String typeNext) {
			this.typeNext = typeNext;
		}

	@XStreamOmitField
	boolean focus;	
		public boolean isFocus() {
			return focus;
		}
		public void setFocus(boolean focus) {
			this.focus = focus;
		}
		
	@XStreamOmitField
	boolean close;
		public boolean isClose() {
			return close;
		}
		public void setClose(boolean close) {
			this.close = close;
		}

	@XStreamOmitField
	Long linkedInstId;	
		public Long getLinkedInstId() {
			return linkedInstId;
		}
		public void setLinkedInstId(Long linkedInstanceId) {
			this.linkedInstId = linkedInstanceId;
		}		
	
	@XStreamOmitField		
	int no;
		public int getNo() {
			return no;
		}
		public void setNo(int no) {
			this.no = no;
		}
		
	@XStreamOmitField
	String parentId;			
		public String getParentId() {
			return parentId;
		}
		public void setParentId(String parentId) {
			this.parentId = parentId;
		}
		
	String url;
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		
	String ext;
		public String getExt() {
			return ext;
		}
		public void setExt(String ext) {
			this.ext = ext;
		}

	String thumbnail;
		public String getThumbnail() {
			return thumbnail;
		}
		public void setThumbnail(String thumbnail) {
			this.thumbnail = thumbnail;
		}
	
	@XStreamOmitField
	String urlNext;
		public String getUrlNext() {
			return urlNext;
		}
		public void setUrlNext(String urlNext) {
			this.urlNext = urlNext;
		}
	
	@XStreamOmitField
	String thumbnailNext;
		public String getThumbnailNext() {
			return thumbnailNext;
		}
		public void setThumbnailNext(String thumbnailNext) {
			this.thumbnailNext = thumbnailNext;
		}

	@XStreamOmitField
	WfNode dragNode;
		public WfNode getDragNode() {
			return dragNode;
		}
		public void setDragNode(WfNode dragNode) {
			this.dragNode = dragNode;
		}
	
	@XStreamAlias("childNodes")
	ArrayList<WfNode> childNode;
		public ArrayList<WfNode> getChildNode() {
			return childNode;
		}
		public void setChildNode(ArrayList<WfNode> childNode) {
			this.childNode = childNode;
		}		
		
	@XStreamOmitField
	public int loadDepth = 0;
		public int getLoadDepth() {
			return loadDepth;
		}
		public void setLoadDepth(int loadDepth) {
			this.loadDepth = loadDepth;
		}
		
	@XStreamOmitField
	String secuopt;
		public String getSecuopt() {
			return secuopt;
		}
		public void setSecuopt(String secuopt) {
			this.secuopt = secuopt;
		}
		
	@XStreamOmitField
	String companyId;
		public String getCompanyId() {
			return companyId;
		}
		public void setCompanyId(String companyId) {
			this.companyId = companyId;
		}
	
	@XStreamOmitField
	boolean first;
		public boolean isFirst() {
			return first;
		}
		public void setFirst(boolean first) {
			this.first = first;
		}
	
	@XStreamOmitField
	int budget;
		public int getBudget() {
			return budget;
		}
		public void setBudget(int budget) {
			this.budget = budget;
		}
	
	@XStreamOmitField
	int effort;
		public int getEffort() {
			return effort;
		}
		public void setEffort(int effort) {
			this.effort = effort;
		}
	
	@XStreamOmitField
	int benefit;
		public int getBenefit() {
			return benefit;
		}
		public void setBenefit(int benefit) {
			this.benefit = benefit;
		}
	
	@XStreamOmitField
	int penalty;
		public int getPenalty() {
			return penalty;
		}
		public void setPenalty(int penalty) {
			this.penalty = penalty;
		}
		
	@XStreamOmitField
	Date startDate;
		public Date getStartDate() {
			return startDate;
		}
		public void setStartDate(Date startDate) {
			this.startDate = startDate;
		}
		
	@XStreamOmitField
	Date endDate;
		public Date getEndDate() {
			return endDate;
		}
		public void setEndDate(Date endDate) {
			this.endDate = endDate;
		}
	
	@XStreamOmitField
	int progress;
		public int getProgress() {
			return progress;
		}
		public void setProgress(int progress) {
			this.progress = progress;
		}
	
	@XStreamOmitField
	String rootId;
		public String getRootId() {
			return rootId;
		}
		public void setRootId(String rootId) {
			this.rootId = rootId;
		}
	
	String description;
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
	
	@XStreamOmitField
	boolean isReleased;
		public boolean getIsReleased() {
			return isReleased;
		}
		public void setIsReleased(boolean isReleased) {
			this.isReleased = isReleased;
		}
	
	
	@XStreamOmitField
	boolean isDistributed;
		public boolean getIsDistributed() {
			return isDistributed;
		}
		public void setIsDistributed(boolean isDistribued) {
			this.isDistributed = isDistribued;
		}
		
	@XStreamOmitField
	public FilepathInfo filepathInfo;
		public FilepathInfo getFilepathInfo() {
			return filepathInfo;
		}
		public void setFilepathInfo(FilepathInfo filepathInfo) {
			this.filepathInfo = filepathInfo;
		}
	
	@XStreamOmitField
	public String projectAlias;
		public String getProjectAlias() {
			return projectAlias;
		}
		public void setProjectAlias(String projectAlias) {
			this.projectAlias = projectAlias;
		}
		
		
	public WfNode() {
		setChildNode(new ArrayList<WfNode>());
		getMetaworksContext().setWhen(MetaworksContext.WHEN_VIEW);
	}
	
	public void search(String keyword) throws Exception {
		
		try {
			
			DAOUtil daoUtil = new DAOUtil();
			
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT *");
			sb.append("  FROM bpm_knol");
			sb.append(" WHERE name like ?name");
			sb.append(" ORDER BY " + daoUtil.replaceReservedKeyword("no"));
			
			IWfNode node = sql(sb.toString());
			node.setName("%" + keyword + "%");
			node.select();
			
			while(node.next()){
				WfNode addNode = new WfNode();
				
				addNode.copyFrom(node);
				addNode.getMetaworksContext().setWhen(getMetaworksContext().getWhen());
				addNode.loadChildren();
				
				this.addChildNode(addNode);
			}
		} catch (Exception e) {
			if(!"ROOT".equals(getMetaworksContext().getHow()))
				getMetaworksContext().setHow("NONE");
		}
	}
	
	public void searchKMS(String keyword) throws Exception {
		
		try {
			
			DAOUtil daoUtil = new DAOUtil();
			
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT *");
			sb.append("  FROM bpm_knol");
			sb.append(" WHERE name REGEXP ?name");
			sb.append(" ORDER BY " + daoUtil.replaceReservedKeyword("no"));
			
			IWfNode node = sql(sb.toString());
			node.setName(keyword);
			node.select();
			
			while(node.next()){
				WfNode addNode = new WfNode();
				
				addNode.copyFrom(node);
				addNode.getMetaworksContext().setWhen(getMetaworksContext().getWhen());
				addNode.loadChildren();
				
				this.addChildNode(addNode);
			}
		} catch (Exception e) {
			if(!"ROOT".equals(getMetaworksContext().getHow()))
				getMetaworksContext().setHow("NONE");
		}
	}
	
	public IWfNode findMe() throws Exception {
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT *");
		sb.append("  FROM bpm_knol");
		sb.append(" WHERE Id=?Id");
		sb.append(" ORDER BY no");
		
		IWfNode findNode = (IWfNode) sql(IWfNode.class,	sb.toString());
		
		findNode.setId(this.getId());
		findNode.select();

		if(findNode.next())
			return findNode;
		else
			return null;

	}
	
	public void load() throws Exception {		
		load(this.getId());
	}
	
	public void load(String nodeId) throws Exception {
		
//		WfNode thisNode = new WfNode();
//		thisNode.setId(nodeId);
//		this.copyFrom(thisNode.databaseMe());
		
		setId(nodeId);
		
		IWfNode thisNode = this.findMe();
		
		if(thisNode != null){
			setName(thisNode.getName());
			setType(thisNode.getType());
		}
		
		if(this.getLoadDepth() < LOAD_DEPTH){
			if(this.getLoadDepth() > -1)
				setLoadDepth(getLoadDepth()+1);
			
			setChildNode(this.loadChildren());
			setClose(false);
		}
/*		try {
			setId(nodeId);
			
			if(this.getLoadDepth() > -1)
				setLoadDepth(0);
			
			IWfNode dao = (IWfNode)get(getClass(), createKeyObject(), this);
			this.copyFrom(dao);
		} catch (Exception e) {
//			if(!"ROOT".equals(getMetaworksContext().getHow()))
//				getMetaworksContext().setHow("NONE");
		}
*/
		
		setFirst(false);
		
	}
	
	
	
	public ArrayList<WfNode> loadChildren() throws Exception {
		
		ArrayList<WfNode> child = new ArrayList<WfNode>();
		
/*		if(this.getLoadDepth() < LOAD_DEPTH){
			if(this.getLoadDepth() > -1)
				setLoadDepth(getLoadDepth()+1);	*/					
			/*
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT *");
			sb.append("  FROM bpm_knol");
			sb.append(" WHERE parentId=?parentId");
			sb.append(" ORDER BY no");
			
			IWfNode findNode = (IWfNode) sql(IWfNode.class,	sb.toString());
			
			findNode.set("parentId", this.getId());
			findNode.select();
			
			if(findNode.size() > 0){
				while (findNode.next()) {
					WfNode node = new WfNode();
					
					node.copyFrom(findNode);
					node.setChildNode(new ArrayList<WfNode>());
					node.setMetaworksContext(new MetaworksContext());
					node.getMetaworksContext().setWhen(getMetaworksContext().getWhen());
					node.getMetaworksContext().setWhere(this.getMetaworksContext().getWhere());
					
					if(node.getVisType()==null && !"ROOT".equals(getMetaworksContext().getHow())){
						node.getMetaworksContext().setHow(getMetaworksContext().getHow());
					}else{
						node.getMetaworksContext().setHow(node.getVisType());
					}
					
					node.setLoadDepth(this.getLoadDepth());
					node.setClose(true);
					node.setFirst(this.isFirst());
					
					if(!this.isFirst())
						node.load();
					//node.loadChildren();  //재귀호출로 하위를 갖고 오네... 기본 3 depth정도로 제한을 둬야할듯.. 
					
					child.add(node);
				}
			}
			*/
			
			/*
		}else{
			
		}*/
		
		DAOUtil daoUtil = new DAOUtil();
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT *");
		sb.append("  FROM bpm_knol");
		sb.append(" WHERE parentId=?parentId");
		sb.append(" ORDER BY " + daoUtil.replaceReservedKeyword("no"));
		
		IWfNode findNode = (IWfNode) sql(IWfNode.class,	sb.toString());
		
		findNode.set("parentId", this.getId());
		findNode.select();
		
		if(findNode.size() > 0){
			while (findNode.next()) {
				WfNode node = this.makeNewNode();
				
				node.copyFrom(findNode);
				node.setChildNode(new ArrayList<WfNode>());
				node.setMetaworksContext(new MetaworksContext());
				node.getMetaworksContext().setWhen(getMetaworksContext().getWhen());
				node.getMetaworksContext().setWhere(this.getMetaworksContext().getWhere());

				if(node.getVisType()==null){
					node.getMetaworksContext().setHow("normal");
				}else{
					node.getMetaworksContext().setHow(node.getVisType());
				}
				
				node.setLoadDepth(this.getLoadDepth());
				node.setClose(true);
				node.setFirst(this.isFirst());
				
				if(!this.isFirst())
					node.load();
				
				child.add(node);
			}
		}
		
		return child;
	}
	
	public String makeId() throws Exception {
		return String.valueOf(UniqueKeyGenerator.issueKey("bpm_knol", TransactionContext.getThreadLocalInstance()));
	}
		
	public void createMe() throws Exception {
		String nodeId = this.makeId();
		
		setId(nodeId);

		createDatabaseMe();
		flushDatabaseMe();
	}
	
	public void createMe(String id) throws Exception {
		setId(id);
		createDatabaseMe();
		flushDatabaseMe();
	}

	public void saveMe() throws Exception {
		syncToDatabaseMe();
		flushDatabaseMe();
	}
	
	public void deleteMe() throws Exception {
		deleteDatabaseMe();
	}
	
	public void clearFocus() {
		setFocus(false);
		
		for(int i=0; i<getChildNode().size(); i++){
			getChildNode().get(i).clearFocus();
		}
	}
	
	/*******************************************
	 * 
	 * function
	 *  
	 *******************************************/
	public int getNodeIndex(WfNode node){
		for(int i=0; i<childNode.size(); i++){
			if(((WfNode)childNode.get(i)).getId().equals(node.getId()))
				return i;
			
		}
		return -1;
	}
	public void addChildNode(WfNode newNode) throws Exception {
		if(childNode == null){
			setChildNode(new ArrayList<WfNode>());
		}
		addChildNode(childNode.size(), newNode);	
	}
	
	public void addChildNode(int index, WfNode newNode) throws Exception {
		newNode.setNo(index);
		newNode.setParentId(this.getId());
		
		childNode.add(index, newNode);
		
		// update
		if(childNode.size()-1 > index){
			
			DAOUtil daoUtil = new DAOUtil();
			
			StringBuffer sb = new StringBuffer();
			sb.append("update bpm_knol");
			sb.append("   set no=no+1");
			sb.append(" where parentId=?parentId");
			sb.append("   and " + daoUtil.replaceReservedKeyword("no") + ">=?no");
			
			IWfNode updateNode = sql(sb.toString());
			updateNode.setParentId(this.getId());
			updateNode.setNo(index);
			
			updateNode.update();
		}
		
		// reorder
		for(int i=index; i<childNode.size(); i++){
			IWfNode node = (WfNode)childNode.get(i);
			
			node.setNo(i);
		}
		
	}
	
	public void removeChildNode(int index) throws Exception {
		childNode.remove(index);
		
		if(childNode.size() > 0){
			for(int i=index; i<childNode.size(); i++){
				IWfNode node = (WfNode)childNode.get(i);
				
				node.setNo(i);
			}
			
			// update
			if(childNode.size() > index-1){
				
				DAOUtil daoUtil = new DAOUtil();
				
				StringBuffer sb = new StringBuffer();
				sb.append("update bpm_knol");
				sb.append("   set no=no-1");
				sb.append(" where parentId=?parentId");
				sb.append("   and " + daoUtil.replaceReservedKeyword("no") + ">=?no");
				
				IWfNode updateNode = sql(sb.toString());
				updateNode.setParentId(this.getId());
				updateNode.setNo(index);
				
				updateNode.update();
			}			
		}
	}
	
	public WfNode getNode(String findId){
		
		WfNode resultNode = null;
		
		if(getId().equals(findId)){
			resultNode = this;
		}else{
			for(int i =0; i<getChildNode().size(); i++){
				resultNode = getChildNode().get(i).getNode(findId);
				
				if(resultNode != null)
					break;				
			}
		}
		
		return resultNode;			
	}
	
	public void copyMetaworksContext(MetaworksContext metaworksContext){
		this.getMetaworksContext().setWhen(metaworksContext.getWhen());
		this.getMetaworksContext().setHow(metaworksContext.getHow());
		this.getMetaworksContext().setWhere(metaworksContext.getWhere());
	}
	/*******************************************
	 * 
	 * Service Method
	 *  
	 *******************************************/
	private void addNodeListener(Integer nodeId){
		
		ArrayList<String> nodeListenerArray = null;
		if(!nodeListeners.containsKey(nodeId)){
			nodeListenerArray = new ArrayList<String>();
			
			nodeListeners.put(nodeId, nodeListenerArray);
		}else{
			nodeListenerArray = nodeListeners.get(nodeId);
		}
		WebContext wctx = WebContextFactory.get();

		nodeListenerArray.add(wctx.getScriptSession().getId());

	}
	
	public Object[] add() throws Exception {
		
		
		// 입력 값이 없고 위치가 최상위가 아니라면 outdent
		if(getName().length() == 0 && getNameNext().length() == 0){
			final Object[] returnObjects = this.outdent();
			
			if(returnObjects != null)
				return returnObjects;				
		}
		
		// 반환 값
		Object targetObject = null;
		
		// 부모 노드를 구한다
		WfNode parentNode = this.makeNewNode();
		parentNode.copyMetaworksContext(getMetaworksContext());
		parentNode.setLoadDepth(-1);
		parentNode.load(this.getParentId());
		
		// 내 노드를 구한다. -> 저장
		final WfNode node = parentNode.getNode(this.getId());
		node.setName(this.getName());
		node.save();
			
		// 새로운 노드를 만든다.
		final WfNode newNode = this.makeNewNode();
		
		if(getTypeNext() != null && getTypeNext().length() > 0){
			newNode.setName(getNameNext());
			newNode.setType(getTypeNext());
			// wiki 로 들어온 데이터는 특별하게 하위노드까지 입력하는 작업을 해줘야 한다.
			if( getTypeNext().equalsIgnoreCase("wiki")){
				String childXmlData = getThumbnailNext();	// searchResult 에서 xml형식으로 넘어온 데이터
				XStream xstream = new XStream();
				Object object = xstream.fromXML(childXmlData);
				System.out.println("111");
			}else{
				if( getUrlNext() != null ){
					newNode.setUrl(getUrlNext());
				}
				if( getThumbnailNext() != null ){
					newNode.setThumbnail(getThumbnailNext());
				}
			}
			node.addChildNode(newNode);
			
			node.setFocus(true);
			node.getMetaworksContext().setHow("add");
		}else{
			newNode.setFocus(true);
			newNode.getMetaworksContext().setHow("add");
			
			if(getNameNext().length() == 0){
				if(node.getChildNode().size() > 0){
					node.addChildNode(0, newNode);
				}else{				
					parentNode.addChildNode(node.getNo()+1, newNode);
					
					targetObject = new ToNext(this, newNode);
				}				
			}else{
				newNode.setName(getNameNext());
				if( getUrl() != null ){
					newNode.setUrl(getUrl());
				}
				if( getThumbnail() != null ){
					newNode.setThumbnail(getThumbnail());
				}
				
				parentNode.addChildNode(node.getNo()+1, newNode);			
							
				targetObject = new ToNext(this, newNode);				
			}
		}
		
		newNode.setAuthorId(session.getUser().getUserId());		
		newNode.setCompanyId(session.getCompany().getComCode());
		newNode.createMe();

		
		final Object[] returnObjects;
		
		if(targetObject == null)
			returnObjects = new Object[]{new Refresh(node)};
		else
			returnObjects = new Object[]{new Refresh(node), targetObject};
		
		

		// 근방에 있는 노드들에게 알려줌... 여그 노드 추가됐어여~~
		//Integer nodeIndexInteger = new Integer(parentId);
		//if(nodeListeners.containsKey(nodeIndexInteger)){
		//	ArrayList<String> parentNodeListeners = nodeListeners.get(nodeIndexInteger);
		
		//ArrayList<String> parentNodeListeners = wctx.getScriptSession();

		/*
		WebContext wctx = WebContextFactory.get();
		String mySessionId = wctx.getScriptSession().getId();

		final Object[] returnObjects;
		
		if(targetObject == null)
			returnObjects = new Object[]{new Refresh(node)};
		else
			returnObjects = new Object[]{new Refresh(node), targetObject};
			
		String currentPage = wctx.getCurrentPage();

		   Collection<ScriptSession> sessions = wctx.getScriptSessionsByPage(currentPage);

			for(ScriptSession session : sessions ){
				String parentNodeListenerSessionId = session.getId();
				
				if(!parentNodeListenerSessionId.equals(mySessionId))
				Browser.withSession(parentNodeListenerSessionId, new Runnable(){

					@Override
					public void run() {
						ScriptSessions.addFunctionCall("mw3.locateObject", new Object[]{returnObjects, null, "body"});
						ScriptSessions.addFunctionCall("mw3.onLoadFaceHelperScript", new Object[]{});

					}
					
				});

			}
		//}
		*/
		
		return returnObjects;
	}
	
	public Object[] addMashup() throws Exception {
		// 검색에서 올라온 데이터이기 때문에 자기자신을 모두 채워준다.
		
		// 새로운 노드를 만든다.
		final WfNode newNode = this.makeNewNode();
		
		if(getTypeNext() != null && getTypeNext().length() > 0){
			newNode.setName(getNameNext());
			newNode.setType(getTypeNext());
			newNode.setParentId(this.getId());
			if( getTypeNext().equalsIgnoreCase("wiki")){
				// TODO
			}else{
				if( getUrlNext() != null ){
					newNode.setUrl(getUrlNext());
				}
				if( getThumbnailNext() != null ){
					newNode.setThumbnail(getThumbnailNext());
				}
			}
			this.addChildNode(newNode);
			this.setFocus(true);
			this.getMetaworksContext().setHow("add");
		}
		
		newNode.setAuthorId(session.getUser().getUserId());		
		newNode.setCompanyId(session.getCompany().getComCode());
		newNode.createMe();
		
		// wiki 로 들어온 데이터는 특별하게 하위노드까지 입력하는 작업을 해줘야 한다.
		if( getTypeNext().equalsIgnoreCase("wiki")){
			String childXmlData = getThumbnailNext();	// searchResult 에서 xml형식으로 넘어온 데이터
			XStream xStream = new XStream();
//						Mapper mapper = xStream.getMapper();
			xStream.alias("node", WfNode.class);
			xStream.aliasField("text", WfNode.class, "name");
//						xStream.registerConverter(new MapConverter(mapper));
			WfNode xmlObject = (WfNode)xStream.fromXML(childXmlData);
			newNode.setChildNode(xmlObject.getChildNode());
			newNode.createChildNodesInsert();
		}
		
		return new Object[]{new Refresh(this)};
	}
	
	public void createChildNodesInsert() throws Exception{
		ArrayList<WfNode> childList = this.getChildNode();
		if( childList != null && childList.size() > 0 ){
			for(int i =0; i<childList.size(); i++){
				WfNode node = childList.get(i);
				String title = node.getName();
				if( title.trim().startsWith("http") ){
					node.setType("img");
				}else{
					node.setType(this.getType());
				}
				// 없는 부가적인 데이터를 채워준다
				node.setParentId(this.getId());
				node.setAuthorId(this.getAuthorId());		
				node.setCompanyId(this.getCompanyId());
				int LimitValue = 1000;
				if( title.length() > LimitValue ){
					int cnt = title.length() / LimitValue ; 	// 몫
					String tempString = null;
					int beginIndex = 0;
					int endIndex = LimitValue;
					for( int j =1 ; j <= cnt+1 ; j++ ){
						if( j != 1 ){
							beginIndex = endIndex ;
							endIndex = beginIndex + LimitValue;
						}
						if( j == cnt + 1 ){
							endIndex = title.length();
						}
						tempString = title.substring(beginIndex, endIndex);
						node.setName(tempString.trim());
						node.createMe();
					}
				}else{
					node.setName(title.trim());
					node.createMe();
				}
				
				
				node.createChildNodesInsert();
			}
		}
	}
	
	public Object[] indent() throws Exception {

		// 부모 노드를 구한다
		WfNode parentNode = this.makeNewNode();
		parentNode.setMetaworksContext(getMetaworksContext());
		parentNode.setLoadDepth(-1);
		parentNode.load(this.getParentId());		

		WfNode node = parentNode.getNode(this.getId());		
		if(node.getNo() == 0){
			return null;			
		}
		
		// 부모 노드의 마지막 자식 노드를 구한다. (변경 부모 노드)
		WfNode appendNode = parentNode.getChildNode().get(node.getNo() - 1);
		
		// 부모 변경 처리
		parentNode.removeChildNode(node.getNo());	// 이전 부모 노드에서 제거
		appendNode.addChildNode(node);				// 변경 부모 노드에 추가
						
		// 변경 정보 적용
		node.setName(this.getName() + this.getNameNext());
		node.setNameNext(this.getNameNext());
		node.saveMe();
		node.setClose(false);
		node.setFocus(true);
		
		if(node.getNo() == 0){
			return new Object[]{new Remover(this), new Refresh(appendNode)};
		}else{
			WfNode targetNode = appendNode.getChildNode().get(node.getNo()-1);
			targetNode.setChildNode(null);
			
			return new Object[]{new Remover(this), new ToNext(targetNode, node)};			
		}
	}
		
	
	public Object[] outdent() throws Exception {

		// 부모 존재여부 검사
		if("-1".equals(this.getParentId()))
			return null;
				
		// 부모 노드를 구한다
		WfNode parentNode = this.makeNewNode();
		parentNode.setMetaworksContext(this.getMetaworksContext());
		parentNode.setId(this.getParentId());		
		
		String parentParentId = parentNode.databaseMe().getParentId();
		
		// 부모 노드의 부모를 구한다. (변경 부모 노드)
		WfNode appendNode = this.makeNewNode();
		appendNode.setMetaworksContext(this.getMetaworksContext());
		appendNode.setId(parentParentId);		
		
		if("-1".equals(parentParentId)){
			appendNode.setLoadDepth(-1);
			appendNode.loadChildren();
			appendNode.getMetaworksContext().setWhen("ROOT");
		}else{
			appendNode.setLoadDepth(-1);
			appendNode.load();			
		}
		parentNode = appendNode.getNode(parentNode.getId());		
		
		// 현재 노드를 구한다.
		WfNode node = parentNode.getNode(this.getId());
		
		// 부모 변경 처리
		appendNode.addChildNode(node);				// 변경 부모 노드에 추가
				
		// 변경 정보 적용
		node.setName(this.getName() + this.getNameNext());
		node.setNameNext(this.getNameNext());
		node.saveMe();
		node.setFocus(true);
		
		WfNode targetNode = appendNode.getChildNode().get(node.getNo()-1);
		targetNode.setChildNode(null);
		
		return new Object[]{new Remover(this), new ToNext(targetNode, node)};
	}	
	
	public Object[] remove() throws Exception {
		
		// 부모 노드를 구한다
		WfNode parentNode = this.makeNewNode();
		parentNode.setMetaworksContext(this.getMetaworksContext());
		parentNode.setId(this.getParentId());
		parentNode.setLoadDepth(-1);
		parentNode.load();
		
		if("-1".equals(parentNode.getId()) && parentNode.getChildNode().size() == 1)
			return null;

		// 현재 노드를 구한다.
		WfNode node = parentNode.getNode(this.getId());
		
		// 반환 노드
		WfNode resultNode = null;
		
		if(node.getNo() == 0){
			if(node.getChildNode().size() > 0){	// 처리X - 첫번째 노드, 자식 존재
				return null;
			}else{
				if(this.getNameNext() != null && this.getNameNext().length() > 0){	// 처리X - 첫번째 노드, 자식 미존재, 붙일 content 존재
					return null;	
				}else{	// 처리O - 첫번째 노드, 자식 미존재, 붙일 content 미존재 -> 현재 노드 제거, 부모 노드 포커싱
					resultNode = parentNode;
				}
			}
		}else{
			WfNode actionNode = parentNode.getChildNode().get(node.getNo()-1);

			if(node.getChildNode().size() > 0){
				if(actionNode.getChildNode().size() > 0){ // 처리X - 두번째 노드 이상, 자식 존재, 이전 노드의 자식 존재
					return null;
				}else{	// 처리O - 두번째 노드 이상, 자식 존재, 이전 노드의 자식 미존재 -> 현재 노드 제거, 현재 노드에 자식들을 이전 노드로 변경, 이전 노드 포커싱					
					// 윗노드 삭제 처리
					actionNode.setName(actionNode.getName() + this.getNameNext());
					actionNode.setNameNext(this.getNameNext());
					actionNode.saveMe();
					
					resultNode = actionNode;
				}	
			}else{
				
				if(actionNode.getChildNode().size() > 0 && this.getNameNext().length() > 0){ // 처리X - 두번째 노드 이상, 자식 미존재, 이전노드 자식노드 존재 and 붙일 content 존재
					return null;
				}else{	// 처리O - 두번째 노드 이상, 자식 미존재, 이전노드 자식노드 미존재 or 붙일 content 미존재 -> 현재 노드 제거, 이전 노드 또는 이전 노드 의 마지막 자식노드에 선택 and 붙임
					while (actionNode.getChildNode().size() > 0)					
						actionNode = actionNode.getChildNode().get(actionNode.getChildNode().size()-1);
						
					actionNode.setName(actionNode.getName() +this.getNameNext());					
					actionNode.setNameNext(this.getNameNext());
					actionNode.saveMe();
					
					resultNode = actionNode;
				}
				
			}			
		}
		
		parentNode.removeChildNode(node.getNo());
		node.deleteMe();
		
		if(resultNode == null){
			return new Object[]{new Remover(this)};
		}else{
			// reload
			resultNode.load();
			resultNode.getMetaworksContext().setHow("remove");
			resultNode.setFocus(true);
			
			return new Object[]{new Remover(this), new Refresh(resultNode)};	
		}
	}
	
	public Object[] move() throws Exception {
		
		if(this.getId() == this.getDragNode().getId()){
			return null;
		}				

		// Child Verify
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT  hi.id");
		sb.append("  FROM    (");
		sb.append("		      SELECT  hierarchy_connect_by_parent_eq_prior_id(id) AS id, @level AS level");
		sb.append("				FROM    (");
		sb.append("						 SELECT  @start_with := ?id,");
		sb.append("								 @id := @start_with,");
		sb.append("						   	     @level := 0");
		sb.append("						) vars, bpm_knol");
		sb.append("			   WHERE   @id IS NOT NULL");
		sb.append("			 ) ho");
		sb.append("	 JOIN    bpm_knol hi");
		sb.append("	   ON    hi.id = ho.id");
		sb.append("	WHERE hi.id = ?dropId");
		
		IWfNode childList = sql(sb.toString());
		childList.setId(dragNode.getId());
		childList.set("dropId", this.getId());
		childList.select();
		if(childList.next())
			return null;
		
		
		WfNode parentNode = this.makeNewNode();
		parentNode.setMetaworksContext(this.getMetaworksContext());
		parentNode.setId(this.getDragNode().getParentId());
		parentNode.load();

		WfNode dragNode = parentNode.getNode(this.getDragNode().getId());		
		
		// 부모 노드를 구한다
		parentNode = this.makeNewNode();;
		parentNode.setMetaworksContext(this.getMetaworksContext());
		parentNode.setId(this.getParentId());
		parentNode.load();
		
		// 자신을 구한다.
		WfNode node = parentNode.getNode(this.getId());
		
		if(node.getParentId().equals(dragNode.getParentId()) && node.getNo() == dragNode.getNo()){
			return null;
		}
		
		WfNode removeNode = this.makeNewNode();
		removeNode.setId(dragNode.getId());
		
		// 자식 존재
		// 자식 맨 위에 붙임
		if(node.getChildNode().size() > 0){
			if(node.getId().equals(dragNode.getParentId()) && dragNode.getNo() == 0){
				return null;
			}
			
			WfNode targetNode = this.makeNewNode();
			targetNode.setId(node.getChildNode().get(0).getId());
			
			node.removeChildNode(dragNode.getNo());
			node.addChildNode(0, dragNode);
			
			dragNode.saveMe();
			
			return new Object[]{new Remover(removeNode), new ToPrev(targetNode, dragNode)};
			//
			
		// 자식 없음
		// 다음 노드에 붙임	
		} else {
			parentNode.removeChildNode(dragNode.getNo());
			parentNode.addChildNode(node.getNo()+1, dragNode);			
			dragNode.saveMe();
			
			return new Object[]{new Remover(removeNode), new ToNext(this, dragNode)};
			
		}
		
	}	
	
	public void save() throws Exception {
		this.saveMe();
	}
	
	public Object[] removeNode() throws Exception {
		
		// 부모 노드를 구한다
		WfNode parentNode = new WfNode();
		parentNode.setMetaworksContext(this.getMetaworksContext());
		parentNode.setId(this.getParentId());
		parentNode.load();
		
		if("-1".equals(parentNode.getId()) && parentNode.getChildNode().size() == 1)
			return null;
		
		if(getAuthor()==null || getAuthor().getUserId().equals(session.getUser().getUserId()) || session.getEmployee().getIsAdmin()) {
		
			WfNode node = parentNode.getNode(this.getId());		
			parentNode.removeChildNode(node.getNo());
			
			node.deleteMe();
			
		} else {
			throw new Exception("$AuthorOnly");
		}
		
		return new Object[]{new Remover(this)};
	}
	
	@Autowired 
	transient public ProcessDefinition processDefinition;
	
	@Autowired
	transient public InstanceViewContent  instanceViewContent;
	
	public ContentWindow linkInstance() throws Exception {
		Instance instance = new Instance();
		instance.setInstId(new Long(getLinkedInstId()));
		if("sns".equals(session.getEmployee().getPreferUX()) ){
			return null;
		}
		instanceViewContent.load(instance);
		
		return instanceViewContent;
	}
	
	public Object newProcessInstance() throws Exception{
		NewInstancePanel newInstancePanel =  new NewInstancePanel();
		newInstancePanel.setKnowledgeNodeId(this.getId());
		newInstancePanel.session = session;
		newInstancePanel.load(session);
		newInstancePanel.getNewInstantiator().setTitle(getName());
		
		return new NewInstanceWindow(newInstancePanel);
	}
	
//	public ContentWindow mashup() throws Exception{
//		return new ContentWindow(new Mashup());
//	}
	
	public ContentWindow newDocument() throws Exception{
		NewInstancePanel newInstancePanel =  new NewInstancePanel();
		newInstancePanel.setKnowledgeNodeId(this.getId());
		newInstancePanel.session = session;
		newInstancePanel.load(session);
		
		newInstancePanel.getNewInstantiator().session = session;
		newInstancePanel.setNewInstantiator((WorkItem) newInstancePanel.getNewInstantiator().newFile());
		newInstancePanel.getNewInstantiator().setTitle(getName());
		
		return new NewInstanceWindow(newInstancePanel);		
	}

	@Override
	public ContentWindow mashup() throws Exception {
		return null;
	}

	public void expand() throws Exception {
		/*
		if(this.getLoadDepth() == LOAD_DEPTH){
			this.setLoadDepth(this.getLoadDepth()-1);
			this.loadChildren();
		}
		*/
		
		this.setLoadDepth(3);
		this.setChildNode(this.loadChildren());
		
		setClose(false);
	}
	
	@AutowiredFromClient
	transient public Session session;
	
	public void collapse() throws Exception {
		setClose(true);
	}
	
	public ModalWindow presentation() throws Exception{
		WfPanel_pt panel = new WfPanel_pt();
		panel.session = session;
		panel.setFirst(false);
		panel.setLoadDepth(0);
		panel.load(/*session.getCompany().getComCode()*/ getParentId(), "presentation");
		
		return new ModalWindow(panel , 1000, 600,  "학습창" );
	}
	
	public ModalWindow showLms() throws Exception{
		MshupLMSPopup panel = new MshupLMSPopup();
		panel.setUrl(getUrl());
		return new ModalWindow(panel , 900, 550,  "학습 콘텐츠" );
	}
	
	@Override
	public WfPanel drillInto() throws Exception {
		WfPanel panel = new WfPanel();
		panel.session = session;
		panel.load(getId());
		
		return panel;
	}
	
	@Override
	public Object[] makeAsTemplate() throws Exception {
		// TODO Auto-generated method stub
		return new Object[]{new ModalWindow()};
	}
	
	@Override
	public Object[] drop() throws Exception {
		Object clipboard = session.getClipboard();
		if(clipboard instanceof IInstance){
			System.out.println("drop : clipboard instanceof IInstance");
			IInstance instanceInClipboard = (IInstance) clipboard;
			
			Instance locatorForInstanceInClipboard = new Instance();
			locatorForInstanceInClipboard.setInstId(instanceInClipboard.getInstId());
			
			instanceInClipboard = locatorForInstanceInClipboard.databaseMe();

			WfNode child = new WfNode();		
			child.setName(instanceInClipboard.getName());
			child.setLinkedInstId(instanceInClipboard.getInstId());
			
			this.addChildNode(child);
			child.createMe();
			
			this.save();
			
			return new Object[]{new Refresh(this)};
		}else if(clipboard instanceof IUser){
			System.out.println("drop : clipboard instanceof IUser");
			IUser user = (IUser) clipboard;
			
			//UnstructuredProcessInstanceStarter instanceStarter = new UnstructuredProcessInstanceStarter();
			//instanceStarter.processManager = processManager;
			instanceStarter.session = session;
			instanceStarter.setTitle("[TODO] " + getName());
			
			instanceStarter.setFriend(user);
			Object[] instanceViewAndInstanceList = instanceStarter.start();
			
			InstanceView instanceView = ((InstanceViewContent)instanceViewAndInstanceList[0]).instanceView;
			Long instId = new Long(instanceView.getInstanceId());
			
			setLinkedInstId(instId);

			
			Instance instanceRef = new Instance();
			instanceRef.setInstId(instId);
			instanceRef.databaseMe().setStatus(Instance.INSTNACE_STATUS_RUNNING);

			save();
			
			Employee theUser = new Employee();
			theUser.setEmpCode(user.getUserId());
			
			return new Object[]{new Refresh(this), new ModalWindow(instanceView.schedule().getPanel(), 400, 300, theUser.databaseMe().getEmpName() + "님에게 과제를 부여합니다.")};
		}else if(clipboard instanceof InstanceDrag){
			System.out.println("drop : clipboard instanceof InstanceDrag");
			InstanceDrag instanceInClipboard = (InstanceDrag) clipboard;
			
			Instance locatorForInstanceInClipboard = new Instance();
			locatorForInstanceInClipboard.setInstId(instanceInClipboard.getInstanceId());
			
			IInstance instance = locatorForInstanceInClipboard.databaseMe();

			WfNode child = new WfNode();		
			child.setName(instance.getName());
			child.setLinkedInstId(instance.getInstId());
			
			this.addChildNode(child);
			child.createMe();
			
			this.save();
			
			return new Object[]{new Refresh(this)};
		}
				
		return null;
	}
	
	
	
	@Override
	public Session drag() {
		session.setClipboard(this);
		return session;
	}
	
	@Autowired
	transient public UnstructuredProcessInstanceStarter instanceStarter;
	
	
	@Override
	public Popup visualizationType() {
		return new Popup(new VisualizationType(this));
		
	}
	@Override
	public Popup connectionType() {
		// TODO Auto-generated method stub
		return new Popup(new ConnectionType(this));
	}
	@Override
	public Popup xml() throws Exception {
		
		WfNodeXML wfNodeXML = new WfNodeXML();
		
		SourceCode sourceCode = new SourceCode();
		this.load();
		sourceCode.setCode(GlobalContext.serialize(this, Object.class));
		wfNodeXML.setXml(sourceCode);
		wfNodeXML.setNodeId(getId());
		
		
		Popup popup = new Popup(wfNodeXML);
		popup.setName("To/From XML");
		
		return popup;
	}
	@Override
	public Popup json() throws Exception {
		
		WfNodeXML wfNodeXML = new WfNodeXML();
		
		SourceCode sourceCode = new SourceCode();
		this.load();
		
		XStream xstream = new XStream(new JsonHierarchicalStreamDriver());
		xstream.setMode(XStream.NO_REFERENCES);
        xstream.alias("지식", this.getClass());

//        System.out.println(xstream.toXML(this));
        
		sourceCode.setCode(xstream.toXML(this));
		wfNodeXML.setXml(sourceCode);
		wfNodeXML.setNodeId(getId());
		
		
		Popup popup = new Popup(wfNodeXML);
		popup.setName("To/From JSON");
		
		return popup;
	}
	
	public void topicList(String type) throws Exception{
		
		DAOUtil daoUtil = new DAOUtil();
		
		StringBuffer sb = new StringBuffer();
		sb.append("select * from bpm_knol knol");
		sb.append(" where knol.type = ?type");	
		sb.append(" order by " + daoUtil.replaceReservedKeyword("no"));
		
		IWfNode dao = sql(sb.toString());
		
//		ITopicNode dao = (ITopicNode)MetaworksDAO.createDAOImpl(TransactionContext.getThreadLocalInstance(), sb.toString(), ITopicNode.class); 
		dao.set("type", "topic");
		dao.select();
		
		if(dao.size() > 0){
			while (dao.next()) {
				WfNode node = this.makeNewNode();
				
				node.copyFrom(dao);
				node.setChildNode(new ArrayList<WfNode>());
				node.setMetaworksContext(new MetaworksContext());
				node.getMetaworksContext().setWhen(getMetaworksContext().getWhen());
				node.getMetaworksContext().setWhere(this.getMetaworksContext().getWhere());

				if(node.getVisType()==null){
					node.getMetaworksContext().setHow("normal");
				}else{
					node.getMetaworksContext().setHow(node.getVisType());
				}
				
				node.setLoadDepth(1);
				node.setClose(true);
				
				childNode.add(node);
			}
		}
		
	}
	
	@Override
	public Object[] topic() throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("update bpm_knol");
		sb.append("   set type = ?type");
		sb.append(" where id=?id");
		
		IWfNode updateNode = sql(sb.toString());
		updateNode.setType("topic");
		updateNode.setId(this.getId());
		
		updateNode.update();
		
		TopicMapping tm = new TopicMapping();
		tm.setTopicId(this.getId());
		tm.setUserId(session.getUser().getUserId());
		tm.setUserName(session.getUser().getName());
		tm.saveMe();
		
		return new Object[]{this};
	}
	
	public WfNode makeNewNode() {
		WfNode node = null;
		
		try{
			Class cls = this.getClass();
			//Constructor ct = cls.getConstructor();
			node = (WfNode)cls.newInstance();

			if(session != null)
				node.setAuthorId(session.getUser().getUserId());

			node.setType(this.getType());
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return node;
	}
	
	public WfNode copy(String parentId, WfNode templeteNode){
		WfNode node = templeteNode.makeNewNode();
		
		try{
			node.copyFrom(templeteNode);
			node.setName(this.getName());
			node.setNo(this.getNo());
			node.setParentId(parentId);			
			node.setChildNode(new ArrayList<WfNode>());
			
			node.createMe();
			
			for(int i=0; i<this.getChildNode().size(); i++){
				WfNode childNode = this.getChildNode().get(i);
				
				WfNode copyCode = childNode.copy(node.getId(), templeteNode);
				node.addChildNode(copyCode);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return node;
	}
	
	public WfNode link(WfNode templeteNode){
		WfNode node = templeteNode.makeNewNode();
		
		try{
			node.copyFrom(templeteNode);
			node.setRefId(this.getId());
			
			node.createMe();
			
			for(int i=0; i<this.getChildNode().size(); i++){
				WfNode childNode = this.getChildNode().get(i);
				
				WfNode linkCode = childNode.link(templeteNode);
				node.addChildNode(linkCode);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return node;
	}

	public Popup modify() {
		this.setRefId(this.getId());
		this.setId(null);
		
		this.getMetaworksContext().setHow(this.getType());
		this.getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);
		
		return new Popup(300,300,this);
	}

	public Object[] apply() throws Exception {
		this.setId(this.getRefId());
		this.setRefId(null);

		this.saveMe();
		
		this.setRefId(this.getId());
		this.setId(null);

		return new Object[]{new Remover(new Popup()), new ToOpener(this)}; 
	}
	
}