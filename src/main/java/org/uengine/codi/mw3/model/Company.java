package org.uengine.codi.mw3.model;

import java.util.HashMap;
import java.util.Map;

import org.metaworks.Remover;
import org.metaworks.dao.DAOFactory;
import org.metaworks.dao.Database;
import org.metaworks.dao.KeyGeneratorDAO;
import org.metaworks.dao.TransactionContext;
import org.metaworks.website.MetaworksFile;
import org.metaworks.widget.ModalWindow;

public class Company extends Database<ICompany> implements ICompany {
	String comCode;
	String comName;
	String description;
	String isDeleted;
	String alias;
	String billAccnt;
	String billSbscr;

		public String getAlias() {
			return alias;
		}
		public void setAlias(String alias) {
			
			this.alias = alias;
		}

	String repMail;
	public String getRepMail() {
		return repMail;
	}

	public void setRepMail(String repMail) {
		this.repMail = repMail;
	}

	public String getRepMlHst() {
		return repMlHst;
	}

	public void setRepMlHst(String repMlHst) {
		this.repMlHst = repMlHst;
	}

	public String getRepMlPwd() {
		return repMlPwd;
	}

	public void setRepMlPwd(String repMlPwd) {
		this.repMlPwd = repMlPwd;
	}

	String repMlHst;
	String repMlPwd;

	public String getComCode() {
		return comCode;
	}

	public void setComCode(String comCode) {
		this.comCode = comCode;
	}

	public String getComName() {
		return comName;
	}

	public void setComName(String comName) {
		this.comName = comName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getBillAccnt() { return billAccnt; }
	public void setBillAccnt(String billAccnt) { this.billAccnt = billAccnt; }

	public String getBillSbscr() { return billSbscr; }
	public void setBillSbscr(String billSbscr) { this.billSbscr = billSbscr; }

	MetaworksFile logo;
		public MetaworksFile getLogo() {
			return logo;
		}
		public void setLogo(MetaworksFile logo) {
			this.logo = logo;
		}
	
	public ICompany load() throws Exception {
		if (getComCode() != null) {
			ICompany company = (ICompany) databaseMe();
			return company;
		}
		return null;
	}

	@Override
	public Object[] save() throws Exception {

		if(getLogo().getDeletedPath() != null)
			getLogo().remove();
		
		if(getLogo().getFileTransfer() != null && !getLogo().getFileTransfer().getFilename().isEmpty())
			getLogo().upload();
		else
			getLogo().setUploadedPath(null);
		
		setRepMlHst("imap.gmail.com");
		syncToDatabaseMe();
		
		return new Object[]{new Remover(new ModalWindow())};
	}

	public ICompany findByName() throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("select * from comtable ");
		sb.append("where alias=?alias");
		
		ICompany dao = null;
		
		try {
			dao = sql(sb.toString());
			dao.setAlias(this.getAlias());
			dao.select();
			
			if(!dao.next())
				dao = null;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return dao;
	}
	
	public ICompany findByCode() throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("select * from comtable ");
		sb.append("where comcode=?comcode");
		
		ICompany dao = null;
		
		try {
			dao = sql(sb.toString());
			dao.setComCode(this.getComCode());
			dao.select();
			
			if(!dao.next())
				dao = null;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return dao;
	}
	
	public ICompany findMe() throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("select * from comtable ");
		
		ICompany dao = null;

		try {
			dao = sql(sb.toString());
			dao.select();
			
			if(!dao.next())
				dao = null;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return dao;
	}
	
	public ICompany findByAlias() throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * ");
		sb.append("  FROM comtable ");
		sb.append(" WHERE alias = ?alias");
		
		ICompany dao = null;
		
		try {
			dao = sql(sb.toString());
			dao.setAlias(this.getAlias());
			dao.select();
			
			if(!dao.next())
				dao = null;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return dao;
	}
	
	
	public String createNewId() throws Exception{
		Map options = new HashMap();
		options.put("useTableNameHeader", "false");
		options.put("onlySequenceTable", "true");
		
		KeyGeneratorDAO kg = DAOFactory.getInstance(TransactionContext.getThreadLocalInstance()).createKeyGenerator("comtable", options);
		kg.select();
		kg.next();
		
		Number number = kg.getKeyNumber();
		
		return number.toString();
	}
}
