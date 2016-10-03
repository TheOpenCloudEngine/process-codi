package org.uengine.codi.mw3.model;

import org.metaworks.annotation.Id;

/**
 * Created by jjy on 2016. 1. 28..
 */
public class InstanceViewDetail {

    String instanceId;

        @Id
        public String getInstanceId() {
            return instanceId;
        }
        public void setInstanceId(String instanceId) {
            this.instanceId = instanceId;
        }
}
