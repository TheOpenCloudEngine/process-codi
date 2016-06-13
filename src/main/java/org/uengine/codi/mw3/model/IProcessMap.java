package org.uengine.codi.mw3.model;

import org.metaworks.MetaworksContext;
import org.metaworks.Remover;
import org.metaworks.ServiceMethodContext;
import org.metaworks.ToEvent;
import org.metaworks.annotation.*;
import org.metaworks.dao.IDAO;
import org.metaworks.website.MetaworksFile;

@Face(ejsPathForArray="dwr/metaworks/genericfaces/ListFace.ejs")//, ejsPathMappingByContext = "{when:'', face:''}")
public interface IProcessMap extends IDAO {


	@Id
	public String getMapId();

	public void setMapId(String mapId);

	public String getDefId();

	public void setDefId(String defId);

	@Name
	public String getName();

	public void setName(String name);

	public String getCmPhrase();

	public void setCmPhrase(String cmPhrase);

	public String getCmTrgr();

	public void setCmTrgr(String cmTrgr);

	public int getNo();

	public void setNo(int no);

	public String getComCode();

	public void setComCode(String comCode);

	public boolean getIsScheduled();

	public void setIsScheduled(boolean isScheduled);

	@NonSavable
	@NonLoadable
	public RoleMappingPanel getRoleMappingPanel();

	public void setRoleMappingPanel(RoleMappingPanel roleMappingPanel);

	@ORMapping(databaseFields = {"iconPath"}, objectFields = {"uploadedPath"})
	public MetaworksFile getIconFile();

	public void setIconFile(MetaworksFile iconFile);

	@ORMapping(databaseFields = {"color"}, objectFields = {"value"})
	public ProcessMapColor getIconColor();

	public void setIconColor(ProcessMapColor color);

	@ServiceMethod(callByContent = true, when = WHEN_VIEW, target = ServiceMethodContext.TARGET_POPUP)
	public Popup modify() throws Exception;

	@ServiceMethod(callByContent = true, when = WHEN_VIEW)
	public Object[] remove() throws Exception;

	@ServiceMethod(callByContent = true, when = WHEN_EDIT, target = ServiceMethodContext.TARGET_APPEND)
	@Face(displayName = "$Run")
	public Object[] save() throws Exception;

	@Available(when = {WHEN_NEW})
	@ServiceMethod
	Remover close() throws Exception;

	@ServiceMethod(callByContent = true, target = ServiceMethodContext.TARGET_POPUP)
	@Test(scenario = "first", starter = true, instruction = "$first.ProcessStart", next = "autowiredObject.org.uengine.codi.mw3.model.CommentWorkItem@-1.add()")
//	@Test(scenario="first", starter=true, instruction="Issue Tracking 프로세스를 선택합니다.", next="autowiredObject.org.uengine.codi.mw3.admin.PageNavigator.goKnowledge()")
	public Object[] initiate() throws Exception;

	@ServiceMethod(callByContent = true)
	public Object[] processListFilter() throws Exception;

	public void createMe() throws Exception;

	public void saveMe() throws Exception;

	@ServiceMethod(callByContent = true, when = WHEN_VIEW)//, target = ServiceMethodContext.TARGET_POPUP)
	public Object startWithRoleMapping() throws Exception;


	@ServiceMethod(onLoad = true, inContextMenu = true, target = ServiceMethod.TARGET_SELF, callByContent = true, when= MetaworksContext.WHEN_VIEW)
	public void loadPreviewer() throws Exception;

}