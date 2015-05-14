package org.uengine.codi.mw3.marketplace;

import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.*;
import org.metaworks.dao.IDAO;
import org.metaworks.website.MetaworksFile;
import org.uengine.codi.mw3.common.MainPanel;
import org.uengine.codi.mw3.model.IUser;

import java.util.Date;

@Table(name="APPMAPPING")
@Face(ejsPathMappingByContext={"{where: 'mapList', face: 'dwr/metaworks/org/uengine/codi/mw3/marketplace/IAppMap.ejs'}"})
public interface IAppMapping extends IDAO{
	
	@Id
	public int getAppId();
	public void setAppId(int appId);

	public String getComCode();
	public void setComCode(String comCode);

	@Name
	public String getAppName();
	public void setAppName(String appName);

	public boolean getIsDeleted();
	public void setIsDeleted(boolean isDeleted);
	
	public String getUrl();
	public void setUrl(String url);
	
	public String getAppType();
	public void setAppType(String appType);

	@NonSavable
	public String getProjectName();
	public void setProjectName(String projectName);
	
	@NonSavable
	@NonLoadable
	@ORMapping(
		databaseFields={"logoContent", "logoFileName"}, 
		objectFields={"uploadedPath", "filename"}
	)
	public MetaworksFile getLogoFile();
	public void setLogoFile(MetaworksFile logoFile);
	
	@NonLoadable
	@NonSavable
	public boolean isSelected();
	public void setSelected(boolean selected);
	
	@NonSavable
	public String getType();
	public void setType(String type);
	
	@NonSavable
	public String getEmpCode();
	public void setEmpCode(String empCode);
	
	@ServiceMethod(callByContent=true)
	public IAppMapping findMe() throws Exception;
	
	@ServiceMethod(callByContent=true)
	public IAppMapping findMyApps() throws Exception;
	
	@ServiceMethod(callByContent=true)
	public void findProject(String appId) throws Exception;
}
