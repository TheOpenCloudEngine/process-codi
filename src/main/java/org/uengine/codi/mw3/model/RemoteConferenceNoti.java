package org.uengine.codi.mw3.model;

import java.util.Calendar;
import java.util.Date;

import org.uengine.codi.CodiProcessManagerBean;
import org.uengine.codi.MetaworksUEngineSpringConnectionAdapter;
import org.uengine.kernel.GlobalContext;

import java.io.Serializable;

public class RemoteConferenceNoti implements Serializable {
	
	public RemoteConferenceNoti(){
	}
	
	String instId;
		public String getInstId() {
			return instId;
		}
		public void setInstId(String instId) {
			this.instId = instId;
		}

	Date date;
		public Date getDate() {
			return date;
		}
		public void setDate(Date date) {
			this.date = date;
		}
	
	String timeGap;
		public String getTimeGap() {
			return timeGap;
		}
		public void setTimeGap(String timeGap) {
			this.timeGap = timeGap;
		}
		
		
	public void loadService() throws Exception{
		System.out.println(this.getInstId()+" - 노티발행시간 설정"); 
		this.setDate(loadWaitTime(false));
	}
	
	public void addWorkItem() throws Exception{
		MetaworksUEngineSpringConnectionAdapter connectionAdapter = new MetaworksUEngineSpringConnectionAdapter();
		
		CodiProcessManagerBean pm = new CodiProcessManagerBean();
		pm.setConnectionFactory(connectionAdapter);
		pm.setAutoCloseConnection(false);
		pm.setManagedTransaction(true);
		
		String title = this.createTitle();
		
		Session codiSession = new Session();
		
		Instance instance = new Instance();
		instance.setInstId(Long.valueOf(this.getInstId()));
		
		IEmployee emp = new Employee();
		emp.setEmpCode(GlobalContext.getPropertyString("codi.user.id", "0"));
		emp.setEmpName(GlobalContext.getPropertyString("codi.user.name", "CODI"));
		emp.setGlobalCom(instance.databaseMe().getInitComCd());
		codiSession.setEmployee(emp);
		
		SystemWorkItem comment = new SystemWorkItem();
		comment.setSystemMessage(title);
		comment.setInstId(new Long(this.getInstId()));
		comment.session = codiSession;
		comment.processManager = pm;
		
		comment.add();

		this.setDate(loadWaitTime(true));
	}
	
	public Date loadWaitTime(Boolean isReload){
		Calendar meetingCal = Calendar.getInstance();
		meetingCal.setTime(this.getDate());
		meetingCal.set(Calendar.SECOND, 0);
		int count = loadTimeGap();
		if(isReload)
			count-=3;
		if(count>=5 && count<10){
			meetingCal.add(Calendar.MINUTE, -5);
		}else if(count>=10 && count<30){
			meetingCal.add(Calendar.MINUTE, -10);
		}else if(count>=30 && count<60){
			meetingCal.add(Calendar.MINUTE, -30);
		}else if(count>=60){
			meetingCal.add(Calendar.MINUTE, -60);
		}
		this.setTimeGap(String.valueOf(count));
		return meetingCal.getTime();
	}
	
	public int loadTimeGap(){
		Calendar nowCal = Calendar.getInstance();
		Calendar meetingCal = Calendar.getInstance();
		meetingCal.setTime(this.getDate());
		nowCal.set(Calendar.SECOND, 0);
		meetingCal.set(Calendar.SECOND, 0);
		int count = 0;
		while (meetingCal.after(nowCal)) {
			if(count>=60){
				break;
			}
			count++;
			nowCal.add(Calendar.MINUTE, 1);
		}
		return count;
	}

	
	public String createTitle(){
		int count = loadTimeGap() - 3;
		if(count>30 && count<=60){
			return "회의시간 1시간 전 입니다.";
		}else if(count>10 && count<=30){
			return "회의시간 30분 전 입니다.";
		}else if(count>5 && count<=10){
			return "회의시간 10분 전 입니다.";
		}else if(count>0 && count<=5){
			return "회의시간 5분 전 입니다.";
		}else{
			this.setTimeGap("-1");
			return "회의가 시작되었습니다.";
		}
	}
}

