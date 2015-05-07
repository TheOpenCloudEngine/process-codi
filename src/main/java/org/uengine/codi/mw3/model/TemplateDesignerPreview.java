package org.uengine.codi.mw3.model;

import java.io.FileOutputStream;
import java.util.Collection;

import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.proxy.dwr.Util;
import org.metaworks.WebObjectType;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dwr.MetaworksRemoteService;
import org.metaworks.widget.layout.Layout;
import org.uengine.kernel.GlobalContext;

public class TemplateDesignerPreview {
	
	public TemplateDesignerPreview(){}
	
	public TemplateDesignerPreview(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		designee = Thread.currentThread().getContextClassLoader().loadClass(className).newInstance();
	}
	
	Object designee;
		public Object getDesignee() {
			return designee;
		}
		public void setDesignee(Object designee) {
			this.designee = designee;
		}

}
