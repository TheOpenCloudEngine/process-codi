package org.uengine.codi.mw3.model;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.UUID;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequestWrapper;

import org.metaworks.ContextAware;
import org.metaworks.EventContext;
import org.metaworks.MetaworksContext;
import org.metaworks.MetaworksException;
import org.metaworks.Refresh;
import org.metaworks.ToEvent;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Available;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dao.TransactionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.uengine.codi.mw3.Login;
import org.uengine.codi.util.CodiStringUtil;
import org.uengine.kernel.GlobalContext;
import org.uengine.webservices.emailserver.impl.EMailServerSoapBindingImpl;



public class Invitation implements ContextAware{
	
	MetaworksContext metaworksContext;
	
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
	
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}

	public Invitation(){
		setMetaworksContext(new MetaworksContext());
		getMetaworksContext().setWhen("edit");
		
		if(session!=null){
			String emailAddress = session.getEmployee().getEmail();
			
			int posAt = emailAddress.indexOf("@");

			if(emailAddress!=null && posAt > -1){
				String domain = emailAddress.substring(posAt + 1);
				setEmail(domain);
			}
		}
	}
	
	String invitedMessage;
		@Available(how={"afterinvite"})
		public String getInvitedMessage() {
			return invitedMessage;
		}
		public void setInvitedMessage(String invitedMessage) {
			this.invitedMessage = invitedMessage;
		}

	String email;
	//@Face(options={"placeholder"}, values={"Enter your work email address"})
