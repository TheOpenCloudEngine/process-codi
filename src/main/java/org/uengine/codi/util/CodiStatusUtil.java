package org.uengine.codi.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class CodiStatusUtil {

	public final static String STATUS_READY 	= "ready";
	public final static String STATUS_QUEUE 	= "queue";
	public final static String STATUS_COMPLETE 	= "complete";
	public final static String STATUS_ERROR 	= "error";

	String statusFolderPath;
		public String getStatusFolderPath() {
			return statusFolderPath;
		}
		public void setStatusFolderPath(String statusFolderPath) {
			this.statusFolderPath = statusFolderPath;
		}

	String statusFilename;
		public String getStatusFilename() {
			return statusFilename;
		}
		public void setStatusFilename(String statusFilename) {
			this.statusFilename = statusFilename;
		}
		
	public CodiStatusUtil(String statusFolderPath, String statusFilename){
		this.setStatusFolderPath(statusFolderPath);
		this.setStatusFilename(statusFilename);
	}
		
	private String makeFilePath(String status){
		return this.getStatusFolderPath() + File.separatorChar + this.getStatusFilename() + "." + status;
	}
	
	public boolean isProcessing(){
		return this.isReady() || this.isQueue() || this.isComplete();
	}
	
	public boolean isReady(){
		return this.exists(makeFilePath(STATUS_READY));
	}

	public boolean isQueue(){
		return this.exists(makeFilePath(STATUS_QUEUE));
	}

	public boolean isComplete(){
		return this.exists(makeFilePath(STATUS_COMPLETE));
	}
	
	public boolean isError(){
		return this.exists(makeFilePath(STATUS_ERROR));
	}
	
	public boolean exists(String path){
		File file = new File(path);
		
		if(file.exists() && file.isFile())
			return true;
		else
			return false;
	}
	
	public boolean ready(){
		File srcFile = new File(this.makeFilePath(STATUS_ERROR));
		File file = new File(makeFilePath(STATUS_READY));
		if( srcFile.exists() ){
			srcFile.renameTo(file);
		}else{
			try {
				FileUtils.touch(file);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	public void queue(){
		File srcFile = new File(this.makeFilePath(STATUS_READY));
		File dstFile = new File(this.makeFilePath(STATUS_QUEUE));
		
		srcFile.renameTo(dstFile);
	}
	
	public void complete(){
		File srcFile = new File(this.makeFilePath(STATUS_QUEUE));
		File dstFile = new File(this.makeFilePath(STATUS_COMPLETE));
		
		srcFile.renameTo(dstFile);		
	}
	
	public void error(){
		File srcFile = new File(this.makeFilePath(STATUS_QUEUE));
		File dstFile = new File(this.makeFilePath(STATUS_ERROR));
		
		srcFile.renameTo(dstFile);		
	}
	
}
