package org.uengine.codi.mw3.model;

import org.metaworks.Forward;
import org.metaworks.MetaworksContext;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.Available;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.Name;
import org.metaworks.annotation.NonLoadable;
import org.metaworks.annotation.NonSavable;
import org.metaworks.annotation.ORMapping;
import org.metaworks.annotation.Range;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.annotation.Table;
import org.metaworks.annotation.Validator;
import org.metaworks.annotation.ValidatorContext;
import org.metaworks.annotation.ValidatorSet;
import org.metaworks.dao.IDAO;

@Table(name = "emptable")
//@Face(options={"fieldOrder"}, values={"email,empName,globalCom,password,confirmPassword"})
@Face(ejsPathMappingByContext={
		"{how: 'signUp', face: 'dwr/metaworks/org/uengine/codi/mw3/model/IEmployeeSignUp.ejs'}"
}, options={"fieldOrder"}, values={"empName,password"})
public interface IEmployee extends IDAO {

/*	
    @Id
	@ORMapping(databaseFields={"empCode", "empName"}, objectFields={"userId", "name"})
	public IUser getUser();
	public void setUser(IUser user);
*/	
	@Id
/*
	@ValidatorSet({
		@Validator(name=ValidatorContext.VALIDATE_NOTNULL, message="이름을 입력해 주십시오."),
		@Validator(name=ValidatorContext.VALIDATE_MIN, options={"3"}, message="이름은 3자 이상 입력하셔야 합니다."),
	})
*/
	public String getEmpCode();
	public void setEmpCode(String empCode);

	@Face(displayName="$Name")
	@Name
	@ValidatorSet({
		@Validator(name=ValidatorContext.VALIDATE_NOTNULL, message="이름을 입력하세요."),
		@Validator(name=ValidatorContext.VALIDATE_MAX , options={"200"}, message="이름은 200자 이내로 입력하셔야 합니다.")
	})	
	public String getEmpName();
	public void setEmpName(String empName);

	public String getAuthKey();
	public void setAuthKey(String authKey);
	
	@Hidden
	public String getFacebookId();
	public void setFacebookId(String facebookId);
	
	@Hidden(when = "view")
	@ValidatorSet({
		@Validator(name=ValidatorContext.VALIDATE_NOTNULL, message="비밀번호를 입력해 주세요."),
	})
	@Face(displayName="$Password", options="type", values="password")
	public String getPassword();
	public void setPassword(String password);
	
//	@Hidden(when = "view")
	@Face(options="type", values="password")
	@NonLoadable
	@NonSavable
	@Validator(name=ValidatorContext.VALIDATE_CONDITION, condition="metaworksContext.where == 'inDetailWindow'", options="(password != confirmPassword)", message="패스워드와 동일하게 다시 입력해 주세요.")
	public String getConfirmPassword();
	public void setConfirmPassword(String confirmPassword);
	
	@Face(displayName="$Position")
	@Validator(name=ValidatorContext.VALIDATE_MAX , options={"20"}, message="직급/직책은 20자 이내로 입력하셔야 합니다.")
	public String getJikName();
	public void setJikName(String jikName);

	@ORMapping(databaseFields={"partCode", "partName"}, objectFields={"partCode", "partName"})
	@NonSavable
	public IDept getDept();
	public void setDept(IDept dept);
	
	public String getPartCode();	
	public void setPartCode(String partCode);
	
	@Face(displayName="$Dept")
	@NonSavable
	public String getPartName();	
	public void setPartName(String partName);
	
	@ValidatorSet({
		@Validator(name=ValidatorContext.VALIDATE_NOTNULL,condition="metaworksContext.when == 'new2'", message="회사이름을 입력하세요")
	})	
	public String getGlobalCom();	
	public void setGlobalCom(String comCode);
	
	@Hidden
	public String getIsDeleted();	
	public void setIsDeleted(String deleted);

	@Validator(name=ValidatorContext.VALIDATE_MAX , options={"20"}, message="전화번호는 20자 이내로 입력하셔야 합니다.")
	public String getMobileNo();
	public void setMobileNo(String mobileNo);
	
	
	
	@Face(displayName="$Email")
	@ValidatorSet({
		@Validator(name=ValidatorContext.VALIDATE_NOTNULL, message="이메일을 입력하세요."),
		@Validator(name=ValidatorContext.VALIDATE_REGULAREXPRESSION, options={"/^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$/"}, message="이메일 형식이 잘못되었습니다"),
		@Validator(name=ValidatorContext.VALIDATE_MAX , options={"50"}, message="이메일은 50자 이내로 입력하셔야 합니다.")
	})
	//@Pattern(regexp="/^([0-9a-zA-Z_\\.-]+)@([0-9a-zA-Z_-]+)(\\.[0-9a-zA-Z_-]+){1,2}$/", message="이메일 형식이 잘못되었습니다")
	public String getEmail();
	public void setEmail(String email);

