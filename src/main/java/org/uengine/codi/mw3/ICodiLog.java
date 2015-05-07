package org.uengine.codi.mw3;

import java.util.Date;
import org.metaworks.annotation.Table;
import org.metaworks.dao.IDAO;


@Table(name="logtable")
public interface ICodiLog extends IDAO {
	
	public String getId();
	public void setId(String id);
	
	public String getType();
	public void setType(String type);
	
	public String getEmpcode();
	public void setEmpcode(String empCode);
	
	public String getComCode();
	public void setComCode(String comCode);
	
	public String getIp();
	public void setIp(String ip);
	
	public Date getDate();
	public void setDate(Date date);
	
	public String createNewId() throws Exception;
}
