package org.uengine.codi.mw3.model;

import org.metaworks.annotation.Id;
import org.metaworks.annotation.ServiceMethod;

public class Preview {

	String pageCount;
		public String getPageCount() {
			return pageCount;
		}
		public void setPageCount(String pageCount) {
			this.pageCount = pageCount;
		}
		
	Long grpTaskId;
		@Id
		public Long getGrpTaskId() {
			return grpTaskId;
		}
		public void setGrpTaskId(Long grpTaskId) {
			this.grpTaskId = grpTaskId;
		}
	Long taskId;
		public Long getTaskId() {
			return taskId;
		}
		public void setTaskId(Long taskId) {
			this.taskId = taskId;
		}
	
	String mimeType;
		public String getMimeType() {
			return mimeType;
		}
		public void setMimeType(String mimeType) {
			this.mimeType = mimeType;
		}
		
	String convertStatus;
		public String getConvertStatus() {
			return convertStatus;
		}
		public void setConvertStatus(String convertStatus) {
			this.convertStatus = convertStatus;
		}
	boolean errorStatus;
		public boolean isErrorStatus() {
			return errorStatus;
		}
		public void setErrorStatus(boolean errorStatus) {
			this.errorStatus = errorStatus;
		}
		
	public void setPageCountInt(int pageCount) {
		this.pageCount = Integer.toString(pageCount);
	}
	
	@ServiceMethod(callByContent=true)
	public void viewAsPDF() throws Exception{
		setMimeType("pdf");
	}
	
	@ServiceMethod(callByContent=true)
	public void viewAsImages()throws Exception{
		setMimeType("image");
	}
	
}
