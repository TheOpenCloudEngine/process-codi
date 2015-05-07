package org.uengine.codi.mw3.knowledge;

import java.net.URL;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Pattern;

import org.directwebremoting.Browser;
import org.directwebremoting.ScriptSessions;
import org.metaworks.ContextAware;
import org.metaworks.EventContext;
import org.metaworks.MetaworksContext;
import org.metaworks.MetaworksException;
import org.metaworks.Refresh;
import org.metaworks.Remover;
import org.metaworks.ServiceMethodContext;
import org.metaworks.ToEvent;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Available;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.annotation.Validator;
import org.metaworks.annotation.ValidatorContext;
import org.metaworks.annotation.ValidatorSet;
import org.metaworks.dao.TransactionContext;
import org.metaworks.website.MetaworksFile;
import org.metaworks.widget.ModalWindow;
import org.springframework.beans.factory.annotation.Autowired;
import org.uengine.codi.mw3.Login;
import org.uengine.codi.mw3.model.Employee;
import org.uengine.codi.mw3.model.IEmployee;
import org.uengine.codi.mw3.model.INotiSetting;
import org.uengine.codi.mw3.model.Instance;
import org.uengine.codi.mw3.model.InstanceListPanel;
import org.uengine.codi.mw3.model.ListPanel;
import org.uengine.codi.mw3.model.Locale;
import org.uengine.codi.mw3.model.NotiSetting;
import org.uengine.codi.mw3.model.Notification;
import org.uengine.codi.mw3.model.NotificationBadge;
import org.uengine.codi.mw3.model.Perspective;
import org.uengine.codi.mw3.model.Session;
import org.uengine.codi.mw3.model.SystemWorkItem;
import org.uengine.codi.mw3.model.TopicInfo;
import org.uengine.codi.mw3.model.TopicPerspective;
import org.uengine.processmanager.ProcessManagerRemote;

@Face(ejsPath="dwr/metaworks/genericfaces/FormFace.ejs",
	  ejsPathMappingByContext=	{
				"{how: 'html', face: 'dwr/metaworks/org/uengine/codi/mw3/knowledge/TopicTitle.ejs'}"
}, options={"fieldOrder"}, values={"topicTitle,topicSecuopt,url,logoFile"})
public class TopicTitle  implements ContextAware{
	public TopicTitle(){
		setMetaworksContext(new MetaworksContext());
	}
	
	MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}
	
	MetaworksFile logoFile;
		@Face(displayName="주제로고")
		@Available(when={MetaworksContext.WHEN_NEW, MetaworksContext.WHEN_EDIT})
		public MetaworksFile getLogoFile() {
			return logoFile;
		}
		public void setLogoFile(MetaworksFile logoFile) {
			this.logoFile = logoFile;
		}	
		
	String topicId;
		@Hidden
		public String getTopicId() {
			return topicId;
		}
		public void setTopicId(String topicId) {
			this.topicId = topicId;
		}

	String topicTitle;
		@Face(displayName="$topicTitle")
		@ValidatorSet({
			@Validator(name=ValidatorContext.VALIDATE_NOTNULL, message="주제 이름을 입력해주세요."),
			@Validator(name=ValidatorContext.VALIDATE_MAX , options={"20"}, message="20자 이내로 입력해주세요."),
			@Validator(name=ValidatorContext.VALIDATE_REGULAREXPRESSION, options={"/^[^~!@\\\\#$%^&*\\()\\-=+_\'\"]+$/"}, message="다음과 같은 문자는 입력 할 수 없습니다. ~!@#$%^&*()\\-=+_\'\"")
		})
		@Available(when={MetaworksContext.WHEN_NEW, MetaworksContext.WHEN_EDIT})
		public String getTopicTitle() {
			return topicTitle;
		}
		public void setTopicTitle(String topicTitle) {
			this.topicTitle = topicTitle;
		}
		
	boolean topicSecuopt;				
		@Face(displayName="$topicSecuopt")
		@Available(when={MetaworksContext.WHEN_NEW, MetaworksContext.WHEN_EDIT})
		public boolean isTopicSecuopt() {
			return topicSecuopt;
		}
		public void setTopicSecuopt(boolean topicSecuopt) {
			this.topicSecuopt = topicSecuopt;
		}
	String url;
		@Face(displayName="$topicUrl")
		@Hidden
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		
	String embeddedHtml;
		@Face(displayName="$topicEmbeddedHtml")
		@Available(how={"html"})
		public String getEmbeddedHtml() {
			return embeddedHtml;
		}
		public void setEmbeddedHtml(String embeddedHtml) {
			this.embeddedHtml = embeddedHtml;
		}
		
	
	public void makeHtml() {
		try{
			HttpServletRequest request = TransactionContext.getThreadLocalInstance().getRequest();
			
	        String url = request.getRequestURL().toString();
	        String codebase = url.substring( 0, url.lastIndexOf( "/" ) );
	        URL urlURL = new java.net.URL(codebase);
	        
	       	String host = urlURL.getHost();
	       	int port = urlURL.getPort();
	       	String path = urlURL.getPath();
	       	String contextOnly = path.substring(0, path.substring(1).indexOf("/")+1);
			String protocol = urlURL.getProtocol();
	
			String defaultUrl = protocol + "://" + host + ":" + port + contextOnly + "/portlet_instanceList.html?type=Topic&id=" + this.getTopicId();
			String embeddedHtml = "<div style='padding:15px;border:1px solid #D7D7D7; font-size:14px; font-weight:bold;margin-bottom:10px;'>&lt;iframe id=\"portlet\" src=\"" + defaultUrl + "\" style=\"width: 500px; height: 500px; border-width:1px; border-color:red; border-style:solid;\"></iframe></div>";
			
			this.setEmbeddedHtml(embeddedHtml);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void saveMe() throws Exception {
		WfNode wfNode = new WfNode();

		if(this.getLogoFile().getFileTransfer() != null &&
				this.getLogoFile().getFilename() != null && 
				this.getLogoFile().getFilename().length() > 0){			
			
			if( this.getLogoFile().getFileTransfer().getMimeType() != null  && !this.getLogoFile().getFileTransfer().getMimeType().startsWith("image")){
				throw new MetaworksException("$OnlyImageFileCanUpload");
			}else{
				this.getLogoFile().upload();
			}
			
		}
		
		if(MetaworksContext.WHEN_NEW.equals(this.getMetaworksContext().getWhen())){
			
			// TODO: 이름으로 바로 검색하여 중복 값 체크 할 수 있게 수정해야함
			ITopicNode topicNodeList = TopicNode.findTopic(session);
			while(topicNodeList.next()){
				if(this.getTopicTitle().equals(topicNodeList.getName())){
					throw new Exception("$DuplicateName");
				}
			}
			
			wfNode.setName(this.getTopicTitle());
			wfNode.setType("topic");
			wfNode.setSecuopt(topicSecuopt ? "1" : "0");
			wfNode.setParentId(session.getCompany().getComCode());	
			wfNode.setAuthorId(session.getUser().getUserId());		
			wfNode.setCompanyId(session.getCompany().getComCode());
			if(this.getLogoFile().getUploadedPath() != null && this.getLogoFile().getFilename() != null){
				wfNode.setUrl(this.getLogoFile().getUploadedPath());
				wfNode.setThumbnail(this.getLogoFile().getFilename());
			}
			wfNode.createMe();
			
			TopicMapping tm = new TopicMapping();
			tm.setTopicId(wfNode.getId());
			tm.setUserId(session.getUser().getUserId());
			tm.setUserName(session.getUser().getName());
			tm.getMetaworksContext().setWhen(this.getMetaworksContext().getWhen());
			
			tm.saveMe();
			tm.flushDatabaseMe();
			this.setTopicId(wfNode.getId());
		}else{
			wfNode.setId(this.getTopicId());
			
			wfNode.copyFrom(wfNode.databaseMe());
			wfNode.setSecuopt(topicSecuopt ? "1" : "0");
			
			if(this.getLogoFile().getUploadedPath() != null && this.getLogoFile().getFilename() != null){
				wfNode.setUrl(this.getLogoFile().getUploadedPath());
				wfNode.setThumbnail(this.getLogoFile().getFilename());
			}
			
			wfNode.setName(this.getTopicTitle());
			wfNode.saveMe();
		}
	}
	
	@Face(displayName="$Create")
	@Available(when={MetaworksContext.WHEN_NEW})
	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_APPEND, validate=true)
	public Object[] save() throws Exception{
		
		this.saveMe();
		
		TopicNode topicNode = new TopicNode();
		topicNode.setId(this.getTopicId());
		topicNode.setName(this.getTopicTitle());
		topicNode.setType(TopicNode.TOPIC);
		topicNode.setSecuopt(this.isTopicSecuopt()?"1":"0");
		topicNode.session = session;
		
		this.makeHtml();
		
//		this.notiToCompany();
		
		this.getMetaworksContext().setWhen(MetaworksContext.WHEN_VIEW);
		this.getMetaworksContext().setHow("html");
		
		Object[] returnObj = topicNode.loadTopic();
		Object[] returnObject = new Object[ returnObj.length + 2];
		for (int i = 0; i < returnObj.length; i++) {
			returnObject[i] = new Refresh(returnObj[i]);
		}
		
		returnObject[returnObj.length ] = new ToEvent(ServiceMethodContext.TARGET_OPENER, EventContext.EVENT_CHANGE);
		returnObject[returnObj.length + 1] = new ToEvent(ServiceMethodContext.TARGET_SELF, EventContext.EVENT_CLOSE);
		
		return returnObject;
	}
	
	
	public void notiToCompany() throws Exception{
		Notification noti = new Notification();
		INotiSetting notiSetting = new NotiSetting();
		Instance instance = new Instance();
		noti.session = session;
		instance = this.createWorkItem();
		
		Employee employee = new Employee();
		employee.setEmpCode(session.getEmployee().getEmpCode());
		employee.copyFrom(employee.databaseMe());
		IEmployee findResult = employee.findByGlobalCom(employee.getGlobalCom());
		INotiSetting findNotiSetting;
		Employee codi = new Employee();
		codi.setEmpCode("0");
		
		while(findResult.next()){
			findNotiSetting = notiSetting.findByUserId(findResult.getEmpCode());
			if(findNotiSetting.next()){
				if(findNotiSetting.isModiTopic()){
					noti.setNotiId(System.currentTimeMillis()); //TODO: why generated is hard to use
					noti.setUserId(findResult.getEmpCode());
					noti.setActorId(session.getEmployee().getEmpName());
					noti.setConfirm(false);
					noti.setInstId(instance.getInstId());
					noti.setInputDate(Calendar.getInstance().getTime());
					if(MetaworksContext.WHEN_NEW.equals(this.getMetaworksContext().getWhen()))
						noti.setActAbstract(session.getUser().getName() + "님이 주제 " + this.getTopicTitle() + "를 등록하였습니다.");
					else{
						noti.setActAbstract(session.getUser().getName() + "님이 주제를 " + this.getTopicTitle() + "로 변경하였습니다.");
					}
				
					//워크아이템에서 노티를 추가할때와 동일한 로직을 수행하도록 변경
			//			noti.createDatabaseMe();
			//			noti.flushDatabaseMe();
					
					noti.add(instance);
				
					String followerSessionId = Login.getSessionIdWithUserId(employee.getEmpCode());
					
					try{
						//NEW WAY IS GOOD
						Browser.withSession(followerSessionId, new Runnable(){
			
							@Override
							public void run() {
								//refresh notification badge
								ScriptSessions.addFunctionCall("mw3.getAutowiredObject('" + NotificationBadge.class.getName() + "').refresh", new Object[]{});
							}
							
						});
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		}		
	}
	
	public Instance createWorkItem() throws Exception{
		
		SystemWorkItem comment = new SystemWorkItem();
		comment.processManager = processManager;
		comment.session = session;
		comment.getMetaworksContext().setWhen(MetaworksContext.WHEN_NEW);
		if(MetaworksContext.WHEN_NEW.equals(this.getMetaworksContext().getWhen()))
			comment.setSystemMessage(session.getUser().getName() + "님이 주제 " + this.getTopicTitle() + "를 등록하였습니다.");
		else{
			comment.setSystemMessage(session.getUser().getName() + "님이 주제 " + this.getTopicTitle() + "로 변경하였습니다.");
		}
		
		
		comment.add();
		
		Instance instance = new Instance();
		instance.setInstId(comment.getInstId());
		instance.copyFrom(instance.databaseMe());
		
		return instance;
	}
	
	@Face(displayName="$Save")
	@Available(when={MetaworksContext.WHEN_EDIT})
	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_APPEND, validate=true)
	public Object[] modify() throws Exception{
		this.saveMe();
		
//		TopicNode topicNode = new TopicNode();
//		topicNode.setId(this.getTopicId());
//		topicNode.setName(this.getTopicTitle());
//		topicNode.setType(TopicNode.TOPIC);
		
//		TopicPanel topicMap = new TopicPanel();
//		topicMap.session = session;
//		topicMap.load();
		
		/*
		TopicInfo topicInfo = new TopicInfo(session);
		topicInfo.setId(this.getTopicId());
		topicInfo.load();new Refresh(topicInfo),
		*/
		
	//	this.notiToCompany();
		
		Locale locale = new Locale(session);
		locale.load();
		
		InstanceListPanel instanceListPanel = Perspective.loadInstanceList(session, Perspective.MODE_TOPIC, Perspective.TYPE_NEWSFEED, getTopicId());

		String title = locale.getString("$Topic") + " - " + this.getTopicTitle();
		session.setWindowTitle(title);
		
		ListPanel listPanel = new ListPanel(instanceListPanel, new TopicInfo(session, this.getTopicId()));
		
		TopicInfo topicInfo = new TopicInfo(session, session.getLastPerspecteType());
		topicInfo.load();
		
		return new Object[]{new Refresh(session),
							new Refresh(listPanel),
				 			new ToEvent(new TopicPerspective(), EventContext.EVENT_CHANGE),
				 			new ToEvent(ServiceMethodContext.TARGET_SELF, EventContext.EVENT_CLOSE)};
		
		
//		return new Object[]{new ToEvent(new TopicPerspective(), EventContext.EVENT_CHANGE), new ToEvent(ServiceMethodContext.TARGET_OPENER, EventContext.EVENT_CHANGE), new ToEvent(ServiceMethodContext.TARGET_SELF, EventContext.EVENT_CLOSE)};		
	}
	
	@Face(displayName="$Close")
	@Available(how={"html"})
	@ServiceMethod(target=ServiceMethodContext.TARGET_APPEND)
	public Object close() {
		return new Remover(new ModalWindow());
	}
	
	@AutowiredFromClient
	transient public Session session;
	
	@Autowired
	public ProcessManagerRemote processManager;
}
