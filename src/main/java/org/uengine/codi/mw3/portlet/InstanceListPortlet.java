package org.uengine.codi.mw3.portlet;

import org.metaworks.MetaworksException;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.widget.Window;
import org.uengine.codi.mw3.Login;
import org.uengine.codi.mw3.model.Locale;
import org.uengine.codi.mw3.model.PersonalPerspective;
import org.uengine.codi.mw3.model.Perspective;
import org.uengine.codi.mw3.model.Session;

public class InstanceListPortlet extends Window {
	Session session;
		public Session getSession() {
			return session;
		}
		public void setSession(Session session) {
			this.session = session;
		}
		
	Object content;
		public Object getContent() {
			return content;
		}
		public void setContent(Object content) {
			this.content = content;
		}

	boolean loaded;
		@Hidden
		public boolean isLoaded() {
			return loaded;
		}
		public void setLoaded(boolean loaded) {
			this.loaded = loaded;
		}

	String cookieUserId;
		@Hidden
		public String getCookieUserId() {
			return cookieUserId;
		}
		public void setCookieUserId(String cookieUserId) {
			this.cookieUserId = cookieUserId;
		}
	String cookiePasswd;
		@Hidden
		public String getCookiePasswd() {
			return cookiePasswd;
		}
		public void setCookiePasswd(String cookiePasswd) {
			this.cookiePasswd = cookiePasswd;
		}
		
	String type;
		@Hidden
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		
	String topicName;

		@Hidden
		public String getTopicName() {
			return topicName;
		}
		public void setTopicName(String topicName) {
			this.topicName = topicName;
		}
		
	@Hidden
	@ServiceMethod(callByContent=true)
	public Object[] load() throws Exception {
		Login login = new Login();
		login.setEmail("test");
		login.setPassword("test");
		
/*		if( getCookiePasswd() == null || getCookieUserId() == null){
			throw new Exception("로그인정보가 없습니다.");
		}
		login.setUserId(getCookieUserId());
		login.setPassword(getCookiePasswd());*/
		
		Session session = login.loginService();
		session.getMetaworksContext().setWhen("hidden");
		session.setLastPerspecteType("sns");
		session.getEmployee().setPreferUX("sns");
		this.setSession(session);
		
		Object[] result = null;
		
		if(this.getType() == null || this.getType().length() == 0)
			throw new MetaworksException("잘못된 경로로 접근하셨습니다.");
			
		if("Newspeed".equals(this.getType()))
			result = goNewspeed();
		else if("Todo".equals(this.getType()))
			result = goNewspeed();
		else if("Topic".equals(this.getType()))
			result = goTopic();
		
		this.setSession((Session)result[0]);		
		this.setLoaded(true);
		
		if(result != null)
			this.setContent(result[1]);
		
		Locale locale = new Locale();
		locale.setLanguage(session.getEmployee().getLocale());
		locale.load();
		
		return new Object[]{locale, this};
	}
	
	@ServiceMethod(payload={"session"})
	public Object[] goNewspeed() throws Exception {
		System.out.println("goNewspeed");
		PersonalPerspective personalPerspective = new PersonalPerspective();
		personalPerspective.session = session;
		
		return personalPerspective.loadAllICanSee();
	}
	
	@ServiceMethod(payload={"session"})
	public Object[] goTodo() throws Exception {
		System.out.println("goTodo");
		
		PersonalPerspective personalPerspective = new PersonalPerspective();
		personalPerspective.session = session;
		
		return personalPerspective.loadInbox();
	}
	
	@ServiceMethod(payload={"session", "topicName"})
	public Object[] goTopic() throws Exception {
		if(this.getTopicName() == null || this.getTopicName().length() ==0)
			throw new MetaworksException("잘못된 경로로 접근하셨습니다.");
		
		/*
		TopicNode topic = new TopicNode();
				
		try{
			topic.setId(this.getTopicName());
			topic.copyFrom(topic.databaseMe());
		}catch(Exception e){
			throw new MetaworksException("잘못된 경로로 접근하셨습니다.");
		}
		*/
		
		return PersonalPerspective.loadInstanceListPanel(session, Perspective.MODE_TOPIC, Perspective.TYPE_NEWSFEED, this.getTopicName());
	}
}
