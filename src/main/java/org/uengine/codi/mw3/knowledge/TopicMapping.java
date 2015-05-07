package org.uengine.codi.mw3.knowledge;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.metaworks.dao.DAOFactory;
import org.metaworks.dao.Database;
import org.metaworks.dao.KeyGeneratorDAO;
import org.metaworks.dao.TransactionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.uengine.codi.mw3.model.Follower;
import org.uengine.codi.mw3.model.IDept;
import org.uengine.codi.mw3.model.IFollower;
import org.uengine.codi.mw3.model.IUser;
import org.uengine.kernel.GlobalContext;
import org.uengine.processmanager.ProcessManagerRemote;

public class TopicMapping extends Database<ITopicMapping> implements ITopicMapping {
	
	@Autowired
	public ProcessManagerRemote processManager;
	
	String topicId;
		public String getTopicId() {
			return topicId;
		}
		public void setTopicId(String topicId) {
			this.topicId = topicId;
		}
		
	Long topicMappingId;
		@Override
		public Long getTopicMappingId() {
			return topicMappingId;
		}
		@Override
		public void setTopicMappingId(Long topicMappingId) {
			this.topicMappingId = topicMappingId;
		}
		
	String userId;
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		
	String userName;
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
	
	int assigntype;
		public int getAssigntype() {
			return assigntype;
		}
		public void setAssigntype(int assigntype) {
			this.assigntype = assigntype;
		}
	
	public ITopicMapping saveMe() throws Exception {

		Map options = new HashMap();
		options.put("useTableNameHeader", "false");
		options.put("onlySequenceTable", "true");
		
		KeyGeneratorDAO kg = DAOFactory.getInstance(TransactionContext.getThreadLocalInstance()).createKeyGenerator("bpm_topicmapping", options);
		kg.select();
		kg.next();
		
		Number number = kg.getKeyNumber();
		
		setTopicMappingId(number.longValue());
		WfNode node = new WfNode();
		node.setId(this.getTopicId());
		node.copyFrom(node.databaseMe());
//		if(node.getType().equals("project")){
//			String host = GlobalContext.getPropertyString("vm.manager.ip","localhost");
//			String userId = GlobalContext.getPropertyString("vm.manager.user","root");
//			String passwd = GlobalContext.getPropertyString("vm.manager.password","root");
//
//			JschCommand jschServerBehaviour = new JschCommand();
//			jschServerBehaviour.sessionLogin(host, userId, passwd);
//			
//			Employee employee = new Employee();
//			employee.setEmpCode(this.getUserId());
//			employee.copyFrom(employee.findMe());			
//			
//			//SVN 유저 추가
//			String command = GlobalContext.getPropertyString("vm.svn.createUser","/home/svn/script/svnUserAdd.sh") + " \"" +  node.getName() + "\" \"" + employee.getEmpCode() + "\" \"" + employee.getPassword() + "\"";
//			jschServerBehaviour.runCommand(command);
//		}
				
		/*
		 * 
		if(node.getType().equals("project") && node.getLinkedInstId() != null){
			if(processManager != null && processManager.getProcessVariable(node.getLinkedInstId().toString(), "", "vm_ip") != null){
				String vmIp = (String)((Serializable)processManager.getProcessVariable(node.getLinkedInstId().toString(), "", "vm_ip"));
				String vmDb = (String)((Serializable)processManager.getProcessVariable(node.getLinkedInstId().toString(), "", "vm_db"));
				
				createDatabaseToTadpole(vmIp, vmDb);
				
			}
		}
		*/
		
		ITopicMapping result = createDatabaseMe();
		flushDatabaseMe();
		
		return result;
	}
	
	public ITopicMapping findUsers() throws Exception {
		
		StringBuffer selectbf = new StringBuffer();
		selectbf.append("select * from bpm_topicmapping where topicid=?topicId");
		
		ITopicMapping dao = (ITopicMapping) sql(ITopicMapping.class, selectbf.toString());
		dao.setTopicId( this.getTopicId());
		dao.select();
		
		return dao;
	}
	
	public ITopicMapping findByUser() throws Exception {
		StringBuffer selectbf = new StringBuffer();
		selectbf.append("select * from bpm_topicmapping ");
		selectbf.append(" where topicid = ?topicid");
		selectbf.append(" and userid = ?userid");
		
		ITopicMapping dao = (ITopicMapping) sql(ITopicMapping.class,	selectbf.toString());
		dao.setTopicId( this.getTopicId());
		dao.setUserId( this.getUserId());
		dao.select();
		
		return dao;
	}
	
	public IUser findUser() throws Exception {
		IUser users = (IUser) Database.sql(IUser.class, "select  userId, userName name from bpm_topicmapping  where topicId=?topicId and assigntype=?assigntype ");
		
		users.set("topicId", this.getTopicId() );
		users.set("assigntype", 0);
		users.select();
		
		return users;
	}
	public IDept findDept() throws Exception {
		IDept dept = (IDept) Database.sql(IDept.class, "select  userId as PARTCODE , userName as  PARTNAME  from bpm_topicmapping  where topicId=?topicId and assigntype=?assigntype ");
		
		dept.set("topicId", this.getTopicId() );
		dept.set("assigntype", 2);
		dept.select();
		
		return dept;
	}
	
	public IFollower findFollowers() throws Exception{
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT distinct tm.userId endpoint, ifnull(e.empname,tm.userName) resname, tm.assigntype, tm.topicId parentId, '" + Follower.TYPE_TOPIC + "' parentType");
		sql.append("  FROM bpm_topicmapping tm,  emptable e");
		sql.append(" WHERE tm.userId = e.empcode and tm.assigntype = 0");
		sql.append("   AND tm.topicid = ?topicId");
		
		IFollower follower = (IFollower) Database.sql(IFollower.class, sql.toString());
		follower.set("topicId", this.getTopicId());
		follower.select();	
		
		return follower;
	}

	public void removeMe() throws Exception {
		
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE");
		sql.append("  FROM bpm_topicmapping");
		sql.append(" WHERE topicId=?topicId");
		sql.append("   AND userId=?userId");
		
		ITopicMapping tm = sql(sql.toString());
		tm.setTopicId(this.getTopicId());
		tm.setUserId(this.getUserId());
		tm.update();
		
	}
	
	protected void createDatabaseToTadpole(String vmDB, String vmIP) {
		
		String ip = GlobalContext.getPropertyString("pole.call.ip","localhost");
		String port = GlobalContext.getPropertyString("pole.call.port","80");
		String db  = GlobalContext.getPropertyString("pole.call.db");
		
		String parameter = "?db=" + vmDB
							+ "&email=" + this.getUserId()
							+ "&url=" + vmIP
							+ "&name=" + "aaaa";
		
		String sUrl = "http://" + ip + ":" + port + db + "/createDatabase" + parameter;
		
		URL url;
		URLConnection connection;
		InputStream is;
		InputStreamReader isr;
		BufferedReader br;
		
		try{
			
			url = new URL(sUrl);
			connection = url.openConnection();
			
			is = connection.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			
			String buf = null;
			
			while(true){
				buf = br.readLine();
				if(buf == null) break;
				System.out.println(buf);
			}
			
		}catch(IOException ioe){
			System.err.println("IOException " + ioe);
			ioe.printStackTrace();
		}
	}
}
