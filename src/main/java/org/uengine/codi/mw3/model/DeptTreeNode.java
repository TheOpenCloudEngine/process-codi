package org.uengine.codi.mw3.model;

import java.util.ArrayList;

import org.metaworks.ServiceMethodContext;
import org.metaworks.ToAppend;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.component.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.uengine.processmanager.ProcessManagerRemote;

public class DeptTreeNode extends TreeNode {

	public final static String TYPE_DEFAULT  = "dept";
	
	@AutowiredFromClient
	public Session session;
	
	@Autowired
	public ProcessManagerRemote processManager;
	
	boolean hiddenEmployee;
		public boolean isHiddenEmployee() {
			return hiddenEmployee;
		}
		public void setHiddenEmployee(boolean hiddenEmployee) {
			this.hiddenEmployee = hiddenEmployee;
		}
		
	public DeptTreeNode() {
		this.setType(TYPE_DEFAULT);
		this.setFolder(true);
	}
	
	public ArrayList<TreeNode> loadExpand() {
		
		ArrayList<TreeNode> child = new ArrayList<TreeNode>();
		
		try{
			IDept dept = new Dept();
			dept.setGlobalCom(session.getEmployee().getGlobalCom());
			dept.setPartCode(this.getId());
			dept = dept.findChildren();
			
			while(dept.next()){
				DeptTreeNode node = new DeptTreeNode();
				node.setId(dept.getPartCode());
				node.setName(dept.getPartName());
				node.setParentId(this.getId());
				node.setHiddenEmployee(this.isHiddenEmployee());
				
				child.add(node);
			}

			
			if(!this.isHiddenEmployee()){
				IEmployee employee = new Employee();
				
				Dept findDept = new Dept();
				findDept.setGlobalCom(session.getEmployee().getGlobalCom());
				findDept.setPartCode(this.getId());
				
				employee = employee.findByDept(findDept);			
				
				while(employee.next()){
					EmployeeTreeNode node = new EmployeeTreeNode();
					node.setId(employee.getEmpCode());
					node.setName(employee.getEmpName());
					node.setParentId(this.getId());
					
					child.add(node);
				}
			}

			this.setExpanded(true);
			this.setLoaded(true);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return child;
	}
	
	@Override
	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_APPEND)
	public Object expand() throws Exception{
		return new ToAppend(this, this.loadExpand());
	}
	
	@ServiceMethod(eventBinding="click", callByContent=true, target=ServiceMethodContext.TARGET_APPEND)
	public Object[] addFollowDept() throws Exception{
		return null;
		/*Dept dept = new Dept();
		dept.session = this.session;
		dept.setMetaworksContext(new MetaworksContext());
		dept.getMetaworksContext().setHow("follower");
		dept.getMetaworksContext().setWhen(Followers.ADD_DEPTFOLLOWERS);*/
		//Followers followers = new Followers();
		
		//return followers.addFollower(this);
		
		/*if( Followers.ADD_INSTANCEFOLLOWERS.equals(getFollowerType()) ){
			String instId = instanceFollowers.getInstanceId();
			// 현재 부서트리에서는 사용자를 체크 안하게 되어있지만 확장성을 위하여 employee쪽도 넣는 부분을 구현해 놓음
			TreeNode node = this;
			RoleMapping roleMap = RoleMapping.create();
			roleMap.setName(node.getName());
			roleMap.setEndpoint(node.getId());
			roleMap.setResourceName(node.getId());
			
			if(DeptTreeNode.TYPE_DEFAULT.equals(node.getType())){
				roleMap.setAssignType(Role.ASSIGNTYPE_DEPT);
			}else if(EmployeeTreeNode.TYPE_DEFAULT.equals(node.getType())){
				roleMap.setAssignType(Role.ASSIGNTYPE_USER);
			}
			processManager.putRoleMapping(instId, roleMap);
			processManager.applyChanges();
			
		}else if( Followers.ADD_TOPICFOLLOWERS.equals(getFollowerType()) ){
			TreeNode node = this;
			TopicMapping tm = new TopicMapping();
			tm.processManager = processManager;
			
			tm.setTopicId( session.getLastSelectedItem() );
			tm.setUserId(node.getId());
			tm.setUserName(node.getName());
			
			if(DeptTreeNode.TYPE_DEFAULT.equals(node.getType())){
				tm.setAssigntype(2);
			}else if(EmployeeTreeNode.TYPE_DEFAULT.equals(node.getType())){
				tm.setAssigntype(0);
			}
			tm.saveMe();
			tm.flushDatabaseMe();
			

		}else if( Followers.ADD_DOCUMENTFOLLOWERS.equals(getFollowerType()) ){
			TreeNode node = this;
			TopicMapping tm = new TopicMapping();
			tm.processManager = processManager;
			
			tm.setTopicId( session.getLastSelectedItem() );
			tm.setUserId(node.getId());
			tm.setUserName(node.getName());
			
			if(DeptTreeNode.TYPE_DEFAULT.equals(node.getType())){
				tm.setAssigntype(2);
			}else if(EmployeeTreeNode.TYPE_DEFAULT.equals(node.getType())){
				tm.setAssigntype(0);
			}
			tm.saveMe();
			tm.flushDatabaseMe();
			

		}
				
		return new Object[]{new ToEvent(ServiceMethodContext.TARGET_OPENER, EventContext.EVENT_CHANGE)};
		*/
	}
}
