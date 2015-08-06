package org.uengine.codi.mw3.model;

import org.metaworks.EventContext;
import org.metaworks.Refresh;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dwr.MetaworksRemoteService;
import org.uengine.codi.mw3.Login;
import org.uengine.codi.mw3.calendar.ScheduleCalendar;
import org.uengine.kernel.GlobalContext;

public class Perspective {
	
	public final static String MODE_PERSONAL 	= "personal";
	public final static String MODE_TOPIC 		= "topic";
	public final static String MODE_DEPT 		= "dept";
	public final static String MODE_ROLE 		= "role";
	public final static String MODE_PROCESS 	= "process";
	public final static String MODE_PROJECT		= "project";
	public final static String MODE_COMPETITION	= "compete";
	
	public final static String TYPE_NEWSFEED 	= "allICanSee";
	public final static String TYPE_FOLLOWING   = "following"; 
	public final static String TYPE_INBOX 		= "inbox";
	public final static String TYPE_STARTEDBYME     = "request";
	public final static String TYPE_CALENDAR	= "calendar";
	
	public final static String USE_PERSONAL = GlobalContext.getPropertyString("personal.use", "1");
	public final static String USE_PROJECT = GlobalContext.getPropertyString("project.use", "1");
	public final static String USE_TOPIC = GlobalContext.getPropertyString("topic.use", "1");
	public final static String USE_COMPETITION = GlobalContext.getPropertyString("competition.use","1");
	public final static String USE_ORGANIZATION = GlobalContext.getPropertyString("organization.use", "0");
	public final static String USE_PROCESS = GlobalContext.getPropertyString("process.use", "1");
	public final static String USE_ROLE = GlobalContext.getPropertyString("role.use", "1");
	public final static String USE_APP = GlobalContext.getPropertyString("app.use", "0");
	public final static String USE_CONTACT = GlobalContext.getPropertyString("contact.use", "1");
	public final static String USE_COMMINGTODO = GlobalContext.getPropertyString("commingTodo.use", "0");
	public final static String USE_PERSPECTIVE_KNOWLEDGE = GlobalContext.getPropertyString("perspective.knowledge.use", "0");
	public final static String USE_DOCUMENT = GlobalContext.getPropertyString("document.use", "0");
	public final static String USE_GROUP		= GlobalContext.getPropertyString("group.use", "1");
	public final static String USE_VALUECHAIN		= GlobalContext.getPropertyString("valuechain.use", "0");
	
	public final static String TYPE_COMMINGTODO = "commingTodo";
	
	String label;
		@Id
		public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			this.label = label;
		}

	Object child;
		public Object getChild() {
			return child;
		}
		public void setChild(Object child) {
			this.child = child;
		}
	
	boolean loaded;
		public boolean isLoaded() {
			return loaded;
		}
		public void setLoaded(boolean loaded) {
			this.loaded = loaded;
		}
		
	boolean loader;
		public boolean isLoader() {
			return loader;
		}
		public void setLoader(boolean loader) {
			this.loader = loader;
		}

	public Perspective(){
		
	}
	
	protected void loadChildren() throws Exception {
		this.setLoaded(true);
	}

	@ServiceMethod(callByContent=true, except="child", eventBinding=EventContext.EVENT_CHANGE)
	public void refresh() throws Exception {
		this.loadChildren();
	}
	
	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_STICK)
	public Popup popupAdd() throws Exception{
		throw new Exception("not defined popupAdd() method");
	}

	public Object[] loadInstanceListPanel(String perspectiveMode, String perspectiveType,
			String selectedItem) throws Exception {
		return loadInstanceListPanel(session, perspectiveMode, perspectiveType, selectedItem);
	}
	
	public static Object[] loadInstanceListPanel(Session session, String perspectiveMode, String perspectiveType,
			String selectedItem) throws Exception {
		return loadInstanceListPanel(session, perspectiveMode, perspectiveType, selectedItem, null);
	}
	
	public static void pushTodoBadge(Session session) throws Exception {
		/* 내가 할일 카운트 다시 계산 */
		TodoBadge todoBadge = new TodoBadge();
		todoBadge.session = session;
		todoBadge.refresh();

		MetaworksRemoteService.pushTargetClientObjects(Login.getSessionIdWithUserId(session.getUser().getUserId()), new Object[]{new Refresh(todoBadge, true)});			
	}
	
	public static InstanceListPanel loadInstanceList(Session session, String perspectiveMode, String perspectiveType) throws Exception {
		return Perspective.loadInstanceList(session, perspectiveMode, perspectiveType, null);
	}
	
	public static InstanceListPanel loadInstanceList(Session session, String perspectiveMode, String perspectiveType, String selectedItem) throws Exception {
	
		savePerspectiveToSession(session, perspectiveMode, perspectiveType, selectedItem);

		InstanceListPanel instListPanel = new InstanceListPanel();
		
		String title = null;
		if(MODE_PERSONAL.equals(perspectiveMode) && session.getEmployee().getEmpCode().equals(selectedItem)){
			title = "$perspective." + perspectiveType;
			
			session.setWindowTitle(title);
		}

		if(TYPE_CALENDAR.equals(perspectiveType)){
			ScheduleCalendar scheduleCalendar = new ScheduleCalendar();
			scheduleCalendar.session = session;
			scheduleCalendar.load();
			
			instListPanel.setScheduleCalendar(scheduleCalendar);
		}else{
			InstanceList instList = new InstanceList(session);
			instList.load();

			instListPanel.setInstanceList(instList);
		}
		
		return instListPanel;
	}
	
	public static Object[] loadInstanceListPanel(Session session, String perspectiveMode, String perspectiveType,
			String selectedItem, String title) throws Exception {
		
		/*
		try {
			if(perspectiveType.equals(session.getLastPerspecteType()) != true) {
				System.out.println("clear text in the search box because perspectiveType is not equal");
				session.setSearchKeyword(null);
	
			} else if(selectedItem.equals(session.getLastSelectedItem()) != true){
				System.out.println("clear text in the search box because selectItem is not equal");
				session.setSearchKeyword(null);
			}
			else {
				System.out.println("remain text in the search box");
			}
		} catch (Exception e) {
			System.out.println("Do nothing");
		}
		*/
		
		InstanceListPanel instListPanel = Perspective.loadInstanceList(session, perspectiveMode, perspectiveType, selectedItem); 
		
		session.setWindowTitle(title);
		
		InstanceSearchBox searchBox = new InstanceSearchBox();
		searchBox.setKeyword(session.getSearchKeyword());
		searchBox.setKeyUpSearch(true);
		searchBox.setKeyEntetSearch(true);
			
		if(perspectiveType.equals("inbox")){
			Perspective.pushTodoBadge(session);
		}

		ListPanel listPanel = new ListPanel();
		listPanel.setInstanceListPanel(instListPanel);

		return new Object[] {session, listPanel};
	}

	private static void savePerspectiveToSession(Session session, String perspectiveMode, String perspectiveType, String selectedItem) {
		session.setLastPerspecteMode(perspectiveMode);
		session.setLastPerspecteType(perspectiveType);
		session.setLastSelectedItem(selectedItem);
	}

	@AutowiredFromClient
	public Session session;
}
