package org.uengine.codi.mw3.model;

import org.metaworks.annotation.ServiceMethod;

/**
 * Created by jangjinyoung on 16. 4. 16..
 */
public class MoreInstanceList extends InstanceList{

    @ServiceMethod //override the onLoad option to be disabled.
    public InstanceList load() throws Exception {
        return super.load();
    }
}
