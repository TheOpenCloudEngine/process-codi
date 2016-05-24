package org.uengine.codi.mw3.email.sender;

import org.uengine.cloud.saasfier.TenantURL;
import org.uengine.codi.mw3.model.Employee;
import org.uengine.codi.util.CodiStringUtil;
import org.uengine.kernel.GlobalContext;
import org.uengine.webservices.emailserver.impl.EMailServerSoapBindingImpl;

import java.io.*;
import java.util.Properties;

public abstract class AbstractEmailSender{

    public AbstractEmailSender(String templateClassPath, String title){
        setTemplateClassPath(templateClassPath);
        setTitle(title);
    }

    Properties properties;
        public void setProperties(Properties properties) {
            this.properties = properties;
        }
        public Properties getProperties() {
            return properties;
        }

    String title;
        public String getTitle() {
            return title;
        }
        public void setTitle(String title) {
            this.title = title;
        }


    String templateClassPath;
        public String getTemplateClassPath() {
            return templateClassPath;
        }
        public void setTemplateClassPath(String templateClassPath) {
            this.templateClassPath = templateClassPath;
        }


    protected String readContent() {
        String tempContent = null;
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        InputStream is;
        try {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(getTemplateClassPath());

            if(is==null){
                throw new Exception("Email template '" + getTemplateClassPath() + "' is not found in classpath.");
            }

            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            BufferedReader br = new BufferedReader(isr);

            int data;
            while ((data = br.read()) != -1) {
                tempContent += (char) data;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return tempContent;
    }

    public void setProperty(String key, String value){
        if(getProperties()==null)
            setProperties(new Properties());

        if(value==null) {
            getProperties().remove(key);
        }else{
            getProperties().setProperty(key, value);
        }

    }

    public String getProperty(String key){
        if(getProperties() == null) return null;

        return getProperties().getProperty(key);
    }

    public void send(String urlPath, String email) throws Exception {
        String from = GlobalContext.getPropertyString("codi.mail.support", "support@processcodi.com");

        String codiServerUrl = GlobalContext.getPropertyString("codi.server.url");
        System.out.println("EMail Sender host : " + codiServerUrl);

        String baseUrl = codiServerUrl!=null ? codiServerUrl : CodiStringUtil.lastLastFileSeparatorChar(TenantURL.getURL()); //TODO: codi-refactoring

        if(!baseUrl.endsWith("/")) baseUrl = baseUrl + "/";

        String url = baseUrl + urlPath;

        setProperty("face.icon", getProperty("empCode"));
        setProperty("user.name", getProperty("empName"));
        setProperty("company.name", Employee.extractTenantName(email));
        setProperty("password.url", url);
        setProperty("signup.baseurl", baseUrl);
        setProperty("base.url", baseUrl);
        setProperty("signup.url", url);

        String tempContent = readContent();
        String title = getTitle();

        for(Object key : properties.keySet()){
            String keyStr = (String) key;
            String value = properties.getProperty(keyStr);

            //TODO: using replaceAll() in a loop may performs very poorly.
            tempContent = tempContent.replaceAll(keyStr, value);
            title = title.replaceAll(keyStr, value);
        }

        try {
            (new EMailServerSoapBindingImpl()).sendMail(from, email, title, tempContent);
        } catch (Exception e) {
            throw new Exception("$FailedToSendInvitationMail");
        }
    }


}
