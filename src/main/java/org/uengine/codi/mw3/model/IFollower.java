package org.uengine.codi.mw3.model;

import org.metaworks.EventContext;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.Available;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.NonSavable;
import org.metaworks.annotation.ORMapping;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.annotation.TypeSelector;
import org.metaworks.dao.IDAO;
import org.uengine.kernel.Role;

public interface IFollower extends IDAO {

	public final static String WHERE_FOLLOWER = "followers";
	
	public final static String TYPE_INSTANCE = "instance";
	public final static String TYPE_TOPIC 	 = "topic";
	public final static String TYPE_ROLE 	 = "role";
	public final static String TYPE_DEPT 	 = "dept";
	
	//, TYPE_ROLE, TYPE_DEPT
	@Hidden
	@NonSavable
	@TypeSelector(values={TYPE_INSTANCE, TYPE_TOPIC, TYPE_DEPT, TYPE_ROLE},
				 classes={InstanceFollower.class, TopicFollower.class, DeptFollower.class, RoleFollower.class})
	public String getParentType();
	public void setParentType(String parentType);
	
	@Hidden
	@NonSavable
	public String getParentId();
	public void setParentId(String parentId);

	@Hidden
	public int getAssigntype();
	public void setAssigntype(int assigntype);

	@Hidden
	public String getEndpoint();
	public void setEndpoint(String endpoint);

	@Hidden
	public String getResName();
	public void setResName(String resName);

	public IUser getUser();
	public void setUser(IUser user);

	public IDept getDept();
	public void setDept(IDept dept);

	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_STICK, mouseBinding=ServiceMethodContext.MOUSEBINDING_LEFTCLICK)
	public Object[] detail() throws Exception;

	
	@Available(where=IUser.WHERE_ADDFOLLOWER)
	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_APPEND, mouseBinding=ServiceMethodContext.MOUSEBINDING_LEFTCLICK)
	public Object[] addFollower() throws Exception;
	
	@Hidden
	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_APPEND, eventBinding=EventContext.EVENT_DELEGATE, bindingHidden=true)
	public Object[] removeFollower() throws Exception;

	public Object[] popupAddFollower() throws Exception;
	public IFollower findFollowers() throws Exception;
	
}
