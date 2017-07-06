package org.uengine.codi;

import org.metaworks.annotation.Resource;
import org.metaworks.dwr.MetaworksRemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.uengine.modeling.resource.DefaultResource;
import org.uengine.modeling.resource.ResourceManager;
import org.uengine.util.UEngineUtil;

import javax.servlet.ServletException;
import java.io.OutputStream;

/**
 * Created by jjy on 2016. 6. 16..
 */

@Component
public class CodiInstaller implements ApplicationListener<ContextRefreshedEvent> {

    public static final String PATH_PROCESSAPP = "processapps/-1.processapp";

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        installDefaultProcessApp();
    }

    @Autowired
    @Qualifier("resourceManagerForMarketplace")
    ResourceManager resourceManager;

    public void installDefaultProcessApp(){

        DefaultResource processAppResource = new DefaultResource();
        processAppResource.setPath("-1.processapp");

        //MetaworksRemoteService.autowire(processAppResource);
        processAppResource.resourceManager = resourceManager;

        //ResourceManager resourceManager = MetaworksRemoteService.getComponent(ResourceManager.class);
        try {
            OutputStream os = resourceManager.getStorage().getOutputStream(processAppResource);

            UEngineUtil.copyStream(Thread.currentThread().getContextClassLoader().getResourceAsStream(PATH_PROCESSAPP), os);

        } catch (Exception e1) {
            new Exception("Failed to install default app - " + PATH_PROCESSAPP).printStackTrace();
            //e1.printStackTrace();
        }
    }
}
