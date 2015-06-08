package org.uengine.codi.mw3.model;

import java.util.Date;

import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.Available;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.NonLoadable;
import org.metaworks.annotation.NonSavable;
import org.metaworks.annotation.ORMapping;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.annotation.Table;
import org.metaworks.annotation.Test;
import org.metaworks.dao.IDAO;
import org.metaworks.widget.ModalWindow;
import org.uengine.codi.mw3.common.MainPanel;

@Face(
		ejsPathMappingByContext=
	{
		"{where: 'pinterest', face: 'dwr/metaworks/org/uengine/codi/mw3/model/IInstance_pinterest.ejs'}",
		"{how: '" + Perspective.TYPE_COMMINGTODO + "', face: 'dwr/metaworks/org/uengine/codi/mw3/model/IInstance_commingTodo.ejs'}",
		"{how: 'action', face: 'dwr/metaworks/org/uengine/codi/mw3/model/IInstance_action.ejs'}",
		"{where: 'dashboard', face: 'dwr/metaworks/org/uengine/oce/dashboard/IInstance_dashboard.ejs'}",
		"{where: 'instancelist', face: 'dwr/metaworks/org/uengine/codi/mw3/model/IInstance.ejs'}",
	}//,
	//ejsPathForArray="dwr/metaworks/genericfaces/ListFace.ejs"
)
@Table(name="bpm_procinst")
public interface IInstance extends IDAO{
	
