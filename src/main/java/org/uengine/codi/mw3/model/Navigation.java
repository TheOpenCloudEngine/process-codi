package org.uengine.codi.mw3.model;

public class Navigation {
	
	String media;
		public String getMedia() {
			return media;
		}
		public void setMedia(String media) {
			this.media = media;
		}

	String perspectiveMode;
		public String getPerspectiveMode() {
			return perspectiveMode;
		}
		public void setPerspectiveMode(String perspectiveMode) {
			this.perspectiveMode = perspectiveMode;
		}

	String perspectiveType;
		public String getPerspectiveType() {
			return perspectiveType;
		}
		public void setPerspectiveType(String perspectiveType) {
			this.perspectiveType = perspectiveType;
		}
	
	String perspectiveValue;
		public String getPerspectiveValue() {
			return perspectiveValue;
		}	
		public void setPerspectiveValue(String perspectiveValue) {
			this.perspectiveValue = perspectiveValue;
		}
		
	boolean isDiffrentCompany;
		public boolean isDiffrentCompany() {
			return isDiffrentCompany;
		}
		public void setDiffrentCompany(boolean isDiffrentCompany) {
			this.isDiffrentCompany = isDiffrentCompany;
		}

	String keyword;
		public String getKeyword() {
			return keyword;
		}
		public void setKeyword(String keyword) {
			this.keyword = keyword;
		}

	String topSearchKeyword;
	public String getTopSearchKeyword() {
		return topSearchKeyword;
	}
	public void setTopSearchKeyword(String topSearchKeyword) {
		this.topSearchKeyword = topSearchKeyword;
	}

	Employee employee;
		public Employee getEmployee() {
			return employee;
		}
		public void setEmployee(Employee employee) {
			this.employee = employee;
		}
	
	public Navigation(){
		this(null);
	}
	
	public Navigation(Session session){
		if(session != null){
			Employee employee = null;
			
			try{
				employee = new Employee();
				employee.copyFrom(session.getEmployee());
			}catch(Exception e){
				e.printStackTrace();
			}
			
			this.setPerspectiveMode(session.getLastPerspecteMode());
			this.setPerspectiveType(session.getLastPerspecteType());
			this.setPerspectiveValue(session.getLastSelectedItem());		
			this.setKeyword(session.getSearchKeyword());
			this.setTopSearchKeyword(session.getTopSearchKeyword());
			this.setEmployee(employee);
			this.setMedia(session.getUx());
		}
	}
	
}
