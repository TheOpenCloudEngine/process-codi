package org.uengine.codi.mw3.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.metaworks.MetaworksContext;
import org.metaworks.website.MetaworksFile;
import org.metaworks.annotation.Hidden;
import org.metaworks.dao.TransactionContext;

public class PortraitPdfFile extends MetaworksFile{
	
	public PortraitPdfFile(){
		setMetaworksContext(new MetaworksContext());
		getMetaworksContext().setWhen("pdf");
	}
	
	String inputFilePath;
		@Hidden
		public String getInputFilePath() {
			return inputFilePath;
		}
		public void setInputFilePath(String inputFilePath) {
			this.inputFilePath = inputFilePath;
		}
		
	String outputFilePath;
		@Hidden
		public String getOutputFilePath() {
			return outputFilePath;
		}
		public void setOutputFilePath(String outputFilePath) {
			this.outputFilePath = outputFilePath;
		}
	
	@Override
	public Object[] upload() throws FileNotFoundException, IOException, Exception {
		if(getFileTransfer() == null && getFileTransfer().getFilename() != null && getFileTransfer().getFilename().length() > 0)
			throw new Exception("No file attached");
//		String outPath = outputFilePath + ".pdf";
//		
//		new File(outPath).getParentFile().mkdirs();
//		copyStream(getFileTransfer().getInputStream(), new FileOutputStream(outPath));
		
		String prefix = TransactionContext.getThreadLocalInstance()
				.getRequest().getSession().getServletContext()
				.getRealPath("/images/portrait/pdf/");
		System.out.println(prefix);
		
		String portraitFileName = "aaa" + ".pdf";
		String uploadPath = prefix + File.separator + portraitFileName;
		new File(uploadPath).getParentFile().mkdirs();
		
		InputStream is = null;
		FileOutputStream os = null;
		
		try {						
			is = this.getFileTransfer().getInputStream();
			os = new FileOutputStream(uploadPath);
			
			MetaworksFile.copyStream(is, os);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(os != null)
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if(is != null)
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
		setFileTransfer(null);
		
		return null;
	}

}
