package org.uengine.codi.mw3;

import org.metaworks.MetaworksException;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dwr.MetaworksRemoteService;
import org.metaworks.website.MetaworksFile;
import org.uengine.codi.mw3.email.sender.SignUpInvitationEmailSender;
import org.uengine.codi.mw3.model.Company;
import org.uengine.codi.mw3.model.Employee;
import org.uengine.codi.mw3.model.ICompany;
import org.uengine.codi.mw3.model.IEmployee;
import org.uengine.kernel.GlobalContext;
import java.util.UUID;

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
		//Login login = new Login();
        Login login = MetaworksRemoteService.getComponent(Login.class);
		login.getMetaworksContext().setWhere("index");
		login.getMetaworksContext().setHow("signup");
		
		setForOCE("true".equals(GlobalContext.getPropertyString("forOCE", "false")));

		this.setLogin(login);
	}
	
	@ServiceMethod(target=ServiceMethodContext.TARGET_SELF)
	public Object goLogin(){
		return MetaworksRemoteService.getComponent(Login.class);
	}

    public IEmployee signUp(String email, boolean signUpCompany) throws Exception{
        ICompany company = null;
        if(signUpCompany)
            company = signUpCompany(email);
        IEmployee employee = signUpEmployee(email, company);
        return employee;
    }

    public IEmployee signUpEmployee(String email, ICompany company) throws Exception{
        Employee employee = new Employee();
        employee.setEmail(email);
        IEmployee employeeRef = employee.findByEmail();

        // already sign up or invite user
        if (employeeRef != null) {
            if (employeeRef.isApproved()) {
                throw new Exception("$AlreadyExistingUser");
            } else {
                SignUpInvitationEmailSender signUpInvitationEmailSender = new SignUpInvitationEmailSender();
                signUpInvitationEmailSender.send("activate.html?key=" + employeeRef.getAuthKey(), email);

                return employeeRef;
            }
        }

        if(company != null){
            employee.setGlobalCom(company.getComCode());
            employee.setPreferUX("wave");
            employee.setPreferMob("auto");
        }

        String authKey = UUID.randomUUID().toString();
        String empName = employee.getEmail().substring(0, employee.getEmail().indexOf("@"));

        employee.setAuthKey(authKey);
        employee.setEmpName(empName);
        employee.setIsAdmin(false);
        employee.setIsDeleted("0");
        employee.setEmpCode(employee.createNewId());
        employee.setLocale("en");

        try {
            employee.createDatabaseMe();
        } catch (Exception e) {
            e.printStackTrace();
            throw new MetaworksException(e.getMessage());
        }

        SignUpInvitationEmailSender signUpInvitationEmailSender = new SignUpInvitationEmailSender();
        signUpInvitationEmailSender.send("activate.html?key=" + authKey, email);

        return employee;
    }

    public ICompany signUpCompany(String email) throws Exception {
        return signUpCompany(email, null, null);
    }

    public ICompany signUpCompany(String email, MetaworksFile logo, String description) throws Exception {
        String comAlias = Employee.extractTenantName(email);
        boolean isAdmin = false;
        Company company = new Company();
        company.setAlias(comAlias);

        ICompany findCompany = company.findByAlias();
        if(findCompany != null)
            throw new MetaworksException("Company is already sign up!");

        isAdmin = true;
        // not yet sign up tenant
        try {
            company.setLogo(logo);
            company.setDescription(description);
            company.setComCode(company.createNewId());
            company.setComName(comAlias);
            findCompany = company.createDatabaseMe();
        } catch (Exception e) {
            e.printStackTrace();
            throw new MetaworksException(e.getMessage());
        }

        return findCompany;
    }
}
