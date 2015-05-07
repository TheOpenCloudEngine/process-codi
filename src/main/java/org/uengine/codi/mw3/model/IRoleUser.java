package org.uengine.codi.mw3.model;

import org.metaworks.annotation.Table;
import org.metaworks.dao.IDAO;

@Table(name="roleUserTable")
public interface IRoleUser extends IDAO {
	public String getRoleCode();
	public void setRoleCode(String roleCode);

	public String getEmpCode();
	public void setEmpCode(String empCode);
	
	public int removeMe() throws Exception;
	
	public IRoleUser findMe() throws Exception;
}
