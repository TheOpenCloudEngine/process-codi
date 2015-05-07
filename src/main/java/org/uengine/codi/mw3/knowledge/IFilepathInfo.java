package org.uengine.codi.mw3.knowledge;

import java.util.Date;

import javax.persistence.GeneratedValue;

import org.metaworks.annotation.Id;
import org.metaworks.annotation.Table;
import org.metaworks.dao.IDAO;

@Table(name="filepathinfo")
public interface IFilepathInfo  extends IDAO{
	
	public String getProjectId();
	public void setProjectId(String projectId);

	public int getReflectVer();
	public void setReflectVer(int reflectVer);

	public int getReleaseVer();
	public void setReleaseVer(int releaseVer);
	
	public String getWarFileName();
	public void setWarFileName(String warFileName);

	public String getSqlFileName();
	public void setSqlFileName(String sqlFileName);

	public String getWarPath();
	public void setWarPath(String warPath);

	public String getSqlPath();
	public void setSqlPath(String sqlPath);
	
	public String getFileType();
	public void setFileType(String fileType);

	@Id
	@GeneratedValue(generator="BPM_SEQ")
	public int getId();
	public void setId(int id);
	
	public Date getModdate();
	public void setModdate(Date moddate);

	public String getComment();
	public void setComment(String comment);

	public String getDistributor();
	public void setDistributor(String distributor);
}
