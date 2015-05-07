package org.uengine.codi.mw3.knowledge;

import java.util.Calendar;

import org.metaworks.MetaworksContext;
import org.metaworks.Refresh;
import org.metaworks.Remover;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.dao.DAOUtil;
import org.metaworks.dao.Database;
import org.metaworks.dao.MetaworksDAO;
import org.metaworks.dao.TransactionContext;
import org.metaworks.website.MetaworksFile;
import org.metaworks.widget.ModalWindow;
import org.uengine.codi.mw3.admin.PageNavigator;
import org.uengine.codi.mw3.model.Instance;
import org.uengine.codi.mw3.model.InstanceDrag;
import org.uengine.codi.mw3.model.InstanceList;
import org.uengine.codi.mw3.model.InstanceListPanel;
import org.uengine.codi.mw3.model.ListPanel;
import org.uengine.codi.mw3.model.Locale;
import org.uengine.codi.mw3.model.NewInstancePanel;
import org.uengine.codi.mw3.model.Perspective;
import org.uengine.codi.mw3.model.RecentItem;
import org.uengine.codi.mw3.model.Session;
import org.uengine.codi.mw3.model.TopicInfo;
import org.uengine.codi.mw3.view.InfoContentListPanel;
import org.uengine.kernel.GlobalContext;

public class TopicNode extends Database<ITopicNode> implements ITopicNode {
	
	public final static String DEFAULT_TOPIC_COUNT = "5";
	public final static String KNOWLEGE = "knowlege";
	public final static String TOPIC = "topic";
	public final static String HTML = "html";
	public final static int MODIFY_POPUP_HEIGHT = 250;
	public final static int MODIFY_POPUP_WIDTH = 500;
	
	@AutowiredFromClient
	public PageNavigator pageNavigator; 
	
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

	
	public static ITopicNode findTopic(Session session) throws Exception {
		return TopicNode.findTopic(session, true);
	}
	
	public static ITopicNode findTopic(Session session, boolean isMore) throws Exception {
		
		StringBuffer sb = new StringBuffer();
		sb.append("select * from bpm_knol knol");
		sb.append(" left join recentItem item on item.itemId = knol.id and item.empcode = ?endpoint and item.itemType=?type");
		sb.append(" where knol.type = ?type");
		
		sb.append("   and exists ( ");
		sb.append("			select 1 from bpm_knol	 ");
		sb.append("		 	 where knol.id = id	 ");
		sb.append("		 	   and secuopt = 0 and companyId = ?initComCd ");
		sb.append("			 union all 	 ");
		sb.append("			select 1 from bpm_topicmapping tm	 ");
		sb.append("			 where knol.id = tm.topicId	 ");
		sb.append("			   and knol.secuopt < 2 ");
		sb.append("			   and ( 	( assigntype = 0 and tm.userid = ?endpoint ) 	 ");
		sb.append("					 or ( assigntype = 2 and tm.userid = ?partcode ) ) ");
		sb.append("		  )	 ");
		
		sb.append(" order by updateDate desc");
		
		if(!isMore)
			sb.append(" limit " + GlobalContext.getPropertyString("topic.more.count", DEFAULT_TOPIC_COUNT));		
		
		ITopicNode dao = (ITopicNode)MetaworksDAO.createDAOImpl(TransactionContext.getThreadLocalInstance(), sb.toString(), ITopicNode.class); 
		dao.set("type", "topic");
		dao.set("endpoint", session.getEmployee().getEmpCode());
		dao.set("partcode", session.getEmployee().getPartCode());
		dao.set("initComCd", session.getCompany().getComCode());
		dao.select();
		
		return dao; 
	}
	
	public static int calcTopicCount(Session session) throws Exception {
		
		DAOUtil daoUtil = new DAOUtil();
		
		StringBuffer sb = new StringBuffer();
		sb.append("select count(*) count from bpm_knol knol");
		sb.append(" where knol.type = ?type");
		sb.append("   and exists ( ");
		sb.append("			select 1 from bpm_knol	 ");
		sb.append("		 	 where knol.id = id	 ");
		sb.append("		 	   and secuopt = 0 and companyId = ?initComCd ");
		sb.append("			 union all 	 ");
		sb.append("			select 1 from bpm_topicmapping tm	 ");
		sb.append("			 where knol.id = tm.topicId	 ");
		sb.append("			   and knol.secuopt < 2 ");
		sb.append("			   and ( 	( assigntype = 0 and tm.userid = ?endpoint ) 	 ");
		sb.append("					 or ( assigntype = 2 and tm.userid = ?partcode ) ) ");
		sb.append("		  )	 ");
		
		ITopicNode dao = (ITopicNode)MetaworksDAO.createDAOImpl(TransactionContext.getThreadLocalInstance(), sb.toString(), ITopicNode.class); 
		dao.set("type", "topic");
		dao.set("endpoint", session.getEmployee().getEmpCode());
		dao.set("partcode", session.getEmployee().getPartCode());
		dao.set("initComCd", session.getCompany().getComCode());
		dao.select();
		
		if(dao.next())
			return dao.getInt("count");
		else
			return 0;
	}
	
