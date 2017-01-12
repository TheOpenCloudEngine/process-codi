package org.uengine.codi.mw3.model;

import org.metaworks.annotation.AutowiredFromClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.uengine.codi.mw3.model.CommentWorkItem;
import org.uengine.kernel.AwareProcessInstanceId;
import org.uengine.kernel.ProcessInstance;
import org.uengine.processmanager.ProcessManagerRemote;

/**
 * Created by uengine on 2017. 1. 12..
 */
public class SubProcessAttatchStartingWorkItem extends CommentWorkItem {

    public Object[] add(@AutowiredFromClient(payload="processInstanceId") AwareProcessInstanceId processInstanceContext) throws Exception {
        Object[] returnValues = super.add();

        //link created process instance to the main process instance
        ProcessInstance processInstance = processManager.getProcessInstance(""+getInstId());
        processInstance.setMainProcessInstanceId(processInstanceContext.getProcessInstanceId());
        processManager.setChanged();

        return returnValues;
    }

    @Autowired
    public ProcessManagerRemote processManager;
}
