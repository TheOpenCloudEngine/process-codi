package org.uengine.codi.mw3.model;

import java.rmi.RemoteException;

import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dao.ORMappingListener;
import org.metaworks.widget.ModalWindow;
import org.springframework.beans.factory.annotation.Autowired;
import org.uengine.codi.ITool;
import org.uengine.codi.mw3.knowledge.KnowledgeTool;
import org.uengine.codi.mw3.knowledge.WfPanel_pt;
import org.uengine.kernel.ProcessInstance;
import org.uengine.processmanager.ProcessManagerRemote;

public class GenericWorkItemHandler implements ORMappingListener{

	String serializedTool;

		public String getSerializedTool() {
			return serializedTool;
		}
	
		public void setSerializedTool(String serializedTool) {
			this.serializedTool = serializedTool;
		}

	@Override
	public void onRelation2Object() {

		try {
			setTool( org.uengine.kernel.GlobalContext.deserialize(getSerializedTool()));
			
			if(getTool() instanceof ITool){
				ITool tool = (ITool)getTool();
				tool.onLoad();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void onObject2Relation() {
		try {
			setSerializedTool(org.uengine.kernel.GlobalContext.serialize(getTool(), Object.class));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		

	Object tool;
		public Object getTool() {
			return tool;
		}
		public void setTool(Object tool) {
			this.tool = tool;
		}
	
	Long instanceId;
		public Long getInstanceId() {
			return instanceId;
		}
		public void setInstanceId(Long instanceId) {
			this.instanceId = instanceId;
		}


	String tracingTag;
		public String getTracingTag() {
			return tracingTag;
		}
		public void setTracingTag(String tracingTag) {
			this.tracingTag = tracingTag;
		}
	
	
	Long taskId;
		public Long getTaskId() {
			return taskId;
		}
		public void setTaskId(Long taskId) {
			this.taskId = taskId;
		}
		
	String status;
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		
	@ServiceMethod(callByContent=true , target="popup")
	@Face(displayName="학습창")
	public ModalWindow presentation() throws Exception{
		String parentId = null;
		if( getTool() instanceof KnowledgeTool){
			parentId = ((KnowledgeTool)getTool()).getNodeId();
		}
		
		WfPanel_pt panel = new WfPanel_pt();
		panel.session = session;
		panel.setFirst(false);
		panel.setLoadDepth(1);
		panel.load(parentId, "presentation");
		
		return new ModalWindow(panel , 1000, 600,  "학습창" );
	}
	
	@ServiceMethod(callByContent=true)
	@Face(displayName="완료")
	public InstanceViewContent complete() throws RemoteException, ClassNotFoundException, Exception{
			
//			ProcessInstance instance = processManager.getProcessInstance(getInstanceId().toString());

			processManager.completeWorkitem(getInstanceId().toString(), getTracingTag(), getTaskId().toString(), null);
			processManager.applyChanges();
			
			//refreshes the instanceview so that the next workitem can be show up
			Instance refInstance = new Instance();
			refInstance.setInstId(getInstanceId().longValue());
			
			instanceViewContent.load(refInstance);
			
			return instanceViewContent;

	}
	@AutowiredFromClient
	public Session session;
	
	@Autowired
	public ProcessManagerRemote processManager;
	
	@Autowired
	public InstanceViewContent instanceViewContent;
}
