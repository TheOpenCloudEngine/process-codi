package org.uengine.codi.mw3;

import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.ServiceMethod;
import org.uengine.webservices.emailserver.impl.EMailServerSoapBindingImpl;

public class SignUpConfirm {

	String email;
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
	String url;
		@Hidden
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		
	public SignUpConfirm(){
		
	}
	
	@ServiceMethod(callByContent=true)
	public void resend() throws Exception {
		
		try{
			(new EMailServerSoapBindingImpl()).sendMail(getEmail(), getEmail(), "Codi Account - Email Authority","<p><a href='" + getUrl() + "'>"+"Click-"+getUrl()+"</a><br/>");
		
		}catch(Exception e){
			throw new Exception("$FailedToSendInvitationMail");
		}
		
		
	}
}
