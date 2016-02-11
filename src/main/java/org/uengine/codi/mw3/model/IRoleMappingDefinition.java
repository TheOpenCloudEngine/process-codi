package org.uengine.codi.mw3.model;

import org.metaworks.EventContext;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.NonSavable;
import org.metaworks.annotation.ORMapping;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.annotation.Table;
import org.metaworks.dao.IDAO;

@Table(name="bpm_roledef")
public interface IRoleMappingDefinition extends IDAO{
	
	@Id
	public String getRoleDefId();
	public void setRoleDefId(String roleDefId);
	
	public String getRoleName();
	public void setRoleName(String roleName);
		
	public String getDefId();
	public void setDefId(String defId);

	public String getComCode();
	public void setComCode(String comCode);

	
//	@ORMapping(databaseFields="mappedUserId", objectFields="userId")
//	public IUser getMappedUser();
//	public void setMappedUser(IUser mappedUser);

	public String getMappedUserId();
	public void setMappedUserId(String mappedUserId);

	@NonSavable
	public String getMappedUserName();
	public void setMappedUserName(String mappedUserName);
	
	
	public String getMappedRoleCode();
	public void setMappedRoleCode(String mappedRoleCode);
	
	public String getRoleDefType();
	public void setRoleDefType(String roleDefType);
	
	@ServiceMethod(callByContent=true, mouseBinding="drop", target=ServiceMethodContext.TARGET_SELF)
	public void drop();
	
	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_SELF)
	public void removeUser();

//	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_SELF, eventBinding=EventContext.EVENT_CHANGE)
//	public void refresh();
}
