package org.uengine.codi.mw3.model;

import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.component.TreeNode;

public class EmployeeTreeNode extends TreeNode {

	public final static String TYPE_DEFAULT  = "employee";
	
	@AutowiredFromClient
	public Session session;
	
	public EmployeeTreeNode() {
		this.setType(TYPE_DEFAULT);
	}	
}
