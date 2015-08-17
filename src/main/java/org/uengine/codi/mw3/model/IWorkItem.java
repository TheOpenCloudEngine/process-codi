package org.uengine.codi.mw3.model;

import java.util.Date;

import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.Default;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.NonLoadable;
import org.metaworks.annotation.NonSavable;
import org.metaworks.annotation.ORMapping;
import org.metaworks.annotation.Range;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.annotation.Table;
import org.metaworks.annotation.Test;
import org.metaworks.annotation.TypeSelector;
import org.metaworks.dao.IDAO;
import org.metaworks.example.ide.SourceCode;
import org.metaworks.website.MetaworksFile;
import org.metaworks.widget.IFrame;
import org.metaworks.widget.ModalWindow;
import org.uengine.bean.factory.MetaworksSpringBeanFactory;
import org.uengine.codi.mw3.admin.WebEditor;
@Table(name = "bpm_worklist")
@Face(
		ejsPath="dwr/metaworks/org/uengine/codi/mw3/model/IWorkItem.ejs",
		ejsPathForArray="dwr/metaworks/org/uengine/codi/mw3/model/IWorkItem_array.ejs",
		ejsPathMappingByContext=
	{
		"{when: 'new', face: 'dwr/metaworks/org/uengine/codi/mw3/model/IWorkItem_edit.ejs'}",
		"{when: 'edit', face: 'dwr/metaworks/org/uengine/codi/mw3/model/IWorkItem_edit.ejs'}",
	}		

)
public interface IWorkItem extends IDAO{

		public final static String WORKITEM_STATUS_FEED		= "FEED";
		public final static String WORKITEM_STATUS_DRAFT	= "DRAFT";
		public final static String WORKITEM_STATUS_NEW 		= "NEW";
		public final static String WORKITEM_STATUS_CONFIRMED= "CONFIRMED";
		public final static String WORKITEM_STATUS_RESERVED	= "RESERVED";
		public final static String WORKITEM_STATUS_REFERENCE= "REFERENCE";
		public final static String WORKITEM_STATUS_COMPLETED= "COMPLETED";
		public final static String WORKITEM_STATUS_CANCELLED= "CANCELLED";
		public final static String WORKITEM_STATUS_SUSPENDED= "SUSPENDED";
		//When user delegate a workitem, the workitem's status whould be changed to this status and another workitem.	
		public final static String WORKITEM_STATUS_DELEGATED= "DELEGATED"; 
		public final static String WORKITEM_STATUS_RUNNING = "Running";
		
		
		public final static String WORKITEM_TYPE_GENERIC = "generic";

		public final static String WORKITEM_TYPE_COMMENT	 = "comment";
		public final static String WORKITEM_TYPE_OVRYCMNT	 = "ovryCmnt";
		public final static String WORKITEM_TYPE_MEMO		 = "memo";
		public final static String WORKITEM_TYPE_FILE		 = "file";
		public final static String WORKITEM_TYPE_SRC		 = "src";
		public final static String WORKITEM_TYPE_DOCUMENT = "document";
		public final static String WORKITEM_TYPE_GOOGLEDOC = "googleDoc";
		
		public final static String WORKITEM_TYPE_REMOTECONF	 = "remoteConf";
		public final static String WORKITEM_TYPE_DOCUMENTLIST = "documentList";
		
		public final static String WORKITEM_TYPE_SYSTEM = "system";		
		
		public final static int TITLE_LIMIT_SIZE             = 200;
		public final static int MEMO_TITLE_LIMIT_SIZE             = 40;
		public final static int LASTCMT_LIMIT_SIZE             = 200;
		
		public final static String WHERE_WORKLIST				= "worklist";
		
		@Id
		//@GeneratedValue(strategy=GenerationType.TABLE, generator="SEQ_BPM")
		public Long getTaskId();
		public void setTaskId(Long taskId);
		
		public String getTrcTag();
		public void setTrcTag(String trcTag);
		
		
		public int getMajorVer();
		public void setMajorVer(int majorVer);

		public int getMinorVer();
		public void setMinorVer(int minorVer);
		
		public Long getPrtTskId();
		public void setPrtTskId(Long prtTskId);

		public Long getGrpTaskId();
		public void setGrpTaskId(Long grpTaskId);
		
		@Hidden
		public boolean getIsDeleted();
		public void setIsDeleted(boolean deleted);
	
		public String getTitle();
		public void setTitle(String title);
		
		public String getFolderId();
		public void setFolderId(String folderId);
		
		@Hidden
		public String getContent();
		public void setContent(String content);

		@Hidden
		public String getExtFile();
		public void setExtFile(String extFile);

		@Hidden
		public String getExecScope();
		public void setExecScope(String execScope);

		@Hidden
		@ORMapping(
			databaseFields = { "title", "content" },
			objectFields = { "id", "code" },
			objectIsNullWhenFirstDBFieldIsNull = true,
			availableWhen= "type=='src'")
		public SourceCode getSourceCode();
		public void setSourceCode(SourceCode sourceCode);
		
