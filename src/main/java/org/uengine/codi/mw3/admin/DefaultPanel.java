package org.uengine.codi.mw3.admin;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.AutowiredFromClient;
import org.uengine.codi.mw3.model.Session;

public class DefaultPanel implements ContextAware {
//	String panelId;
	Object content;

//	@Id
//	public String getPanelId() {
//		return panelId;
//	}
//
//	public void setPanelId(String panelId) {
//		this.panelId = panelId;
//	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	@AutowiredFromClient
	public Session session;

	MetaworksContext context;

	@Override
	public MetaworksContext getMetaworksContext() {
		if (context == null) {
			context = createNewContext();
		}
		return context;
	}

	private MetaworksContext createNewContext() {
		MetaworksContext metaworksContext = new MetaworksContext();
		metaworksContext.setHow(MetaworksContext.HOW_EVER);
		metaworksContext.setWhen(MetaworksContext.WHEN_EVER);
		metaworksContext.setWhere(MetaworksContext.WHERE_EVER);
		return metaworksContext;
	}

	@Override
	public void setMetaworksContext(MetaworksContext arg0) {
		this.context = arg0;
	}

	public DefaultPanel() {
		super();
		init();
	}

	public void init() {
		// Override and Do Initiate Panel.
	}
}
