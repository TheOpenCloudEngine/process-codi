package org.uengine.codi.mw3.model;

import java.util.ArrayList;
import java.util.Date;

import org.metaworks.MetaworksContext;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.NonLoadable;
import org.metaworks.annotation.NonSavable;
import org.metaworks.annotation.ORMapping;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.annotation.Table;
import org.metaworks.dao.IDAO;
import org.metaworks.widget.ModalWindow;

@Table(name="bpm_knol")
@Face(ejsPathForArray="dwr/metaworks/genericfaces/ListFace.ejs")
public interface IDocumentNode extends IDAO {

	@Id
	public String getId();
	public void setId(String id);
	
	public String getName();
	public void setName(String name);
	
	@Hidden
	public String getUrl() ;
	public void setUrl(String url);
	
	@Hidden
	public String getThumbnail();
	public void setThumbnail(String thumbnail);
	
	public String getSecuopt();
	public void setSecuopt(String secuopt);
	
	@ORMapping(databaseFields="authorId", objectFields="userId")
	public String getAuthorId();
	public void setAuthorId(String authorId);
	
	public String getType();
	public void setType(String type);
	
	public String getCompanyId();
	public void setCompanyId(String companyId);
	
	public String getDescription();
	public void setDescription(String description);
	
	public Date getStartDate();
	public void setStartDate(Date startDate);
	
	
	@Hidden
	public String getParentId();
	public void setParentId(String parentId);
	
	@Hidden
	@NonLoadable
	@NonSavable
	public int getLoadDepth();
	public void setLoadDepth(int loadDepth);
	
	@NonLoadable
	@NonSavable
	@Hidden(when=MetaworksContext.WHEN_EDIT)
	public ArrayList<DocumentNode> getChildNode();
	public void setChildNode(ArrayList<DocumentNode> childNode) ;
	
	@NonLoadable
	@NonSavable
	@Hidden
	public boolean isClose();
	public void setClose(boolean close);

	@NonLoadable
	@NonSavable
	@Hidden
	public boolean isFirst();
	public void setFirst(boolean first);
	
	@ServiceMethod(callByContent=true, needToConfirm=true, target=ServiceMethodContext.TARGET_APPEND)
	@Face(displayName="$Remove")
	public Object[] remove() throws Exception;
	
	@ServiceMethod(callByContent=true, target="popup")
	@Face(displayName="$Edit")
	public ModalWindow modify() throws Exception;
	
	@ServiceMethod(target="popup")
	@Face(displayName="$addSubDocument")
	public ModalWindow addSubFolder() throws Exception;
	
	
	@ServiceMethod(callByContent=true, mouseBinding="drop", target=ServiceMethodContext.TARGET_APPEND)
	public Object[] drop() throws Exception;
	
	@Hidden
	@ServiceMethod(callByContent=true, except={"childNode"})
	void expand() throws Exception;	
	
	@Hidden
	@ServiceMethod(callByContent=true, except={"childNode"})
	void collapse() throws Exception;	
	
	@ServiceMethod(callByContent=true, except={"childNode"}, target=TARGET_SELF)
	IDocumentNode loadDocumentList() throws Exception;
	
	@ServiceMethod(callByContent=true)
	public Object[] loadExplorerDocument() throws Exception;
	
	@ServiceMethod(callByContent=true, except={"childNode"}, target=TARGET_SELF)
	ArrayList<DocumentNode> loadChildren() throws Exception;
	
	@ServiceMethod(callByContent=true)
	public Object[] loadDocument() throws Exception;
	
	
	public void saveMe() throws Exception;
	
	public void createMe() throws Exception;
}
