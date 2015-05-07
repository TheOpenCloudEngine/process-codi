package org.uengine.codi.mw3.model;


public class DeptFollower extends Follower {

	public DeptFollower(){
		this.setParentType(TYPE_DEPT);
	}

	@Override
	public void put() throws Exception {
		IEmployee findEmp = new Employee();
		findEmp.setEmpCode(this.getUser().getUserId());
		
		Employee emp = new Employee();
		emp.copyFrom(findEmp.findMe());
		
		if(emp.getPartCode() == null || !emp.getPartCode().equals(this.getParentId())){
			emp.setPartCode(this.getParentId());
			emp.syncToDatabaseMe();
			emp.flushDatabaseMe();
			
			this.addPushListener();
		}
	}

	@Override
	public void delegate() throws Exception {
		Employee employee = new Employee();
		employee.setEmpCode(this.getUser().getUserId());
		employee.copyFrom(employee.databaseMe());
					
		employee.setPartCode(null);
		employee.syncToDatabaseMe();
		employee.flushDatabaseMe();
		
		this.push();
	}
	
	@Override
	public IFollower findFollowers() throws Exception {
		
		Dept dept = new Dept();
		dept.setPartCode(this.getParentId());
		
		IFollower follower = dept.findFollowers();
		follower.getMetaworksContext().setWhere(WHERE_FOLLOWER);
		
		return follower;
	}
	
	@Override
	public IContact findContacts(String keyword) throws Exception {
		return Contact.findContactsForDept(session.getEmployee().getGlobalCom(), this.getParentId(), session.getUser(), keyword);
	}

}
