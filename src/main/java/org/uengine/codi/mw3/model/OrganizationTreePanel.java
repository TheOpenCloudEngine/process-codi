package org.uengine.codi.mw3.model;

public class OrganizationTreePanel  {
	
	public void load(Session session, String type){
		OrganizationTree deptTree = new OrganizationTree(session);;
		deptTree.setId("dept");
		deptTree.setHiddenEmployee(true);
		deptTree.setHiddenCheckBoxFolder(true);
		deptTree.load();

	}
	
}
