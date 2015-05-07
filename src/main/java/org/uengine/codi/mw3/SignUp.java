package org.uengine.codi.mw3;

import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.ServiceMethod;
import org.uengine.kernel.GlobalContext;

public class SignUp {

	Login login;
		public Login getLogin() {
			return login;
		}
		public void setLogin(Login login) {
			this.login = login;
		}
		
	boolean forOCE;	
		public boolean isForOCE() {
			return forOCE;
		}
		public void setForOCE(boolean forOCE) {
			this.forOCE = forOCE;
		}
		
	public SignUp(){
		Login login = new Login();
		login.getMetaworksContext().setWhere("index");
		login.getMetaworksContext().setHow("signup");
		
		setForOCE("true".equals(GlobalContext.getPropertyString("forOCE", "false")));

		this.setLogin(login);
	}
	
	@ServiceMethod(target=ServiceMethodContext.TARGET_SELF)
	public Object goLogin(){
		return new Login();
	}
	
}
