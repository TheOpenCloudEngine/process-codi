package org.uengine.codi.mw3.model;

import java.util.ArrayList;

import org.metaworks.ServiceMethodContext;
import org.metaworks.ToAppend;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.component.TreeNode;

public class CompanyTreeNode extends TreeNode {

	public final static String TYPE_DEFAULT  = "company";
	
	@AutowiredFromClient
	public Session session;
	
	boolean hiddenEmployee;
		public boolean isHiddenEmployee() {
			return hiddenEmployee;
		}
		public void setHiddenEmployee(boolean hiddenEmployee) {
			this.hiddenEmployee = hiddenEmployee;
		}

	public CompanyTreeNode() {
		this.setType(TYPE_DEFAULT);
		this.setFolder(true);
	}
	
	public ArrayList<TreeNode> loadExpand() {
		
		ArrayList<TreeNode> child = new ArrayList<TreeNode>();
		
		try{
			IDept dept = new Dept();
			dept.setGlobalCom(session.getEmployee().getGlobalCom());
			dept = dept.findChildren();
			
			while(dept.next()){
				DeptTreeNode node = new DeptTreeNode();
				node.setId(dept.getPartCode());
				node.setName(dept.getPartName());
				node.setParentId(this.getId());
				node.setHiddenEmployee(this.isHiddenEmployee());
				
				child.add(node);
			}

			if(!this.isHiddenEmployee()){
				IEmployee employee = new Employee();
				employee.setGlobalCom(session.getEmployee().getGlobalCom());
//				employee = employee.findByDeptOther();
				
				while(employee.next()){
					EmployeeTreeNode node = new EmployeeTreeNode();
					node.setId(employee.getEmpCode());
					node.setName(employee.getEmpName());
					node.setParentId(this.getId());
					
					child.add(node);
				}
			}
			
			this.setExpanded(true);
			this.setLoaded(true);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return child;
	}
	
	@Override
	@ServiceMethod(payload={"id", "hiddenEmployee"}, target=ServiceMethodContext.TARGET_APPEND)
	public Object expand() throws Exception{
		return new ToAppend(this, this.loadExpand());
	}
}
