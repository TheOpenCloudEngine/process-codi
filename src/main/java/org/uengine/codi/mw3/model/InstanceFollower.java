package org.uengine.codi.mw3.model;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import org.metaworks.MetaworksException;
import org.metaworks.dwr.MetaworksRemoteService;
import org.uengine.codi.mw3.Login;
import org.uengine.codi.mw3.filter.AllSessionFilter;
import org.uengine.kernel.Role;


public class InstanceFollower extends Follower {

	public InstanceFollower(){
		this.setParentType(TYPE_INSTANCE);
	}
	
	public InstanceFollower(String instanceId){
		this();
		
		this.setParentId(instanceId);
	}
	
	public RoleMapping makeRoleMapping() throws Exception {
		Long instId = Long.parseLong(this.getParentId());
		
		RoleMapping rm = new RoleMapping();
		rm.setRootInstId(instId);
		rm.setInstId(instId);
		
		if(Role.ASSIGNTYPE_USER == this.getAssigntype()){
			rm.setEndpoint(user.getUserId());
			rm.setResName(user.getName());
		}else if(Role.ASSIGNTYPE_DEPT == this.getAssigntype()){
			rm.setEndpoint(dept.getPartCode());
			rm.setResName(dept.getPartName());
		}
		
		rm.setAssignType(this.getAssigntype());
		rm.setRoleName(RoleMapping.ROLEMAPPING_FOLLOWER_ROLENAME);
		
		return rm;
	}
	
	@Override
	public IFollower find() throws Exception {
		
		RoleMapping rm = this.makeRoleMapping();
		IFollower follower = rm.findFollower();
		
		// not exist save rolemapping
		if( follower.next() ){
			return follower;
		}else{
			return null;
		}
	}
	
	@Override
	public void put() throws Exception {
		// not exist save rolemapping
		Object findObj = find();
		if( findObj == null ){
			RoleMapping rm = this.makeRoleMapping();
			rm.saveMe();
			rm.flushDatabaseMe();
			
			this.noti(Followers.ADD_NOTI);
			this.addPushListener();
		}
	}

	@Override
	public void delegate() throws Exception {
		RoleMapping rm = new RoleMapping();
		rm.setInstId(Long.parseLong(this.getParentId()));
		rm.setEndpoint(this.getEndpoint());
		rm.setAssignType(this.getAssigntype());
		rm.setRoleName(RoleMapping.ROLEMAPPING_FOLLOWER_ROLENAME);
		
		rm.removeMe();
		
		this.noti(Followers.REMOVE_NOTI);
		this.push();

	}
	
	@Override
	public Object[] popupAddFollower() throws Exception {

		Instance instance = new Instance();
		instance.setInstId(new Long(this.getParentId()));
		instance.session = this.session;
		instance.copyFrom(instance.databaseMe());
		
		if(!instance.checkAuth()){
			throw new MetaworksException("$NotPermittedToWork");
		}
		if( instance.getIsDeleted() ){
			throw new MetaworksException("$alreadyDeletedPost");
		}
		
		
		return super.popupAddFollower();
	}
	
	@Override
	public IFollower findFollowers() throws Exception {
		RoleMapping roleMapping = new RoleMapping();
		roleMapping.setRootInstId(Long.parseLong(this.getParentId()));

		IFollower follower = roleMapping.findFollowers();
		follower.getMetaworksContext().setWhere(WHERE_FOLLOWER);
		
//		System.out.println("follower : " + follower.size());
		return follower;
	}
	
	@Override
	public AddFollowerPanel makeFollowerPanel(Session session, Follower follower) throws Exception{
		AddFollowerPanel addFollowerPanel = new AddFollowerPanel(session, this);
//		addFollowerPanel.loadDept(session, follower);
		return addFollowerPanel;
	}
	
	@Override
	public IContact findContacts(String keyword) throws Exception {
		return Contact.findContactsForInstance(this.getParentId(), session.getUser(), keyword);
	}
	
	@Override
	public IDept findDepts(String keyword) throws Exception {
		Dept dept = new Dept();
		dept.setGlobalCom(session.getEmployee().getGlobalCom());
		
		return dept.findDeptForInstance(this.getParentId(), keyword);
	}
	
	public void noti(String command) throws Exception {
		if(this.isEnablePush() && !session.getUser().getUserId().equals(this.getUser().getUserId())){	// TODO 이걸로 체크를 하는게 맞는것인가?? 노티부분이 다르게 있다면 해당노티로 체크해야함
			String actAbstract = null;
			Instance instance = new Instance();
			instance.setInstId(new Long(this.getParentId()));
			if( Followers.ADD_NOTI.equals(command)){
				// add
				actAbstract = session.getUser().getName() + " added follower to '" + instance.databaseMe().getName() +"'" ;
			}else{
				// remove
				actAbstract = session.getUser().getName() + " removed follower from '" + instance.databaseMe().getName() +"'" ;
			}
			HashMap<String, String> notiUsers = new HashMap<String, String>();
			
			// noti 저장
			if(Role.ASSIGNTYPE_USER == this.getAssigntype()){
				notiUsers.put(user.getUserId(), Login.getSessionIdWithUserId(user.getUserId()));
			}else if(Role.ASSIGNTYPE_DEPT == this.getAssigntype()){
				Dept deptRef = new Dept();
				deptRef.copyFrom(dept);
				
				IEmployee employee = new Employee();
				employee.setMetaworksContext(this.getMetaworksContext());
				employee = employee.findByDept(deptRef);
				if( employee != null ){
					while(employee.next()){
						String followerUserId = employee.getEmpCode();
						if( session.getUser().getUserId().equals(followerUserId)){	// 자기 자신은 제외
							continue;
						}
						notiUsers.put(followerUserId, Login.getSessionIdWithUserId(followerUserId));
					}
				}
			}
			Iterator<String> iterator = notiUsers.keySet().iterator();
			while(iterator.hasNext()){
				String followerUserId = (String)iterator.next();
				Notification noti = new Notification();
				INotiSetting notiSetting = new NotiSetting();
				INotiSetting findResult = notiSetting.findByUserId(followerUserId);
				if(!findResult.next() || findResult.isWriteInstance()){
					noti.setNotiId(System.currentTimeMillis()); //TODO: why generated is hard to use
					noti.setUserId(followerUserId);
					noti.setActorId(session.getUser().getUserId());
					noti.setConfirm(false);
					noti.setInputDate(Calendar.getInstance().getTime());
					noti.setInstId(instance.getInstId());					
					noti.setActAbstract(actAbstract);
					noti.add(instance);
				}
			}
			MetaworksRemoteService.pushTargetScriptFiltered(new AllSessionFilter(notiUsers),
					"mw3.getAutowiredObject('" + NotificationBadge.class.getName() + "').refresh",
					new Object[]{});
		}
	}
}
