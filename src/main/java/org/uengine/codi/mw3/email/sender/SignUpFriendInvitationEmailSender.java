package org.uengine.codi.mw3.email.sender;

import org.metaworks.dao.TransactionContext;
import org.uengine.cloud.saasfier.TenantURL;
import org.uengine.codi.mw3.Login;
import org.uengine.codi.mw3.model.Employee;
import org.uengine.codi.util.CodiStringUtil;
import org.uengine.kernel.GlobalContext;
import org.uengine.webservices.emailserver.impl.EMailServerSoapBindingImpl;

import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Created by ho.lim on 2015-04-20.
 */
public class SignUpFriendInvitationEmailSender extends TempEmailSender {
    @Override
    public String getPath() {
        String resourcePath = CodiStringUtil.lastLastFileSeparatorChar(new HttpServletRequestWrapper(TransactionContext.getThreadLocalInstance().getRequest()).getRealPath(""));
        String path = resourcePath + GlobalContext.getPropertyString("email.invite", "resources/mail/invitationMail.html");
        return path;
    }

    @Override
    public void sendEmail(String urlPath, String email) throws Exception {
        String from = GlobalContext.getPropertyString("codi.mail.support", "support@processcodi.com");
        String beforeName = "user.name";
        String afterName = getProperties().getProperty("empName"); //초대 하는사람 이름
        String beforeCompany = "user.company";
        String afterCompany = Employee.extractTenantName(getProperties().getProperty("email")); //초대 하는사람

        String baseUrl = CodiStringUtil.lastLastFileSeparatorChar(TenantURL.getURL());
        String url = baseUrl + urlPath;

        String beforeFaceIcon = "face.icon";
        String afterFaceIcon = getProperties().getProperty("empCode");
        String baseLinkUrl = "base.url";
        String signUpURL = "signup.url";

        String tempContent = readContent();
        String title = afterCompany + " 의 " + afterName + " 님이  당신을  코디에 초대 하였습니다";

        tempContent = tempContent.replaceAll(beforeName, afterName);
        tempContent = tempContent.replaceAll(beforeCompany, afterCompany);
        tempContent = tempContent.replaceAll(baseLinkUrl, baseUrl);
        tempContent = tempContent.replaceAll(signUpURL, url);
        tempContent = tempContent.replaceAll(beforeFaceIcon, afterFaceIcon);
        System.out.println(tempContent);

        try {
            (new EMailServerSoapBindingImpl()).sendMail(from, email, title, tempContent);
        } catch (Exception e) {
            throw new Exception("$FailedToSendInvitationMail");
        }
    }

}
