package org.uengine.codi.mw3.model;

import javax.persistence.Id;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.ServiceMethod;

import org.metaworks.dao.Database;
import org.metaworks.dao.IDAO;

public class DatabaseReferenceComboBox extends DynamicComboBox{
	
	public DatabaseReferenceComboBox(String sqlStmt){
		setSqlStmt(sqlStmt);
	}
	
	String sqlStmt;
	
	@Id
	public String getSqlStmt() {
		return sqlStmt;
	}


	public void setSqlStmt(String sqlStmt) {
		this.sqlStmt = sqlStmt;
	}


	@ServiceMethod(target="stick")
	public DropDownPanel onSelect(){
		DropDownPanel dropdownPanel = new DropDownPanel();
		
		IDAO dao;
		try {
			dao = Database.sql(IDAO.class, sqlStmt);
			dao.select();
			if(dao.next()){
				String value = (String) dao.get("value");
				String name = (String) dao.get("name");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		dropdownPanel.setSelections(new String[]{"a", "b", "c"});
		
		return dropdownPanel;
	}
	
}


