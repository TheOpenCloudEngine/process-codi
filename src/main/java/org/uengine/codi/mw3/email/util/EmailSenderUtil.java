package org.uengine.codi.mw3.email.util;

import org.uengine.codi.mw3.email.factory.EmailSenderFactory;
import org.uengine.codi.mw3.email.sender.IEmailSender;
import org.uengine.codi.mw3.email.type.EmailSenderType;

import java.util.Properties;

/**
 * Created by ho.lim on 2015-04-20.
 */
public class EmailSenderUtil {

    public static void sendEmail(String urlPath, EmailSenderType emailSenderType, String email)throws Exception{
        IEmailSender emailSender = EmailSenderFactory.createEmailSender(emailSenderType);
        emailSender.sendEmail(urlPath, email);
    }

    public static void sendEmail(String urlPath, EmailSenderType emailSenderType, String email, Properties properties)throws Exception{
        IEmailSender emailSender = EmailSenderFactory.createEmailSender(emailSenderType);
        emailSender.setProperties(properties);
        emailSender.sendEmail(urlPath, email);
    }

}
