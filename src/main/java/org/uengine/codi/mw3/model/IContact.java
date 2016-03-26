package org.uengine.codi.mw3.model;

import java.rmi.RemoteException;

import org.metaworks.EventContext;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.Available;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.NonLoadable;
import org.metaworks.annotation.NonSavable;
import org.metaworks.annotation.ORMapping;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dao.IDAO;

//@Face(ejsPathForArray="dwr/metaworks/genericfaces/ListFace.ejs")
public interface IContact extends IDAO{
	
	public final static String HOW_PICK 					= "contact_pick";
	public final static String HOW_PICKER 					= "picker";
	
	@NonLoadable
	@NonSavable
	public boolean isChecked();
	public void setChecked(boolean checked);
	
	public String getFriendId();
	public void setFriendId(String friendId);
	
	@ORMapping(databaseFields = {"friendId", "friendName", "network", "mood"}, objectFields = {"userId", "name", "network", "mood"})
	public IUser getFriend();
	public void setFriend(IUser friend);

	@Id
	public String getUserId();
	public void setUserId(String userId);
	
	@NonSavable
	public boolean isFollowed();
	public void setFollowed(boolean followed);
	
//	@NonSavable
//	@NonLoadable
//	public String getMood();
//	public void setMood(String mood);
	
	@Available(how="comitter")
	@ServiceMethod(callByContent=true, mouseBinding=ServiceMethodContext.MOUSEBINDING_LEFTCLICK, target=ServiceMethodContext.TARGET_SELF)
	public void check();
	
	@Available(where=IUser.WHERE_ADDFOLLOWER)
	@ServiceMethod(callByContent=true, target=TARGET_APPEND)
	public Object[] addFollower() throws RemoteException, Exception;

	@Available(where=IUser.WHERE_FOLLOWERS)
	@ServiceMethod(callByContent=true, target=TARGET_APPEND)
	public Object[] removeFollower() throws Exception;
}
