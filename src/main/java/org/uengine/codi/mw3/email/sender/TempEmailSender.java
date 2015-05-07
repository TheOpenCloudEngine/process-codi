package org.uengine.codi.mw3.email.sender;

import org.uengine.codi.mw3.email.sender.IEmailSender;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by ho.lim on 2015-04-20.
 */
public abstract class TempEmailSender implements IEmailSender {
    Properties properties;

    public String readContent() {
        String tempContent = null;
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        FileInputStream is;
        try {
            is = new FileInputStream(getPath());
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            BufferedReader br = new BufferedReader(isr);

            int data;
            while ((data = br.read()) != -1) {
                tempContent += (char) data;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return tempContent;
    }

    public abstract String getPath();

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Override
    public Properties getProperties() {
        return properties;
    }
}
