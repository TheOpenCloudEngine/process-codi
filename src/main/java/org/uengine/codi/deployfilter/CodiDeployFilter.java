package org.uengine.codi.deployfilter;

import org.metaworks.dao.TransactionContext;
import org.uengine.kernel.ObjectDeployFilter;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.persistence.processdefinition.ProcessDefinitionDAO;
import org.uengine.processmanager.ProcessTransactionContext;

public class CodiDeployFilter implements ObjectDeployFilter {


	@Override
	public void beforeDeploy(String definition, String definitionId,
			String definitionVersionId, ProcessTransactionContext tc,
			String folder, boolean isNew) throws Exception {
		
		ProcessDefinitionDAO procDef = (ProcessDefinitionDAO) tc.findSynchronizedDAO("bpm_procdef", "defid", definitionId, ProcessDefinitionDAO.class);
		
		if (isNew) {
			String userId = (String) TransactionContext.getThreadLocalInstance().getRequest().getSession().getAttribute("userId");
			procDef.set("author", userId);
			
		} 
	}
}
