package org.uengine.codi.mw3.knowledge;

import java.util.Date;

import org.metaworks.annotation.Id;
import org.metaworks.annotation.Table;
import org.metaworks.dao.IDAO;

@Table(name="buildInfo")
public interface IBuildInfo extends IDAO{

	@Id
	public String getId();
	public void setId(String id);

	
	public String getProjectId();
	public void setProjectId(String projectId);
	
	public String getProjectName();
	public void setProjectName(String projectName);
	
	public int getMajorVer();
	public void setMajorVer(int majorVer);
	
	public int getMinorVer();
	public void setMinorVer(int minorVer);
	
	public int getBuildVer();
	public void setBuildVer(int buildVer);
	
	public String getUserName();
	public void setUserName(String userName);

	public String getUserId();
	public void setUserId(String userId);
	
	public Date getModDate();
	public void setModDate(Date modDate);
	
	public String getComment() ;
	public void setComment(String comment);
	
	public Boolean getDevDistributed() ;
	public void setDevDistributed(Boolean devDistributed);
	
	public Boolean getProdDistributed();
	public void setProdDistributed(Boolean prodDistributed);
	
}
