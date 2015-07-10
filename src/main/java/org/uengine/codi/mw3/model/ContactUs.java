package org.uengine.codi.mw3.model;
import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.MetaworksException;
import org.metaworks.Remover;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.widget.ModalWindow;
import org.uengine.codi.mw3.admin.WebEditor;
import org.uengine.kernel.GlobalContext;
import org.uengine.webservices.emailserver.impl.EMailServerSoapBindingImpl;

@Face(ejsPath="dwr/metaworks/genericfaces/FormFace.ejs", options={"fieldOrder"}, values={"title,contents"})
public class ContactUs implements ContextAware{
	@AutowiredFromClient
	public Session session;
	
	String title;
		@Face(displayName="$ContactUs.title")
		public String getTitle() {
			return title;
		}
	
		public void setTitle(String title) {
			this.title = title;
		}
	
	WebEditor contents;
		@Face(displayName="$ContactUs.content")
		public WebEditor getContents() {
			return contents;
		}
		
		public void setContents(WebEditor contents) {
			this.contents = contents;
		}
		
	MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}
		
	
	public ContactUs(){
		this.setContents(new WebEditor());
		this.setMetaworksContext(new MetaworksContext());
		this.getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);
	}
	


	@ServiceMethod(callByContent=true)
	@Face(displayName="$sendContactUs")
	public Object sendFeedback() throws Exception{
		String contents = this.getContents().getContents();
		String adminEmail = GlobalContext.getPropertyString("contactUs.feedbackMail","help@uengine.org");  //help@uengine.org
		
		EMailServerSoapBindingImpl emailServerSoapBindingImpl = new EMailServerSoapBindingImpl();
		Company company = new Company();
		company.setComCode(session.getEmployee().getGlobalCom());
		ICompany iCompany;
		try {
			iCompany = company.findByCode();
//			String mailto = iCompany.getRepMail();
			emailServerSoapBindingImpl.sendMail(session.getEmployee().getEmail(), //송신자
					session.getUser().getName(),
					adminEmail, //수신자
					this.getTitle(), 
					contents,
					null,
					null,
					"UTF-8");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new MetaworksException("$FailedToSendInvitationMail");
		}
	
		
		return new Remover(new ModalWindow());
		
	}
}
