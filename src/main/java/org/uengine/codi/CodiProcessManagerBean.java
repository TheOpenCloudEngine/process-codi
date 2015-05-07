package org.uengine.codi;

import org.uengine.processmanager.ProcessManagerBean;

import java.rmi.RemoteException;

public class CodiProcessManagerBean extends ProcessManagerBean{

	@Override
	public String getProcessDefinitionProductionVersion(String pdid)
			throws RemoteException {
		// TODO Auto-generated method stub
		return pdid;
	}

	@Override
	public String getProcessDefinitionProductionVersionByName(String pdName)
			throws RemoteException {
		// TODO Auto-generated method stub
		return pdName;
	}

	@Override
	public String getProcessDefinitionProductionVersionByAlias(String alias)
			throws RemoteException {
		// TODO Auto-generated method stub
		return alias;
	}
	
	

}
