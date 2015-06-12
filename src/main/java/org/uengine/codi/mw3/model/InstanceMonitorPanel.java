package org.uengine.codi.mw3.model;

import org.metaworks.MetaworksContext;
import org.uengine.kernel.*;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.Role;
import org.uengine.modeling.modeler.ProcessCanvas;
import org.uengine.modeling.modeler.ProcessModeler;
import org.uengine.processmanager.ProcessManagerRemote;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soo on 2015. 6. 12..
 */
public class InstanceMonitorPanel {

    ProcessModeler processModeler;
        public ProcessModeler getProcessModeler() {
            return processModeler;
        }
        public void setProcessModeler(ProcessModeler processModeler) {
            this.processModeler = processModeler;
        }

    public InstanceMonitorPanel(){
        ProcessModeler processModeler = new ProcessModeler();
        processModeler.setPalette(null);

        ((ProcessCanvas) processModeler.getCanvas()).setMetaworksContext(new MetaworksContext());
        ((ProcessCanvas) processModeler.getCanvas()).getMetaworksContext().setWhen(MetaworksContext.WHEN_VIEW);
        setProcessModeler(processModeler);
    }

    public ProcessModeler load(Long instanceId, ProcessManagerRemote processManager) throws Exception {

        ProcessInstance processInstance = processManager.getProcessInstance(String.valueOf(instanceId));
        ProcessDefinition processDefinition = processInstance.getProcessDefinition();

        getProcessModeler().setModel(processDefinition);

        ProcessModeler pm = getProcessModeler();
        return pm;
    }
}