	public Object[] loadTopic() throws Exception{
		
		if(pageNavigator != null && KNOWLEGE.equals(pageNavigator.getPageName())){
			return new Object[]{new BrainstormPanel(this.getId())};
		}else{
			// recentItem 에 create
			RecentItem recentItem = new RecentItem();
			recentItem.setEmpCode(session.getEmployee().getEmpCode());
			recentItem.setItemId(this.getId());
			recentItem.setItemType(this.getType());
			recentItem.setUpdateDate(Calendar.getInstance().getTime());
			recentItem.add();

			// init search keyword
			session.setSearchKeyword(null);
			
			Locale locale = new Locale(session);
			locale.load();
			
			InstanceListPanel instanceListPanel = Perspective.loadInstanceList(session, Perspective.MODE_TOPIC, Perspective.TYPE_NEWSFEED, getId());
			
			ListPanel listPanel = new ListPanel(instanceListPanel, new TopicInfo(session, this.getId()));
			
			String title = locale.getString("$Topic") + " - " + getName();
			session.setWindowTitle(title);
			
			return new Object[]{session, listPanel};
		}
	}
	
	
	public Object[] remove() throws Exception {
		
		// 삭제는 진짜 삭제가 아닌 topic 만 제거를 하여 지식노드에서는 보이도록 설정됨
		// deleteDatabaseMe();
		StringBuffer sb = new StringBuffer();
		sb.append("update bpm_knol");
		sb.append("   set type = null ");
		sb.append(" where id=?topicId");
		
		ITopicNode updateNode = (ITopicNode) sql(ITopicNode.class,	sb.toString());
		updateNode.set("topicId", this.getId());
		
		updateNode.update();
		
		return new Object[]{new Refresh(new InstanceListPanel()), new Remover(this)};
	}
	

	public ModalWindow modify() throws Exception {
		
		TopicNode topicNode = new TopicNode();
		topicNode.setId(this.getId());
		topicNode.copyFrom(topicNode.databaseMe());
		
		TopicTitle topicTitle = new TopicTitle();
		topicTitle.setTopicId(this.getId());
		topicTitle.setTopicTitle(topicNode.getName());
		topicTitle.setTopicSecuopt("0".equals(topicNode.getSecuopt()) ? false : true );
		topicTitle.setMetaworksContext(new MetaworksContext());
		topicTitle.getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);
		topicTitle.setLogoFile(new MetaworksFile());
		topicTitle.session = session;
		return new ModalWindow(topicTitle, MODIFY_POPUP_WIDTH, MODIFY_POPUP_HEIGHT, "$EditTopic");

	}
	
	public ModalWindow exportHtml() throws Exception {
		TopicTitle topicTitle = new TopicTitle();
		topicTitle.setTopicId(this.getId());
		topicTitle.setTopicTitle(this.getName());
		topicTitle.getMetaworksContext().setHow(HTML);
		topicTitle.getMetaworksContext().setWhen(MetaworksContext.WHEN_VIEW);
		
		topicTitle.makeHtml();
				
		return new ModalWindow(topicTitle, MODIFY_POPUP_WIDTH, MODIFY_POPUP_HEIGHT, "$ExportHtml");
	}
	
	public void addUser() throws Exception {
		
	}

	@Override
	public Object[] drop() throws Exception {
		Object clipboard = session.getClipboard();
		if(clipboard instanceof InstanceDrag){
			InstanceDrag instanceInClipboard = (InstanceDrag) clipboard;
			
			Instance locatorForInstanceInClipboard = new Instance();
			locatorForInstanceInClipboard.setInstId(instanceInClipboard.getInstanceId());
			locatorForInstanceInClipboard.databaseMe().setTopicId(this.getId());			
			locatorForInstanceInClipboard.flushDatabaseMe();
			
			InstanceList instList = new InstanceList(session);
			instList.load();
			
			return new Object[]{new Refresh(instList)};
		}
		return null;
	}
	
	@AutowiredFromClient
	transient public Session session;
	
//	@AutowiredFromClient
//	public ContentWindow contentWindow;
}
