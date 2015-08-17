package org.uengine.bean.factory;

import org.metaworks.dwr.MetaworksRemoteService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.uengine.codi.mw3.model.InstanceTooltip;

/**
 * Created by hoo.lim on 6/11/2015.
 */
public class MetaworksSpringBeanFactory {
    public static <T> T getBean(Class<T> clazz) throws IllegalAccessException, InstantiationException {
        T t = null;

        try {
            t = MetaworksRemoteService.getInstance().getBeanFactory().getAutowireCapableBeanFactory().getBean(clazz);
        } catch (NoSuchBeanDefinitionException e) {
            //System.out.printf("No qualifying bean of type [%s] is defined", clazz.toString());
            t = clazz.newInstance();
        }

        return t;
    }

    public static Object getBeanByName(String name){
        Object obj = null;

        try {
            obj =  MetaworksRemoteService.getInstance().getBeanFactory().getAutowireCapableBeanFactory().getBean(name);
        } catch (NoSuchBeanDefinitionException e) {
            System.out.printf("No qualifying bean of name [%s] is defined", name);
        }

        return obj;
    }

}
