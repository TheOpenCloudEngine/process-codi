package org.uengine.codi.mw3.model;

import org.metaworks.annotation.ServiceMethod;

public class ProcessStatusPerspective extends Perspective {

	public ProcessStatusPerspective() {
		setLabel("Status");
	}

	@ServiceMethod
	public Object[] loadReady() throws Exception {
		return loadInstanceListPanel(Perspective.MODE_PROCESS, "status", "Ready");
	}
	@ServiceMethod
	public Object[] loadCompleted() throws Exception {
		return loadInstanceListPanel(Perspective.MODE_PROCESS,"status", "Completed");
	}
	@ServiceMethod
	public Object[] loadFailed() throws Exception {
		return loadInstanceListPanel(Perspective.MODE_PROCESS,"status", "Failed");
	}
	@ServiceMethod
	public Object[] loadRetrying() throws Exception {
		return loadInstanceListPanel(Perspective.MODE_PROCESS,"status", "Retrying");
	}
	@ServiceMethod
	public Object[] loadRunning() throws Exception {
		return loadInstanceListPanel(Perspective.MODE_PROCESS,"status", "Running");
	}
	@ServiceMethod
	public Object[] loadSuspended() throws Exception {
		return loadInstanceListPanel(Perspective.MODE_PROCESS,"status", "Suspended");
	}
	@ServiceMethod
	public Object[] loadStopped() throws Exception {
		return loadInstanceListPanel(Perspective.MODE_PROCESS,"status", "Stopped");
	}
	@ServiceMethod
	public Object[] loadTimeout() throws Exception {
		return loadInstanceListPanel(Perspective.MODE_PROCESS,"status", "Timeout");
	}
	@ServiceMethod
	public Object[] loadCancelled() throws Exception {
		return loadInstanceListPanel(Perspective.MODE_PROCESS,"status", "Cancelled");
	}

}
