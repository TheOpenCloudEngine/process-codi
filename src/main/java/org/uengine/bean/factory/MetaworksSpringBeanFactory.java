package org.uengine.bean.factory;

import org.metaworks.dwr.MetaworksRemoteService;
import org.uengine.codi.mw3.model.InstanceTooltip;

/**
 * Created by hoo.lim on 6/11/2015.
 */
public class MetaworksSpringBeanFactory {
    public static <T> T getBean(Class<T> clazz) throws IllegalAccessException, InstantiationException {
        T t = MetaworksRemoteService.getInstance().getBeanFactory().getBean(clazz);

        if(t == null){
            t = clazz.newInstance();
        }
        return t;
    }
}
