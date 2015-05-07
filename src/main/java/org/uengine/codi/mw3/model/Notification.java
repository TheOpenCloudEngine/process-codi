package org.uengine.codi.mw3.model;

import java.util.Date;
import java.util.HashMap;

import org.metaworks.Refresh;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.dao.Database;
import org.metaworks.dao.TransactionContext;
import org.metaworks.dao.TransactionListener;
import org.metaworks.dwr.MetaworksRemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.uengine.codi.mw3.Login;
import org.uengine.codi.mw3.filter.AllSessionFilter;
import org.uengine.kernel.Role;

public class Notification extends Database<INotification> implements INotification{

	Long notiId;
		public Long getNotiId() {
			return notiId;
		}
		public void setNotiId(Long notiId) {
			this.notiId = notiId;
		}
		
	Date inputDate;
		public Date getInputDate() {
			return inputDate;
		}
		public void setInputDate(Date inputDate) {
			this.inputDate = inputDate;
		}

	String userId;
		public String getUserId() {
			return userId;
		}
	
		public void setUserId(String userId) {
			this.userId = userId;
		}

	String actorId;
		public String getActorId() {
			return actorId;
		}
		public void setActorId(String actorId) {
			this.actorId = actorId;
		}
		
	IUser actor;
		public IUser getActor() {
			return actor;
		}
		public void setActor(IUser actor) {
			this.actor = actor;
		}

	Long instId;
		public Long getInstId() {
			return instId;
		}
		public void setInstId(Long instId) {
			this.instId = instId;
		}
		
	Long taskId;
		public Long getTaskId() {
			return taskId;
		}
		public void setTaskId(Long taskId) {
			this.taskId = taskId;
		}
		
	String actAbstract;
		public String getActAbstract() {
			return actAbstract;
		}
		public void setActAbstract(String actAbstract) {
			
			if(actAbstract.length() > 97){
				actAbstract = actAbstract.substring(0, 97) + "...";
			}
			
			this.actAbstract = actAbstract;
		}
		
	boolean confirm;
		public boolean isConfirm() {
			return confirm;
		}
		public void setConfirm(boolean confirm) {
			this.confirm = confirm;
		}

	public String url;
	