		@Hidden
		@ORMapping(
			databaseFields = { "content", "instId", "taskId", "trcTag" , "status" }, 
			objectFields = { "serializedTool", "instanceId", "taskId", "tracingTag" , "status" },
			objectIsNullWhenFirstDBFieldIsNull = true,
			availableWhen= {"type=='generic'", "type=='document'"}
		)
		public GenericWorkItemHandler getGenericWorkItemHandler();
		public void setGenericWorkItemHandler(
				GenericWorkItemHandler genericWorkItemHandler);
		
		/*
		@ORMapping(
			objectFields={"taskId","instId","content","extFile","tool","startDate","endDate"},
			databaseFields={"taskId","instId","content","extFile","tool","startDate","endDate"},
			availableWhen= "type=='file'"
			)
		public DocumentDrag getDocumentDrag();
		public void setDocumentDrag(DocumentDrag documentDrag);
		*/
		
		@Hidden
		@Range(
				options={"WorkItem","document", "googleDoc", "Comment",	"Image",	"Movie",	"Source Code", 	"File", "Schedule", "Postings", "ovryCmnt"},
				values ={"wih", "document",	"googleDoc", "comment",	"img",		"mov",		"src", 			"file", "schedule" , "postings", "ovryCmnt"}
		)
		@Default(value="process")
		@TypeSelector(
				values = 		{ 
						"wih",
						"document",
						"googleDoc",
						"comment",
						"img",		
						"mov", 				
						"src",
						"file",
						"schedule",
						"postings",
						"generic",
						"memo",
						"email",
						"ovryCmnt",
						"replyCmnt",
						"remoteConf",
						"process"
					}, 
				classes = 		{ 
						WorkItem.class,  	
						DocWorkItem.class,
						GoogleDriveAttachmentWorkItem.class,
						CommentWorkItem.class,
						ImageWorkItem.class,
						MovieWorkItem.class,
						SourceCodeWorkItem.class,
						FileWorkItem.class,
						ScheduleWorkItem.class,
						FacebookFeedback.class,
						GenericWorkItem.class,
						MemoWorkItem.class,
						EmailWorkItem.class,
						OverlayCommentWorkItem.class,
						ReplyOverlayCommentWorkItem.class,
						RemoteConferenceWorkItem.class,
						ProcessWorkItem.class
					} 
		)
		public String getType();
		public void setType(String type);

//		public boolean isLike();
//		public void setLike(boolean like);
		
		@Hidden
		public String getEndpoint();
		public void setEndpoint(String endpoint);

		@ORMapping(databaseFields = { "endpoint", "resname" }, objectFields = { "userId", "name" }	)
		public IUser getWriter();
		public void setWriter(IUser writer);
		
		
		@Hidden
		@ORMapping(
			databaseFields = {"content", "extfile", "ext3", "tool"}, 
			objectFields = {"uploadedPath", "filename", "filesize", "mimeType"},
			objectIsNullWhenFirstDBFieldIsNull = true,
			availableWhen = {"type=='file'","type=='document'"}	
		)
		public MetaworksFile getFile();
		public void setFile(MetaworksFile file);
		
		
		@ORMapping(
			databaseFields = {"taskId", "grpTaskId","ext1", "ext2"}, 
			objectFields = {"taskId", "grpTaskId","convertStatus", "pageCount"},
			objectIsNullWhenFirstDBFieldIsNull = true,
			availableWhen = {"type=='file'"}		
		)
		public Preview getPreview();
		public void setPreview(Preview file);

		
		@Hidden
		@ORMapping(
			databaseFields = {"prtTskId", "ext1", "ext2"}, 
			objectFields = {"parentTaskId", "x", "y"},
			objectIsNullWhenFirstDBFieldIsNull = true,
			availableWhen = {"type=='" + OverlayCommentWorkItem.TYPE + "'" , "type=='" + ReplyOverlayCommentWorkItem.TYPE + "'"}		
		)
		public OverlayCommentOption getOverlayCommentOption();
		public void setOverlayCommentOption(OverlayCommentOption overlayCommentOption);
		
