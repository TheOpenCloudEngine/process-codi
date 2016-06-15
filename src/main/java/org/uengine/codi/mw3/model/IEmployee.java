package org.uengine.codi.mw3.model;

import org.metaworks.annotation.*;
import org.metaworks.dao.IDAO;

@Table(name = "emptable")
public interface IEmployee extends IDAO {
    @Id
	public String getEmpCode();
	public void setEmpCode(String empCode);

	@Face(displayName="$Name")
	public String getEmpName();
	public void setEmpName(String empName);

	public String getAuthKey();
	public void setAuthKey(String authKey);
	
	public String getFacebookId();
	public void setFacebookId(String facebookId);

	@Face(displayName="$Password", options="type", values="password")
	public String getPassword();
	public void setPassword(String password);
	
	@Face(displayName="$Position")
	public String getJikName();
	public void setJikName(String jikName);
	
	public String getPartCode();	
	public void setPartCode(String partCode);

	public String getGlobalCom();	
	public void setGlobalCom(String comCode);
	
	public String getIsDeleted();
	public void setIsDeleted(String deleted);

	public String getMobileNo();
	public void setMobileNo(String mobileNo);

	@Face(displayName="$Email")
	public String getEmail();
	public void setEmail(String email);

	public String getLocale();
	public void setLocale(String locale);

	public String getPreferUX();
	public void setPreferUX(String preferUX);

	public String getPreferMob();
	public void setPreferMob(String preferMob);

	public boolean getIsAdmin();
	public void setIsAdmin(boolean value);

	public String getMood();
	public void setMood(String mood);

	public boolean isApproved();
	public void setApproved(boolean approved);

	public boolean isGuest();
	public void setGuest(boolean guest);
	
	public boolean isMailNoti();
	public void setMailNoti(boolean mailNoti);
	
	public String getInviteUser();
	public void setInviteUser(String inviteUser);

    @ORMapping(databaseFields={"partCode", "partName"}, objectFields={"partCode", "partName"})
    @NonSavable
    public IDept getDept();
    public void setDept(IDept dept);

    @ServiceMethod(callByContent = true)
    public IEmployee load() throws Exception;

	public IEmployee findMe() throws Exception;
	public IEmployee findMeByEmpName() throws Exception;
	public IEmployee findByDept(Dept dept) throws Exception;
	public IEmployee findByRole(Role role) throws Exception;
	public IEmployee findByDeptOther(String empCode) throws Exception;
	public IEmployee findByGlobalCom(String GlobalCom) throws Exception;
    public IEmployee findByEmail() throws Exception;
    public IEmployee findForLogin();
    public IEmployee findByKey();
    public IUser getUser();
}
