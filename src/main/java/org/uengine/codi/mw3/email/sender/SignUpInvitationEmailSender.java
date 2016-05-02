package org.uengine.codi.mw3.email.sender;

public class SignUpInvitationEmailSender extends AbstractEmailSender {

    public SignUpInvitationEmailSender() {
        super("mail/signupMail.html", "Please activate your account");
    }
}
