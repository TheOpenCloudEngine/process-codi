package org.uengine.codi.mw3.model;

import java.io.File;
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
import org.uengine.codi.mw3.CodiClassLoader;
import org.uengine.kernel.GlobalContext;

public class TemplateDesigner {
	
	public TemplateDesigner(){}
	
	public TemplateDesigner(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		init(className);
	}

	private void init(String className) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		designee = Thread.currentThread().getContextClassLoader().loadClass(className).newInstance();
//		preview = new TemplateDesignerPreview();
//		preview.setDesignee(designee);
	}
	
	TemplateDesignerPreview preview;
		
		public TemplateDesignerPreview getPreview() {
			return preview;
		}
	
		public void setPreview(TemplateDesignerPreview preview) {
			this.preview = preview;
		}

	Object designee;
		public Object getDesignee() {
			return designee;
		}
		public void setDesignee(Object designee) {
			this.designee = designee;
		}
	
	@ServiceMethod(callByContent=true)
	public void save() throws Exception{
		
		String className = designee.getClass().getName();
		//WebObjectType webObjectType = MetaworksRemoteService.getInstance().getMetaworksType(designee.getClass().getName());
		
        CodiClassLoader contextClassLoader = CodiClassLoader.getMyClassLoader();
		String myCodeBase = ((CodiClassLoader)contextClassLoader).getCodebase();
		
        if(myCodeBase==null)
        	throw new Exception("소셜코딩을 환영합니다! 소스코드를 수정하려면 먼저 페이스북 로그인을 하신후 체크아웃(checkout)을 받으셔야 합니다.");
        	
        File myCodeBaseRoot = new File(myCodeBase).getParentFile(); //project folder is one level parent folder than 'src'
        
        if (!myCodeBaseRoot.exists()) {
        	throw new Exception("소셜코딩을 환영합니다! 소스코드를 수정하려면 체크아웃(checkout)을 먼저 하십시오.");
        }
		
		GlobalContext.serialize(designee, new FileOutputStream(myCodeBase + className + ".xml"), String.class);
		
		MetaworksRemoteService.getInstance().clearMetaworksType(className);

		init(className);
		
		preview = new TemplateDesignerPreview();
		preview.setDesignee(designee);


	}

}
