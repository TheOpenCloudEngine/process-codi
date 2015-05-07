package org.uengine.codi.mw3.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.directwebremoting.io.FileTransfer;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.ServiceMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.uengine.codi.mw3.CodiClassLoader;
import org.uengine.processmanager.ProcessManagerRemote;
import org.uengine.util.UEngineUtil;


public class Posting {
	

	String text;
	
		public String getText() {
			return text;
		}
	
		public void setText(String text) {
			this.text = text;
		}

	FileTransfer file;

		public FileTransfer getFile() {
			return file;
		}
	
		public void setFile(FileTransfer file) {
			this.file = file;
		}

	@ServiceMethod(callByContent=true)
	public void post() throws Exception{
		
		//CodiClassLoader.codiClassLoader.sourceCodeBase() == /Users/jyjang
		
		String path = "/Users/jyjang/Documents/" + file.getFilename();
		UEngineUtil.copyStream(file.getInputStream(), new FileOutputStream(path));
		file = new FileTransfer(file.getFilename(), file.getMimeType(), new FileInputStream(path));
		
		codiPmSVC.addFolder("test", "-1"); 
		codiPmSVC.initializeProcess("22");
		codiPmSVC.applyChanges();
	}

	
	@Autowired
	public ProcessManagerRemote codiPmSVC;

}
