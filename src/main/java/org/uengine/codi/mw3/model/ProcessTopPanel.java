package org.uengine.codi.mw3.model;

import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.ServiceMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.uengine.processmanager.ProcessManagerRemote;

public class ProcessTopPanel extends PerspectiveTopPanel{
	
	@Autowired
	public ProcessManagerRemote processManager;
	
	@Override
	@ServiceMethod(target=ServiceMethodContext.TARGET_POPUP)
	public Object[] newInstance() throws Exception{
		String windowTitle = session.getWindowTitle();
		String[] ref = windowTitle.split("-");
		String processName = "";
		if( ref != null && ref.length > 1){
			processName += ref[1].trim();
			if( ref.length > 2 ){
				for( int i = 2; i < ref.length; i++){
					processName += "-" + ref[i].trim();
				}
			}
		}else{
			processName = windowTitle;
		}
		ProcessMap processMap = new ProcessMap();
		processMap.processManager = processManager;
		processMap.session = session;
		processMap.instanceView = new InstanceViewContent();
		processMap.setName(processName);
		processMap.setDefId(session.getLastSelectedItem());
		return processMap.initiate();
		
	}

}
