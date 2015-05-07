package org.uengine.codi.mw3.model;

import org.metaworks.MetaworksContext;
import org.metaworks.MetaworksException;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.annotation.Table;
import org.metaworks.website.MetaworksFile;
import org.uengine.util.UEngineUtil;

@Table(name = "bpm_worklist")
@Face(
		ejsPath="dwr/metaworks/org/uengine/codi/mw3/model/DocWorkItem.ejs",
		ejsPathMappingByContext=
	{
		"{when: 'new', face: 'dwr/metaworks/org/uengine/codi/mw3/model/IWorkItem_edit.ejs'}",
		"{when: 'edit', face: 'dwr/metaworks/org/uengine/codi/mw3/model/IWorkItem_edit.ejs'}",
	}		
)
public class DocWorkItem extends GenericWorkItem {
	
	
	public DocWorkItem(){
		setType(WorkItem.WORKITEM_TYPE_DOCUMENT);
		setFile(new MetaworksFile());
	}
	
	@Override
	@Hidden(on=false)
	public MetaworksFile getFile(){
		return super.getFile();
	}
	
	@Override
	@Hidden(on=false)
	public void setFile(MetaworksFile file) {
		this.file = file;
	}
	
	@Override
	public Object[] add() throws Exception{
		
		Object[] returnObj = null;
		
		FileWorkItem fileWorkItem = new FileWorkItem();
		fileWorkItem.session = session;
		fileWorkItem.processManager = this.processManager;
		fileWorkItem.instanceViewContent = this.instanceViewContent;
		fileWorkItem.newInstancePanel = this.newInstancePanel;
		fileWorkItem.getMetaworksContext().setWhen(MetaworksContext.WHEN_NEW);
		
		fileWorkItem.setRootInstId(this.getRootInstId());
		fileWorkItem.setWriter(session.getUser());
		fileWorkItem.setFile(this.getFile());
		fileWorkItem.setTitle(this.getTitle());
		fileWorkItem.setMajorVer(1);
		fileWorkItem.setFolderId(this.getFolderId());
		fileWorkItem.setGrpTaskId(this.getTaskId());
		fileWorkItem.add();

		DocumentTool tool = new DocumentTool();
		tool.session = session;
		tool.setInstId(fileWorkItem.getInstId().toString());
		fileWorkItem.getMetaworksContext().setWhen(MetaworksContext.WHEN_VIEW);
		fileWorkItem.getMetaworksContext().setHow(MetaworksContext.HOW_MINIMISED);
		tool.setWorkitem(fileWorkItem);
		
		GenericWorkItemHandler genericWIH = new GenericWorkItemHandler();
		genericWIH.setTool(tool);
		
		GenericWorkItem genericWI = new GenericWorkItem();
		genericWI.session = session;
		this.getMetaworksContext().setWhen(MetaworksContext.WHEN_NEW);
//			if("normal".equals(this.getMetaworksContext().getHow())){
//				genericWI.setInstId(this.getInstId());
//			}else{
//				genericWI.getMetaworksContext().setWhen(MetaworksContext.WHEN_NEW);
//			}
		this.setWriter(session.getUser());
		this.setGrpTaskId(fileWorkItem.getTaskId());
		this.setGenericWorkItemHandler(genericWIH);
		
		returnObj = super.add();
		
		Instance fileInstance = new Instance();
		fileInstance.setInstId(fileWorkItem.getInstId());
		fileInstance.databaseMe().setRootInstId(this.getRootInstId());

		// TODO: ProcesManagerRemote 의 여러개의 인스턴스에 대해서 applyChange 시 기존 인스턴스에 대한 영향도 존재
		// 수정처리 해야함
		//fileInstance.databaseMe().setStatus(WORKITEM_STATUS_RUNNING);
		
		return returnObj;
	}
	
	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_APPEND)
	public Object[] addDocument() throws Exception{
		Object[] returnObjects = null;
		String sql = " select * from bpm_worklist where instId=?instId";
		IWorkItem workitem = (IWorkItem) sql(IWorkItem.class , sql);
		workitem.set("instId", this.getInstId());
		workitem.select();
		
		WorkItem item = new WorkItem();
		while(workitem.next()){
			item.copyFrom(workitem);
			System.out.println(item.getMajorVer());
			System.out.println(item.getGrpTaskId());
		}
		
		InstanceViewThreadPanel instanceViewThreadPanel = new InstanceViewThreadPanel();
		instanceViewThreadPanel.setInstanceId(this.getInstId().toString());
		
		if("document".equals(this.getType())){
			FileWorkItem fileWorkItem = new FileWorkItem();
			fileWorkItem.setInstId(this.getInstId());
			fileWorkItem.session = session;
			fileWorkItem.processManager = this.processManager;
			fileWorkItem.instanceViewContent = this.instanceViewContent;
			fileWorkItem.getMetaworksContext().setWhen(MetaworksContext.WHEN_NEW);
			fileWorkItem.getMetaworksContext().setHow("document");
			fileWorkItem.setWriter(session.getUser());
			fileWorkItem.setFile(this.getFile());
			fileWorkItem.setGrpTaskId(item.getGrpTaskId());
			fileWorkItem.setMajorVer(item.getMajorVer()+1);
			if(!UEngineUtil.isNotEmpty(getTitle()))
				fileWorkItem.setTitle(new String(this.getFile().getFilename()));
			else
				fileWorkItem.setTitle(this.getTitle());
//			fileWorkItem.add();	
//			returnObjects = new Object[]{new ToAppend(fileWorkItem, this), new Refresh(fileWorkItem,false,true),new Refresh(this)};
			returnObjects = fileWorkItem.add();
		}
		
		
		
		return returnObjects;
		
		
	}
	
	
}
