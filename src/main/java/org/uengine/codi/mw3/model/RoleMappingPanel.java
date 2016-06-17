package org.uengine.codi.mw3.model;

import java.util.ArrayList;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Id;
import org.metaworks.dwr.MetaworksRemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.uengine.kernel.*;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.modeling.modeler.ProcessCanvas;
import org.uengine.modeling.modeler.ProcessModeler;
import org.uengine.modeling.resource.VersionManager;
import org.uengine.processmanager.ProcessManagerRemote;
import org.uengine.social.ElementViewActionDelegateForInstanceMonitoring;


@Face(displayName="$RoleMapping", options="hideLabels", values="true")
public class RoleMappingPanel implements ContextAware{
	
	
	MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}
		
	ArrayList<IRoleMappingDefinition> roleMappingDefinitions;
	@Face(options="alignment", values="horizontal")
		public ArrayList<IRoleMappingDefinition> getRoleMappingDefinitions() {
			return roleMappingDefinitions;
		}
		public void setRoleMappingDefinitions(
				ArrayList<IRoleMappingDefinition> roleMappingDefinitions) {
			this.roleMappingDefinitions = roleMappingDefinitions;
		}

	String defId;
		@Id
		public String getDefId() {
			return defId;
		}

		public void setDefId(String defId) {
			this.defId = defId;
		}



	public RoleMappingPanel(){}

	@Autowired
	public ProcessManagerRemote processManager;

	@AutowiredFromClient
	public Session session;


	public RoleMappingPanel(ProcessManagerRemote processManager, String defId, Session session) throws Exception {
		this.processManager = processManager;
		this.session = session;

		load(defId);
	}

	ProcessModeler processView;
		public ProcessModeler getProcessView() {
			return processView;
		}
		public void setProcessView(ProcessModeler processView) {
			this.processView = processView;
		}


	public void load(String defId_) throws Exception {

		setDefId(defId_);

		defId_ = VersionManager.getProductionResourcePath("codi", defId_);

		if(defId_ == null)
			throw new NoSuchProcessDefinitionException();



		roleMappingDefinitions = new ArrayList<IRoleMappingDefinition>();

		org.uengine.kernel.ProcessDefinition definition = processManager.getProcessDefinition(defId_);


		installProcessView(definition);


		if(definition.getRoles()!=null)
		for(org.uengine.kernel.Role role : definition.getRoles()){
			if( "rootRole".equals(role.getName()) || "Initiator".equalsIgnoreCase(role.getName()) ){
				continue;
			}
			RoleMappingDefinition roleMappingDefinition = new RoleMappingDefinition();
			roleMappingDefinition.setRoleDefId(session.getEmployee().getGlobalCom() + "." + defId_ + "." + role.getName());
			try{
				roleMappingDefinition.copyFrom(roleMappingDefinition.findRoleMappingDefinition());
				roleMappingDefinition.setRoleMappedUser(new RoleMappedUser());
				roleMappingDefinition.setRoleName(role.getName());

				IUser user = new User();
				user.setName(roleMappingDefinition.getMappedUserName());
				user.setUserId(roleMappingDefinition.getMappedUserId());
				user.getMetaworksContext().setHow(IUser.HOW_PICKER);
				roleMappingDefinition.getRoleMappedUser().getUsers().add(user);

				roleMappingDefinitions.add(roleMappingDefinition);
			}catch(Exception e){
				RoleMappingDefinition roleMappingDef = new RoleMappingDefinition();
				roleMappingDef.setRoleDefId(roleMappingDefinition.getRoleDefId());
				roleMappingDef.setDefId(defId_);
				roleMappingDef.setRoleName(role.getName());
				roleMappingDefinition.getRoleMappedUser().getUsers().add(session.getUser());

				roleMappingDef.setComCode(session.getEmployee().getGlobalCom());
				roleMappingDef.setRoleDefType(RoleMappingDefinition.ROLE_DEF_TYPE_USER);
				roleMappingDef.getMetaworksContext().setHow(IUser.HOW_PICKER);
				
				roleMappingDefinitions.add(roleMappingDef);
			}
		}
	}

	protected void installProcessView(ProcessDefinition definition) throws Exception {
		//setting process view
		ProcessModeler processModeler = new ProcessModeler();
		processModeler.setPalette(null);

		((ProcessCanvas) processModeler.getCanvas()).setMetaworksContext(new MetaworksContext());
		((ProcessCanvas) processModeler.getCanvas()).getMetaworksContext().setWhen("monitor");
		setProcessView(processModeler);

		getProcessView().setModel(definition);
		getProcessView().setElementViewActionDelegate(MetaworksRemoteService.getComponent(ElementViewActionDelegateForInstanceMonitoring.class));
	}

	public void save() throws Exception{
		for(IRoleMappingDefinition roleMappingDefinition: roleMappingDefinitions){
			RoleMappingDefinition saveRoleMappingDef = null;
			
			if(roleMappingDefinition instanceof RoleMappingDefinition){
				saveRoleMappingDef = (RoleMappingDefinition)roleMappingDefinition;
				saveRoleMappingDef.setMappedUserId(((RoleMappingDefinition) roleMappingDefinition).getRoleMappedUser().getUsers().get(0).getUserId());
//				saveRoleMappingDef.setMappedUserName(((RoleMappingDefinition) roleMappingDefinition).getRoleMappedUser().getUsers().get(0).getUserName());
			}else{
				saveRoleMappingDef = new RoleMappingDefinition();
				saveRoleMappingDef.copyFrom(roleMappingDefinition);
			}
			
			if(roleMappingDefinition.getMappedUserId()!=null){
				if(roleMappingDefinition.getRoleDefId() == null){
					saveRoleMappingDef.setRoleDefId(saveRoleMappingDef.getComCode() + "." + saveRoleMappingDef.getDefId() + "." + saveRoleMappingDef.getRoleName());
					saveRoleMappingDef.createDatabaseMe();
				}else{
					saveRoleMappingDef.syncToDatabaseMe();
				}
				/*
				if(roleMappingDefinition instanceof RoleMappingDefinition){
					((RoleMappingDefinition) roleMappingDefinition).createDatabaseMe();
				}else{
					
					RoleMappingDefinition copy = new RoleMappingDefinition();
					copy.copyFrom(roleMappingDefinition);
					
					try{
						copy.syncToDatabaseMe();
					}catch(Exception e){
						copy.createDatabaseMe();
					}
				}
				*/
				
			}else{
				if(roleMappingDefinition.getRoleDefId() != null)
					saveRoleMappingDef.syncToDatabaseMe();
			}
		}
		
	}
	
	public void putRoleMappings(ProcessManagerRemote processManager, String instId) throws Exception{
		for(IRoleMappingDefinition roleMappingDefinition: roleMappingDefinitions){
			if( RoleMappingDefinition.ROLE_DEF_TYPE_USER.equals(roleMappingDefinition.getRoleDefType() )){
				if(roleMappingDefinition.getMappedUserId()!=null){
					processManager.putRoleMapping(instId, roleMappingDefinition.getRoleName(), roleMappingDefinition.getMappedUserId());
				}
			}else if( RoleMappingDefinition.ROLE_DEF_TYPE_ROLE.equals(roleMappingDefinition.getRoleDefType() )){
				if(roleMappingDefinition.getMappedRoleCode()!=null ){
					RoleUser roleUser = new RoleUser();
					roleUser.setRoleCode(roleMappingDefinition.getMappedRoleCode());
					IRoleUser refRoleUser = roleUser.findUserByRoleCode();
					while(refRoleUser.next()){
						processManager.putRoleMapping(instId, roleMappingDefinition.getRoleName(), refRoleUser.getEmpCode() );
					}
					
				}
			}
		}
	}
	

}

