package org.uengine.codi.mw3.email.sender;

import java.util.Properties;

/**
 * Created by ho.lim on 2015-04-20.
 */
public interface IEmailSender {
    public void sendEmail(String urlPath, String email) throws Exception;

    public void setProperties(Properties properties);

    public Properties getProperties();
}