	@Range(
			values={"ko"},
			options={"Korean"}			
//			values={"en", "ko", "jp", "cn", "joodle"},
//			options={"English", "Korean", "Japanese", "Chinese", "joodle"}	
			)
	public String getLocale();
	public void setLocale(String locale);
	
	
	@Range(
			values={"tw", "sns", "outlook", "asana", "wave"},
			options={"I love twitter", "I love facebook", "I love Outlook", "I love Asana", "I love Google Wave"}			
			)
	public String getPreferUX();
	public void setPreferUX(String preferUX);

	@Range(
			values={"auto", "phone", "pad", "desktop"},
			options={"Auto Detection", "Phone (1-column)", "Pad (2-column)", "Desktop version"}			
			)
	public String getPreferMob();
	public void setPreferMob(String preferMob);

	
	@NonLoadable
	@NonSavable
	@Available(when={WHEN_EDIT, WHEN_NEW})
	public PortraitImageFile getImageFile();
	public void setImageFile(PortraitImageFile imageFile);
	
	@ServiceMethod(callByContent = true)
	public IEmployee load() throws Exception;

	public abstract boolean getIsAdmin();
	public abstract void setIsAdmin(boolean value);
	
	public String getMood();
	public void setMood(String mood);

	public boolean isApproved();
	public void setApproved(boolean approved);

	public boolean isGuest();
	public void setGuest(boolean guest);
	
	public boolean isMailNoti();
	public void setMailNoti(boolean mailNoti);
	
	@Hidden
	public String getInviteUser();
	public void setInviteUser(String inviteUser);
	
	@NonLoadable
	@NonSavable
	@Hidden
	public boolean isValidEmail();
	public void setValidEmail(boolean validEmail);	
		
	public IEmployee findMe() throws Exception;	
	public IEmployee findMeByEmpName() throws Exception;
	public IEmployee findByDept(Dept dept) throws Exception;
	public IEmployee findByRole(Role role) throws Exception;
	public IEmployee findByDeptOther(String empCode) throws Exception;
	public IEmployee findByGlobalCom(String GlobalCom) throws Exception;
	
	@ServiceMethod(where="navigator", payload={"empCode", "empName", "jikName"})
	public Object[] loadOrganization() throws Exception;
	
	@ServiceMethod(target="append", where="picker")
	public Object pickup() throws Exception;
	
	@ServiceMethod(target="popup")
	public Popup openPicker();
	
	@ServiceMethod(callByContent=true, when=MetaworksContext.WHEN_NEW)
	public Object[] subscribeStep1() throws Exception;
	
	@ServiceMethod(callByContent=true, when=MetaworksContext.WHEN_NEW, validate=true)
	public Object subscribeStep2() throws Exception;
	
	@ServiceMethod(callByContent=true, when=MetaworksContext.WHEN_NEW, validate=true)
	public Object subscribeStep3() throws Exception;
	
	@ServiceMethod(callByContent=true, when=MetaworksContext.WHEN_NEW, validate=true, target=ServiceMethodContext.TARGET_POPUP)
	public Object[] saveEmployeeInfo() throws Exception;
	
	@ServiceMethod(callByContent=true)
	public Object[] closeEmployeeInfo() throws Exception;
	
	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_NONE)
	public void saveMe() throws Exception;
	
	/*
	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_NONE)
	boolean createCodi() throws Exception;
	
	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_NONE)
	void createCodiThumNail(String target) throws Exception;
	*/
		
	@ServiceMethod(callByContent=true, target="popup")
	public void addTopicUser() throws Exception;
	
	@ServiceMethod(callByContent=true)
	public void checkEmpCode() throws Exception ;
	
	@ServiceMethod(payload={"email"}, target=ServiceMethodContext.TARGET_NONE)
	public String checkId() throws Exception ;
	
	@ServiceMethod(needToConfirm=true, inContextMenu=true)
	@Face(displayName="$Unsubscribe")
	@Available(where={"inDetailWindow", "inDetailPopup"})
	public Object[] unsubscribe() throws Exception;
	
	@Available(how={"tree"}) // 상황에 맞춰서 넣어 줘야 한다.
	@ServiceMethod(callByContent=true, mouseBinding="drag-enableDefault")
	public Session drag() throws Exception;
	
	@Hidden(where={"step1"})
	@ServiceMethod(callByContent=true)
	public void prevStep();
	
	@Hidden(where={"step2"})
	@ServiceMethod(callByContent=true, validate=true)
	public void nextStep();
	
	@Available(where={"step2", "forgotpassword"})
	@ServiceMethod(callByContent=true, validate=true)
	public Forward finish() throws Exception;
	
	public IEmployee findCompanyAdmin() throws Exception;
	
	public IUser getUser();
	
	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_NONE)
	public Object facebookSSO() throws Exception;
}
