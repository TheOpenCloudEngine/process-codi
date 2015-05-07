package org.uengine.codi.mw3.model;

import javax.persistence.Table;

import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.NonLoadable;
import org.metaworks.annotation.NonSavable;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dao.IDAO;

@Table(name="bpm_procdef")
@Face(
		ejsPathMappingByContext=
			{
				"{when: 'newInstance', face: 'dwr/metaworks/org/uengine/codi/mw3/model/IProcessDefinition_newInstance.ejs'}",
//				"{when: 'newInstance', face: 'genericFaces/ObjectFace.ejs'}",
			}		

	)
public interface IProcessDefinition extends IDAO{

	@ServiceMethod(callByContent=true, except="childs")
	public void drillDown() throws Exception;
	
	@ServiceMethod
	public Object[] initiate() throws Exception;
	
	@ServiceMethod
	public ContentWindow design() throws Exception;
	

	@ServiceMethod(target=ServiceMethodContext.TARGET_POPUP)
	public Popup contextMenu();
	
	public IProcessDefinition findAll() throws Exception;

	@Id
	public Long getDefId();
	public void setDefId(Long defId);
	
	public String getDescription();
	public void setDescription(String description);

	public boolean getIsFolder();
	public void setIsFolder(boolean folder);

	@Hidden
	public boolean getIsAdhoc();
	public void setIsAdhoc(boolean adhoc);

	@Hidden
	public String getParentFolder();
	public void setParentFolder(String parentFolder);
	
	@Hidden
	public int getProdVer();
	public void setProdVer(int prodVer);
	
	@Hidden
	public String getObjType();
	public void setObjType(String objType);
	
	@Hidden
	public Long getProdVerId();
	public void setProdVerId(Long prodVerId) ;

	public String getName() ;
	public void setName(String name);
	
	@Hidden
	public String getAuthor();
	public void setAuthor(String author);

	@Hidden
	public boolean getIsDeleted();
	public void setIsDeleted(boolean deleted);

	@Hidden
	public boolean getIsVisible();
	public void setIsVisible(boolean visible);

	@Hidden
	public String getAlias();
	public void setAlias(String alias);

	@Hidden
	public Long getSuperDefId();
	public void setSuperDefId(Long superDefId);

	@Hidden
	public String getComCode();
	public void setComCode(String comCode);
	
	@NonLoadable
	@NonSavable
	public IProcessDefinition getChilds();
	public void setChilds(IProcessDefinition child);

}
