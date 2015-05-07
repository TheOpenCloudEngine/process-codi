package org.uengine.codi.mw3.model;

import java.util.ArrayList;
import java.util.Date;

import org.metaworks.MetaworksContext;
import org.metaworks.Refresh;
import org.metaworks.Remover;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;
import org.metaworks.dao.DAOUtil;
import org.metaworks.dao.Database;
import org.metaworks.dao.MetaworksDAO;
import org.metaworks.dao.TransactionContext;
import org.metaworks.dao.UniqueKeyGenerator;
import org.metaworks.website.MetaworksFile;
import org.metaworks.widget.ModalWindow;
import org.springframework.beans.factory.annotation.Autowired;
import org.uengine.processmanager.ProcessManagerRemote;

public class DocumentNode extends Database<IDocumentNode> implements IDocumentNode{
	@AutowiredFromClient
	transient public Session session;
	
	@Autowired
	public ProcessManagerRemote processManager;
	
	public final static String TYPE_DOC = "doc";
	public final static String TYPE_FILE = "file";
	public final static int DEPTH = 1;

	String id;
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
		
	String parentId;			
		public String getParentId() {
			return parentId;
		}
		public void setParentId(String parentId) {
			this.parentId = parentId;
		}		
	String secuopt;
		public String getSecuopt() {
			return secuopt;
		}
		public void setSecuopt(String secuopt) {
			this.secuopt = secuopt;
		}
	String authorId;
		public String getAuthorId() {
			return authorId;
		}
		public void setAuthorId(String authorId) {
			this.authorId = authorId;
		}
	String url;
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		
	String thumbnail;
		public String getThumbnail() {
			return thumbnail;
		}
		public void setThumbnail(String thumbnail) {
			this.thumbnail = thumbnail;
		}
		
		

	public int loadDepth = 0;
		public int getLoadDepth() {
			return loadDepth;
		}
		public void setLoadDepth(int loadDepth) {
			this.loadDepth = loadDepth;
		}	

	String type;
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
	
	String companyId;
		public String getCompanyId() {
			return companyId;
		}
		public void setCompanyId(String companyId) {
			this.companyId = companyId;
		}
		
