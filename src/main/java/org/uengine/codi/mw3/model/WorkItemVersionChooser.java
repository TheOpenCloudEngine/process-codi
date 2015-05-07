package org.uengine.codi.mw3.model;

import java.util.ArrayList;

import org.metaworks.EventContext;
import org.metaworks.MetaworksContext;
import org.metaworks.Refresh;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.component.SelectBox;
import org.metaworks.dao.Database;
import org.metaworks.dao.ORMappingListener;
import org.metaworks.widget.Window;

public class WorkItemVersionChooser implements ORMappingListener{
	
	public WorkItemVersionChooser(){
		
	}
	
	Long taskId;
		
		public Long getTaskId() {
			return taskId;
		}
		public void setTaskId(Long taskId) {
			this.taskId = taskId;
		}

	Long instId;
		public Long getInstId() {
			return instId;
		}
		public void setInstId(Long instId) {
			this.instId = instId;
		}

	Long grpTaskId;
		public Long getGrpTaskId() {
			return grpTaskId;
		}
		public void setGrpTaskId(Long grpTaskId) {
			this.grpTaskId = grpTaskId;
		}
	
	ArrayList<Long> taskIdsPerVersion;
		public ArrayList<Long> getTaskIdsPerVersion() {
			return taskIdsPerVersion;
		}
		public void setTaskIdsPerVersion(ArrayList<Long> taskIdsPerVersion) {
			this.taskIdsPerVersion = taskIdsPerVersion;
		}

	SelectBox versionSelector;
			public SelectBox getVersionSelector() {
				return versionSelector;
			}
			public void setVersionSelector(SelectBox versionSelector) {
				this.versionSelector = versionSelector;
			}
	
	String selectedVersion;
		public String getSelectedVersion() {
			return selectedVersion;
		}
		public void setSelectedVersion(String selectedVersion) {
			this.selectedVersion = selectedVersion;
		}

	@ServiceMethod(callByContent=true, eventBinding=EventContext.EVENT_CHANGE, target=ServiceMethodContext.TARGET_APPEND)
	public Object[] choose() throws Exception{
		FileWorkItem workItem = null;
		
		IWorkItem wi = null;
		int i=0;
		for(String value : getVersionSelector().getOptionValues()){
			if(value.equals(getVersionSelector().getSelected())){
				workItem = new FileWorkItem();
				workItem.setTaskId(getTaskIdsPerVersion().get(i));
				wi = workItem.databaseMe();
				wi.setMetaworksContext(new MetaworksContext());
				wi.getMetaworksContext().setHow(MetaworksContext.HOW_MINIMISED);
				
				break;
			}
			i++;
		}
		
		DocumentTool documentTool = new DocumentTool();
		documentTool.setInstId(instId.toString());
		documentTool.setWorkitem(wi);

		return new Object[]{new OverlayCommentReloadPanel(WorkItem.findCommentByTaskId(workItem.getTaskId().toString())), new Refresh(documentTool)};
	}

	@Override
	public void onRelation2Object() {
	
		taskIdsPerVersion = new ArrayList<Long>();
		IWorkItem workitem;
		
		try {
			
			// TODO : 임시 성능 튜닝 최종적으로는 아래 부분이 빠져야함
			if(this.getGrpTaskId() != null){ 
			
				
				//workitem = (IWorkItem) Database.sql(IWorkItem.class, "select * from bpm_worklist where instid=?instId and grptaskid=?grpTaskId order by taskid desc, majorver desc");
				//workitem.set("instId", getInstId());
				workitem = (IWorkItem) Database.sql(IWorkItem.class, "select * from bpm_worklist where grptaskid=?grpTaskId and majorver != 0 order by taskid desc, majorver desc");				
				workitem.set("grpTaskId", getGrpTaskId());
				
				workitem.select();
				
				versionSelector = new SelectBox();
				
				if(workitem.size() > 0){
					while(workitem.next()){
						String version = String.valueOf(workitem.getMajorVer()+ "." + workitem.getMinorVer());
						versionSelector.add(version, version);
						taskIdsPerVersion.add(workitem.getTaskId());
	
						if(workitem.getTaskId().equals(getTaskId())){
							versionSelector.setSelected(version);
						}
					}
				}
				if( selectedVersion != null){
					versionSelector.setSelectedValue(selectedVersion);
				}
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	
		
	}
	
	@Override
	public void onObject2Relation() {
		// TODO Auto-generated method stub
		
	}
	
}
