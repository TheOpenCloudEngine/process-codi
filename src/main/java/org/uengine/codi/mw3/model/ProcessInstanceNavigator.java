package org.uengine.codi.mw3.model;

import org.metaworks.annotation.AutowiredFromClient;
import org.uengine.processmanager.ProcessManagerRemote;

public class ProcessInstanceNavigator {
	
	@AutowiredFromClient
	public Session session;
	
	public void load(String instanceId) throws Exception{
		Instance instance = new Instance();
//		instance.setInstId(new Long(instanceId));
		
//		rootInstance = instance.databaseMe();
//		rootInstance.getMetaworksContext().setWhere("instanceNavigator");
//		
		subInstances = instance.sql("select * from bpm_procinst where RootInstId = ?RootInstId and substring_index(DEFVERID, '.', -1)='process2'");
		subInstances.setRootInstId(new Long(instanceId));
		subInstances.select();
		//subInstances.setInstId(rootInstance.getInstId());
		subInstances.getMetaworksContext().setHow("instanceNavigator");
		
		
	}
	
//	IInstance rootInstance;
//		
//		public IInstance getRootInstance() {
//			return rootInstance;
//		}
//	
//		public void setRootInstance(IInstance rootInstance) {
//			this.rootInstance = rootInstance;
//		}
		
	IInstance subInstances;

		public IInstance getSubInstances() {
			return subInstances;
		}
	
		public void setSubInstances(IInstance subInstances) {
			this.subInstances = subInstances;
		}

	public ProcessManagerRemote processManager;

}

