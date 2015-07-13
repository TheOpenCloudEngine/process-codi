package org.uengine.codi.mw3.model;

import javax.persistence.GeneratedValue;
import javax.validation.constraints.NotNull;

import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.Available;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.NonLoadable;
import org.metaworks.annotation.NonSavable;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.annotation.Table;
import org.metaworks.annotation.Validator;
import org.metaworks.annotation.ValidatorContext;
import org.metaworks.annotation.ValidatorSet;
import org.metaworks.dao.IDAO;
import org.metaworks.website.MetaworksFile;

@Table(name="roletable")
public interface IRole extends IDAO {
	
	@Id
	@GeneratedValue
	public String getRoleCode();
	public void setRoleCode(String roleCode);

	public String getComCode();
	public void setComCode(String comCode);

	public String getDescr();
	public void setDescr(String descr);

	public String getIsDeleted();
	public void setIsDeleted(String isDeleted);
	
	public String getUrl();
	public void setUrl(String url);
	
	public String getThumbnail();
	public void setThumbnail(String thumbnail);
	
	@NonLoadable
	@NonSavable
	public MetaworksFile getLogoFile();
	public void setLogoFile(MetaworksFile logoFile);
	
	@NonLoadable
	@NonSavable
	@Hidden
	public boolean isSelected();
	public void setSelected(boolean selected);
	
	
	@ValidatorSet({
		@Validator(name=ValidatorContext.VALIDATE_NOTNULL, message="역할 이름을 입력해주세요."),
		@Validator(name=ValidatorContext.VALIDATE_MAX , options={"20"}, message="20자 이내로 입력해주세요."),
		@Validator(name=ValidatorContext.VALIDATE_REGULAREXPRESSION, options={"/^[^~!@\\\\#$%^&*\\()\\-=+_\'\"]+$/"}, message="다음과 같은 문자는 입력 할 수 없습니다. ~!@#$%^&*()\\-=+_\'\"")
	})
	public String getRoleName();
	public void setRoleName(String roleName);
	
	@NonLoadable
	@NonSavable
	public EmployeeList getEmployeeList();
	public void setEmployeeList(EmployeeList employeeList);

	// service method
	// 역활 부분에서 리스트 이름이 가려 졌는데 payload에 descr을 추가한다.
	@ServiceMethod(target="self", payload={"roleCode", "selected", "descr"})
	void drillDown() throws Exception;

	@Face(displayName="$role.Subscribe")
	@ServiceMethod(target=ServiceMethodContext.TARGET_POPUP, needToConfirm=true)
	public Object subscribe() throws Exception;
	
	@ServiceMethod(target=ServiceMethodContext.TARGET_POPUP, needToConfirm=true)
	@Face(displayName="$Remove")
	public Object[] removeMe() throws Exception;

	@Face(displayName="$Edit")
	@ServiceMethod(target=ServiceMethodContext.TARGET_POPUP)
	public Popup editPopup() throws Exception;

	@Face(displayName="$Save")
	@ServiceMethod(callByContent=true, except={"employeeList"}, target=ServiceMethodContext.TARGET_APPEND, validate=true)
	Object[] saveMe() throws Exception;
	
	@ServiceMethod(callByContent=true, mouseBinding="drop", target=ServiceMethodContext.TARGET_APPEND)
	public Object[] drop() throws Exception;
	
	@ServiceMethod(callByContent=true)
	public Object[] loadRole() throws Exception;
	
	@Available(where=IUser.WHERE_PICKERLIST)
	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_APPEND, mouseBinding=ServiceMethodContext.MOUSEBINDING_LEFTCLICK)
	public Object pickup() throws Exception;
	
	// function
	public IRole findByGlobalCom() throws Exception;
	
	public IRole findByEmployee(IEmployee employee) throws Exception;
		
}
