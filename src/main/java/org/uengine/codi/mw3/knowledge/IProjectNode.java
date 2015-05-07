package org.uengine.codi.mw3.knowledge;

import org.metaworks.annotation.ServiceMethod;
import org.uengine.codi.mw3.model.Application;

public interface IProjectNode extends ITopicNode {
	
	public final static String HOW_PERSPECTIVE = "perspective";
	
	public String getProjectAlias();
	public void setProjectAlias(String projectAlias);
	
	@ServiceMethod(callByContent=true, payload="name")
	public IProjectNode findById() throws Exception;
	
	@ServiceMethod(payload={"id", "name", "projectAlias"})
	public Object[] goIDE() throws Exception;
}
