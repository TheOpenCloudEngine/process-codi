package org.uengine.codi.mw3.model;

import org.metaworks.dao.IDAO;
import org.metaworks.dao.MetaworksDAO;
import org.metaworks.dao.TransactionContext;
import org.metaworks.dao.TransactionListener;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;


public class RoleFollower extends Follower {

	public RoleFollower(){
		this.setParentType(TYPE_ROLE);
	}

	public RoleUser makeRoleUser(){
		RoleUser roleUser = new RoleUser();
		roleUser.setRoleCode(this.getParentId());
		roleUser.setEmpCode(this.getUser().getUserId());
		
		return roleUser;
	}
	
	@Override
	public IFollower find() throws Exception {
		RoleUser roleUser = this.makeRoleUser();
		IFollower follower = roleUser.findFollower();
		if(follower.next())
			return follower;
		else
			return null;
	}
	
	@Override
	public void put() throws Exception {
		
		if(this.find() == null){
			RoleUser roleUser = this.makeRoleUser();
			roleUser.createDatabaseMe();
			roleUser.flushDatabaseMe();
			
			this.addPushListener();
		}
	}

	@Override
	public void delegate() throws Exception {
		RoleUser roleUser = new RoleUser();
		roleUser.setRoleCode(this.getParentId());
		roleUser.setEmpCode(this.getUser().getUserId());
		
		roleUser.removeUser();
		
		this.push();
	}
	
	@Override
	public IFollower findFollowers() throws Exception {
		RoleUser roleUser = new RoleUser();
		roleUser.setRoleCode(this.getParentId());

		IFollower follower = roleUser.findFollowers();
		follower.getMetaworksContext().setWhere(WHERE_FOLLOWER);
		
		System.out.println("size : " + follower.size());
		
		return follower;
	}
	
	@Override
	public IContact findContacts(String keyword) throws Exception {
		return Contact.findContactsForRole(session.getEmployee().getGlobalCom(), this.getParentId(), session.getUser(), keyword);
	}

}