//	@Pattern(regexp = "/^*@(?:\\w+\\.)+\\w+$/", message = "이메일 형식이 잘못되었습니다.")
	@Face(options={"hideLabel","placeholder"}, values={"true", "$InputEmail"})
		public String getEmail() {
			return email;
		}
	
		public void setEmail(String email) {
			this.email = email;
		}


	@AutowiredFromClient
	public Session session;
	
	@Autowired
    public Locale locale;
	
	//randomPassword()생성
	public String randomPassword(){
	    SecureRandom random = new SecureRandom();
	    return new BigInteger(40, random).toString(32);
	}
	
	@ServiceMethod(callByContent=true, target="popup", validate=true)
	@Face(displayName="$Invite")
	public Object[] invite() throws Exception{
		
		String authKey = UUID.randomUUID().toString();
		String[] emilList = null;
		emilList = this.getEmail().split(",");
		this.setEmail(this.getEmail().replace("\"", ""));

		ArrayList<InternetAddress> emailAddrList = new ArrayList<InternetAddress>();

		int invitedCount = emilList.length;
		
		for(int i=0; i<invitedCount; i++){
			try {
				emailAddrList.add(new InternetAddress(emilList[i].trim()));
				emailAddrList.get(i).validate();
				System.out.println(emailAddrList.get(i));
			} catch (AddressException e) {
				// TODO Auto-generated catch block
				throw new Exception("$InviteEmailFormValidate");
			}
		}
		
		for(int i=0; i<emailAddrList.size(); i++){
			
			String friendEmail = null;
			String friendName=null;
			
			friendEmail = emailAddrList.get(i).getAddress();
			
			Employee emp = new Employee();
			emp.setEmail(friendEmail);
			IEmployee findEmp = emp.findByEmail();

			if(emailAddrList.get(i).getPersonal() == null){
				friendName = friendEmail.substring(0, friendEmail.indexOf("@"));
			}else{
				friendName = emailAddrList.get(i).getPersonal();
			}
			
			
			// 1. 코디 사용자가 아닐때
			if(findEmp==null){
				Employee newUser = new Employee();
				newUser.setEmail(friendEmail);
				newUser.setEmpName(friendName);
				newUser.setEmpCode(newUser.createNewId());
				newUser.setAuthKey(authKey);
				newUser.setIsDeleted("0");
				newUser.setApproved(false);
				newUser.setInviteUser(session.getEmployee().getEmpCode());
				
				try {
					newUser.createDatabaseMe();
					newUser.flushDatabaseMe();
				} catch (Exception e) {
					e.printStackTrace();
					throw new MetaworksException(e.getMessage());
				}
				sendMailToNoUser(authKey, friendEmail);
				addContactEachother(friendEmail);
				this.setInvitedMessage("친구초대 메일을 보냈습니다.");
				continue;
			}
			
			
			Contact contact = new Contact();
			IContact findContact = contact.findContactsWithFriendId(this.session.getUser(), findEmp.getEmpCode());
			
			// 2. 코디 사용자 이고, 이미 친구일 때
			if(findContact != null){
				// 3. 초대는 받았지만 아직 가입을 하지 않은 유저이고, 친구 일때
				if(!findEmp.isApproved()){
									
					Employee saveEmp = new Employee();
					saveEmp.copyFrom(findEmp);
					saveEmp.setInviteUser(session.getEmployee().getEmpCode());
					saveEmp.setEmpName(friendName);
					saveEmp.setAuthKey(authKey);
					saveEmp.syncToDatabaseMe();
					
					sendMailToNoUser(authKey,friendEmail);
					this.setInvitedMessage("친구초대 메일을 보냈습니다.");
					continue;
				}
				
				this.setInvitedMessage("이미 등록된 친구입니다.");
				continue;
			}
		 
		// 4. 초대는 받았지만 아직 가입을 하지 않은 유저이고, 친구가 아닐때
			if(!findEmp.isApproved()){
				
				findEmp.setAuthKey(authKey);
				
				Employee saveEmp = new Employee();
				saveEmp.copyFrom(findEmp);
				
				saveEmp.setEmpName(friendName);
				saveEmp.syncToDatabaseMe();
				
				sendMailToNoUser(authKey, friendEmail);
				addContactEachother(friendEmail);
				
				this.setInvitedMessage("친구초대 메일을 보냈습니다.");
				continue;
			}
			
			// 5. 코디 사용자이고, 친구가 아니고 다른회사 사람일때
			if(!session.getEmployee().getGlobalCom().equals(findEmp.getGlobalCom())){
				
				addContactEachother(friendEmail);
				
				this.setInvitedMessage("친구가 등록 되었습니다.");
				continue;
				
			
			}else{
				//6. 코디 사용자이고, 친구가 아니고 같은회사 사람일때
				Contact newContact = new Contact();
				newContact.setUserId(session.getUser().getUserId());
				
				IUser user = new User();
				user.setName(friendName);
				user.setUserId(findEmp.getEmpCode());
				user.setNetwork("local");
				newContact.setFriendId(user.getUserId());
				newContact.setFriend(user);
				newContact.createDatabaseMe();
				newContact.flushDatabaseMe();
	
				this.setInvitedMessage("친구가 등록 되었습니다.");
				continue;
			}
		}
		
		if(invitedCount>1){
			this.setInvitedMessage("친구들을 초대하였습니다.");
		}
		
		this.getMetaworksContext().setHow("afterinvite");
		return new Object[]{new ToEvent(new ContactPerspective(), EventContext.EVENT_CHANGE), new Refresh(this)};
	}
	
	public void addContactEachother(String friendEmail) throws Exception{
		Employee newUser = new Employee();
		newUser.setEmail(friendEmail);
		newUser.copyFrom(newUser.findByEmail());
		
		Contact newContact = new Contact();
		newContact.setUserId(session.getUser().getUserId());
		
		IUser newUser_ = new User();
		newUser_.setName(newUser.getEmpName());
		newUser_.setUserId(newUser.getEmpCode());
		newUser_.setNetwork("local");
		newContact.setFriendId(newUser.getEmpCode());
		newContact.setFriend(newUser_);
		newContact.createDatabaseMe();
		newContact.flushDatabaseMe();
		
		newContact = new Contact();
		newContact.setUserId(newUser_.getUserId());
		
		IUser me_ = new User();
		me_.setName(session.getUser().getName());
		me_.setUserId(session.getUser().getUserId());
		me_.setNetwork("local");
		newContact.setFriendId(me_.getUserId());
		newContact.setFriend(me_);
		newContact.createDatabaseMe();
		newContact.flushDatabaseMe();
	}
	

	
	public void sendMailToNoUser(String authKey, String friendEmail) throws Exception {
		
		String from = GlobalContext.getPropertyString("codi.mail.support", "support@processcodi.com");
		String beforeName = "user.name";
		String afterName = session.getEmployee().getEmpName(); //초대 하는사람 이름
		String beforeCompany = "user.company";
		String afterCompany =  Employee.extractTenantName(session.getEmployee().getEmail()); //초대 하는사람


		String baseUrl = "base/";//CodiStringUtil.lastLastFileSeparatorChar(TenantURL.getURL());

		String url = "";
		url += baseUrl + "activate.html?key=" + authKey;
		String beforeFaceIcon = "face.icon";
		String afterFaceIcon = session.getEmployee().getEmpCode();
		String baseLinkUrl = "base.url";
		String signUpURL = "signup.url";
		
		String content;
		String tempContent = "";
		
		String resourcePath = CodiStringUtil.lastLastFileSeparatorChar(new HttpServletRequestWrapper(TransactionContext.getThreadLocalInstance().getRequest()).getRealPath(""));
		String path = resourcePath + GlobalContext.getPropertyString("email.invite", "resources/mail/invitationMail.html");

		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		FileInputStream is;
		try {
			is = new FileInputStream(path);
			InputStreamReader isr = new InputStreamReader(is,"UTF-8");
			BufferedReader br = new BufferedReader(isr);
			
			int data;
			while((data = br.read()) != -1){
				tempContent += (char)data;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		String title = afterCompany+" 의 "+ afterName + " 님이  당신을  코디에 초대 하였습니다";
		
		
		Login login = new Login();
		content = login.replaceString(tempContent,beforeName,afterName);
		content = login.replaceString(content, beforeCompany, afterCompany);
		content = login.replaceString(content, baseLinkUrl, baseUrl);
		content = login.replaceString(content, signUpURL, url);
		content = login.replaceString(content, beforeFaceIcon, afterFaceIcon);
		System.out.println(content);
		
		try{
			(new EMailServerSoapBindingImpl()).sendMail(from, friendEmail, title, content);
		}catch(Exception e){
			throw new Exception("$FailedToSendInvitationMail");
		}
	}

}