		@Hidden
		@ORMapping(
			databaseFields = { 	"content" }, 
			objectFields = { 	"contents" },
			objectIsNullWhenFirstDBFieldIsNull = true,
			availableWhen= {"type=='memo'", "type=='email'"}
		)
		@ServiceMethod(callByContent=true)		
		public WebEditor getMemo();
		public void setMemo(WebEditor memo);
		
		
		@ORMapping(
			databaseFields = { 	"taskId", "grpTaskId", "instId" }, 
			objectFields = { 	"taskId", "grpTaskId", "instId" }
		)
		@NonSavable
		@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_SELF)		
		public WorkItemVersionChooser getWorkItemVersionChooser();
		public void setWorkItemVersionChooser(WorkItemVersionChooser workItemVersionChooser);
		
		public Long getInstId();
		public void setInstId(Long instId);

		public Long getRootInstId();
		public void setRootInstId(Long rootInstId);

		public Date getStartDate();
		public void setStartDate(Date date);

		public Date getEndDate();
		public void setEndDate(Date date);

		public Date getSaveDate();
		public void setSaveDate(Date date);

		public Date getDueDate();
		public void setDueDate(Date date);


		public String getTool();
		public void setTool(String tool);
		
		public int getDispatchOption();
		public void setDispatchOption(int dispatchOption);

		public String getRoleName();
		public void setRoleName(String roleName);
				
		public String getExt1();
		public void setExt1(String ext1);

		public String getExt2();
		public void setExt2(String ext2);

		public String getExt3();
		public void setExt3(String ext3);

		public String getExt4();
		public void setExt4(String ext4);

		public String getExt5();
		public void setExt5(String ext5);

		public String getExt6();
		public void setExt6(String ext6);

		public String getExt7();
		public void setExt7(String ext7);

		public String getExt8();
		public void setExt8(String ext8);

		public String getExt9();
		public void setExt9(String ext9);

		public String getExt10();
		public void setExt10(String ext10);

		@NonSavable
		@NonLoadable
		public IFrame getConference();
		public void setConference(IFrame conference);

		@NonSavable
		@NonLoadable
		public RemoteConferenceDate getRemoteConferenceDate();
		public void setRemoteConferenceDate(RemoteConferenceDate remoteConferenceDate);
		
		@NonSavable
		@NonLoadable
		public boolean isUseBBB();
		public void setUseBBB(boolean useBBB);
		
		@Hidden
		@NonSavable
		@NonLoadable
		public boolean isMore();
		public void setMore(boolean more);
		
		@ServiceMethod(when = WHEN_VIEW)
		public void like() throws Exception;
		
		@ServiceMethod(when = WHEN_VIEW, callByContent=true, target=ServiceMethodContext.TARGET_SELF)
		public void detail() throws Exception;

		@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_APPEND)
		public Object[] add() throws Exception;

		@ServiceMethod(inContextMenu=true, when = WHEN_VIEW, needToConfirm=true, callByContent=true /*TODO: later add except*/)
		@Face(displayName="$Delete")
		public Object remove() throws Exception;

		@ServiceMethod(inContextMenu=true, when = WHEN_VIEW, callByContent=true)
		@Face(displayName="$Edit")
		public void edit() throws Exception;
		
		
		@ServiceMethod(callByContent = true, target=ServiceMethodContext.TARGET_SELF)
		public void loadContents() throws Exception;
		
//		@ServiceMethod(callByContent=true)
		public IWorkItem find() throws Exception;
		
		@NonLoadable
		@NonSavable
		@Hidden(on=false)
		public WorkItemHandler getWorkItemHandler();
		public void setWorkItemHandler(WorkItemHandler workItemHandler);
		
		@NonLoadable
		@NonSavable
		@Hidden		
		public boolean isContentLoaded();
		public void setContentLoaded(boolean contentLoaded);
		
		public String getFolderName();
		public void setFolderName(String folderName);
		
		
		@Face(displayName="$Open")
		@ServiceMethod(inContextMenu=true, when = WHEN_VIEW, callByContent=true, target=ServiceMethodContext.TARGET_POPUP)
		@Hidden
		public ModalWindow workItemPopup() throws Exception;

		public String getStatus();
		public void setStatus(String status);

		////////// 
		@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_SELF)
		public IWorkItem newComment() throws Exception;
		
		@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_SELF)
		@Test(scenario="first", starter=true, instruction="메모를 입력할 수 있습니다.", next="autowiredObject.org.uengine.codi.mw3.model.WorkItem.newFile()")	
		public IWorkItem newSourceCode() throws Exception;
		
		@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_STICK)
		public Popup newActivity() throws Exception;
		
		@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_SELF)
		public IWorkItem newSchedule();
		
		@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_SELF)
		public IWorkItem newImage();
		
		@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_SELF)
		public IWorkItem newMovie();

		@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_SELF)
		public IWorkItem newFile() throws Exception;
		
		@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_SELF)
		public IWorkItem newDocument() throws Exception;
		
		@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_SELF)
		public IWorkItem newMemo() throws Exception;
		
		@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_SELF)
		public IWorkItem newRemoteConference() throws Exception;
		
		@ServiceMethod(inContextMenu=true, when = WHEN_VIEW, payload={"instId", "taskId", "rootInstId"}, target=ServiceMethodContext.TARGET_POPUP)
		@Face(displayName="$Comment")
		public OverlayCommentWorkItem comment() throws Exception;
		
		@ServiceMethod(payload={"instId", "taskId"}, target=ServiceMethodContext.TARGET_SELF)
		public Object moreView() throws Exception;
	
		public IWorkItem loadMajorVersionFile(String id) throws Exception;
		
		@ServiceMethod(callByContent=true)
		public IWorkItem loadCurrentView() throws Exception;
		
		@ServiceMethod(callByContent=true)
		public IWorkItem loadChooseView() throws Exception;
}

