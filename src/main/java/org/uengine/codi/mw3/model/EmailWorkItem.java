package org.uengine.codi.mw3.model;

import java.util.ArrayList;

public class EmailWorkItem extends MemoWorkItem{
	
	public EmailWorkItem(){
		super();
		
		setType("email");		
	}
	
	ArrayList<User> ccUsers;
		public ArrayList<User> getCcUsers() {
			return ccUsers;
		}
		public void setCcUsers(ArrayList<User> ccUsers) {
			this.ccUsers = ccUsers;
		}
	ArrayList<User> bccUsers;
		public ArrayList<User> getBccUsers() {
			return bccUsers;
		}
		public void setBccUsers(ArrayList<User> bccUsers) {
			this.bccUsers = bccUsers;
		}

	@Override
	public Object[] add() throws Exception {
		
		if(getMemo()!=null && getMemo().getContents()!=null){
			if( ccUsers != null && ccUsers.size() > 0 ){
				String ext2 = "";
				for( int i = 0; i < ccUsers.size(); i++){
					User ccUser = ccUsers.get(i);
					if( i != 0){
						ext2 = ext2 + ",";
					}
					ext2 +=  ccUser.getUserId();
				}
				setExt2(ext2);
			}
			if( bccUsers != null && bccUsers.size() > 0 ){
				String ext3 = "";
				for( int i = 0; i < bccUsers.size(); i++){
					User bccUser = bccUsers.get(i);
					if( i != 0){
						ext3 = ext3 + ",";
					}
					ext3 +=  bccUser.getUserId();
				}
				setExt3(ext3);
			}
		}
		
		return super.add();
	}
}
