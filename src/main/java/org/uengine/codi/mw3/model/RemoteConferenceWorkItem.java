package org.uengine.codi.mw3.model;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dwr.MetaworksRemoteService;
import org.metaworks.widget.IFrame;
import org.metaworks.widget.ModalWindow;
import org.uengine.cloud.saasfier.TenantURL;
import org.uengine.codi.mw3.Login;
import org.uengine.codi.mw3.filter.OtherSessionFilter;
import org.uengine.kernel.EJBProcessInstance;
import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.ProcessInstance;

public class RemoteConferenceWorkItem extends WorkItem{
	
	final public String BBBSalt = GlobalContext.getPropertyString("bbb.security.salt");
	final public String BBBHost = GlobalContext.getPropertyString("bbb.server.host","localhost");
	final public String BBBPort = GlobalContext.getPropertyString("bbb.server.port","80");

	final public String RETURNCODE 			= "returncode";
	final public String CONFERENCE_STARTED	= "startTime";
	final public String METTING_RUNNING 	= "running";
	final public String PARTICIPANTCOUNT 	= "participantCount";
	
	final public String TRUE			 	= "true";
	final public String FALSE 				= "false";
	final public String FAILED				= "FAILED";
	/**
	 * ext1 = meetingID;
	   ext2 = attendeePW;
	   ext3 = moderatorPW;
	   ext4 = URL;
	   ext5 = confereceStarted;
	   ext6 = MeetingRunning;
	   ext7 = participantCount;
	   ext8 = remoteConferece startTime
	 * 
	 */
	public RemoteConferenceWorkItem() throws Exception {
		setType(WORKITEM_TYPE_REMOTECONF);
	}
	
		
	@Override
	public void edit() throws Exception {
		throw new Exception("$CanNotEdit");
	}


