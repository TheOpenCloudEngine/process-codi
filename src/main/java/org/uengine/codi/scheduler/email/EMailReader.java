package org.uengine.codi.scheduler.email;

//import com.sun.mail.pop3.POP3SSLStore;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;

import org.metaworks.MetaworksContext;
import org.springframework.stereotype.Component;
import org.uengine.codi.mw3.admin.WebEditor;
import org.uengine.codi.mw3.model.Company;
import org.uengine.codi.mw3.model.EmailWorkItem;
import org.uengine.codi.mw3.model.Employee;
import org.uengine.codi.mw3.model.ICompany;
import org.uengine.codi.mw3.model.IEmployee;
import org.uengine.codi.mw3.model.User;
import org.uengine.processmanager.ProcessManagerRemote;

@Component
public class EMailReader {
	
	public ProcessManagerRemote processManager;
	
    public EMailReader() {
        
    }
  
    
    public static void dumpPart(Part p, StringBuffer sb, Properties mailInfo) throws Exception {
        //if (p instanceof Message)
        //    dumpEnvelope((Message)p, sb, mailInfo);
       
//        String ct = p.getContentType();
//        try {
//        	sb.append("CONTENT-TYPE: " + (new ContentType(ct)).toString());
//        } catch (ParseException pex) {
//        	sb.append("BAD CONTENT-TYPE: " + ct);
//        }
        
        /*
         * Using isMimeType to determine the content type avoids
         * fetching the actual content data until we need it.
         */
    	if(p.isMimeType("TEXT/HTML")){
    		sb.append((String)p.getContent()); 
    	} else if (p.isMimeType("text/plain")) {
//        	sb.append("This is plain text");
//        	sb.append("---------------------------");
//        	sb.append((String)p.getContent());        
        }  else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart)p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                dumpPart(mp.getBodyPart(i), sb, mailInfo);
            }
        }
    }
    
    public static void dumpEnvelope(Message m, StringBuffer sb, Properties mailInfo) throws Exception {        
        
        // SUBJECT
        sb.append("SUBJECT: " + m.getSubject() +"<br>");
        
        // DATE
        Date d = m.getSentDate();
        sb.append("SendDate: " +
                (d != null ? d.toString() : "UNKNOWN")+"<p>");
        

    }
    
    public void read() throws Exception{
    	
    	System.out.println("--- 메일 읽기 시작");
    	System.out.println("processManager");
    	System.out.println(processManager);
    	

  		Company company = new Company();
		ICompany findcompany = company.findMe();

		
   		Properties props = System.getProperties();
   		props.setProperty("mail.store.protocol", "imaps");
    				
		Session session = Session.getDefaultInstance(props, null);
		Store store = null;

		while(findcompany.next()){
			
			if(findcompany.getRepMlHst()==null || findcompany.getRepMail()==null || findcompany.getRepMlPwd()==null)
				continue;
			
			if(findcompany.getRepMail().equals("")){
				continue;
			}		

			try {
				store = session.getStore("imaps");
				
				org.uengine.codi.mw3.model.Session codiSession = new org.uengine.codi.mw3.model.Session();
				codiSession.setCompany(findcompany);
				
				/*
				try{
					Invitation invitation = new Invitation();
					invitation.setEmail(findcompany.getRepMail());
					invitation.session = codiSession;
					invitation.invite();
				}catch(Exception e){}
				*/
				
				Employee representiveMailEmp = new Employee();
				representiveMailEmp.setEmpCode("0");
				
				
				IEmployee repMailEmp = representiveMailEmp.findMe();
				
				repMailEmp.setGlobalCom(findcompany.getComCode());
				
				//store.connect("imap.gmail.com", "jinyoungj3@gmail.com", "29036jyjang");
								
				store.connect(findcompany.getRepMlHst(), findcompany.getRepMail(), findcompany.getRepMlPwd());
				
				System.out.println(findcompany.getAlias() + " : "+store);
	
				Folder inbox = store.getFolder("Inbox");
				inbox.open(Folder.READ_WRITE);
				
		    	Flags seen = new Flags(Flags.Flag.SEEN);
		        FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
		        
		        Message[] messages = inbox.search(unseenFlagTerm);
		        
		        
		        FetchProfile fp = new FetchProfile();
		        fp.add(FetchProfile.Item.ENVELOPE);        
		        inbox.fetch(messages, fp);
		  
	
				//Message messages[] = inbox.search()
				for(Message message:messages) {
					//System.out.println(message);
					Properties mailInfo = new Properties();
					StringBuffer sb = new StringBuffer();
					
					
			        Address[] from;
			        // FROM
			        from = message.getFrom();
			        
			        
			        Address[] to;// TO
			        to = message.getRecipients(Message.RecipientType.TO);
			        
			        Multipart mp = (Multipart)message.getContent();
			        BodyPart bp = mp.getBodyPart(0);
			        
			        System.out.println("subject : " + message.getSubject());
			        System.out.println("send date : " + message.getSentDate());
			        System.out.println("content : " + bp.getContent());
	
					
					dumpPart(message, sb, mailInfo);
					
					ArrayList<User> fromUsers = new ArrayList<User>();
	//	    					ArrayList<User> fromUsers = new ArrayList<User>();
					
					
					EmailWorkItem emailWorkItem = new EmailWorkItem();
					emailWorkItem.processManager = processManager;
					emailWorkItem.setMemo(new WebEditor());
					emailWorkItem.getMemo().setContents(sb.toString());
					emailWorkItem.getMetaworksContext().setWhen(MetaworksContext.WHEN_NEW);
//                    emailWorkItem.setNotReturn(true);

					User codi = new User();
					codi.setUserId(repMailEmp.getEmpCode());
					codi.setName(repMailEmp.getEmpName());
					
					emailWorkItem.setWriter(codi);
					emailWorkItem.setTitle(message.getSubject());
					emailWorkItem.setStartDate(message.getReceivedDate());
					
					User receiver = new User();
					receiver.setUserId(repMailEmp.getEmpCode());
					receiver.setName(repMailEmp.getEmpName());
					receiver.setNetwork("local");
					codiSession.setUser(receiver);
					codiSession.setEmployee(repMailEmp);
					codiSession.setCompany(findcompany);
					
					emailWorkItem.session = codiSession;
					//emailWorkItem.instanceViewContent.instanceView.session = codiSession;
					
					emailWorkItem.add();
					
					//emailWorkItem.flushDatabaseMe();
					
					message.setFlag(Flags.Flag.SEEN, true);
					//message.saveChanges();
	
				}
				
			} catch (NoSuchProviderException e) {
				
				throw new Exception("No Provider", e);
			} catch (MessagingException e) {
				throw new Exception("Messaging Error", e);
			} catch (Exception e) {
				throw new Exception("Error while getting email", e);
			} finally {
				if(store != null){
					store.close();
					store = null;
				}
			}
		}
    	
    }
    
   /* public void readEmailByUser() throws Exception{
    	
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");
		
		IEmployee employee = new Employee().auto();
		employee.setIsDeleted("0");
		employee.select();
		
		while(employee.next()){
			Employee findEmployee = new Employee();
			findEmployee.copyFrom(employee);
			org.uengine.codi.mw3.model.Session codiSession = new org.uengine.codi.mw3.model.Session();
			codiSession.setEmployee(findEmployee);
			codiSession.fillSession();
			if(codiSession != null && codiSession.getEmployee() != null 
					&& !"".equals(codiSession.getEmployee().getEmail()) && !"".equals(codiSession.getEmployee().getPassword()) ){
				String userEmail = codiSession.getEmployee().getEmail();
				String userPasssd = codiSession.getEmployee().getPassword();
				if( userEmail == null || "".equals(userEmail)){
					continue;
				}
	    			try {
	    				try{
	    					Invitation invitation = new Invitation();
	    					invitation.setEmail(userEmail);
	    					invitation.session = codiSession;
	    					invitation.invite();
	    				}catch(Exception e){}
	
	    				Session session = Session.getDefaultInstance(props, null);
	    				Store store = session.getStore("imaps");
	    				try{
	    					store.connect("imap.gmail.com", userEmail, userPasssd);
	    				}catch(Exception excp){
	    					System.err.println(userEmail + " can not connect google mail server! ");
	    					continue;
	    				}
	//    				store.connect("imap.gmail.com", "scottUengine@gmail.com", "uengine81");
	    				//"imap.gmail.com", "jinyoungj3@gmail.com", "29036jyjang");
	    				System.out.println(store);
	
	    				Folder inbox = store.getFolder("Inbox");
	    				inbox.open(Folder.READ_WRITE);
	    				
	    		    	Flags seen = new Flags(Flags.Flag.SEEN);
	    		        FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
	    		        
	    		        Message[] messages = inbox.search(unseenFlagTerm);
	    		        
	    		        
	    		        FetchProfile fp = new FetchProfile();
	    		        fp.add(FetchProfile.Item.ENVELOPE);        
	    		        inbox.fetch(messages, fp);
	    		  
	    				//Message messages[] = inbox.search()
	    				for(Message message:messages) {
	    					//System.out.println(message);
	    					Properties mailInfo = new Properties();
	    					StringBuffer sb = new StringBuffer();
	    					
	    			        Address[] from;
	    			        // FROM
	    			        from = message.getFrom();
	    			        
	    			        Address[] to;// TO
	    			        to = message.getRecipients(Message.RecipientType.TO);
	    			        
	    					dumpPart(message, sb, mailInfo);
	    					
	    					ArrayList<User> fromUsers = new ArrayList<User>();
	    					for(Address theFrom : from){
	        					IEmployee fromUserIsEmployee = new Employee().auto();
	        					String theFromString = theFrom.toString();
	        					String theFromName = theFromString;
	        					
	        					int whereMailAddress = theFromString.indexOf("<");
								if(whereMailAddress>-1){
	            					theFromName = theFromString.substring(0, whereMailAddress);
	            					theFromString = theFromString.substring(whereMailAddress+1, theFromString.length()-1);
	        					}
	        					
	        					fromUserIsEmployee.setEmail(theFromString);
	        					fromUserIsEmployee.select();
	        					
	        					if(fromUserIsEmployee.next()){
	        						User fromUser = new User();
	        						fromUser.setName(fromUserIsEmployee.getEmpName());
	        						fromUser.setUserId(fromUserIsEmployee.getEmpCode());
	        						fromUser.setNetwork("local");
	        						fromUsers.add(fromUser);
	        					}else{
	        						//TODO tries facebook, twitter, google+ by email searching
	        						User fromUser = new User();
	        						fromUser.setName(theFromName);
	        						fromUser.setUserId(theFromString);
	        						fromUser.setNetwork("ext");
	        						fromUsers.add(fromUser);					
	        					}
	    					}
	    					
	    					emailWorkItem.setMemo(new WebEditor());
	    					emailWorkItem.getMemo().setContents(sb.toString());
//	    					emailWorkItem.setInstantiation(true);
	    					
	    					User theFirstWriter;
	    					if(fromUsers.size() > 0){
	    						theFirstWriter = fromUsers.get(0);
	    					}else{
	    						theFirstWriter = new User();
	    						theFirstWriter.setName("unknown");
	    						theFirstWriter.setUserId("unknown");
	    						theFirstWriter.setNetwork("ext");
	    					}
	    					
	    					Address[] cc = message.getRecipients(Message.RecipientType.CC);
	    					ArrayList<User> ccUsers = new ArrayList<User>();
	    					if( cc != null ){
		    					for(Address ccUser : cc){
		        					IEmployee fromUserIsEmployee = new Employee().auto();
		        					String theFromString = ccUser.toString();
		        					String theFromName = theFromString;
		        					
		        					int whereMailAddress = theFromString.indexOf("<");
									if(whereMailAddress>-1){
		            					theFromName = theFromString.substring(0, whereMailAddress);
		            					theFromString = theFromString.substring(whereMailAddress+1, theFromString.length()-1);
		        					}
		        					
		        					fromUserIsEmployee.setEmail(theFromString);
		        					fromUserIsEmployee.select();
		        					
		        					if(fromUserIsEmployee.next()){
		        						User fromUser = new User();
		        						fromUser.setName(fromUserIsEmployee.getEmpName());
		        						fromUser.setUserId(fromUserIsEmployee.getEmpCode());
		        						fromUser.setNetwork("local");
		        						ccUsers.add(fromUser);
		        					}else{
		        						//TODO tries facebook, twitter, google+ by email searching
		        						User fromUser = new User();
		        						fromUser.setName(theFromName);
		        						fromUser.setUserId(theFromString);
		        						fromUser.setNetwork("ext");
		        						ccUsers.add(fromUser);					
		        					}
		    					}
	    					}
	    					Address[] bcc = message.getRecipients(Message.RecipientType.BCC);
	    					ArrayList<User> bccUsers = new ArrayList<User>();
	    					if( bcc != null ){
		    					for(Address bccUser : bcc){
		        					IEmployee fromUserIsEmployee = new Employee().auto();
		        					String theFromString = bccUser.toString();
		        					String theFromName = theFromString;
		        					
		        					int whereMailAddress = theFromString.indexOf("<");
									if(whereMailAddress>-1){
		            					theFromName = theFromString.substring(0, whereMailAddress);
		            					theFromString = theFromString.substring(whereMailAddress+1, theFromString.length()-1);
		        					}
		        					
		        					fromUserIsEmployee.setEmail(theFromString);
		        					fromUserIsEmployee.select();
		        					
		        					if(fromUserIsEmployee.next()){
		        						User fromUser = new User();
		        						fromUser.setName(fromUserIsEmployee.getEmpName());
		        						fromUser.setUserId(fromUserIsEmployee.getEmpCode());
		        						fromUser.setNetwork("local");
		        						bccUsers.add(fromUser);
		        					}else{
		        						//TODO tries facebook, twitter, google+ by email searching
		        						User fromUser = new User();
		        						fromUser.setName(theFromName);
		        						fromUser.setUserId(theFromString);
		        						fromUser.setNetwork("ext");
		        						bccUsers.add(fromUser);					
		        					}
		    					}
	    					}
	    					emailWorkItem.setWriter(theFirstWriter);
	    					emailWorkItem.setCcUsers(ccUsers);
	    					emailWorkItem.setBccUsers(bccUsers);
	    					emailWorkItem.setTitle(message.getSubject());
	    					emailWorkItem.setStartDate(message.getReceivedDate());
	    					
	    					User receiver = new User();
	    					receiver.setUserId(findEmployee.getEmpCode());
	    					receiver.setName(findEmployee.getEmpName());
	    					receiver.setNetwork("local");
	    					
	    					emailWorkItem.session = codiSession;
	    					emailWorkItem.instanceViewContent.instanceView.session = codiSession;
	    					emailWorkItem.add();
	    					
	    					message.setFlag(Flags.Flag.SEEN, true);
	    					//message.saveChanges();
	
	    				}
	    				
	    			} catch (NoSuchProviderException e) {
	        			
	        			throw new Exception("No Provider", e);
	        		} catch (MessagingException e) {
	        			throw new Exception("Messaging Error", e);
	        		} catch (Exception e) {
	        			throw new Exception("Error while getting email", e);
	    			}
				}
	    }
    }*/
    
}

