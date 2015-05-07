package org.uengine.codi.mw3.model;

import javax.persistence.GeneratedValue;

import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.Available;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.Name;
import org.metaworks.annotation.NonLoadable;
import org.metaworks.annotation.NonSavable;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.annotation.Table;
import org.metaworks.annotation.Validator;
import org.metaworks.annotation.ValidatorContext;
import org.metaworks.annotation.ValidatorSet;
import org.metaworks.dao.IDAO;

@Face(ejsPath="dwr/metaworks/org/uengine/codi/mw3/model/IDept.ejs",
		ejsPathMappingByContext={"{how: '" + IUser.HOW_INFO + "', face: 'dwr/metaworks/org/uengine/codi/mw3/model/IDeptInfo.ejs'}"})
@Table(name = "PARTTABLE")
public interface IDept extends IDAO {
	public static final String CUSTOMER_DEPT_PARTCODE = "CUSTOMER_DEP";

	@Id
//	@Pattern(regexp="/[*a-z][*0-9]/", message="$deptcode.space.error.message")
	@GeneratedValue
	public String getPartCode();
	public void setPartCode(String partCode);

	@Name
	@ValidatorSet({
		@Validator(name=ValidatorContext.VALIDATE_NOTNULL, message="부서 이름을 입력해주세요."),
		@Validator(name=ValidatorContext.VALIDATE_MAX , options={"20"}, message="20자 이내로 입력해주세요."),
		@Validator(name=ValidatorContext.VALIDATE_REGULAREXPRESSION, options={"/^[^~!@\\\\#$%^&*\\()\\-=+_\'\"]+$/"}, message="다음과 같은 문자는 입력 할 수 없습니다. ~!@#$%^&*()\\-=+_\'\"")
	})
	public String getPartName();
	public void setPartName(String partName);

	public String getParent_PartCode();
	public void setParent_PartCode(String parentPartCode);

	@Hidden
	public String getIsDeleted();
	public void setIsDeleted(String deleted);

	public String getDescription();
	public void setDescription(String description);

	@NonSavable
	@NonLoadable
	public PortraitImageFile getLogoFile();
	public void setLogoFile(PortraitImageFile logoFile);
	
	public String getGlobalCom();
	public void setGlobalCom(String globalCom);

	@Hidden
	@NonSavable
	@NonLoadable
	public boolean getSelected() throws Exception;
	public void setSelected(boolean value) throws Exception;

	@NonSavable
	@NonLoadable
	public DeptList getChildren() throws Exception;
	public void setChildren(DeptList children) throws Exception;
	
	@NonSavable
	public boolean isFollowed();
	public void setFollowed(boolean followed);
	
	@NonSavable
	public String getDeptPath();
	public void setDeptPath(String deptPath);
	
	@NonSavable
	@NonLoadable
	public EmployeeList getDeptEmployee() throws Exception;
	public void setDeptEmployee(EmployeeList deptEmployee) throws Exception;
	
	// methods
	public IDept load() throws Exception;

	public IDept findByGlobalCom() throws Exception;

	public IDept findTreeByGlobalCom(String globalCom) throws Exception;


	public IDept findChildren() throws Exception;
	
	public IDept findRootDeptByGlobalCom(String globalCom) throws Exception;

	@Available(where=IUser.WHERE_ADDFOLLOWER)
	@ServiceMethod(callByContent=true, target=TARGET_APPEND)
	public Object[] addFollower() throws Exception;
	
	@Available(where=IUser.WHERE_FOLLOWERS)
	@ServiceMethod(callByContent=true, target=TARGET_APPEND)
	public Object[] removeFollower() throws Exception;

	// service methods
	@Available(condition="(typeof self == 'undefined' || !self)")
	@ServiceMethod(target="self", callByContent = true, payload = { "partCode", "selected" })
	public void drillDown() throws Exception;
	
	@Available(condition="(typeof self == 'undefined' || !self)")
	@Hidden(how={IUser.HOW_INFO, IUser.HOW_PICKER}, where={IUser.WHERE_ADDFOLLOWER, IUser.WHERE_FOLLOWERS})
	public Popup detail() throws Exception;

	@Available(condition="(typeof self == 'undefined' || !self)")
	@ServiceMethod(target=ServiceMethodContext.TARGET_POPUP)
	public Object editDeptInfo() throws Exception;

	@Available(condition="(typeof self == 'undefined' || !self)")
	@ServiceMethod(callByContent = true, target=ServiceMethodContext.TARGET_APPEND, validate=true) 
	public Object[] saveDeptInfo() throws Exception;

	@Available(condition="(typeof self == 'undefined' || !self)")
	@ServiceMethod(needToConfirm=true , target=ServiceMethodContext.TARGET_APPEND)
	public Object[] deleteDept() throws Exception;
	
	@Available(condition="(typeof self == 'undefined' || !self)")
	@ServiceMethod(callByContent = true)
	public Object[] restoreDept() throws Exception;
			
	@Available(condition="(typeof self == 'undefined' || !self)")
	@ServiceMethod(target=ServiceMethodContext.TARGET_POPUP)
	public Object addNewChildDept() throws Exception;

	@Available(condition="(typeof self == 'undefined' || !self)")
	@ServiceMethod(target=ServiceMethodContext.TARGET_POPUP, needToConfirm=true)
	public Object[] subscribe() throws Exception;

	
	@Available(condition="(typeof self == 'undefined' || !self)")
	@ServiceMethod(target="popup")
	public Popup openPicker() throws Exception;
	
	@Available(where=IUser.WHERE_PICKERLIST)
	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_APPEND, mouseBinding=ServiceMethodContext.MOUSEBINDING_LEFTCLICK)
	public Object pickup() throws Exception;
	
	@ServiceMethod(callByContent=true, mouseBinding="drop", target=ServiceMethodContext.TARGET_APPEND)
	public Object[] drop() throws Exception;

	@Available(condition="(typeof self == 'undefined' || !self)")
	@ServiceMethod(payload={"partCode", "partName"})
	public Object[] loadDeptList() throws Exception;
	
}
