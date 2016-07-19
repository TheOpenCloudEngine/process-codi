package org.uengine.codi.mw3.model;

import java.util.*;

import javax.sql.RowSet;

import org.directwebremoting.Browser;
import org.directwebremoting.ScriptSessions;
import org.metaworks.EventContext;
import org.metaworks.MetaworksContext;
import org.metaworks.MetaworksException;
import org.metaworks.Refresh;
import org.metaworks.Remover;
import org.metaworks.ServiceMethodContext;
import org.metaworks.ToEvent;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Id;
import org.metaworks.dao.Database;
import org.metaworks.dao.TransactionContext;
import org.metaworks.dwr.MetaworksRemoteService;
import org.metaworks.widget.ModalWindow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.uengine.codi.mw3.ErrorPage;
import org.uengine.codi.mw3.Login;
import org.uengine.codi.mw3.calendar.ScheduleCalendar;
import org.uengine.codi.mw3.calendar.ScheduleCalendarEvent;
import org.uengine.codi.mw3.common.MainPanel;
import org.uengine.codi.mw3.filter.AllSessionFilter;
import org.uengine.codi.mw3.knowledge.TopicNode;
import org.uengine.codi.mw3.widget.IFrame;
import org.uengine.kernel.Role;
import org.uengine.processmanager.ProcessManagerRemote;
import org.uengine.solr.SolrServerManager;

public class Instance extends Database<IInstance> implements IInstance{

	public final static String INSTNACE_STATUS_STOPPED    = "Stopped";
	public final static String INSTNACE_STATUS_FAILED     = "Failed";
	public final static String INSTNACE_STATUS_COMPLETED   = "Completed";
	public final static String INSTNACE_STATUS_RUNNING        = "Running";
	public final static String INSTNACE_STATUS_CANCELED       = "Canceled";

	public static final String TASK_DIRECT_APPEND_SQL_KEY = "task.";
	public static final String INSTANCE_DIRECT_APPEND_SQL_KEY = "inst.";

	public static final String DEFAULT_DEFVERID = "Unstructured.process";



	@Autowired
	public InstanceViewContent instanceViewContent;

	@AutowiredFromClient
	public Session session;

	@AutowiredFromClient
	public Locale localeManager;

	public Instance(){

	}

	static public IInstance load(List<String> ids) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * ");
		sql.append("  FROM bpm_procinst ");
		sql.append(" WHERE instid in ( ");

		for(int i = 0; i < ids.size(); i++) {
			if(i == ids.size() - 1) {
				sql.append(ids.get(i));

			} else {
				sql.append(ids.get(i) + ", ");
			}
		}

		sql.append(" )");

		IInstance instanceContents = null;
		try {
			instanceContents = (IInstance) sql(Instance.class, sql.toString());
			instanceContents.select();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return instanceContents;
	}

	// solr load
	static public ArrayList<IInstance> loadWithSolr(Navigation navigation, int page, int count) throws Exception {
		// 다른 회사 사람 확인
		if(navigation.getPerspectiveValue() != null &&
				Perspective.MODE_PERSONAL.equals(navigation.getPerspectiveMode())){

			Employee employee = new Employee();
			employee.setEmpCode(navigation.getEmployee().getEmpCode());
			employee.copyFrom(employee.databaseMe());

			if(!employee.getGlobalCom().equals(navigation.getEmployee().getGlobalCom()))
				navigation.setDiffrentCompany(true);
		}

		String searchKeyword = navigation.getKeyword();
		SolrServerManager solrServerManager = new SolrServerManager();
		ArrayList<IInstance> instanceContents = solrServerManager.search(searchKeyword, page, count);

		return instanceContents;
	}

	static public IInstance load(Navigation navigation, int page, int count)
			throws Exception {

		// 다른 회사 사람 확인
		Employee employee = null;

		if(navigation.getPerspectiveValue() != null &&
				Perspective.MODE_PERSONAL.equals(navigation.getPerspectiveMode())){

			employee = new Employee();
			employee.setEmpCode(navigation.getEmployee().getEmpCode());
			employee.copyFrom(employee.databaseMe());

			if(!employee.getGlobalCom().equals(navigation.getEmployee().getGlobalCom()))
				navigation.setDiffrentCompany(true);
		}

		Map<String, String> criteria = new HashMap<String, String>();

		StringBuffer worklistSql = new StringBuffer();
		StringBuffer instanceSql = new StringBuffer();

		// TODO makes all criteria

		createSqlPhase1(navigation,
				criteria, worklistSql, instanceSql);

		StringBuffer stmt = new StringBuffer();

		String searchKeyword = navigation.getKeyword();
		if(searchKeyword != null && !searchKeyword.isEmpty() && !Perspective.TYPE_COMMINGTODO.equals(navigation.getPerspectiveType())) {
			StringBuffer appendedInstanceSql = new StringBuffer(instanceSql);

			if(    "inbox".equals(navigation.getPerspectiveType())) {
				appendedInstanceSql.append("   AND (wl.title like ?keyword or inst.name like ?keyword)");
			}else{
				appendedInstanceSql.append(" AND (exists (select 1 from bpm_worklist wl where inst.INSTID = wl.instid and title like ?keyword) or inst.name like ?keyword)  ");
			}

			criteria.put("keyword", "%" + searchKeyword + "%");

			createSQLPhase2(navigation, criteria, stmt, worklistSql, appendedInstanceSql);
		}else{
			createSQLPhase2(navigation, criteria, stmt, worklistSql, instanceSql);
		}

		// TODO add direct append to sql
//    criteria.put(Instance.TASK_DIRECT_APPEND_SQL_KEY, taskSql.toString());
//    criteria.put(Instance.INSTANCE_DIRECT_APPEND_SQL_KEY,
//          instanceSql.toString());




		StringBuffer bottomList = new StringBuffer();
		bottomList.append("select bottomlist.* from (");
		bottomList
				.append(stmt)
				.append(") bottomlist");

//    if(oracle)
//       stmt.append("where rindex between ?startIndex and ?lastIndex ");
      

/*    if ("ORACLE".equals(typeOfDBMS))
         bottomList.append( " limit " + criteria.get("startIndex") + ", "+InstanceList.PAGE_CNT);
      else if ("MYSQL".equals(typeOfDBMS))*/
		//if( count != 0 && page != 0 )

		if(!Perspective.TYPE_CALENDAR.equals(navigation.getPerspectiveType())){
			if( page < 0 ){
				page = 0;
			}
			// paging
			String tempStr = "";
			tempStr = "" + (page * count);
			criteria.put("startIndex", tempStr);

			tempStr = "" + (page + 1) * count;
			criteria.put("lastIndex", tempStr);

			bottomList.append( " limit " + criteria.get("startIndex") + ", "+ count);
		}
		IInstance instanceContents = (IInstance) sql(Instance.class, bottomList.toString());

		if(!Perspective.MODE_PERSONAL.equals(navigation.getPerspectiveMode()))
			employee = navigation.getEmployee();

		if(employee == null)
			throw new Exception("can't load employee. perspective mode personal!");

		criteria.put("self_endpoint", employee.getEmpCode());
		criteria.put("self_partcode", employee.getPartCode());
		criteria.put("initComCd", employee.getGlobalCom());
		criteria.put("endpoint", employee.getEmpCode());
		criteria.put("partcode", employee.getPartCode());

		if(navigation.isDiffrentCompany){
			criteria.put("self_endpoint", navigation.getEmployee().getEmpCode());
			criteria.put("self_partcode", navigation.getEmployee().getPartCode());
		}

		// TODO add criteria
		Set<String> keys = criteria.keySet();
		for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			if(key.equals(INSTANCE_DIRECT_APPEND_SQL_KEY) || key.equals(TASK_DIRECT_APPEND_SQL_KEY)) {
				continue;
			} else {
				//System.out.println(key + " : " + criteria.get(key) );
				instanceContents.set(key, criteria.get(key));
			}
		}

