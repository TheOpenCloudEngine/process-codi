package org.uengine.codi.mw3.listener;

import org.uengine.codi.mw3.Login;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;

/**
 * Created by ho.lim on 4/1/2015.
 */
public class LoginStaticSessionListener implements ServletContextListener{

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        File theDir = new File(Login.CACHE_PATH);
        if (!theDir.exists()) {
            theDir.mkdirs();
        }

        try {
            Login.recoverLoginStaticSessionInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        try {
            Login.storeLoginStaticSessionInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
