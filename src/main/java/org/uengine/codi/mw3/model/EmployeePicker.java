package org.uengine.codi.mw3.model;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.ServiceMethod;

public class EmployeePicker implements ContextAware {

	public EmployeePicker() {
	}

	public EmployeePicker(String comCode) {
		setComCode(comCode);
		initialize();
	}

	String comCode;

	@Hidden
	public String getComCode() {
		return comCode;
	}

	public void setComCode(String comCode) {
		this.comCode = comCode;
	}

	IDept dept;

	public IDept getDept() {
		return dept;
	}

	public void setDept(IDept dept) {
		this.dept = dept;
	}

	@Hidden
	@ServiceMethod
	public void initialize() {
		try {
			IDept dept = new Dept();
			setDept(dept.findTreeByGlobalCom(getComCode()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	MetaworksContext context;
	
	@Override
	public MetaworksContext getMetaworksContext() {
		if(context == null){
			context = new MetaworksContext();
		}
		return context;
	}

	@Override
	public void setMetaworksContext(MetaworksContext context) {
		this.context = context;
	}
	
}
