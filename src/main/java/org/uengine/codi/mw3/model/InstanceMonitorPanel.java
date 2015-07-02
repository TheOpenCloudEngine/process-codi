package org.uengine.codi.mw3.model;

import org.metaworks.MetaworksContext;
import org.metaworks.annotation.Face;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.ProcessInstance;
import org.uengine.modeling.modeler.ProcessCanvas;
import org.uengine.modeling.modeler.ProcessModeler;
import org.uengine.processmanager.ProcessManagerRemote;

/**
 * Created by soo on 2015. 6. 12..
 */
@Face(ejsPath = "dwr/metaworks/genericfaces/CleanObjectFace.ejs")
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
        ((ProcessCanvas) processModeler.getCanvas()).getMetaworksContext().setWhen("monitor");
        setProcessModeler(processModeler);
    }

    public ProcessModeler load(Long instanceId, ProcessManagerRemote processManager) throws Exception {

        ProcessInstance processInstance = processManager.getProcessInstance(String.valueOf(instanceId));
        ProcessDefinition processDefinition = processInstance.getProcessDefinition();


        getProcessModeler().setModelForMonitor(processDefinition, processInstance);

        ProcessModeler pm = getProcessModeler();
        return pm;
    }
}
