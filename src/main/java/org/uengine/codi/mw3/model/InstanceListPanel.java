package org.uengine.codi.mw3.model;

import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Id;
import org.uengine.codi.mw3.calendar.ScheduleCalendar;

//@Face(
//		ejsPathMappingByContext=
//	{
//		"{where: 'pinterest', face: 'dwr/metaworks/org/uengine/codi/mw3/model/InstanceListPanel_pinterest.ejs'}",
//		"{where: 'sns', face: 'dwr/metaworks/org/uengine/codi/mw3/model/InstanceListPanel_sns.ejs'}",
//		"{where: 'oce_app', face: 'dwr/metaworks/org/uengine/oce/InstanceListPanel_oce.ejs'}",
//		"{where: 'oce_project', face: 'dwr/metaworks/org/uengine/oce/InstanceListPanel_oce.ejs'}"
//		
//	}		
//
//)
public class InstanceListPanel {

	String id;
	@Id
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	@AutowiredFromClient
	public Session session;

	ScheduleCalendar scheduleCalendar;
		public ScheduleCalendar getScheduleCalendar() {
			return scheduleCalendar;
		}
		public void setScheduleCalendar(ScheduleCalendar scheduleCalendar) {
			this.scheduleCalendar = scheduleCalendar;
		}
	
	boolean preloaded;
		public boolean isPreloaded() {
			return preloaded;
		}
		public void setPreloaded(boolean preloaded) {
			this.preloaded = preloaded;
		}
		
	public InstanceListPanel() throws Exception{
		this(null);
	}
	
	public InstanceListPanel(Session session) throws Exception{		
		
		// TODO:2013-01-10
/*		if(session!=null){
			this.session = session;
			
			newInstantiator = new CommentWorkItem();
			newInstantiator.setWriter(session.getUser());
			newInstantiator.getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);
			newInstantiator.setInstantiation(true);
			
			if("sns".equals(session.getEmployee().getPreferUX())){
				this.getMetaworksContext().setWhere("sns");
				newInstantiator.getMetaworksContext().setHow("sns");
			}
			if("topic".equals(session.getLastPerspecteType()))
				this.getMetaworksContext().setHow("topic");
			
		}*/
		
		/*
		
		if(session!=null){
			if("sns".equals(session.getEmployee().getPreferUX())){
				try{
					newInstancePanel = new NewInstancePanel();			
					newInstancePanel.load(session);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
			if("sns".equals(session.getEmployee().getPreferUX())){
				this.getMetaworksContext().setWhere("oce");
			}
			if("topic".equals(session.getLastPerspecteType())){
				this.getMetaworksContext().setHow("topic");
				this.setPerspectiveInfo(new TopicInfo(session, session.getLastSelectedItem()));
			}
			if("project".equals(session.getLastPerspecteType())){
				this.getMetaworksContext().setHow("project");
				this.setPerspectiveInfo(new ProjectInfo(session));
			}
			if("document".equals(session.getLastPerspecteType())){
				this.getMetaworksContext().setHow("document");
				this.setPerspectiveInfo(new DocumentInfo(session));
			}
			if("organization.group".equals(session.getLastPerspecteType())){
				this.getMetaworksContext().setHow("organization.group");
				this.setPerspectiveInfo(new DeptInfo(session));
			}
			if("role".equals(session.getLastPerspecteType())){
				this.getMetaworksContext().setHow("role");
				this.setPerspectiveInfo(new RoleInfo(session));
			}
			
			if("explorer".equals(session.getLastPerspecteType()))
				this.getMetaworksContext().setHow("explorer");		
			
			if("app".equals(session.getLastPerspecteType()))
				this.getMetaworksContext().setHow("app");
			
			if("oce_project".equals(session.getUx()))
				this.getMetaworksContext().setWhere("oce_project");
			
			if("process".equals(session.getLastPerspecteType())){
				this.getMetaworksContext().setHow("process");
				this.setPerspectiveInfo(new ProcessInfo(session));
			}
			
			if(this.getPerspectiveInfo() == null)
				this.setPerspectiveInfo(new PerspectiveInfo());
			
			instanceList = new InstanceList(session);
			
			this.setPreloaded(true);
		}
		*/
	}

	InstanceList instanceList;
		public InstanceList getInstanceList() {
			return instanceList;
		}
		public void setInstanceList(InstanceList instanceList) {
			this.instanceList = instanceList;
		}


}