	@Override
	public Object[] add() throws Exception {
		Calendar modifyCal = Calendar.getInstance();
		Calendar nowCal = Calendar.getInstance();
		
		int minutes = Integer.parseInt(this.getRemoteConferenceDate().getMinute().getSelectedText()) ; 
		int hours = Integer.parseInt(this.getRemoteConferenceDate().getHour().getSelectedText());
		int days = this.getRemoteConferenceDate().getDate().getDate();
		int months = this.getRemoteConferenceDate().getDate().getMonth()+1;
		int year = this.getRemoteConferenceDate().getDate().getYear()+1900;
		
		String resultExpression =  year+" "+months+" "+days+" "+hours+" "+minutes;
		
		// 입력한 값 세팅.
		modifyCal.set(Calendar.MONTH, 		 months-1);
		modifyCal.set(Calendar.DAY_OF_MONTH, days);
		modifyCal.set(Calendar.HOUR_OF_DAY,  hours);
		modifyCal.set(Calendar.MINUTE, 		 minutes);
		modifyCal.set(Calendar.YEAR, 		 year);
		
		System.out.println(modifyCal.getTime());
		System.out.println(nowCal.getTime());
		if(!modifyCal.after(nowCal)){
			throw new Exception(("$ResetMeetingTime"));
		}
		
		Object[] returnData =  super.add();
		
//		
//		ProcessMap processMap = new ProcessMap();
//		processMap.processManager = processManager;
//		processMap.session = session;
//		processMap.setDefId("remoteConf/process/remoteConfNoti.process");
		
		String rootInstanceId = this.getInstId().toString();
		String defId = "remoteConf/process/remoteConfNoti.process";
		String instId = processManager.initializeProcess(defId);
		
		
		//프로세스 설정.
		Date date = modifyCal.getTime();

		RemoteConferenceData data = new RemoteConferenceData(); 
		data.setInstanceId(rootInstanceId);
		data.setStartDate(date);
		
		
		ProcessInstance instanceObject = processManager.getProcessInstance(instId);
		
		EJBProcessInstance ejbParentInstance = (EJBProcessInstance)instanceObject;
		ejbParentInstance.getProcessInstanceDAO().setRootInstId(new Long(rootInstanceId));
		
		RoleMappingPanel roleMappingPanel = new RoleMappingPanel(processManager, defId, session);
		roleMappingPanel.putRoleMappings(processManager, rootInstanceId);
		
		processManager.getProcessInstance(instId.toString()).setBeanProperty("RemoteConferenceData", data);
		processManager.executeProcess(instId.toString());
		processManager.applyChanges();
		
		boolean record = true;
		
		String createParam = "name=" + URLEncoder.encode(session.getUser().getName(), "UTF-8") + "&record=" + record + "&meetingID=" + getTaskId();// + "&attendeePW=" + password;
		String checkSum = hex_sha1("create" + createParam + BBBSalt);
		String callURI = "/bigbluebutton/api/create?" + createParam + "&checksum=" + checkSum;
		
		HttpClient httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
		httpClient.getHostConfiguration().setHost(GlobalContext.getPropertyString("bbb.server.host", "localhost"), Integer.parseInt(BBBPort), "http");
		httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
			
		GetMethod getMethod = new GetMethod(callURI);

		httpClient.executeMethod(getMethod);
		
		InputStream is = getMethod.getResponseBodyAsStream();
		
		SAXBuilder builder = new SAXBuilder();
		builder.setValidation(false);
		builder.setExpandEntities(false);
		builder.setIgnoringElementContentWhitespace(true);

		Document documentToAttach = builder.build(is);
		Element rootElement = documentToAttach.getRootElement();
		List<Element> childs = rootElement.getChildren();
		
		String meetingID = null;
		String attendeePW = null;
		String moderatorPW = null;
		
		for(Element elem : childs){
			if("meetingID".equals(elem.getName())){
				meetingID = elem.getValue();
			}else if("attendeePW".equals(elem.getName())){
				attendeePW = elem.getValue();
			}else if("moderatorPW".equals(elem.getName())){
				moderatorPW = elem.getValue();
			}
		}
		
		is.close();
		Instance instance = new Instance();
		instance.setInstId(rootInstId);
		
		instance.databaseMe().setName(this.getTitle());
		databaseMe().setExt1(meetingID);
		databaseMe().setExt2(attendeePW);
		databaseMe().setExt3(moderatorPW);
		databaseMe().setExt8(resultExpression);
		
		return returnData;
	}

	
	@ServiceMethod(target=ServiceMethodContext.TARGET_APPEND, callByContent=true)
	public Object[] join() throws Exception{
 		
		String meetingTime[] = databaseMe().getExt8().split(" ");
		Calendar nowCal = Calendar.getInstance();
		Calendar meetingCal = Calendar.getInstance();
		
		meetingCal.set(Calendar.YEAR, 		  Integer.parseInt(meetingTime[0]));
		meetingCal.set(Calendar.MONTH, 		  Integer.parseInt(meetingTime[1])-1);
		meetingCal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(meetingTime[2]));
		meetingCal.set(Calendar.HOUR_OF_DAY,  Integer.parseInt(meetingTime[3]));
		meetingCal.set(Calendar.MINUTE, 	  Integer.parseInt(meetingTime[4]));
		
		if(meetingCal.after(nowCal)){
			SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm ");
			throw new Exception(format.format(meetingCal.getTime())+("$JoinNotUntil"));
		}
		
		String meetingID = databaseMe().getExt1();
		String attendeePW = databaseMe().getExt2();
		String moderatorPW = databaseMe().getExt3();
		String title = databaseMe().getTitle();
		String endPoint = databaseMe().getEndpoint();
		String joinParam=null;
		
		joinParam = "meetingID=" + meetingID + "&fullName=" + URLEncoder.encode(session.getUser().getName(), "UTF-8");
				
		if(session.getEmployee().getEmpCode().equals(endPoint))
			joinParam += "&password=" + moderatorPW;
		else
			joinParam += "&password=" + attendeePW;
		
		
		String joinCheckSum = hex_sha1("join" + joinParam + BBBSalt);
		String joinURI = "join?" + joinParam + "&checksum=" + joinCheckSum;
		
