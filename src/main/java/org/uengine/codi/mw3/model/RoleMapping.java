package org.uengine.codi.mw3.model;

import org.metaworks.dao.Database;
import org.metaworks.dao.TransactionContext;
import org.metaworks.dao.UniqueKeyGenerator;

public class RoleMapping extends Database<IRoleMapping> implements IRoleMapping {

	Long roleMappingId;
	Long rootInstId;
	Long instId;
	String roleName;
	String endpoint;
	String resName;
	int assignType;
	int dispatchOption;

	public RoleMapping() {
		this.setDispatchOption(-1);
	}

	public RoleMapping(Long rootInstId, String roleName, String endpoint) {
		this();
		
		this.rootInstId = rootInstId;
		this.roleName = roleName;
		this.endpoint = endpoint;
	}

	@Override
	public Long getRoleMappingId() {
		return roleMappingId;
	}

	@Override
	public void setRoleMappingId(Long roleMappingId) {
		this.roleMappingId = roleMappingId;
	}

	@Override
	public Long getRootInstId() {
		return rootInstId;
	}

	@Override
	public void setRootInstId(Long rootInstId) {
		this.rootInstId = rootInstId;
	}

	@Override
	public String getRoleName() {
		return roleName;
	}

	@Override
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Override
	public String getEndpoint() {
		return endpoint;
	}

	@Override
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
	
	public String getResName() {
		return resName;
	}
	public void setResName(String resName) {
		this.resName = resName;
	}

	public Long getInstId() {
		return instId;
	}
	public void setInstId(Long instId) {
		this.instId = instId;
	}

	public int getAssignType() {
		return assignType;
	}
	public void setAssignType(int assignType) {
		this.assignType = assignType;
	}

	public int getDispatchOption() {
		return dispatchOption;
	}
	public void setDispatchOption(int dispatchOption) {
		this.dispatchOption = dispatchOption;
	}

	
	public boolean confirmFollower() throws Exception {
		StringBuffer querry = new StringBuffer();
		querry.append("select * from bpm_rolemapping ");
		querry.append("where rootinstid=?rootInstId ");
		querry.append("and rolename=?roleName ");
		querry.append("and endpoint=?endpoint ");
		
//		System.out.println(getRootInstId() + "," + getRoleName() + "," + getEndpoint());
		
		IRoleMapping findRoleMapping = (IRoleMapping)sql(querry.toString());
		
		findRoleMapping.setRootInstId(getRootInstId());
		findRoleMapping.setRoleName(getRoleName());
		findRoleMapping.setEndpoint(getEndpoint());
		findRoleMapping.select();
		if(findRoleMapping.next()) {
			return true;
		} else {
			return false;
		}		
	}
	
	public boolean deleteByInfo(Session session) throws Exception {
		RoleMapping roleMapping = new RoleMapping();
		StringBuffer querry = new StringBuffer();
		querry.append("select * from bpm_rolemapping ");
		querry.append("where rootinstid=?rootInstId ");
		querry.append("and rolename=?roleName ");
		querry.append("and endpoint=?endpoint ");
		
//		System.out.println(getRootInstId() + "," + getRoleName() + "," + getEndpoint());
		
		IRoleMapping findRoleMapping = (IRoleMapping)sql(querry.toString());
		
		findRoleMapping.setRootInstId(getRootInstId());
		findRoleMapping.setRoleName(getRoleName());
		findRoleMapping.setEndpoint(getEndpoint());
		findRoleMapping.select();
		if(findRoleMapping.next()) {
			roleMapping.setRoleMappingId(findRoleMapping.getRoleMappingId());
			roleMapping.deleteDatabaseMe();
			return true;
		} else {
			return false;
		}
	}
	
	public static IRoleMapping allFollower(Long instId) throws Exception{
		StringBuffer str = new StringBuffer();
		// rolomapping 테이블에서  follower 를 가져올 경우에 instId 와 endpoint가 동일한 결과를 가져올수 있다
		// 그리하여 group by 로 묶어서 중복된 데이터를 제거함
		str.append(" select * from ( ")
			.append("	select * from bpm_rolemapping where rootinstid=?rootInstId " )
			.append(" ) rolemapping ")
			.append(" group by rolemapping.endpoint ");
		
		IRoleMapping findRoleMapping = (IRoleMapping) Database.sql(IRoleMapping.class, str.toString());
		findRoleMapping.set("rootInstId", instId);
		findRoleMapping.select();
		
		return findRoleMapping;
	}
	
	public IRoleMapping findMe() throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT *");
		sql.append("  FROM bpm_rolemapping ");
		sql.append(" WHERE instid=?instid");
		sql.append("   AND endpoint=?endpoint");
		sql.append("   AND assigntype=?assigntype");
		sql.append("   AND rolename=?rolename");
		
		System.out.println(sql.toString());
		
		IRoleMapping rm = sql(sql.toString());
		rm.setInstId(this.getInstId());
		rm.setEndpoint(this.getEndpoint());
		rm.setAssignType(this.getAssignType());
		rm.setRoleName(this.getRoleName());
		rm.select();
		
		return rm;
	}

	public IFollower findFollower() throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT distinct rm.endpoint, rm.resname, rm.assigntype, CAST(rm.rootinstid as char) parentId, '" + Follower.TYPE_INSTANCE + "' parentType");
		sql.append("  FROM bpm_rolemapping rm");
		sql.append(" WHERE instid=?instid");
		sql.append("   AND endpoint=?endpoint");
		sql.append("   AND assigntype=?assigntype");
		//sql.append("   AND rolename=?rolename");
		
		IFollower follower = (IFollower) Database.sql(IFollower.class, sql.toString());
		follower.set("instid", this.getInstId());
		follower.set("endpoint", this.getEndpoint());
		follower.set("assigntype", this.getAssignType());
		//follower.set("rolename", this.getRoleName());
		
		follower.select();	
		
		return follower;
	}
	
	public IFollower findFollowers() throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT distinct rm.endpoint, ifnull(e.empname,rm.resname) resname, rm.assigntype, CAST(rm.rootinstid as char) parentId, '" + Follower.TYPE_INSTANCE + "' parentType");
		sql.append("  FROM bpm_rolemapping rm");
		sql.append("  LEFT JOIN emptable e on rm.endpoint = e.empcode and rm.assigntype = 0");
		sql.append(" WHERE rm.instId=?instId");
		
		IFollower follower = (IFollower) Database.sql(IFollower.class, sql.toString());
		follower.set("instId", this.getRootInstId());
		follower.select();	
		
		return follower;
	}

	public IRoleMapping saveMe() throws Exception{
		Long mappingId = UniqueKeyGenerator.issueRoleMappingKey(TransactionContext.getThreadLocalInstance());
		
		this.setRoleMappingId(mappingId);
		
		return createDatabaseMe();
	}
	
	public void removeMe() throws Exception{
		
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM bpm_rolemapping");
		sql.append(" WHERE instid=?instid");
		sql.append("   AND endpoint=?endpoint");
		sql.append("   AND assigntype=?assigntype");
		sql.append("   AND rolename=?rolename");
		
		System.out.println(sql.toString());
		
		IRoleMapping rm = sql(sql.toString());
		rm.setInstId(this.getInstId());
		rm.setEndpoint(this.getEndpoint());
		rm.setAssignType(this.getAssignType());
		rm.setRoleName(this.getRoleName());
		
		int updateCnt = rm.update();
		
		if(updateCnt == 0){
			System.out.println("Process participants can not be deleted.");
		}
	}
}