	public final static String WHERE_INSTANCELIST = "instancelist";

	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_POPUP)
	public Object view() throws Exception;
	
	public InstanceViewContent detail() throws Exception;

	@ServiceMethod(target="popup")
	public ModalWindow popupDetail() throws Exception;

	@Id
	public Long getInstId();
	public void setInstId(Long id);

	public String getDefVerId();
	public void setDefVerId(String defVerId);

	public String getDefId();
	public void setDefId(String defId);

	public String getDefPath();
	public void setDefPath(String DefPath);

	public String getDefName();
	public void setDefName(String defName);

	public String getLastCmnt();
	public void setLastCmnt(String lastComment);

	public String getLastCmnt2();
	public void setLastCmnt2(String lastComment2);

	public Date getStartedDate();
	public void setStartedDate(Date when);

	public Date getFinishedDate();
	public void setFinishedDate(Date when);

	public Date getDueDate();
	public void setDueDate(Date when);

	public Date getDefModDate();
	public void setDefModDate(Date when);

	public Date getModDate();
	public void setModDate(Date when);

	public String getStatus();
	public void setStatus(String status);

	public String getInfo();
	public void setInfo(String info);

	public String getName();
	public void setName(String name);
	
	public boolean getIsDeleted();
	public void setIsDeleted(boolean isDeleted);
	
	public boolean getIsAdhoc();
	public void setIsAdhoc(boolean isAdhoc);

	public boolean getIsDocument();
	public void setIsDocument(boolean isDocument);

	
	public boolean getIsSubProcess();
	public void setIsSubProcess(boolean isSubProcess);
	
	public Long getRootInstId();
	public void setRootInstId(Long instanceId);
	
	public abstract Long getMainInstId();
	public abstract void setMainInstId(Long instanceId);
	
	public abstract String getMainActTrcTag();
	public abstract void setMainActTrcTag(String value);	

	public abstract String getMainExecScope();
	public abstract void setMainExecScope(String value);	

	public abstract Long getMainDefVerId();
	public abstract void setMainDefVerId(Long defVerId);
	
	public boolean getIsArchive();
	public void setIsArchive(boolean isArchive);
	
	public boolean getIsFileAdded();
	public void setIsFileAdded(boolean isFileAdded);
	
	public abstract String getAbsTrcPath();
	public abstract void setAbsTrcPath(String value);

	public boolean getDontReturn();
	public void setDontReturn(boolean value);
	
	public Number getStrategyId();
	public void setStrategyId(Number strategyId);
	
	public String getInitEp();
	public void setInitEp(String initEp);
	
	public String getInitComCd();
	public void setInitComCd(String initComCd);

	public String getSecuopt();
	public void setSecuopt(String secuopt);
	
	public String getAssignee();
	public void setAssignee(String assignee);

	public boolean isInitCmpl();
	public void setInitCmpl(boolean initCmpl);
	
	public String getTopicId();
	public void setTopicId(String topicId);
	
	public String getProgress();
	public void setProgress(String progress);
	
	public int getBenefit();
	public void setBenefit(int benefit);
	
	public int getPenalty();
	public void setPenalty(int penalty);
	
	public int getEffort();
	public void setEffort(int effort);
	
	public Long getLastcmntTaskId();
	public void setLastcmntTaskId(Long lastcmntTaskId);

	public Long getLastcmnt2TaskId();
	public void setLastcmnt2TaskId(Long lastcmnt2TaskId);
	
	@NonSavable
	public String getTopicName();
	public void setTopicName(String topicName);
	
	@NonLoadable
	@NonSavable
	public InstanceViewThreadPanel getInstanceViewThreadPanel();
	public void setInstanceViewThreadPanel(InstanceViewThreadPanel instanceViewThreadPanel);
	
	@NonSavable
	@ORMapping(
			objectFields={ "instanceId" , "status" }, 
			databaseFields={ "instId" , "status" })
	public InstanceTooltip getInstanceTooltip();
	public void setInstanceTooltip(InstanceTooltip instanceTooltip);
	
	@ORMapping(objectFields="instanceId", databaseFields="instId")
	public InstanceDrag getInstanceDrag();
	public void setInstanceDrag(InstanceDrag instanceDrag);
	
	/////// following setter/getters stands for mapping tuple data to object by bean mapping /////////
	
	@ORMapping(
			objectFields = { "userId", "name"},
			databaseFields = { "initEp", "initRsNm" } 
	)
	public IUser getInitiator();
	public void setInitiator(IUser user);
	
	@ORMapping(
			objectFields = { "userId", "name"},
			databaseFields = { "currEp", "currRsNm" } 
	)
	public IUser getCurrentUser();
	public void setCurrentUser(IUser user);
	
	@ORMapping(
			objectFields = { "userId", "name"},
			databaseFields = { "lastCmntEp", "lastCmntRsnm"} 
			)
	public IUser getLastCmntUser();
	public void setLastCmntUser(IUser lastCmntUser);
	
	@ORMapping(
			objectFields = { "userId", "name"},
			databaseFields = { "lastCmnt2Ep", "lastCmnt2Rsnm" } 
			)
	public IUser getLastCmnt2User();
	public void setLastCmnt2User(IUser lastCmnt2User);

	public String getExt1();
	public void setExt1(String ext1);

	/*
	 * 2013-01-10 cjw
	 * push client 의 보안 처리
	 */
	@Hidden
	@NonLoadable
	@NonSavable
	public Followers getFollowers();
	public void setFollowers(Followers followers);
	
	@ServiceMethod(inContextMenu=true, callByContent=true, keyBinding="Ctrl+X")
	@Available(how="instance")
	@Face(displayName="$Cut")
	public Session cut();
	
	@ServiceMethod(inContextMenu=true, callByContent=true, needToConfirm=true, target="popup"/*, mouseBinding="drop"*/, keyBinding="Ctrl+V")
	@Available(how="instance")
	@Face(displayName="$Paste")
	public Object[] paste() throws Exception;
	
	@ServiceMethod(inContextMenu=true, callByContent=true)
	@Available(how="instance")
	@Face(displayName="$AddTrayBar")
	public Object[] addTrayBar() throws Exception;

	@ServiceMethod(inContextMenu=true, callByContent=true, needToConfirm=true)
	@Available(when="instanceNavigator")
	@Face(displayName="$SplitFromRootProcess")
	public void split() throws Exception;
	
	public void fillFollower();
	
	@ServiceMethod(callByContent=true)
	public MainPanel goSns() throws Exception;
	
	public boolean checkAuth() throws Exception;

	@ServiceMethod(callByContent=true)
	public Object[] loadTopic() throws Exception;
	/*
	@ServiceMethod(payload={"instId"}, target=ServiceMethodContext.TARGET_POPUP)
	public ModalWindow monitor() throws Exception;
	
	@ServiceMethod(payload={"instId", "initEp"}, target=ServiceMethodContext.TARGET_POPUP)
	public Popup schedule() throws Exception;
	
	@ServiceMethod(payload={"instId"}, target=ServiceMethodContext.TARGET_POPUP)
	public Popup newSubInstance() throws Exception;

	@ServiceMethod(payload={"instId", "initEp"}, needToConfirm=true, target=ServiceMethodContext.TARGET_APPEND)
	public Object[] remove() throws Exception;
	
	@ServiceMethod(payload={"instId", "secuopt"}, target=ServiceMethodContext.TARGET_SELF)
	public void toggleSecurityConversation() throws Exception;
	
	@ServiceMethod(payload={"instId", "status"}, target=ServiceMethodContext.TARGET_SELF)
	public void complete() throws Exception;
	*/
}
