package org.uengine.codi.mw3.model;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.Available;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Order;
import org.uengine.codi.mw3.admin.WindowPanel;

@Face(ejsPath="dwr/metaworks/genericfaces/CleanObjectFace.ejs")
public class TopCenterPanel implements ContextAware {
	
	public final static String HOW_TRAY = "tray";
	public final static String HOW_MENU = "menu";
	
	public Session session;
	
	MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}

	Tray tray;
		@Order(value=2)
		@Available(how=HOW_TRAY)
		public Tray getTray() {
			return tray;
		}
		public void setTray(Tray tray) {
			this.tray = tray;
		}
	
	WindowPanel windowPanel;
		@Order(value=1)
		@Available(how=HOW_TRAY)
		public WindowPanel getWindowPanel() {
			return windowPanel;
		}	
		public void setWindowPanel(WindowPanel windowPanel) {
			this.windowPanel = windowPanel;
		}

	public TopCenterPanel(){
		this(null);
	}
	
	public TopCenterPanel(String contextHow){
		this.setMetaworksContext(new MetaworksContext());
		this.getMetaworksContext().setHow(contextHow);
	}
	
	public void load() throws Exception {
			Tray tray = new Tray();
			tray.session = session;
			tray.load();
			
			this.setTray(tray);
			this.setWindowPanel(new WindowPanel());

	}
}
