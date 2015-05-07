package org.uengine.search.solr;

import java.util.Date;

import org.apache.solr.client.solrj.beans.Field;

public class SolrWorkItem {

	@Field
	String id;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		
	@Field("title")
	String[] title;
		public String[] getTitle() {
			return title;
		}
		public void setTitle(String[] title) {
			this.title = title;
		}
	@Field("instanceid")
	String instanceid;
		public String getInstanceid() {
			return instanceid;
		}
		public void setInstanceid(String instanceid) {
			this.instanceid = instanceid;
		}
	@Field("taskid")
	String taskid;
		public String getTaskid() {
			return taskid;
		}
		public void setTaskid(String taskid) {
			this.taskid = taskid;
		}
	@Field("writer")
	String writer;
		public String getWriter() {
			return writer;
		}
		public void setWriter(String writer) {
			this.writer = writer;
		}
		
	@Field("workitemtype")
	String workitemtype;
		public String getWorkitemtype() {
			return workitemtype;
		}
		public void setWorkitemtype(String workitemtype) {
			this.workitemtype = workitemtype;
		}
	@Field("uploadpath")
	String uploadpath;
		public String getUploadpath() {
			return uploadpath;
		}
		public void setUploadpath(String uploadpath) {
			this.uploadpath = uploadpath;
		}
	@Field("content")
	String[] contents;
		public String[] getContents() {
			return contents;
		}
		public void setContents(String[] contents) {
			this.contents = contents;
		}
		
	@Field("topicid")
	String topicid;
		public String getTopicid() {
			return topicid;
		}
		public void setTopicid(String topicid) {
			this.topicid = topicid;
		}

	@Field("last_modified")
	Date last_modified;
		public Date getLast_modified() {
			return last_modified;
		}
		public void setLast_modified(Date last_modified) {
			this.last_modified = last_modified;
		}
		
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		builder.append("\"id\"").append(":\"").append(id).append("\"");
		builder.append(",\"title\"").append(":\"").append(title != null ? title[0] : "").append("\"");
		builder.append(",\"instanceid\"").append(":\"").append(instanceid).append("\"");
		builder.append(",\"taskid\"").append(":\"").append(taskid).append("\"");
		builder.append(",\"writer\"").append(":\"").append(writer).append("\"");
		builder.append(",\"workitemtype\"").append(":\"").append(workitemtype).append("\"");
		builder.append(",\"uploadpath\"").append(":\"").append(uploadpath).append("\"");
		builder.append(",\"contents\"").append(":\"").append(contents != null ? contents[0] : "").append("\"");
		builder.append("}");
		
		return builder.toString();
	}
		
}