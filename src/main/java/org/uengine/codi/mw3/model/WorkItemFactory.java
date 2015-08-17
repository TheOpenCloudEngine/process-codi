package org.uengine.codi.mw3.model;

import org.uengine.bean.factory.MetaworksSpringBeanFactory;

/**
 * Created by hoo.lim on 8/13/2015.
 */
public class WorkItemFactory {
    public static IWorkItem newInstance(String type){
        IWorkItem newItem = null;

        if("document".equals(type)|| "UnlabeledDocument".equals(type)){
            newItem = (IWorkItem) MetaworksSpringBeanFactory.getBeanByName("docWorkItem");
        }else{
            newItem = new CommentWorkItem();
        }

        return newItem;
    }
}
