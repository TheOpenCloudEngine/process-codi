package org.uengine.codi.mw3.model;

import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;

import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.ORMapping;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dao.IDAO;

@Table(name="bpm_noti")
public interface INotification extends IDAO{

	@Id
//	@GeneratedValue(strategy=GenerationType.TABLE, generator="SEQ_BPM")
	public Long getNotiId();
	public void setNotiId(Long notiId);
	

	public Date getInputDate();
	public void setInputDate(Date inputDate);
	
	public String getUserId();
	public void setUserId(String userId);

	
	public String getActorId();
	public void setActorId(String actorId);
	
	@ORMapping(databaseFields="actorId", objectFields="userId")
	public IUser getActor();
	public void setActor(IUser actor);


	public Long getInstId();
	public void setInstId(Long instId);
	

	public Long getTaskId();
	public void setTaskId(Long taskId);
	

	public String getActAbstract();
	public void setActAbstract(String actAbstract);
	
	
	public boolean isConfirm();
	public void setConfirm(boolean confirm);

	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_POPUP)
	public Object[] see() throws Exception;


}
