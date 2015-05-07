package org.uengine.codi.mw3;

import org.metaworks.dao.DAOFactory;
import org.metaworks.dao.Database;
import org.metaworks.dao.KeyGeneratorDAO;
import org.metaworks.dao.TransactionContext;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CodiLog extends Database<ICodiLog> implements ICodiLog {
	String id;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
	String type;
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
	String empCode;
		public String getEmpcode() {
			return empCode;
		}
		public void setEmpcode(String empCode) {
			this.empCode = empCode;
		}
	String comCode;
		public String getComCode() {
			return comCode;
		}
		public void setComCode(String comCode) {
			this.comCode = comCode;
		}
	String ip;
		public String getIp() {
			return ip;
		}
		public void setIp(String ip) {
			this.ip = ip;
		}
	Date date;
		public Date getDate() {
			return date;
		}
		public void setDate(Date date) {
			this.date = date;
		}

		
		public String createNewId() throws Exception{
			Map options = new HashMap();
			options.put("useTableNameHeader", "false");
			options.put("onlySequenceTable", "true");
			
			KeyGeneratorDAO kg = DAOFactory.getInstance(TransactionContext.getThreadLocalInstance()).createKeyGenerator("codilog", options);
			kg.select();
			kg.next();
			
			Number number = kg.getKeyNumber();
			
			return number.toString();
		}
}
