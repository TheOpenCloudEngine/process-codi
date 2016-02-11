package org.uengine.codi.mw3.model;

import org.metaworks.Refresh;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.dao.Database;
import org.uengine.codi.mw3.knowledge.WfNode;

import java.util.ArrayList;

public class RoleMappingDefinition extends Database<IRoleMappingDefinition> implements IRoleMappingDefinition{
	
	public static final String ROLE_DEF_TYPE_ROLE = "role";  
	public static final String ROLE_DEF_TYPE_USER = "user";  
	
	String roleDefId;		
		public String getRoleDefId() {
			return roleDefId;
		}
		public void setRoleDefId(String roleDefId) {
			this.roleDefId = roleDefId;
		}

	String roleName;
		public String getRoleName() {
			return roleName;
		}
		public void setRoleName(String roleName) {
			this.roleName = roleName;
		}
		
	String defId;
		public String getDefId() {
			return defId;
		}
		public void setDefId(String defId) {
			this.defId = defId;
		}

//	IUser mappedUser;
//		public IUser getMappedUser() {
//			return mappedUser;
//		}
//		public void setMappedUser(IUser mappedUser) {
//			this.mappedUser = mappedUser;
//		}

	RoleMappedUser roleMappedUser;
		public RoleMappedUser getRoleMappedUser() {
			return roleMappedUser;
		}
		public void setRoleMappedUser(RoleMappedUser roleMappedUser) {
			this.roleMappedUser = roleMappedUser;
		}

	String mappedUserId;
		public String getMappedUserId() {
			return mappedUserId;
		}
		public void setMappedUserId(String mappedUserId) {
			this.mappedUserId = mappedUserId;
		}


	String mappedUserName;
		public String getMappedUserName() {
			return mappedUserName;
		}
		public void setMappedUserName(String mappedUserName) {
			this.mappedUserName = mappedUserName;
		}

	String comCode;
		public String getComCode() {
			return comCode;
		}
		public void setComCode(String comCode) {
			this.comCode = comCode;
		}
		
	String mappedRoleCode;
		public String getMappedRoleCode() {
			return mappedRoleCode;
		}
		public void setMappedRoleCode(String mappedRoleCode) {
			this.mappedRoleCode = mappedRoleCode;
		}
		
	String roleDefType;
		public String getRoleDefType() {
			return roleDefType;
		}
		public void setRoleDefType(String roleDefType) {
			this.roleDefType = roleDefType;
		}

	@AutowiredFromClient
	public Session session;
	
	@Override
	public void drop() {
		Object clipboard = session.getClipboard();
		if(clipboard instanceof IUser){
			
			IUser user = (IUser) clipboard;
			user.getMetaworksContext().setWhen(WHEN_EDIT);
			user.getMetaworksContext().setHow("picker");
			user.getMetaworksContext().setWhere(WHERE_EVER);
			//setMappedUser(user);

			setRoleMappedUser(new RoleMappedUser());
			getRoleMappedUser().setUsers(new ArrayList<IUser>());
			getRoleMappedUser().getUsers().add(user);
		}	
	
	}
	
	public void removeUser(){
		setRoleMappedUser(new RoleMappedUser());
	}
	
//	public void refresh() {
//		System.out.println("refresh");
//	}
	
	public RoleMappingDefinition findRoleMappingDefinition() throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT rd.*, emp.empname mappedUserName");
		sb.append("  FROM bpm_roledef rd, emptable emp");
		sb.append("  where rd.mappedUserId = emp.empcode ");
		sb.append("  and rd.roledefId=?roledefId ");
		
		IRoleMappingDefinition roleMappingDefinition = (IRoleMappingDefinition)sql(IRoleMappingDefinition.class, sb.toString());
		roleMappingDefinition.setRoleDefId(this.getRoleDefId());
		roleMappingDefinition.select();
		
		if( roleMappingDefinition.next() ){
			RoleMappingDefinition roleDef = new RoleMappingDefinition();
			roleDef.copyFrom(roleMappingDefinition);
			roleDef.setRoleMappedUser(new RoleMappedUser());

			User user = new User();
			user.setUserId(roleDef.getMappedUserId());
			roleDef.getRoleMappedUser().getUsers().add(user);


			return roleDef;
		}else{
			return null;
		}
		
	}
	
}
