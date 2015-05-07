package org.uengine.codi.mw3.model;

import javax.persistence.Table;

import org.metaworks.Remover;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.NonLoadable;
import org.metaworks.annotation.NonSavable;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.component.SelectBox;
import org.metaworks.dao.IDAO;

@Table(name="notiSetting")
public interface INotiSetting extends IDAO{
	
	@Id
	@Hidden
	public int getId();
	public void setId(int id);
	
	@Hidden
	public String getUserId();
	public void setUserId(String userId);
	
	@Face(displayName = "$CheckLogin")
	public boolean isCheckLogin();
	public void setCheckLogin(boolean checkLogin);

	@Face(displayName = "$WriteBookmark")
	public boolean isWriteBookmark();
	public void setWriteBookmark(boolean writeBookmark);

	@Face(displayName = "$NotiAdvice")
	public boolean isNotiAdvice();
	public void setNotiAdvice(boolean notiAdvice);

	@Face(displayName = "$ModiUser")
	public boolean isModiUser();
	public void setModiUser(boolean modiUser);

	@Face(displayName = "$ModiTopic")
	public boolean isModiTopic();
	public void setModiTopic(boolean modiTopic);

	@Face(displayName = "$ModiOrgan")
	public boolean isModiOrgan();
	public void setModiOrgan(boolean modiOrgan);

	@Face(displayName = "$WriteTopic")
	public boolean isWriteTopic();
	public void setWriteTopic(boolean writeTopic);
	
	@Face(displayName = "$WriteOrgan")
	public boolean isWriteOrgan();
	public void setWriteOrgan(boolean writeOrgan);

	@Face(displayName = "$WriteInstance")
	public boolean isWriteInstance();
	public void setWriteInstance(boolean writeInstance);
	
	@Face(displayName = "$InviteTopic")
	public boolean isInviteTopic();
	public void setInviteTopic(boolean inviteTopic);

	@Face(displayName = "$InviteOrgan")
	public boolean isInviteOrgan();
	public void setInviteOrgan(boolean inviteOrgan);
	
	@Face(displayName = "$IsAddFriend")
	public boolean isAddFriend();
	public void setAddFriend(boolean addFriend);
	
	@Face(displayName = "$BeforehandNoti")
	public boolean isBeforehandNoti();
	public void setBeforehandNoti(boolean beforehandNoti);

	@Face(displayName = "$NotiTime")
	public boolean isNotiTime();
	public void setNotiTime(boolean notiTime);
	
	@NonLoadable
	@NonSavable
	public SelectBox getSelectTime();
	public void setSelectTime(SelectBox selectTime);
	
	public String getDefaultNotiTime();
	public void setDefaultNotiTime(String defaultNotiTime);

	@Face(displayName = "$NotiEmail")
	public boolean isNotiEmail();
	public void setNotiEmail(boolean notiEmail);
	
	@ServiceMethod(callByContent=true)
	@Face(displayName = "$Save")
	public Remover save() throws Exception;
	
	public INotiSetting findByUserId(String userId) throws Exception;
}
