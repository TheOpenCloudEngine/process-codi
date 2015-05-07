package org.uengine.codi.mw3.model;

import java.util.Date;

import org.metaworks.annotation.Id;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.annotation.Table;
import org.metaworks.dao.IDAO;

@Table(name="recentItem")
public interface IRecentItem extends IDAO{

	public final static String TYPE_FRIEND		= "friend";
	
	@Id
	public String getEmpCode();
	public void setEmpCode(String empCode);

	public Date getUpdateDate();
	public void setUpdateDate(Date updateDate);
	
	public String getItemType();
	public void setItemType(String itemType);

	public String getItemId();
	public void setItemId(String itemId);
	
	public int getClickedCount();
	public void setClickedCount(int clickedCount);
	
	@ServiceMethod
	public void add() throws Exception;
	
	public IRecentItem updateDate() throws Exception;
	
	public IRecentItem findMe() throws Exception;
	
}
