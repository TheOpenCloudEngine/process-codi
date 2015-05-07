package org.uengine.codi.mw3.knowledge;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.Available;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.ServiceMethod;


public class SvnUser implements ContextAware {
	MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
	
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}
		
	String committor;
		public String getCommittor() {
			return committor;
		}
		public void setCommittor(String committor) {
			this.committor = committor;
		}
		
	Boolean isJoined;
		@Hidden
		public Boolean getIsJoined() {
			return isJoined;
		}
		public void setIsJoined(Boolean isJoined) {
			this.isJoined = isJoined;
		}

	Boolean isChecked;
		public Boolean getIsChecked() {
			return isChecked;
		}
		public void setIsChecked(Boolean isChecked) {
			this.isChecked = isChecked;
		}
	
	String userName;
		public String getUserName() {
			return userName;
		}
	
		public void setUserName(String userName) {
			this.userName = userName;
		}

	String email;
		public String getEmail() {
			return email;
		}
	
		public void setEmail(String email) {
			this.email = email;
		}

	public SvnUser(){
	}
	
	public SvnUser(String committor, String email, String userName){
		setCommittor(committor);
		setEmail(email);
		setUserName(userName);
		metaworksContext = new MetaworksContext();
	}
	
	@Available(how="svnUser")
	@ServiceMethod(callByContent=true, mouseBinding=ServiceMethodContext.MOUSEBINDING_LEFTCLICK, target=ServiceMethodContext.TARGET_SELF)
	public void check(){
		this.setIsChecked(!this.getIsChecked());
	}	
	
}