	String description;
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}

	ArrayList<DocumentNode> childNode;
		public ArrayList<DocumentNode> getChildNode() {
			return childNode;
		}
		public void setChildNode(ArrayList<DocumentNode> childNode) {
			this.childNode = childNode;
		}

	boolean first;
		public boolean isFirst() {
			return first;
		}
		public void setFirst(boolean first) {
			this.first = first;
		}
	
	boolean close;
		public boolean isClose() {
			return close;
		}
		public void setClose(boolean close) {
			this.close = close;
		}	
		
	Date startDate;
		public Date getStartDate() {
			return startDate;
		}
		public void setStartDate(Date startDate) {
			this.startDate = startDate;
		}
	
	
	boolean documentSecuopt;		
		@Hidden
		@Face(displayName="$DocumentSecuopt")
		public boolean isDocumentSecuopt() {
			return documentSecuopt;
		}
		public void setDocumentSecuopt(boolean documentSecuopt) {
			this.documentSecuopt = documentSecuopt;
		}		
		
	public DocumentNode(){
		setChildNode(new ArrayList<DocumentNode>());
		getMetaworksContext().setWhen(MetaworksContext.WHEN_VIEW);
	}
	
	public IDocumentNode loadDocumentList() throws Exception{
		
		DAOUtil daoUtil = new DAOUtil();
		StringBuffer sb = new StringBuffer();
		sb.append("select * from bpm_knol knol");
		sb.append(" where knol.type = ?type");
		sb.append(" and knol.parentid = ?parentid");
//		sb.append(" and ( knol.secuopt=0 OR (knol.secuopt=1 and ( exists (select topicid from BPM_TOPICMAPPING tp where tp.userid=?userid and knol.id=tp.topicid)  ");
//		sb.append(" 																	 or ?userid in ( select empcode from emptable where partcode in (  ");
//		sb.append(" 																	 						select userId from BPM_TOPICMAPPING where assigntype = 2 and topicID = knol.id )))))  ");
		sb.append(" order by "  + daoUtil.replaceReservedKeyword("no"));
		
		
		IDocumentNode dao = (IDocumentNode)MetaworksDAO.createDAOImpl(TransactionContext.getThreadLocalInstance(),sb.toString(),IDocumentNode.class);
		dao.set("type", TYPE_DOC);
		dao.set("parentid", "tid_"+ session.getCompany().getComCode());
		dao.select();
		
		return dao;
	}
	
	//
	
	public ArrayList<DocumentNode> loadChildren() throws Exception{
		childNode = new ArrayList<DocumentNode>();
		
		DAOUtil daoUtil = new DAOUtil();
		StringBuffer sb = new StringBuffer();
		sb.append("select * from bpm_knol");
		sb.append(" where parentId=?parentId");
		sb.append(" and type=?type");
		sb.append(" order by " +daoUtil.replaceReservedKeyword("no"));
		
		IDocumentNode parentNode = (IDocumentNode) sql(IDocumentNode.class,sb.toString());
		
		parentNode.set("parentId", this.getId());
		parentNode.set("type",TYPE_DOC);
		parentNode.select();
		
		if(parentNode.size() > 0){
			while(parentNode.next()){
				
				DocumentNode documentNode = new DocumentNode();
				
				documentNode.copyFrom(parentNode);
				documentNode.setChildNode(new ArrayList<DocumentNode>());

				documentNode.setMetaworksContext(this.getMetaworksContext());
				documentNode.setLoadDepth(this.getLoadDepth());
				documentNode.setClose(true);
				documentNode.setFirst(this.isFirst());
				
				if(!this.isFirst())
					documentNode.load();
				
				childNode.add(documentNode); 
				
			}
		}
		return childNode;
	}
	
	public void expand() throws Exception{
		this.setLoadDepth(1);
		this.setChildNode(loadChildren());
		
		setClose(true);
	}
	
	
	
	public void collapse() throws Exception{
		setClose(false);
	}
	
	public void load() throws Exception {
		load(this.getId());
	}
	
	public void load(String nodeId) throws Exception{
		setId(nodeId);
		
		if(this.getLoadDepth() < DEPTH){
			if(this.getLoadDepth() > -1)
				setLoadDepth(getLoadDepth()+1);
			
			setChildNode(loadChildren());
			setClose(false);
		}
			setFirst(false);
	}
	
	public Object[] loadDocument() throws Exception {
		String title = "문서명: " + this.getName();
		//Object[] returnObject = Perspective.loadDocumentListPanel(session, "document", getId(), title);
		
		//return returnObject;
		return null;
	}
	
	@Override
	public Object[] loadExplorerDocument() throws Exception{
		
		String title = "문서명: " + this.getName();
		//Object[] returnObject = Perspective.loadDocumentListPanel(session, "explorer", getId(), title);
		
		//return returnObject;
		return null;
		
		
	}
	
	
	
	
	
	@Override
	public Object[] remove() throws Exception {

		StringBuffer sb = new StringBuffer();
		sb.append("update bpm_knol");
		sb.append("  set type = null ");
		sb.append(" where id=?id");
		
		IDocumentNode documentNode = (IDocumentNode) sql(IDocumentNode.class, sb.toString());
		documentNode.set("id", this.getId());
		
		documentNode.update();
		
		
		return new Object[]{new Refresh(new InstanceListPanel()), new Remover(this)};
	}

	@Override
	public ModalWindow modify() throws Exception {
		DocumentTitle documentTitle = new DocumentTitle();
		documentTitle.setId(this.getId());
		documentTitle.setName(this.getName());
		documentTitle.setDescription(this.getDescription());
		documentTitle.setParentId(this.getParentId());
		documentTitle.getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);
		documentTitle.setLogoFile(new MetaworksFile());
		documentTitle.session = session;
		return new ModalWindow(documentTitle, 500,250, "문서제목수정");
	}
	
	public void saveMe() throws Exception {
		syncToDatabaseMe();
		flushDatabaseMe();
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
	
	@Override
	public Object[] drop() throws Exception {
		Object clipboard = session.getClipboard();
		if(clipboard instanceof DocumentDrag){
				
			DocumentDrag documentInClipboard = (DocumentDrag) clipboard;
			
			int size = (Integer) searchInstance(documentInClipboard.getTaskId());
			if(size == 1){
				WorkItem workitem = new WorkItem();
				workitem.processManager = processManager;
				workitem.setType("file");
				workitem.session = session;
				
				IInstance instanceRef = workitem.save();
				workitem.databaseMe().setTitle(documentInClipboard.getExtFile());
				workitem.databaseMe().setFolderId(this.getId());
				workitem.databaseMe().setFolderName(this.getName());
				workitem.databaseMe().setTool(documentInClipboard.getTool());
				workitem.databaseMe().setContent(documentInClipboard.getContent());
				workitem.databaseMe().setExtFile(documentInClipboard.getExtFile());
				workitem.databaseMe().setGrpTaskId(documentInClipboard.getTaskId());
				Instance inst = new Instance();
				inst.copyFrom(instanceRef);
				inst.databaseMe().setTopicId(this.getId());
				inst.databaseMe().setName(documentInClipboard.getExtFile());
				
				
				workitem.setTaskId(new Long(documentInClipboard.getTaskId()));
				workitem.databaseMe().setFolderId(this.getId());
				workitem.databaseMe().setFolderName(this.getName());
				workitem.flushDatabaseMe();
				inst.flushDatabaseMe();
			}else{
				//수정
				WorkItem workitem = new WorkItem();
				workitem.setTaskId(new Long(documentInClipboard.getTaskId()));
				workitem.databaseMe().setFolderId(this.getId());
				workitem.databaseMe().setFolderName(this.getName());
				
				Instance inst = new Instance();
				inst.setInstId(new Long(documentInClipboard.getInstId()));
				inst.databaseMe().setTopicId(this.getId());
//				this.syncToDatabaseMe();
				workitem.flushDatabaseMe();
			}
			
			
			InstanceViewThreadPanel instView = new InstanceViewThreadPanel();
			instView.session = session;
			instView.load(documentInClipboard.getInstId()+"");
			
			InstanceListPanel instListPanel = new InstanceListPanel();
			instListPanel.session = session;
			//instListPanel.load();
			return new Object[]{new Refresh(instView), new Refresh(instListPanel)};
		}
		
		return null;
	}
	
	public Object searchInstance(Long taskId) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("Select * from bpm_worklist");
		sb.append(" where taskId=?taskId");
		sb.append(" and foldername is null");
		
		IWorkItem dao = (IWorkItem) sql(IWorkItem.class, sb.toString());
		
		dao.set("taskId", taskId);
		dao.select();
		int size = dao.size();
		return size;
		
	}
	@Override
	public ModalWindow addSubFolder() throws Exception {
		DocumentTitle documentSubTitle = new DocumentTitle();
		documentSubTitle.setId(this.getId());
		documentSubTitle.getMetaworksContext().setHow("sub");
		documentSubTitle.getMetaworksContext().setWhen(MetaworksContext.WHEN_NEW);
		documentSubTitle.setLogoFile(new MetaworksFile());
		documentSubTitle.session = session;
		
		return new ModalWindow(documentSubTitle , 500, 200,  "$addSubDocument");
	}
	
	

	
}
