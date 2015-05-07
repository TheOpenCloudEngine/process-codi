package org.uengine.codi.mw3.knowledge;

import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.Name;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.annotation.Table;
import org.metaworks.dao.IDAO;
import org.metaworks.widget.ModalWindow;

@Table(name="bpm_knol")
public interface ITopicNode extends IDAO {
	
	@Id
	public String getId();
	public void setId(String id);
	
	@Name
	public String getName();
	public void setName(String name);
	
	public String getSecuopt();
	public void setSecuopt(String secuopt);
	
	public String getAuthorId();
	public void setAuthorId(String authorId);
	
	public String getType();
	public void setType(String type);
	
	public String getCompanyId();
	public void setCompanyId(String companyId);
	
	public String getDescription();
	public void setDescription(String description);

	@ServiceMethod(callByContent=true)
	public Object[] loadTopic() throws Exception;
	
	@ServiceMethod(callByContent=true, needToConfirm=true ,target=ServiceMethodContext.TARGET_APPEND)
	@Face(displayName="$Remove")
	public Object[] remove() throws Exception;
		
	@ServiceMethod(callByContent=true, target="popup")
	@Face(displayName="$Edit")
	public ModalWindow modify() throws Exception;
	
	@ServiceMethod(callByContent=true,target="popup")
	@Face(displayName="$ExportHtml")
	public ModalWindow exportHtml() throws Exception;
	
	@ServiceMethod(callByContent=true, mouseBinding="drop", target=ServiceMethodContext.TARGET_APPEND)
	public Object[] drop() throws Exception;
}
