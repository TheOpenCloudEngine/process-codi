package org.uengine.codi.mw3.model;

import org.metaworks.dao.Database;

public class RoleUser extends Database<IRoleUser> implements IRoleUser {
	String roleCode;
		@Override
		public String getRoleCode() {
			return roleCode;
		}
		@Override
		public void setRoleCode(String roleCode) {
			this.roleCode = roleCode;
		}

	String empCode;
		@Override
		public String getEmpCode() {
			return empCode;
		}
		@Override
		public void setEmpCode(String empCode) {
			this.empCode = empCode;
		}
		
	public IRoleUser findUserByRoleCode() throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("select * from roleusertable");
		sb.append(" WHERE roleCode=?roleCode ");
		
		IRoleUser dao = (IRoleUser) sql(IRoleUser.class, sb.toString());
		
       	dao.setRoleCode(this.getRoleCode());
       	
       	dao.select();
       	
       	return dao;
	}
		
	public int removeMe() throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("delete from roleusertable");
		sb.append(" WHERE roleCode=?roleCode");
		sb.append("   AND empCode=?empCode");
		
		IRoleUser dao = (IRoleUser) sql(IRoleUser.class, sb.toString());
		
       	dao.set("roleCode", this.getRoleCode());
       	dao.set("empCode", this.getEmpCode());
        return dao.update();
	}
	
	public IUser findByRole() throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("select emptable.empcode as userId, emptable.empname as name ");
		sb.append(" from emptable left outer join roleusertable ");
		sb.append("   on emptable.empcode=roleusertable.EMPCODE ");
		sb.append(" where rolecode=?rolecode");
		
		IUser dao = (IUser) sql(IUser.class, sb.toString());
		
       	dao.set("roleCode", this.getRoleCode());
       	dao.select();
       	return dao;
	}
	
	public IRoleUser findMe() throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("select * from roleUserTable ");
		sb.append("  where roleCode=?roleCode ");	
		sb.append("   AND empCode=?empCode ");
		
		IRoleUser dao = null;
		
		try {
			dao = sql(sb.toString());
			dao.setRoleCode(this.getRoleCode());
			dao.setEmpCode(this.getEmpCode());
			dao.select();
			
			if(!dao.next())
				dao = null;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return dao;
	}
	
	public int removeUser() throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("delete from roleUserTable ");
		sb.append("  where roleCode=?roleCode ");	
		sb.append("   AND empCode=?empCode ");
		
		IRoleUser dao = null;
		
		dao = sql(sb.toString());
		dao.setRoleCode(this.getRoleCode());
		dao.setEmpCode(this.getEmpCode());
		
		return dao.update();
		
	}
	
	public IFollower findFollowers() throws Exception {
		
		StringBuffer sql = new StringBuffer();
		sql.append("select emptable.empcode as endpoint, emptable.empname as resname, rolecode parentId, '" + Follower.TYPE_ROLE + "' parentType");
		sql.append(" from emptable left outer join roleusertable ");
		sql.append("   on emptable.empcode=roleusertable.EMPCODE ");
		sql.append(" where rolecode=?rolecode");
		
		IFollower follower = (IFollower) Database.sql(IFollower.class, sql.toString());
		follower.set("rolecode", this.getRoleCode());
		follower.select();	
		
		return follower;
		
	}
	
	public IFollower findFollower() throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select emptable.empcode as endpoint, emptable.empname as resname, rolecode parentId, '" + Follower.TYPE_ROLE + "' parentType");
		sql.append("  from emptable left outer join roleusertable ");
		sql.append("    on emptable.empcode=roleusertable.EMPCODE ");
		sql.append(" where rolecode=?rolecode");
		sql.append("   and emptable.empcode=?empcode");
		
		IFollower follower = (IFollower) Database.sql(IFollower.class, sql.toString());
		follower.set("rolecode", this.getRoleCode());
		follower.set("empcode", this.getEmpCode());
		follower.select();	
		
		return follower;	
	}
}
