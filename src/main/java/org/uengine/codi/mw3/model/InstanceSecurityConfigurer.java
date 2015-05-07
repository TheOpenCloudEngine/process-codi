package org.uengine.codi.mw3.model;

import org.metaworks.annotation.Id;
import org.metaworks.annotation.ServiceMethod;

public class InstanceSecurityConfigurer {

	String instanceId;

	@Id
	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	@ServiceMethod(callByContent = true)
	public void toggleSecureConversation() throws Exception {
		Instance instance = new Instance();
		instance.setInstId(new Long(instanceId));
		String secuopt = instance.databaseMe().getSecuopt();
		if (secuopt.charAt(0) != '0') {
			instance.databaseMe().setSecuopt("0");
		} else {
			instance.databaseMe().setSecuopt("1");
		}

	}
}
