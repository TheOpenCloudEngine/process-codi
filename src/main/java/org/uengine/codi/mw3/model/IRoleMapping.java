package org.uengine.codi.mw3.model;

import org.metaworks.annotation.Id;
import org.metaworks.annotation.Table;
import org.metaworks.dao.IDAO;

@Table(name = "BPM_ROLEMAPPING")
public interface IRoleMapping extends IDAO {

	public static final String ROLEMAPPING_FOLLOWER_ROLENAME = "Follower";
	
	@Id
	public Long getRoleMappingId();
	public void setRoleMappingId(Long roleMappingId);

	public Long getRootInstId();
	public void setRootInstId(Long rootInstId);

	public String getRoleName();
	public void setRoleName(String roleName);

	public String getEndpoint();
	public void setEndpoint(String endpoint);

	public String getResName();
	public void setResName(String resName);

	public Long getInstId();
	public void setInstId(Long instId);

	public int getAssignType();
	public void setAssignType(int assignType);

	public int getDispatchOption();
	public void setDispatchOption(int dispatchOption);

}
