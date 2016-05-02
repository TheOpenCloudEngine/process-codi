package org.uengine.codi.mw3.email.sender;

public class SignUpFriendInvitationEmailSender extends AbstractEmailSender {

    public SignUpFriendInvitationEmailSender() {
        super("/mail/passwordChangeMail.html", "company.name user.name invited you");
    }

}
