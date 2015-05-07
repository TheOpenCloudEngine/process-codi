package org.uengine.codi.mw3.email.factory;

import org.uengine.codi.mw3.email.sender.IEmailSender;
import org.uengine.codi.mw3.email.sender.SignUpFriendInvitationEmailSender;
import org.uengine.codi.mw3.email.type.EmailSenderType;
import org.uengine.codi.mw3.email.sender.LoginForgerPasswordInvitationEmailSender;
import org.uengine.codi.mw3.email.sender.SignUpInvitationEmailSender;

/**
 * Created by ho.lim on 2015-04-20.
 */
public class EmailSenderFactory {

    public static IEmailSender createEmailSender(EmailSenderType emailSenderType){
        IEmailSender emailSender = null;
        switch (emailSenderType){
            case SIGN_UP_INVITATION:
                emailSender = new SignUpInvitationEmailSender();
            break;
            case LOGIN_FORGET_PASSWORD:
                emailSender = new LoginForgerPasswordInvitationEmailSender();
                break;
            case SIGN_UP_FRIEND_INVITATION:
                emailSender = new SignUpFriendInvitationEmailSender();
                break;
            default:
                System.out.println("There is no email sender.");
        }
        return emailSender;
    }

}
