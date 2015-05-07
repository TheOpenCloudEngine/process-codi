package org.uengine.codi.mw3.model;

import java.io.Serializable;
import java.util.Date;

import org.uengine.processmanager.ProcessManagerRemote;

public class RemoteConferenceData implements Serializable{

	String instanceId;
		public String getInstanceId() {
			return instanceId;
		}
		public void setInstanceId(String instanceId) {
			this.instanceId = instanceId;
		}
	
	Date startDate;
		public Date getStartDate() {
			return startDate;
		}
		public void setStartDate(Date startDate) {
			this.startDate = startDate;
		}

	
	
}
