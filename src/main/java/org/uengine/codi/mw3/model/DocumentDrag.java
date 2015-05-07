package org.uengine.codi.mw3.model;

import java.util.Date;

import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.ServiceMethod;

public class DocumentDrag {

	Long taskId;
		@Id
		public Long getTaskId() {
			return taskId;
		}
		public void setTaskId(Long taskId) {
			this.taskId = taskId;
		}
	Long instId;
			public Long getInstId() {
				return instId;
			}
			public void setInstId(Long instId) {
				this.instId = instId;
			}
	String content;
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
	String extFile;
		public String getExtFile() {
			return extFile;
		}
		public void setExtFile(String extFile) {
			this.extFile = extFile;
		}
	
	String tool;
		public String getTool() {
			return tool;
		}
		public void setTool(String tool) {
			this.tool = tool;
		}
	Date startDate;
		public Date getStartDate() {
			return startDate;
		}
		public void setStartDate(Date startDate) {
			this.startDate = startDate;
		}
	
	Date endDate;
		public Date getEndDate() {
			return endDate;
		}
		public void setEndDate(Date endDate) {
			this.endDate = endDate;
		}	

	@ServiceMethod(callByContent=true,mouseBinding="drag")
		public Session cut(){
			session.setClipboard(this);
			return session;
		}
		
	@ServiceMethod(callByContent=true, mouseBinding="drop", target=ServiceMethodContext.TARGET_APPEND)
	public Object[] drop() throws Exception{
		Object clipboard = session.getClipboard();
		
		return null;
		
	}
		@AutowiredFromClient
		public Session session;
	}
