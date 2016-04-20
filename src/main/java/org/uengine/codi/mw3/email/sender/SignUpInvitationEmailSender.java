package org.uengine.codi.mw3.email.sender;

import org.metaworks.dao.TransactionContext;
import org.uengine.cloud.saasfier.TenantURL;
import org.uengine.codi.mw3.model.Employee;
import org.uengine.codi.util.CodiStringUtil;
import org.uengine.kernel.GlobalContext;
import org.uengine.webservices.emailserver.impl.EMailServerSoapBindingImpl;

import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Created by ho.lim on 2015-04-20.
 */
public class SignUpInvitationEmailSender extends TempEmailSender {

    @Override
    public void sendEmail(String urlPath, String email) throws Exception{
        String from = GlobalContext.getPropertyString("codi.mail.support", "support@processcodi.com");
        String beforeCompany = "company.name";
        String afterCompany = Employee.extractTenantName(email);

        String parameterSignUpURL = "signup.url";
        String signUpBaseUrl = "signup.baseurl";

        String codiServerUrl = GlobalContext.getPropertyString("codi.server.url");

        String baseUrl = codiServerUrl!=null ? codiServerUrl : CodiStringUtil.lastLastFileSeparatorChar(TenantURL.getURL()); //TODO: codi-refactoring

        if(!baseUrl.endsWith("/")) baseUrl = baseUrl + "/";

        String url = baseUrl + urlPath;
        String tempContent = readContent();

        String title = "Please activate your account";

        tempContent = tempContent.replaceAll(beforeCompany, afterCompany);
        tempContent = tempContent.replaceAll(parameterSignUpURL, url);
        tempContent = tempContent.replaceAll(signUpBaseUrl, baseUrl);
        System.out.println(tempContent);

        try {
            (new EMailServerSoapBindingImpl()).sendMail(from, email, title, tempContent);
        } catch (Exception e) {
            throw new Exception("$FailedToSendInvitationMail");
        }
    }

    @Override
    public String getPath() {
        String resourcePath = CodiStringUtil.lastLastFileSeparatorChar(new HttpServletRequestWrapper(TransactionContext.getThreadLocalInstance().getRequest()).getRealPath(""));
        String path = resourcePath + GlobalContext.getPropertyString("email.signup", "resources/mail/signupMail.html");
        return path;
    }
}