		instanceContents.select();

//    instanceContents.getCurrentUser().getMetaworksContext().setHow("small");

		return instanceContents;
	}


	public Session cut(){
		session.setClipboard(this);
		return session;
	}

	public Object[] paste() throws Exception{
		Object clipboard = session.getClipboard();
		if(clipboard instanceof IInstance){
			IInstance instanceInClipboard = (IInstance) clipboard;

			if(instanceInClipboard.getInstId().equals(getInstId())){
				return null; //ignores drag n drop same object
			}

			Instance locatorForInstanceInClipboard = new Instance();
			locatorForInstanceInClipboard.setInstId(instanceInClipboard.getInstId());

			instanceInClipboard = locatorForInstanceInClipboard.databaseMe();

			instanceInClipboard.setRootInstId(this.getInstId());
			instanceInClipboard.setMainInstId(this.getInstId());

			IWorkItem workItemsToUpdate = (IWorkItem) sql(IWorkItem.class, "update bpm_worklist set rootInstId=?rootInstId where rootInstId = ?oldRootInstId");
			workItemsToUpdate.setRootInstId(this.getInstId());
			workItemsToUpdate.set("oldRootInstId", instanceInClipboard.getInstId());
			workItemsToUpdate.update();

			IRoleMapping roleMappingsToUpdate = (IRoleMapping) sql(IRoleMapping.class, "update bpm_rolemapping set rootInstId=?rootInstId where rootInstId = ?oldRootInstId");
			roleMappingsToUpdate.setRootInstId(this.getInstId());
			roleMappingsToUpdate.set("oldRootInstId", instanceInClipboard.getInstId());
			roleMappingsToUpdate.update();

			return new Object[]{new Remover(locatorForInstanceInClipboard)};
		}else{
			return null;//new Object[]{new Refresh(this.databaseMe())};
		}
	}
	public Object[] addTrayBar() throws Exception{
		Browser.withSession(Login.getSessionIdWithUserId(session.getUser().getUserId()), new Runnable(){
			InstanceView instanceView = ((InstanceViewContent)detail()).getInstanceView();
			public void run() {
				ScriptSessions.addFunctionCall("mw3.getAutowiredObject('org.uengine.codi.mw3.model.Tray').__getFaceHelper().addTray", new Object[]{instanceView.getInstanceName(), getInstId()+""});
			}

		});
		return null;
	}

	static private void createSQLPhase2(Navigation navigation, Map<String, String> criteria, StringBuffer stmt, StringBuffer taskSql, StringBuffer instanceSql) throws Exception {
		stmt.append("select");

		//if(oracle)
		//   stmt.append(" ROWNUM rindex,");
      
      
/*    if("inbox".equals(navigation.getPerspectiveType())) {
//       2012-10-25 내가할일 및 참여중 쿼리 변경
         stmt.append(" (select max(worklist.startdate) startdate, worklist.rootinstid ");
         stmt.append("from bpm_worklist worklist INNER JOIN bpm_rolemapping rolemapping ");
         stmt.append("ON (worklist.rolename = rolemapping.rolename OR worklist.refrolename=rolemapping.rolename) ");
         stmt.append("OR worklist.ENDPOINT=?endpoint ");
         stmt.append("where worklist.rootinstid=rolemapping.rootinstid and (worklist.status != '"+ DefaultWorkList.WORKITEM_STATUS_RESERVED +"') ");
      }else{*/
/*       stmt.append(" (select max(worklist.startdate) startdate, worklist.rootinstid ");
         stmt.append("from bpm_worklist worklist, bpm_rolemapping rolemapping ");
         stmt.append("where worklist.rootinstid=rolemapping.rootinstid and (worklist.status != '"+ DefaultWorkList.WORKITEM_STATUS_RESERVED +"') ");
      if(taskSql!=null) {
         stmt.append(taskSql);
      }        
*/
		//}



		stmt.append(" instanceList.* FROM ")
				//.append(" (SELECT distinct inst.*, task.startdate, knol.name as topicName ")
				.append(" (SELECT distinct inst.instId, inst.name, inst.duedate, inst.startedDate, inst.secuopt, inst.defid, inst.defverid, inst.status, inst.benefit, inst.effort, inst.penalty, inst.topicId, inst.isfileadded, inst.initep, instep.empname initrsnm, inst.currep, currep.empname currrsnm, inst.lastCmnt, inst.lastcmntep, lastcmntep.empname lastcmntrsnm, inst.lastCmnt2, inst.lastcmnt2ep, lastcmnt2ep.empname lastcmnt2rsnm, task.startdate, knol.name as topicName ")
				.append("  FROM bpm_procinst inst LEFT JOIN bpm_knol knol ON inst.topicId = knol.id ")
				.append("  LEFT JOIN emptable instep ON inst.initep = instep.empcode ")
				.append("  LEFT JOIN emptable currep ON inst.currep = currep.empcode ")
				.append("  LEFT JOIN emptable lastcmntep ON inst.lastcmntep = lastcmntep.empcode ")
				.append("  LEFT JOIN emptable lastcmnt2ep ON inst.lastcmnt2ep = lastcmnt2ep.empcode ");

		stmt
				.append("      ,(SELECT max(startdate) startdate, worklist.rootinstid")
				.append("         FROM bpm_worklist worklist");

		if(Perspective.MODE_ROLE.equals(navigation.getPerspectiveMode())) {
			stmt.append("        ,    bpm_rolemapping      rolemapping ");
		}
		if(Perspective.MODE_DEPT.equals(navigation.getPerspectiveMode())) {
			stmt.append("        ,    bpm_rolemapping      rolemapping ");
		}

		stmt.append("           WHERE worklist.status != 'RESERVED'");

		if(Perspective.MODE_ROLE.equals(navigation.getPerspectiveMode())) {
			stmt.append("and rolemapping.rootinstid = worklist.instid ");
			stmt.append("and rolemapping.endpoint in (select empcode from roleusertable where rolecode=?select_roleCode) ");

			criteria.put("select_roleCode", navigation.getPerspectiveValue());
		}
		if(Perspective.MODE_DEPT.equals(navigation.getPerspectiveMode())) {
			stmt.append("and rolemapping.rootinstid = worklist.instid ");
			stmt.append("and rolemapping.endpoint in (select empcode from emptable where partcode=?select_partCode) ");

			criteria.put("select_partCode", navigation.getPerspectiveValue());
		}

		stmt.append("           GROUP BY worklist.rootinstid) task ");
//    stmt
//    .append("      ,(SELECT max(startdate) startdate, worklist.rootinstid")
//    .append("         FROM bpm_worklist worklist")
//    .append("           WHERE worklist.status != 'RESERVED'")
//    .append("           GROUP BY worklist.rootinstid) task ");

		stmt.append(taskSql.toString());

		// add instance criteria
      /* 2013-05-08 cjw
      stmt.append(" WHERE initcomcd=?initComCd");
      stmt.append("   AND inst.instid=task.rootinstid ");       
      */
		stmt.append(" WHERE inst.instid=task.rootinstid ");

		if(instanceSql!=null) {
			stmt.append(instanceSql);
		}

		if(Perspective.TYPE_COMMINGTODO.equals(navigation.getPerspectiveType())){
			stmt.append("  AND inst.duedate > now()");
			stmt.append(" ORDER BY inst.duedate) instanceList ");
		}else
			stmt.append(" ORDER BY task.startdate desc) instanceList ");
	}

	static private void createSqlPhase1(Navigation navigation,
										Map<String, String> criteria, StringBuffer taskSql,
										StringBuffer instanceSql) throws Exception {

		instanceSql.append(" and inst.isdeleted!=?instIsdelete ");
		instanceSql.append(" and inst.isDocument =?isDocument ");

		criteria.put("instIsdelete", "1");
		criteria.put("isDocument", "0");

		if(Perspective.TYPE_NEWSFEED.equals(navigation.getPerspectiveType())
				|| Perspective.TYPE_FOLLOWING.equals(navigation.getPerspectiveType())
				|| Perspective.TYPE_CALENDAR.equals(navigation.getPerspectiveType())
				|| Perspective.TYPE_INBOX.equals(navigation.getPerspectiveType())
				|| Perspective.TYPE_COMMINGTODO.equals(navigation.getPerspectiveType())) {
			instanceSql
					.append(" and  exists ( ");

			if(!navigation.isDiffrentCompany){
				instanceSql
						.append("        select 1 from bpm_procinst  ")
						.append("        where inst.instid = instid  ")
						.append("        and secuopt = 0    and topicId is null and inst.initcomcd = ?initComCd ")
						.append("        union all   ");
			}

			instanceSql
					.append("        select 1 from bpm_rolemapping rm    ")
					.append("        where inst.instid = rm.rootinstid   ")
					.append("        and inst.secuopt <= 1   ")
					.append("        and (  ( assigntype = 0 and rm.endpoint = ?self_endpoint )     ")
					.append("              or ( assigntype = 2 and rm.endpoint = ?self_partcode ) ) ")

					.append("        union all   ")
					.append("        select 1 from bpm_knol topic ")
					.append("        where topic.secuopt = 0 ")
					.append("        and topic.type = 'topic'    ")
					.append("        and inst.secuopt = 0    ")
					.append("        and inst.topicId = topic.id     ")
					.append("        and topic.companyId = ?initComCd  ")


					.append("        union all   ")
					.append("        select 1 from bpm_topicmapping tm, bpm_knol topic ")
					.append("        where inst.topicId = tm.topicId     ")
					.append("        and topic.type = 'topic'    ")
					.append("        and inst.secuopt = 0")
					.append("        and inst.topicId = tm.topicId   ")
					.append("        and (  ( assigntype = 0 and tm.userid = ?self_endpoint )   ")
					.append("              or ( assigntype = 2 and tm.userid = ?self_partcode ) ) ")
					.append("     )   ");

			if(Perspective.TYPE_INBOX.equals(navigation.getPerspectiveType())
					|| Perspective.TYPE_COMMINGTODO.equals(navigation.getPerspectiveType())) {
				instanceSql
						.append("   AND wl.instid=inst.instid")
						.append("   AND inst.status<>'" + Instance.INSTNACE_STATUS_STOPPED + "'")
						.append("   AND inst.status<>'" + Instance.INSTNACE_STATUS_FAILED + "'")
						.append("   AND inst.status<>'" + Instance.INSTNACE_STATUS_COMPLETED + "'")
						.append("   AND inst.status<>'" + Instance.INSTNACE_STATUS_CANCELED + "'")
						.append("   AND ((inst.defVerId != '"+Instance.DEFAULT_DEFVERID+"' and wl.status in ('" + WorkItem.WORKITEM_STATUS_NEW + "','" + WorkItem.WORKITEM_STATUS_DRAFT + "','" + WorkItem.WORKITEM_STATUS_CONFIRMED + "'))")
						.append("     OR   (inst.defVerId = '"+Instance.DEFAULT_DEFVERID+"' and inst.DUEDATE is not null and wl.status = '" + WorkItem.WORKITEM_STATUS_FEED + "'))");

			}
		}else if(Perspective.TYPE_STARTEDBYME.equals(navigation.getPerspectiveType())){
			instanceSql.append(" and inst.initep=?instInitep ");
			criteria.put("instInitep", navigation.getPerspectiveValue());

			if( !navigation.getEmployee().getEmpCode().equals(navigation.getPerspectiveValue())){
				// 아래경우는 다른사람의 담벼락을 보는 경우이다.
				instanceSql.append(" and   exists ( ")
						.append("        select 1 from bpm_rolemapping rm    ")
						.append("        where inst.instid = rm.rootinstid   ")
						.append("        and inst.secuopt <= 1   ")
						.append("        and (  ( assigntype = 0 and rm.endpoint = ?self_endpoint )     ")
						.append("              or ( assigntype = 2 and rm.endpoint = ?self_partcode ) ) ")
						.append("     )   ");
			}
		}else{
			throw new Exception("wrong perspective");
		}

		if(navigation.isDiffrentCompany
				|| Perspective.TYPE_FOLLOWING.equals(navigation.getPerspectiveType())
				|| Perspective.TYPE_INBOX.equals(navigation.getPerspectiveType())
				|| Perspective.TYPE_COMMINGTODO.equals(navigation.getPerspectiveType())
				|| Perspective.TYPE_CALENDAR.equals(navigation.getPerspectiveType())) {
			instanceSql.append(" and wl.instid=inst.instid");

			taskSql
					.append("      , bpm_worklist wl")
					.append("           INNER JOIN bpm_rolemapping rm")
					.append("                   ON WL.INSTID=rm.INSTID")
					.append("                  AND rm.endpoint=?endpoint");
		}

		if(Perspective.TYPE_CALENDAR.equals(navigation.getPerspectiveType())){
			instanceSql.append(" and inst.duedate is not null ");
		}

		if(Perspective.MODE_TOPIC.equals(navigation.getPerspectiveMode())){
			instanceSql.append(" and inst.topicId = ?topicId ");
			criteria.put("topicId", navigation.getPerspectiveValue());
		}else if(Perspective.MODE_PROCESS.equals(navigation.getPerspectiveMode())){
			instanceSql.append(" and inst.defverid=?instDefVerId ");
			criteria.put("instDefVerId", navigation.getPerspectiveValue());
		}else if(Perspective.MODE_PROJECT.equals(navigation.getPerspectiveMode())){
			instanceSql.append(" and inst.topicId = ?topicId ");
			criteria.put("topicId", navigation.getPerspectiveValue());
		}

	}

	public Object[] view() throws Exception{
		session.setLastInstanceId(this.getInstId().toString());

		InstanceViewContent instanceViewContent = this.detail();

		if(SNS.isPhone()){
			Popup popup = new Popup(instanceViewContent.instanceView);
			popup.setName(instanceViewContent.getTitle());

			return new Object[]{ popup };
		}else{
			return new Object[]{ new Refresh(instanceViewContent, true), new Refresh(session, true)};
		}
	}


	public InstanceViewContent detail() throws Exception{

		IInstance instanceRef = databaseMe();

		if(!checkAuth()){
			throw new MetaworksException("$NotPermittedToWork");
		}
		if( instanceRef.getIsDeleted() ){
			throw new MetaworksException("$alreadyDeletedPost");
		}



		if(getMetaworksContext()==null){
			setMetaworksContext(new MetaworksContext());
		}

		getMetaworksContext().setHow("");
		getMetaworksContext().setWhere("");
		TransactionContext.getThreadLocalInstance().setSharedContext("codi_session", session);

		if(instanceViewContent == null)
			instanceViewContent = new InstanceViewContent();

		instanceViewContent.setTitle(instanceRef.getName());
		instanceViewContent.session = session;
		instanceViewContent.setMetaworksContext(getMetaworksContext());
		instanceViewContent.load(instanceRef);

		return instanceViewContent;
	}

	public ModalWindow popupDetail() throws Exception{
		ModalWindow modalWindow = new ModalWindow();
		InstanceView instanceView = ((InstanceViewContent)detail()).getInstanceView();
		modalWindow.setPanel(instanceView);
		modalWindow.setTitle(instanceView.getInstanceName());
		modalWindow.setWidth(800);

		return modalWindow;
	}

	@Override
	public ListWindow detailInTwoColumn() throws Exception{
		ListWindow detailListWindow = new ListWindow();
		InstanceView instanceView = ((InstanceViewContent)detail()).getInstanceView();
		detailListWindow.setPanel(instanceView);

		return detailListWindow;
	}


	@Autowired
	public ProcessManagerRemote processManager;

	Long instId;
	@Id
	public Long getInstId() {
		return instId;
	}
	public void setInstId(Long instId) {
		this.instId = instId;
	}

	String initComCd;
	public String getInitComCd() {
		return initComCd;
	}
	public void setInitComCd(String initComCd) {
		this.initComCd = initComCd;
	}

	String lastCmnt;
	public String getLastCmnt() {
		return lastCmnt;
	}
	public void setLastCmnt(String lastCmnt) {
		this.lastCmnt = lastCmnt;
	}

	String lastCmnt2;
	public String getLastCmnt2() {
		return lastCmnt2;
	}
	public void setLastCmnt2(String lastCmnt2) {
		this.lastCmnt2 = lastCmnt2;
	}

	IUser lastCmntUser;
	public IUser getLastCmntUser() {
		return lastCmntUser;
	}
	public void setLastCmntUser(IUser lastCmntUser) {
		this.lastCmntUser = lastCmntUser;
	}

	IUser lastCmnt2User;
	public IUser getLastCmnt2User() {
		return lastCmnt2User;
	}
	public void setLastCmnt2User(IUser lastCmnt2User) {
		this.lastCmnt2User = lastCmnt2User;
	}

	String defId;
	public String getDefId() {
		return this.defId;
	}
	public void setDefId(String defId) {
		this.defId = defId;
	}

	String defVerId;
	public String getDefVerId() {
		return defVerId;
	}
	public void setDefVerId(String defVerId) {
		this.defVerId = defVerId;
	}

	String defPath;
	public String getDefPath() {
		return defPath;
	}
	public void setDefPath(String defPath) {
		this.defPath = defPath;
	}
	String defName;
	public String getDefName() {
		return defName;
	}
	public void setDefName(String defName) {
		this.defName = defName;
	}

	Date   startedDate;
	public Date getStartedDate() {
		return startedDate;
	}

	public void setStartedDate(Date startedDate) {
		this.startedDate = startedDate;
	}

	Date finishedDate;
	public Date getFinishedDate() {
		return finishedDate;
	}

	public void setFinishedDate(Date finishedDate) {
		this.finishedDate = finishedDate;
	}

	Date dueDate;
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	Date   defModDate;
	public Date getDefModDate() {
		return defModDate;
	}
	public void setDefModDate(Date defModDate) {
		this.defModDate = defModDate;
	}

	Date   modDate;
	public Date getModDate() {
		return modDate;
	}
	public void setModDate(Date modDate) {
		this.modDate = modDate;
	}

	String status;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	String info;
	public String getInfo() {
		return this.info;
	}
	public void setInfo(String info) {
		this.info = info;
	}

	String name;
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;

	}

	boolean isDeleted;
	public boolean getIsDeleted() {
		return this.isDeleted;
	}
	public void setIsDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	boolean isAdhoc;
	public boolean getIsAdhoc() {
		return this.isAdhoc;
	}
	public void setIsAdhoc(boolean isAdhoc) {
		this.isAdhoc = isAdhoc;
	}

	boolean isDocument;
	public boolean getIsDocument() {
		return isDocument;
	}
	public void setIsDocument(boolean isDocument) {
		this.isDocument = isDocument;
	}

	boolean isFileAdded;
	public boolean getIsFileAdded() {
		return isFileAdded;
	}
	public void setIsFileAdded(boolean isFileAdded) {
		this.isFileAdded = isFileAdded;
	}

	boolean isSubProcess;
	public boolean getIsSubProcess() {
		return this.isSubProcess;
	}
	public void setIsSubProcess(boolean isSubProcess) {
		this.isSubProcess = isSubProcess;
	}

	Long rootInstId;
	public Long getRootInstId() {
		return this.rootInstId;
	}
	public void setRootInstId(Long rootInstId) {
		this.rootInstId = rootInstId;
	}

	Long mainInstId;
	public Long getMainInstId() {
		return this.mainInstId;
	}
	public void setMainInstId(Long mainInstId) {
		this.mainInstId = mainInstId;
	}

	String mainActTrcTag;
	public String getMainActTrcTag() {
		return this.mainActTrcTag;
	}
	public void setMainActTrcTag(String mainActTrcTag) {
		this.mainActTrcTag = mainActTrcTag;
	}

	String mainExecScope;
	public String getMainExecScope() {
		return this.mainExecScope;
	}
	public void setMainExecScope(String mainExecScope) {
		this.mainExecScope = mainExecScope;

	}

	Long mainDefVerId;
	public Long getMainDefVerId() {
		return this.mainDefVerId;
	}
	public void setMainDefVerId(Long mainDefVerId) {
		this.mainDefVerId = mainDefVerId;
	}

	boolean isArchive;
	public boolean getIsArchive() {
		return this.isArchive;
	}
	public void setIsArchive(boolean isArchive) {
		this.isArchive = isArchive;
	}

	String absTrcPath;
	public String getAbsTrcPath() {
		return this.absTrcPath;
	}
	public void setAbsTrcPath(String absTrcPath) {
		this.absTrcPath = absTrcPath;
	}

	boolean dontReturn;
	public boolean getDontReturn() {
		return this.dontReturn;
	}
	public void setDontReturn(boolean dontReturn) {
		this.dontReturn = dontReturn;
	}

	Number strategyId;
	public Number getStrategyId() {
		return this.strategyId;
	}
	public void setStrategyId(Number strategyId) {
		this.strategyId = strategyId;
	}

	String initEp;
	public String getInitEp() {
		return this.initEp;
	}
	public void setInitEp(String initEp) {
		this.initEp = initEp;
	}

	IUser currentUser;
	public IUser getCurrentUser() {
		return currentUser;
	}
	public void setCurrentUser(IUser currentUser) {
		this.currentUser = currentUser;
	}

	IUser initiator;
	public IUser getInitiator() {
		return initiator;
	}
	public void setInitiator(IUser initiator) {
		this.initiator = initiator;
	}

	String secuopt;
	public String getSecuopt() {
		return secuopt;
	}
	public void setSecuopt(String secuopt) {
		this.secuopt = secuopt;
	}

	String topicId;
	public String getTopicId() {
		return topicId;
	}
	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	String assignee;
	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	//only the initiator can complete this thread
	boolean initCmpl;
	public boolean isInitCmpl() {
		return initCmpl;
	}
	public void setInitCmpl(boolean initCmpl) {
		this.initCmpl = initCmpl;
	}

	String progress;
	public String getProgress() {
		return progress;
	}
	public void setProgress(String progress) {
		this.progress = progress;
	}

	InstanceViewThreadPanel instanceViewThreadPanel;
	public InstanceViewThreadPanel getInstanceViewThreadPanel() {
		return instanceViewThreadPanel;
	}
	public void setInstanceViewThreadPanel(InstanceViewThreadPanel instanceViewThreadPanel) {
		this.instanceViewThreadPanel = instanceViewThreadPanel;
	}

	InstanceTooltip instanceTooltip;
	public InstanceTooltip getInstanceTooltip() {
		return instanceTooltip;
	}
	public void setInstanceTooltip(InstanceTooltip instanceTooltip) {
		this.instanceTooltip = instanceTooltip;
	}

	public InstanceDrag instanceDrag;
	public InstanceDrag getInstanceDrag() {
		return instanceDrag;
	}
	public void setInstanceDrag(InstanceDrag instanceDrag) {
		this.instanceDrag = instanceDrag;
	}

	int benefit;
	public int getBenefit() {
		return benefit;
	}
	public void setBenefit(int benefit) {
		this.benefit = benefit;
	}

	int penalty;
	public int getPenalty() {
		return penalty;
	}
	public void setPenalty(int penalty) {
		this.penalty = penalty;
	}

	int effort;
	public int getEffort() {
		return effort;
	}
	public void setEffort(int effort) {
		this.effort = effort;
	}

	String ext1;
	public String getExt1() {
		return ext1;
	}
	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	String topicName;
	public String getTopicName() {
		return topicName;
	}
	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	/*
        * 2013-01-10 cjw
        * push client 의 보안 처리
        */
	Followers followers;
	public Followers getFollowers() {
		return followers;
	}
	public void setFollowers(Followers followers) {
		this.followers = followers;
	}

	Long lastcmntTaskId;
	public Long getLastcmntTaskId() {
		return lastcmntTaskId;
	}
	public void setLastcmntTaskId(Long lastcmntTaskId) {
		this.lastcmntTaskId = lastcmntTaskId;
	}

	Long lastcmnt2TaskId;
	public Long getLastcmnt2TaskId() {
		return lastcmnt2TaskId;
	}
	public void setLastcmnt2TaskId(Long lastcmnt2TaskId) {
		this.lastcmnt2TaskId = lastcmnt2TaskId;
	}

	InstancePreview preview;
		public InstancePreview getPreview() {
			return preview;
		}
		public void setPreview(InstancePreview preview) {
			this.preview = preview;
		}


	public void split() throws Exception {
		Long root = new Long(-1);
		databaseMe().setMainInstId(root);
		databaseMe().setRootInstId(root);

		IWorkItem workItemsToUpdate = (IWorkItem) sql(IWorkItem.class, "update bpm_worklist set rootInstId=?rootInstId where rootInstId = ?oldRootInstId");
		workItemsToUpdate.setRootInstId(root);
		workItemsToUpdate.update();

		IRoleMapping roleMappingsToUpdate = (IRoleMapping) sql(IRoleMapping.class, "update bpm_rolemapping set rootInstId=?rootInstId where rootInstId = ?oldRootInstId");
		roleMappingsToUpdate.setRootInstId(root);
		roleMappingsToUpdate.update();

	}

	public void fillFollower(){
      /*
       * 2013-01-10 cjw
       * push client 의 보안 처리
       */
		try{
			InstanceFollower follower = new InstanceFollower();
			follower.setParentId(this.getInstId().toString());

			Followers followers = new Followers(follower);
			followers.load();

			this.setFollowers(followers);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	static public int countTodo(Session session) {

		int result = 0;

		StringBuffer sb = new StringBuffer();
		sb
				.append("SELECT count(*) cnt")
				.append(" FROM bpm_procinst inst")
				.append(" WHERE inst.isdeleted = 0")
				.append("  AND inst.isDocument = 0")
				.append("  AND inst.status <> 'Stopped'")
				.append("  AND inst.status <> 'Failed'")
				.append("  AND inst.status <> 'Completed'")
				.append("  AND inst.status <> 'Canceled'")
				.append("  AND ((inst.defVerId != 'Unstructured.process')")
				.append("   OR (inst.defVerId = 'Unstructured.process'")
				.append("    AND inst.DUEDATE is not null))")
				.append("  AND exists(")
				.append("   SELECT 1")
				.append("   FROM bpm_rolemapping rm")
				.append("   WHERE inst.instid = rm.rootinstid")
				.append("    AND ((assigntype = 0")
				.append("     AND rm.endpoint =?endpoint)")
				.append("      OR (assigntype = 2")
				.append("       AND rm.endpoint =?partcode)))");

		try{
			IInstance instance = (IInstance) sql(Instance.class, sb.toString());
			instance.set("endpoint", session.getEmployee().getEmpCode());
			instance.set("partcode", session.getEmployee().getPartCode());
			instance.select();

			if(instance.next())
				result = instance.getInt("cnt");

		}catch(Exception e){
			e.printStackTrace();
		}

		return result;
	}

	static public int countTodo(String empCode, String partCode) {

		int result = 0;

		StringBuffer sb = new StringBuffer();
		sb
				.append("SELECT count(*) cnt")
				.append(" FROM bpm_procinst inst")
				.append(" WHERE inst.isdeleted = 0")
				.append("  AND inst.isDocument = 0")
				.append("  AND inst.status <> 'Stopped'")
				.append("  AND inst.status <> 'Failed'")
				.append("  AND inst.status <> 'Completed'")
				.append("  AND inst.status <> 'Canceled'")
				.append("  AND ((inst.defVerId != 'Unstructured.process')")
				.append("   OR (inst.defVerId = 'Unstructured.process'")
				.append("    AND inst.DUEDATE is not null))")
				.append("  AND exists(")
				.append("   SELECT 1")
				.append("   FROM bpm_rolemapping rm")
				.append("   WHERE inst.instid = rm.rootinstid")
				.append("    AND ((assigntype = 0")
				.append("     AND rm.endpoint =?endpoint)")
				.append("      OR (assigntype = 2")
				.append("       AND rm.endpoint =?partcode)))");

		try{
			IInstance instance = (IInstance) sql(Instance.class, sb.toString());
			instance.set("endpoint", empCode);
			instance.set("partcode", partCode);
			instance.select();

			if(instance.next())
				result = instance.getInt("cnt");

		}catch(Exception e){
			e.printStackTrace();
		}

		return result;
	}

	public static IInstance loadServers(String topicId) throws Exception {

		StringBuffer sb = new StringBuffer();
		sb
				.append("SELECT *")
				.append("  FROM bpm_procinst inst")
				.append(" WHERE topicId=?topicId")
				.append("   AND isdeleted = 0");

		IInstance instance = (IInstance) sql(Instance.class, sb.toString());
		instance.setTopicId(topicId);
		instance.select();

		return instance;
	}

	public static IInstance loadProcessFormRootInstId(String rootInstId) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb
				.append("select *")
				.append("  from bpm_procinst")
				.append(" where RootInstId = ?RootInstId ")
				.append("   and defVerId != 'Unstructured.process'");

		IInstance instance = (IInstance) sql(Instance.class, sb.toString());
		instance.setRootInstId(new Long(rootInstId));
		instance.select();

		return instance;
	}



	public ModalWindow ganttChart() throws Exception{


		IFrame iframe = new IFrame("ganttChart.html?instanceId=" + this.getInstId().toString());

		return new ModalWindow(iframe, 1024, 768, "Process Gantt Chart");
	}

	public boolean checkAuth() throws Exception{
		boolean isRelated = false;
		boolean secuoptTopic = false;
		boolean secuoptInst = false;
		boolean topicFollow = false;

		IInstance instanceRef = databaseMe();
		secuoptInst = "1".equals(instanceRef.getSecuopt())?true:false;

		TopicNode topic = new TopicNode();
		topic.setId(instanceRef.getTopicId());
		try {
			topic.copyFrom(topic.databaseMe());
			secuoptTopic = "1".equals(topic.getSecuopt())?true:false;
		} catch (Exception e) {
			secuoptTopic = false;
		}

		//주제비공개
		if(secuoptTopic){
			topicFollow = checkRelatedTopic();
			//주제팔로우
			if(topicFollow){
				//인스턴스비공개
				if(secuoptInst){
					isRelated = checkRelatedUser();
					//인스턴스공개
				}else{
					isRelated = true;
				}
				//주제팔로우아님
			}else{
				isRelated = checkRelatedUser();
			}


			//주제공개
		}else{
			if(secuoptInst){
				isRelated = checkRelatedUser();
			}else{
				isRelated = true;
			}
		}
		return isRelated;
	}

	public boolean checkRelatedTopic() throws Exception{

		boolean isRelated = false;
		TopicNode topic = new TopicNode();
		String topicId = this.databaseMe().getTopicId();
		topic.setId(this.databaseMe().getTopicId());
		try {
			topic.copyFrom(topic.databaseMe());
			// 비공개 주제일 경우 본건의 관련자가 아니면 주제참여 막음.
			if( "1".equals(topic.getSecuopt())){
				TopicFollower findFollower = new TopicFollower();
				findFollower.setParentId(topicId);
				IFollower follower = findFollower.findFollowers();
				while(follower.next()){
					if(Role.ASSIGNTYPE_USER == follower.getAssigntype()){
						if(follower.getEndpoint().equals(session.getEmployee().getEmpCode())){
							isRelated = true;
							break;
						}
					}else if(Role.ASSIGNTYPE_DEPT == follower.getAssigntype()){
						if(follower.getEndpoint().equals(session.getEmployee().getPartCode())){
							isRelated = true;
							break;
						}
					}
				}

			}else{
				isRelated = true;
			}
		} catch (Exception e) {
			isRelated = true;
		}

		return isRelated;
	}

	public boolean checkRelatedUser() throws Exception{

		boolean isRelated = false;
		IInstance instanceRef = databaseMe();

		InstanceFollower findFollower = new InstanceFollower(instanceRef.getInstId().toString());
		IFollower follower = findFollower.findFollowers();
		while(follower.next()){
			if(Role.ASSIGNTYPE_USER == follower.getAssigntype()){
				if(follower.getEndpoint().equals(session.getEmployee().getEmpCode())){
					isRelated = true;
					break;
				}
			}else if(Role.ASSIGNTYPE_DEPT == follower.getAssigntype()){
				if(follower.getEndpoint().equals(session.getEmployee().getPartCode())){
					isRelated = true;
					break;
				}
			}
		}

		return isRelated;

	}

	public Popup schedule() throws Exception{

		IInstance instanceRef = databaseMe();

		if(!checkAuth()){
			throw new MetaworksException("$NotPermittedToWork");
		}
		if( instanceRef.getIsDeleted() ){
			throw new MetaworksException("$alreadyDeletedPost");
		}

		InstanceDueSetter ids = new InstanceDueSetter();
		ids.setInstId(this.getInstId());
		ids.setDueDate(instanceRef.getDueDate());
		ids.setBenefit(instanceRef.getBenefit());
		ids.setPenalty(instanceRef.getPenalty());
		ids.setEffort(instanceRef.getEffort());
		ids.setOnlyInitiatorCanComplete(instanceRef.isInitCmpl());
		ids.setProgress(instanceRef.getProgress());

		if("sns".equals(session.getEmployee().getPreferUX()) ){
			ids.getMetaworksContext().setHow("instanceList");
			ids.getMetaworksContext().setWhere("sns");
		}
		ids.getMetaworksContext().setWhen("edit");
		Popup pop = new Popup(400,340,ids);
		pop.setName("$Schedule");
		return pop;
	}

	public Popup newSubInstance() throws Exception{
		ProcessMapList processMapList = new ProcessMapList();
		processMapList.load(session);
		processMapList.setParentInstanceId(this.getInstId());

		Popup popup = new Popup();
		popup.setPanel(processMapList);

		return popup;
	}

	public Object[] remove() throws Exception{

		IInstance instanceRef = databaseMe();

		if(!checkAuth()){
			throw new MetaworksException("$NotPermittedToWork");
		}
		if( instanceRef.getIsDeleted() ){
			throw new MetaworksException("$alreadyDeletedPost");
		}

		if(!instanceRef.getInitEp().equals(session.getUser().getUserId())  && !(session.getEmployee()!=null && session.getEmployee().getIsAdmin())){
			throw new Exception("$OnlyInitiatorCanDeleteTheInstance");
		}

		processManager.stopProcessInstance(this.getInstId().toString());

		databaseMe().setIsDeleted(true);
		flushDatabaseMe();
      
      /* 내가 할일 카운트 다시 계산 */
		TodoBadge todoBadge = new TodoBadge();
		todoBadge.session = session;
		todoBadge.refresh();

		UpcommingTodoPerspective upcommingTodoPerspective = new UpcommingTodoPerspective();
      
      /* push 부분 */
		// 자기자신의 todoBadge 와 다가오는 일정을 refresh 시킨다.
		MetaworksRemoteService.pushTargetClientObjects(Login.getSessionIdWithUserId(session.getUser().getUserId()), new Object[]{new Refresh(todoBadge), new Refresh(upcommingTodoPerspective)});

		// 자기자신의 달력화면이 열려있다면 달력의 글을 제거한다.
		ScheduleCalendarEvent scEvent = new ScheduleCalendarEvent();
		scEvent.setId(instanceRef.getInstId().toString());
		MetaworksRemoteService.pushTargetScript(Login.getSessionIdWithUserId(session.getUser().getUserId()),
				"if(mw3.getAutowiredObject('org.uengine.codi.mw3.calendar.ScheduleCalendar')!=null) mw3.getAutowiredObject('org.uengine.codi.mw3.calendar.ScheduleCalendar').__getFaceHelper().removeEvent",
				new Object[]{scEvent});
      
      /* return 부분 */
		if(SNS.isPhone()){
			return new Object[]{new ToEvent(ServiceMethodContext.TARGET_SELF, EventContext.EVENT_CLOSE)};
		}else{
			if(!"sns".equals(session.getEmployee().getPreferUX())){
				NewInstancePanel instancePanel = new NewInstancePanel();
				instancePanel.load(session);
				return new Object[]{new Remover(this), new Refresh(new ContentWindow(instancePanel))};
			}else{
				return new Object[]{new Remover(this)};
			}
		}
	}

	public void toggleSecurityConversation() throws Exception{

		IInstance instanceRef = databaseMe();

		if(!checkAuth()){
			throw new MetaworksException("$NotPermittedToWork");
		}
		if( instanceRef.getIsDeleted() ){
			throw new MetaworksException("$alreadyDeletedPost");
		}

		String title=null;
		String secuopt = this.getSecuopt();

		if(secuopt.charAt(0) != '0'){
			title = localeManager.getString("$InstanceSecuopt");
		}else{
			title = localeManager.getString("$InstanceNoSecuopt");
		}

		// add comment schedule changed
		WorkItem workItem = new WorkItem();
		workItem.getMetaworksContext().setHow("changeSchedule");
		workItem.session = session;
		workItem.processManager = processManager;
		workItem.setInstId(this.getInstId());

		workItem.generateNotiWorkItem(title);


		if (secuopt.charAt(0) != '0') {
			databaseMe().setSecuopt("0");
		} else {
			databaseMe().setSecuopt("1");
		}

		Instance instance = new Instance();
		instance.copyFrom(instanceRef);
		instance.fillFollower();

		// 주제 제목 설정
		if(instance.getTopicId() != null){
			TopicNode topic = new TopicNode();
			topic.setId(instance.getTopicId());
			topic.copyFrom(topic.databaseMe());
			instance.setTopicName(topic.getName());
		}

		// 모든 사람의 인스턴스 상태를 변경함
		MetaworksRemoteService.pushClientObjects(new Object[]{new InstanceListener(InstanceListener.COMMAND_REFRESH, instance)});
		// 자기자신의 인스턴스 상태를 변경함
//    MetaworksRemoteService.pushTargetClientObjects(Login.getSessionIdWithUserId(session.getUser().getUserId()), new Object[]{new InstanceListener(InstanceListener.COMMAND_REFRESH, instance)});

		//비공개&공개 이벤트 발생자 팔로우 추가
		addFollowActUser();
	}

	public void complete() throws Exception{

		IInstance instanceRef = databaseMe();

		if(!checkAuth()){
			throw new MetaworksException("$NotPermittedToWork");
		}
		if( instanceRef.getIsDeleted() ){
			throw new MetaworksException("$alreadyDeletedPost");
		}
		if(instanceRef.isInitCmpl() && !session.getUser().getUserId().equals(instanceRef.getInitEp())){
			throw new Exception("$OnlyInitiatorCanComplete");
		}

		String tobe = null;
		String title = null;
		if(getStatus().equals("Completed")){
			tobe = "Running";
			title = localeManager.getString("$CancleCompleted");
		}else{
			tobe = "Completed";
			title = localeManager.getString("$CompletedDate");
		}

		// add comment schedule changed
		WorkItem workItem = new WorkItem();
		workItem.getMetaworksContext().setHow("changeSchedule");
		workItem.session = session;
		workItem.processManager = processManager;
		workItem.setInstId(this.getInstId());

		workItem.copyFrom(workItem.generateNotiWorkItem(title));

		// instance update flush
		instanceRef.setStatus(tobe);

		Instance instance = new Instance();
		instance.copyFrom(instanceRef);
		instance.flushDatabaseMe();
		instance.fillFollower();

		instance.getMetaworksContext().setWhere("instancelist");

		//MetaworksRemoteService.pushClientObjects(new Object[]{new InstanceListener(InstanceListener.COMMAND_REFRESH, instance)});
		MetaworksRemoteService.pushClientObjectsFiltered(
				new AllSessionFilter(Login.getSessionIdWithCompany(session.getEmployee().getGlobalCom())),
				new Object[]{new InstanceListener(InstanceListener.COMMAND_REFRESH, instance)});

		// workItem.add(); 에서 비슷한 일을 하는것처럼 보이나, 완료시점에만 동작하기 위해서 workItem에 구현하는건 비용이 높아보인다.
		if(instanceRef.getDueDate() != null){
			ScheduleCalendarEvent scEvent = new ScheduleCalendarEvent();
			scEvent.setTitle(instanceRef.getName());
			scEvent.setId(instanceRef.getInstId().toString());
			scEvent.setStart(instanceRef.getDueDate());

			Calendar c = Calendar.getInstance();
			c.setTime(instanceRef.getDueDate());

			// TODO : 현재는 무조건 종일로 설정
			scEvent.setAllDay(true);
			scEvent.setCallType(ScheduleCalendar.CALLTYPE_INSTANCE);
			scEvent.setComplete(Instance.INSTNACE_STATUS_COMPLETED.equals(instanceRef.getStatus()));

			MetaworksRemoteService.pushTargetScript(Login.getSessionIdWithUserId(session.getUser().getUserId()),
					"if(mw3.getAutowiredObject('org.uengine.codi.mw3.calendar.ScheduleCalendar')!=null) mw3.getAutowiredObject('org.uengine.codi.mw3.calendar.ScheduleCalendar').__getFaceHelper().addEvent",
					new Object[]{scEvent});
		}
      /* 내가 할일 카운트 다시 계산 */
		TodoBadge todoBadge = new TodoBadge();
		todoBadge.session = session;
		todoBadge.refresh();

		UpcommingTodoPerspective upcommingTodoPerspective = new UpcommingTodoPerspective();

		//본인에게 알림 워크아이템 발행
		MetaworksRemoteService.pushTargetClientObjects(Login.getSessionIdWithUserId(session.getUser().getUserId()),
				new Object[]{new Refresh(todoBadge), new WorkItemListener(workItem), new Refresh(upcommingTodoPerspective)});

		MetaworksRemoteService.pushTargetClientObjects(Login.getSessionIdWithUserId(session.getUser().getUserId()), new Object[]{new Refresh(todoBadge), new Refresh(upcommingTodoPerspective)});

		//inst_emp_perf 테이블에 성과정보 저장 insert
		int businessValue = instanceRef.getBenefit() + instanceRef.getPenalty();

		if(tobe.equals("Running")){
			deleteBV();
		}else if (tobe.equals("Completed")){
			insertBV(businessValue);
		}

		//완료 이벤트 발생자 팔로우 추가
		addFollowActUser();
	}
	public void addFollowActUser() throws Exception{
		InstanceFollower findFollower = new InstanceFollower(getInstId().toString());
		findFollower.session = session;
		IUser actUser = new User();
		actUser.setUserId(session.getEmployee().getEmpCode());
		actUser.setName(session.getEmployee().getEmpName());
		findFollower.setUser(actUser);
		findFollower.put();

	}

	private void insertBV(int businessValue) throws Exception{
		IRoleMapping allFollower = RoleMapping.allFollower(this.getInstId());
		RowSet rowset = allFollower.getImplementationObject().getRowSet();

		InstanceEmployeePerformance bizVal = new InstanceEmployeePerformance();

		if(allFollower.size() > 0){
			int eachBV = businessValue/ allFollower.size();


			while(rowset.next()){
				bizVal.setInstId(rowset.getLong("instId"));
				bizVal.setEmpCode(rowset.getString("endPoint"));
				bizVal.setBusinessValue(eachBV);

				bizVal.createDatabaseMe();
			}

		}

	}

	private void deleteBV() throws Exception{
		InstanceEmployeePerformance bizVal = new InstanceEmployeePerformance();

		bizVal.setInstId(this.getInstId());

		try {
			// TODO: WorkItem 에 의한 Completed 시 insertBV 가 실행되지 않아 오류 발생함. 차후 처리
			bizVal.deleteDatabaseMe();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public IInstance loadDocument(String folderId) throws Exception {
		// TODO Auto-generated method stub

		StringBuffer sql = new StringBuffer();

//select * from bpm_procinst where isdocument ='1' and topicid='324' order by instid desc;
		sql.append("select * from bpm_procinst ");
		sql.append("where isdocument =?isdocument and topicId=?topicId ");
		sql.append(" and initcomcd=?initcomcde ");
		sql.append("order by InstId desc");
		IInstance instance = (IInstance) Database.sql(IInstance.class, sql.toString());

		instance.set("isdocument", 1);
		instance.set("topicId",folderId);
		instance.set("initcomcd", session.getEmployee().getGlobalCom());
		instance.select();

		while(instance.next()){
			Instance inst = new Instance();
			inst.copyFrom(instance);
			inst.getMetaworksContext().setHow("document");
		}

		return instance;

	}
	public IInstance loadDocument() throws Exception{
		StringBuffer sql = new StringBuffer();

		sql.append("select * from bpm_procinst ");
		sql.append("where isdocument =?isdocument and topicId is null ");
		sql.append(" and initcomcd=?initcomcd ");
		sql.append("order by InstId desc");
		IInstance instance = (IInstance) Database.sql(IInstance.class, sql.toString());

		instance.set("initcomcd", session.getEmployee().getGlobalCom());
		instance.set("isdocument", 1);
		instance.select();

		while(instance.next()){
			Instance inst = new Instance();
			inst.copyFrom(instance);
			inst.getMetaworksContext().setHow("document");
		}

		return instance;
	}
	public MainPanel goSns() throws Exception {
		if(session != null){
			session.setLastPerspecteType("allICanSee");
			session.setLastSelectedItem("goSns");
			session.setUx("sns");
		}

		return new MainPanel(new Main(session, String.valueOf(this.getInstId())));
	}


	public Object[] loadTopic() throws Exception{
		if(!checkRelatedTopic()){
			throw new MetaworksException("$NotPermittedToTopic");
		}
		TopicNode topicNode = new TopicNode();
		topicNode.session = session;
		topicNode.setType(TopicNode.TOPIC);
		topicNode.setId(this.getTopicId());
		topicNode.setName(this.getTopicName());
		return topicNode.loadTopic();
	}

	@Override
	public void loadPreview() throws Exception {
		InstancePreview instancePreview = MetaworksRemoteService.getComponent(InstancePreview.class);
		instancePreview.load(this);

		setPreview(instancePreview);
	}


}