		IFrame iframe = new IFrame();
		iframe.setSrc("http://"+ BBBHost + "/bigbluebutton/api/" + joinURI);
		iframe.setWidth("100%");
		
		this.databaseMe().setExt7("0");
		
		this.databaseMe().setExt6(TRUE);
				
		HashMap<String, String> pushUserMap = new HashMap<String, String>();
		Notification notification = new Notification();
		notification.session = session;
		pushUserMap = notification.findTopicNotiUser(String.valueOf(this.getTaskId()));
		String currentUserId = session.getUser().getUserId();
	
		final IWorkItem copyOfThis = this;
		
		MetaworksRemoteService.pushTargetClientObjects(Login.getSessionIdWithUserId(currentUserId), new Object[]{new WorkItemListener(WorkItemListener.COMMAND_REFRESH , copyOfThis)});
		
		// 본인 이외에 다른 사용자에게 push
		MetaworksRemoteService.pushClientObjectsFiltered(
				new OtherSessionFilter(pushUserMap, currentUserId.toUpperCase()),
				new Object[]{new WorkItemListener(WorkItemListener.COMMAND_REFRESH , copyOfThis)});
				
		return new Object[]{new ModalWindow(iframe, 1000, 550, title)};
	}
	
	@ServiceMethod(target=ServiceMethodContext.TARGET_SELF, callByContent=true)
	public void checkJoined() throws Exception{
 		
		this.meetingInfo();
		
		HashMap<String, String> pushUserMap = new HashMap<String, String>();
		Notification notification = new Notification();
		notification.session = session;
		pushUserMap = notification.findTopicNotiUser(String.valueOf(this.getTaskId()));
		String currentUserId = session.getUser().getUserId();
	
		final IWorkItem copyOfThis = this;
		
		MetaworksRemoteService.pushTargetClientObjects(Login.getSessionIdWithUserId(currentUserId), new Object[]{new WorkItemListener(WorkItemListener.COMMAND_REFRESH , copyOfThis)});
		
		// 본인 이외에 다른 사용자에게 push
		MetaworksRemoteService.pushClientObjectsFiltered(
				new OtherSessionFilter(pushUserMap, currentUserId.toUpperCase()),
				new Object[]{new WorkItemListener(WorkItemListener.COMMAND_REFRESH , copyOfThis)});
		
	}
	
	@ServiceMethod
	public void end() throws Exception {
		this.meetingInfo();
		String participantCount = this.databaseMe().getExt7();
		
		if(!"0".equals(participantCount)){
			throw new Exception("회의가 진행 중입니다.");
		}
		
		String salt = GlobalContext.getPropertyString("bbb.security.salt");
		
		String meetingID = databaseMe().getExt1();
		String moderatorPW = databaseMe().getExt3();
		
		String endParam = "meetingID=" + meetingID + "&password=" + moderatorPW ;
		String checkSum = hex_sha1("end" + endParam + salt);
		String endURI = "end?" + endParam + "&checksum=" + checkSum;
		
		URL url = new URL("http://" + BBBHost + "/bigbluebutton/api/" + endURI);
		
        URLConnection urlConn = url.openConnection();
        BufferedReader burr = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

        burr.close();
        
		databaseMe().setStatus("Waited");
		setWriter(session.getUser());
		setTitle(databaseMe().getTitle());
		
		recodingCheck();
		
	}

	@ServiceMethod
	public void meetingInfo() throws Exception{
		
		String meetingID = databaseMe().getExt1();
		String moderatorPW = databaseMe().getExt3();
		
		String param = "meetingID=" + meetingID + "&password=" + moderatorPW;
		String checksum = hex_sha1("getMeetingInfo" + param + BBBSalt);
		String url = "http://" + BBBHost + "/bigbluebutton/api/getMeetingInfo?"+ param + "&checksum=" + checksum;
		
		
		HttpClient httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
		httpClient.getHostConfiguration().setHost(BBBHost, Integer.parseInt(BBBPort), "http");
		httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
			
		GetMethod getMethod = new GetMethod(url);

		httpClient.executeMethod(getMethod);
		
		InputStream is = getMethod.getResponseBodyAsStream();
		
		SAXBuilder builder = new SAXBuilder();
		builder.setValidation(false);
		builder.setExpandEntities(false);
		builder.setIgnoringElementContentWhitespace(true);

		Document documentToAttach = builder.build(is);
		Element rootElement = documentToAttach.getRootElement();
		List<Element> childs = rootElement.getChildren();
		
		
		for(Element elem : childs){
			if(RETURNCODE.equals(elem.getName())){
				if(FAILED.equals(elem.getValue())){
					this.databaseMe().setExt5(FAILED);
					this.databaseMe().setExt6(FAILED);
					break;
				}
			}
			
			if(CONFERENCE_STARTED.equals(elem.getName())){
				if("0".equals(elem.getValue()))
					this.databaseMe().setExt5(FALSE);
				else
					this.databaseMe().setExt5(TRUE);
			
			}else if(METTING_RUNNING.equals(elem.getName())){
				
				if(Boolean.parseBoolean(elem.getValue()))
					this.databaseMe().setExt6(TRUE);
				else
					this.databaseMe().setExt6(FALSE);
			
			}else if(PARTICIPANTCOUNT.equals(elem.getName())){
				
				this.databaseMe().setExt7(elem.getValue());
				
			}
			
		}
		
	}
	
	
	@ServiceMethod(callByContent=true)
	public Object recodingCheck() throws Exception{
		String salt = GlobalContext.getPropertyString("bbb.security.salt");
		
		String meetingID = databaseMe().getExt1();
		String recordParam = "meetingID=" + meetingID;
		String checkSum = hex_sha1("getRecordings" + recordParam + salt);
		String recordURI = "/bigbluebutton/api/getRecordings?" + recordParam + "&checksum=" + checkSum;
		
		String recordUrl = null;
		String recordID = null;
		
		HttpClient httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
		httpClient.getHostConfiguration().setHost(BBBHost, Integer.parseInt(BBBPort), "http");
		httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
			
		GetMethod getMethod = new GetMethod(recordURI);

		httpClient.executeMethod(getMethod);
		
		InputStream is = getMethod.getResponseBodyAsStream();
		boolean returnFalg = false;
		try{
			SAXBuilder builder = new SAXBuilder();
			builder.setValidation(false);
			builder.setExpandEntities(false);
			builder.setIgnoringElementContentWhitespace(true);
	
			Document documentToAttach = builder.build(is);
			Element rootElement = documentToAttach.getRootElement();
			
			Element childs = rootElement.getChild("recordings").getChild("recording");
			if( childs != null ){
				List<Element> childsList = childs.getChildren();
				
				for(Element elem : childsList){
					if("recordID".equals(elem.getName())){
						recordID = elem.getValue();
					}
				}
				recordUrl = "http://" + BBBHost+ "/playback/presentation/playback.html?meetingId=" + recordID;
				IFrame iframe = new IFrame();
				iframe.setSrc(recordUrl);
				iframe.setWidth("100%");
				this.setConference(iframe);
				this.setContentLoaded(true);
				returnFalg = true;
				databaseMe().setStatus("Completed");
				databaseMe().setExt4(recordUrl);

			}else{
				
				IFrame iframe = new IFrame();
				String loadingUrl = TenantURL.getURL() 
						+ "dwr/metaworks/images/circleloading.gif";
				
				iframe.setSrc(loadingUrl);
				iframe.setWidth("100%");
				this.setConference(iframe);
				this.setContentLoaded(false);
				returnFalg = false;

				
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			is.close();
			
		}
		if( returnFalg ){
			return this;
		}else{
			return null;
		}
		
	}

//	private String getRecordingInfo() throws Exception{
//		String salt = GlobalContext.getPropertyString("bbb.security.salt");
//		
//		String meetingID = databaseMe().getExt1();
//		
//		String recordParam = "meetingID=" + meetingID;
//		String checkSum = hex_sha1("getRecordings" + recordParam + salt);
//		String recordURI = "/bigbluebutton/api/getRecordings?" + recordParam + "&checksum=" + checkSum;
//		
//		HttpClient httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
//		httpClient.getHostConfiguration().setHost(bbbHost, Integer.parseInt(bbbPort), "http");
//		httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
//		
//		GetMethod getMethod = new GetMethod(recordURI);
//		
//		httpClient.executeMethod(getMethod);
//		
//		InputStream is = getMethod.getResponseBodyAsStream();
//		
//		SAXBuilder builder = new SAXBuilder();
//		builder.setValidation(false);
//		builder.setExpandEntities(false);
//		builder.setIgnoringElementContentWhitespace(true);
//		
//		Document documentToAttach = builder.build(is);
//		Element rootElement = documentToAttach.getRootElement();
//		Element childs = rootElement.getChild("recordings").getChild("recording");
//		
//		String url = null;
//		String recordID = null;
//		
//		if(childs != null){
//			
//			List<Element> childsList = childs.getChildren();
//			
//			
//			for(Element elem : childsList){
//				if("recordID".equals(elem.getName())){
//					recordID = elem.getValue();
//				}
//			}
//			
//			is.close();
//			
//			url = "http://" + bbbHost+ "/playback/slides/playback.html?meetingId=" + recordID;
//			
//		}
//		
//		databaseMe().setExt4(url);
//		
//		return url;
//		
//	}
	
	@ServiceMethod(target="popup")
	public ModalWindow palyBack() throws Exception{
		String salt = GlobalContext.getPropertyString("bbb.security.salt");
		
		String meetingID = databaseMe().getExt1();
		
		String recordParam = "meetingID=" + meetingID;
		String checkSum = hex_sha1("getRecordings" + recordParam + salt);
		String recordURI = "/bigbluebutton/api/getRecordings?" + recordParam + "&checksum=" + checkSum;
		
		String url = null;
		String recordID = null;
		
		HttpClient httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
		httpClient.getHostConfiguration().setHost(BBBHost, Integer.parseInt(BBBPort), "http");
		httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
			
		GetMethod getMethod = new GetMethod(recordURI);

		httpClient.executeMethod(getMethod);
		
		InputStream is = getMethod.getResponseBodyAsStream();
		
		SAXBuilder builder = new SAXBuilder();
		builder.setValidation(false);
		builder.setExpandEntities(false);
		builder.setIgnoringElementContentWhitespace(true);

		Document documentToAttach = builder.build(is);
		Element rootElement = documentToAttach.getRootElement();
		
		try{
			Element childs = rootElement.getChild("recordings").getChild("recording");
			List<Element> childsList = childs.getChildren();
			
			for(Element elem : childsList){
				if("recordID".equals(elem.getName())){
					recordID = elem.getValue();
				}
			}
		}catch (Exception e) {
			throw new Exception("레코딩 변환작업 중입니다.");
		}
		
		is.close();
		
		url = "http://" + BBBHost+ "/playback/presentation/playback.html?meetingId=" + recordID;
		
		IFrame iframe = new IFrame();
		iframe.setSrc(url);
		iframe.setWidth("100%");
		iframe.setHeight("100%");
		
		return new ModalWindow(iframe, 1500, 750, databaseMe().getTitle());
		
	}
	
	@Override
	public void loadContents() throws Exception {
		this.setContentLoaded(true);
		
	}
	
	
	private String hex_sha1(String seed) throws NoSuchAlgorithmException{
		 MessageDigest mDigest = MessageDigest.getInstance("SHA1");
	        byte[] result = mDigest.digest(seed.getBytes());
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < result.length; i++) {
	            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
	        }
	         
	        return sb.toString();
	}
	
	@ServiceMethod(callByContent=true, eventBinding="change")
	public void refresh() throws Exception{
		//this.loadContents();
	}
	
	ArrayList<String> initialFollowers;
		public ArrayList<String> getInitialFollowers() {
			return initialFollowers;
		}
		public void setInitialFollowers(ArrayList<String> initialFollowers) {
			this.initialFollowers = initialFollowers;
		}
	
}
