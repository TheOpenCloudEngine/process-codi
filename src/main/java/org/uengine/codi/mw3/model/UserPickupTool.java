package org.uengine.codi.mw3.model;

import org.uengine.codi.ITool;

public class UserPickupTool implements ITool {

	transient IUser selectedUser;
	
	public IUser getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(IUser selectedUser) {
		this.selectedUser = selectedUser;
	}
	
	String selectedUserEmpCode;

	public String getSelectedUserEmpCode() {
		return selectedUserEmpCode;
	}

	public void setSelectedUserEmpCode(String selectedUserEmpCode) {
		this.selectedUserEmpCode = selectedUserEmpCode;
	}

	@Override
	public void onLoad() {
		// TODO Auto-generated method stub
		setSelectedUser(new User());
		getSelectedUser().getMetaworksContext().setWhere(IUser.MW3_WHERE_ROLEUSER_PICKER_CALLER);
	}

	@Override
	public void beforeComplete() {
		// TODO Auto-generated method stub
		setSelectedUserEmpCode(getSelectedUser().getUserId());
		
		System.out.println(getSelectedUserEmpCode());

	}

	@Override
	public void afterComplete() {
		// TODO Auto-generated method stub

	}

}