	public void add(final IInstance instance) throws Exception{
		createDatabaseMe();
		flushDatabaseMe();
		
		final Employee userInfo = new Employee();
		userInfo.setEmpCode(getUserId());

		IEmployee userInfoTemp;
		try{
			userInfoTemp = userInfo.databaseMe();
		}catch(Exception e){
			userInfoTemp = new Employee();
			userInfoTemp.setFacebookId(userInfo.getEmpCode());
		}
		

//		/**
//		 * TODO: e-mail nofitication
//		 * 
//		 * 성능 이슈가 야기됨.
//		 * follower마다 Emptable에서 데이터를 읽어 오기 때문에 ...(actorUserInfo.databaseMe()) 차후 수정
//		 * 
//		 */
//		
//		String smtpUse= GlobalContext.getPropertyString("smtp.use", "0");
//		if("0".equals(smtpUse)){
//			return;
//		}
//		if(userInfo.databaseMe().isMailNoti()){
//			final Employee userInfoDB = new Employee();
//			userInfoDB.copyFrom(userInfoTemp);
//			
//			//String realPath = System.getenv("CODI_HOME");
//			String realPath = CodiStringUtil.lastLastFileSeparatorChar(new HttpServletRequestWrapper(TransactionContext.getThreadLocalInstance().getRequest()).getRealPath(""));
//			final String path = realPath + GlobalContext.getPropertyString("email.noti", "resources/mail/notiMail.html");
//			
//			final String url = TenantContext.getURL();
//			
//			/*
//			final Employee actorUserInfo = new Employee();
//			actorUserInfo.setEmpCode(getActorId());
//			
//			final IEmployee actorUserInfoDB = actorUserInfo.databaseMe();
//			*/
//			
//			new Thread(){
//
//				@Override
//				public void run() {
//					try {
//						// facebook
//						/*
//						if(userInfoDB.getEmpCode()==null && userInfoDB.getFacebookId()!=null){
//							//TODO: this case should be chosen by more deterministic option (e.g. User.network=='facebook') 
//							FacebookClient facebookClient = new DefaultFacebookClient();
//							String facebookId = getUserId();
//							String query = "SELECT uid, name, email, locale FROM user WHERE uid=" + facebookId;
//							
//							List<com.restfb.types.User> users = facebookClient.executeQuery(query, com.restfb.types.User.class);
//							if (users.size() == 0) {
//								throw new UEngineException("There's no such user [" + facebookId + "]");
//							} else {
//								com.restfb.types.User user = users.get(0);
//								userInfoDB.setEmail(user.getEmail());
//							}
//						}
//						*/
//						String content = "";
//						
//						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd a h:mm");
//						String date = df.format(getInputDate());
//						
//						FileInputStream is;
//						try {
//							is = new FileInputStream(path);
//							InputStreamReader isr = new InputStreamReader(is,"UTF-8");
//							BufferedReader br = new BufferedReader(isr);
//							
//							int data;
//							while((data = br.read()) != -1){
//								content += (char)data;
//							}
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						
//						content = this.replaceString(content,"user.name", userInfoDB.getEmpName());
//						content = this.replaceString(content, "notimail.date", date);
//						content = this.replaceString(content, "notimail.image", url + "portrait/" + getActorId() + ".thumnail");
//						
//						content = this.replaceString(content, "notimail.content", getActAbstract());
//						content = this.replaceString(content, "processcodi.url", url);
//						
//						String from = GlobalContext.getPropertyString("codi.mail.noreply", "noreply@processcodi.com");
//						String title = "프로세스 코디의 알림이 도착했습니다.";
//						
//						// TODO 상대방 이메일을 userInfoDB에서 가져오질 못함
//						(new EMailServerSoapBindingImpl()).sendMail(from, userInfoDB.getEmail(), title, content);
//					} catch (RemoteException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//				
//				public String replaceString(String lineString, String from, String to){
//					String returnValue = "";
//					returnValue = lineString.replaceAll(from, to);
//					return returnValue;
//				}
//			}.start();
//		}
	}
	
	public HashMap<String, String> findInstanceNotiUser(String instanceId) throws Exception{
		HashMap<String, String> notiUsers = new HashMap<String, String>();
		
		InstanceFollower findFollower = new InstanceFollower(instanceId);
		findFollower.session = session;
		findFollower.setEnablePush(false);
		
		IFollower follower = findFollower.findFollowers();
		while(follower.next()){
			int assigntype = follower.getAssigntype();
			if( assigntype == Role.ASSIGNTYPE_USER ){
				String followerUserId = follower.getEndpoint();
				if( session.getUser().getUserId().equals(followerUserId)){	// 자기 자신은 제외
					continue;
				}
				notiUsers.put(followerUserId, Login.getSessionIdWithUserId(followerUserId));
				
			}else if( assigntype == Role.ASSIGNTYPE_DEPT ){
				String partcode = follower.getEndpoint();
				Dept dept = new Dept();
				dept.setGlobalCom(session.getCompany().getComCode());
				dept.setPartCode(partcode);
				
				IEmployee employee = new Employee();
				employee.setMetaworksContext(this.getMetaworksContext());
				employee = employee.findByDept(dept);
				if( employee != null ){
					while(employee.next()){
						String followerUserId = employee.getEmpCode();
						if( session.getUser().getUserId().equals(followerUserId)){	// 자기 자신은 제외
							continue;
						}
						notiUsers.put(followerUserId, Login.getSessionIdWithUserId(followerUserId));
					}
				}
			}
		}
		
		return notiUsers;
	}
	
