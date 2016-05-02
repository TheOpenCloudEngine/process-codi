package org.uengine.codi.mw3.email.sender;

public class LoginForgerPasswordInvitationEmailSender extends AbstractEmailSender {

    public LoginForgerPasswordInvitationEmailSender() {
        super("/mail/passwordChangeMail.html", "You've requested password change.");
    }
}
