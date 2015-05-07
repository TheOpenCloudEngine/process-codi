package org.uengine.codi.mw3.model;

import org.metaworks.widget.layout.Layout;

public class ProcessApp extends Application{
	
	public ProcessApp(){
		
	}

	Layout layout;
		public Layout getLayout() {
			return layout;
		}
		public void setLayout(Layout layout) {
			this.layout = layout;
		}
		
	public ProcessApp(Session session, String appName, String defId) throws Exception{
		
		Locale locale = new Locale(session);
		locale.load();

		String title = locale.getString("$Process") + " - " + appName;
		session.setWindowTitle(title);
		
		InstanceListPanel instanceListPanel = Perspective.loadInstanceList(session, Perspective.MODE_PROCESS, Perspective.TYPE_NEWSFEED, defId);
		ListPanel listPanel = new ListPanel();
		listPanel.setPerspectiveInfo(new ProcessInfo(session));
		listPanel.setInstanceListPanel(instanceListPanel);
		
		ListWindow listWindow = new ListWindow();
		listWindow.setPanel(listPanel);

		ContentWindow contentWindow = new ContentWindow(); 
		
		Layout layout = new Layout();
		layout.setWest(listWindow);
		layout.setCenter(contentWindow);
		layout.setOptions("togglerLength_open:0, spacing_open:0, spacing_closed:0, south__spacing_open:5, west__spacing_open:5, west__size:'40%'");
		layout.setName("app");
		layout.setUseHideBar(false);
		
		setLayout(layout);
	}
}