	public HashMap<String, String> findTopicNotiUser(String topicId) throws Exception{
		HashMap<String, String> notiUsers = new HashMap<String, String>();
		
		TopicFollower topicFollower = new TopicFollower();
		topicFollower.setParentId(topicId);
		
		IFollower topicFollowers = topicFollower.findFollowers();
		while(topicFollowers.next()){
			int assigntype = topicFollowers.getAssigntype();
			if( assigntype == Role.ASSIGNTYPE_USER ){
				String followerUserId = topicFollowers.getEndpoint();
				if( session.getUser().getUserId().equals(followerUserId)){	// 자기 자신은 제외
					continue;
				}
				notiUsers.put(followerUserId, Login.getSessionIdWithUserId(followerUserId));
				
			}else if( assigntype == Role.ASSIGNTYPE_DEPT ){
				String partcode = topicFollowers.getEndpoint();
				Dept dept = new Dept();
				dept.setGlobalCom(session.getCompany().getComCode());
				dept.setPartCode(partcode);
				
				IEmployee employee = new Employee();
				employee.setMetaworksContext(this.getMetaworksContext());
				employee = employee.findByDept(dept);
				if( employee != null ){
					while(employee.next()){
						String followerUserId = employee.getEmpCode();
						if( session.getUser().getUserId().equals(followerUserId)){	// 자기 자신은 제외
							continue;
						}
						notiUsers.put(followerUserId, Login.getSessionIdWithUserId(followerUserId));
					}
				}
			}
		}
		return notiUsers;
	}
		
	INotification list(Session session) throws Exception{
		INotification noti =  sql("select * from bpm_noti where userId = ?userId order by inputdate desc limit 30");
		noti.setUserId(session.user.getUserId());
		noti.select();
		
		return noti;
	}
	
	int count(Session session) throws Exception{
		INotification noti =  sql("select count(1) CNT from bpm_noti where userId = ?userId and confirm=0");
		noti.setUserId(session.user.getUserId());
		noti.select();
		noti.next();
		return (Integer) noti.getInt("CNT");
	}
	
	public Object[] see() throws Exception{
		
		// 확인 표시
		this.setConfirm(true);
		
		Instance instance = new Instance();
		instance.setInstId(getInstId());
		instance.session = session;

		InstanceViewContent instanceViewContent = instance.detail();
		
		if(SNS.isPhone()){
			Popup popup = new Popup(instanceViewContent.instanceView);
			popup.setName(instanceViewContent.getTitle());
			
			return new Object[]{ popup };
		}else{
			return new Object[]{new Refresh(instanceViewContent), new Refresh(this)};
		}
		
	}
	
	public static void addPushListener(final HashMap<String, String> notiUsers){

		/*
		 * event 순서 문제 해결
		 * 
		 * desc : flushDatabaseMe() 에서 beforeCommit 이벤트를 발생 시켜 create 및 update 를 실행하는데
		 * push 가 create 및 update 보다 더 빨리 실행 되여 발생하여 afterCommit 시  push 되도록 수정
		 */
		TransactionListener flusher = new TransactionListener(){
			public void beforeCommit(TransactionContext tx) throws Exception {
				// TODO Auto-generated method stub
			}

			public void beforeRollback(TransactionContext tx) throws Exception {
				// TODO Auto-generated method stub
			}

			public void afterCommit(TransactionContext tx) throws Exception {
				// TODO Auto-generated method stub
				Notification.push(notiUsers);
			}

			public void afterRollback(TransactionContext tx) throws Exception {
				// TODO Auto-generated method stub
			}
		};

		TransactionContext tx = TransactionContext.getThreadLocalInstance();
		tx.addTransactionListener(flusher);
	}

	public static void push(HashMap<String, String> notiUsers) throws Exception {

		MetaworksRemoteService.pushTargetScriptFiltered(new AllSessionFilter(notiUsers),
				"mw3.getAutowiredObject('" + NotificationBadge.class.getName() + "').refresh",
				new Object[]{});

	}
	
	
	@AutowiredFromClient
	public Session session;
	
	@Autowired
	public InstanceViewContent instanceViewContent;
	
}
