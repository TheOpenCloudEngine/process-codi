package org.uengine.codi.mw3.model;

import java.util.Date;

import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.dao.Database;

public class RecentItem extends Database<IRecentItem> implements IRecentItem{
	
	public RecentItem() {
		// TODO Auto-generated constructor stub
	}

	String empCode;
		public String getEmpCode() {
			return empCode;
		}
		public void setEmpCode(String empCode) {
			this.empCode = empCode;
		}

	String itemType;
		public String getItemType() {
			return itemType;
		}
		public void setItemType(String itemType) {
			this.itemType = itemType;
		}

	String itemId;
		public String getItemId() {
			return itemId;
		}
		public void setItemId(String itemId) {
			this.itemId = itemId;
		}
		
	Date updateDate;
		public Date getUpdateDate() {
			return updateDate;
		}
		public void setUpdateDate(Date updateDate) {
			this.updateDate = updateDate;
		}
		
	int clickedCount;
		public int getClickedCount() {
			return clickedCount;
		}
		public void setClickedCount(int clickedCount) {
			this.clickedCount = clickedCount;
		}
		
	public void add() throws Exception {
		
		IRecentItem findMe = this.findMe();
		
		if(findMe == null){
			this.setClickedCount(1);
			createDatabaseMe();
			flushDatabaseMe();
		}else {
			this.setClickedCount(findMe.getClickedCount() + 1);
			updateDate();
		}
		
	}
	
	public IRecentItem updateDate() throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("update recentItem set updateDate=?updateDate, clickedCount=?clickedCount where empcode=?empCode and itemType=?itemType and itemId=?itemId");
		
		IRecentItem updateMe = (IRecentItem) Database.sql(IRecentItem.class, sql.toString());
		
		updateMe.setEmpCode(this.getEmpCode());
		updateMe.setUpdateDate(this.getUpdateDate());
		updateMe.setItemId(this.getItemId());
		updateMe.setItemType(this.getItemType());
		updateMe.setClickedCount(this.getClickedCount());
		updateMe.update();
		
		return updateMe;
		
	}
	
	public IRecentItem findMe() throws Exception {
		
		StringBuffer sql = new StringBuffer();
		sql.append("select * from recentItem where empcode=?empCode and itemType=?itemType and itemId=?itemId");
		
		IRecentItem findMe = (IRecentItem) Database.sql(IRecentItem.class, sql.toString());
		
		findMe.setEmpCode(this.getEmpCode());
		findMe.setItemId(this.getItemId());
		findMe.setItemType(this.getItemType());
		findMe.select();
		
		if(findMe.next())
			return findMe;
		else
			return null;
	}
	
}
