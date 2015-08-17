package org.uengine.codi.mw3.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.metaworks.MetaworksContext;
import org.metaworks.annotation.Hidden;
import org.metaworks.website.MetaworksFile;
import org.uengine.kernel.GlobalContext;

public class PortraitImageFile extends MetaworksFile {

	public PortraitImageFile() {
		setMetaworksContext(new MetaworksContext());
		getMetaworksContext().setWhen(MetaworksContext.WHEN_NEW);
	}

	String empCode;
		@Hidden
		public String getEmpCode() {
			return empCode;
		}
		public void setEmpCode(String empCode) {
			this.empCode = empCode;
		}


	@Override
	public Object[] upload() throws FileNotFoundException, IOException, Exception {
		if (getFileTransfer() == null && getFileTransfer().getFilename() != null && getFileTransfer().getFilename().length() > 0)
			throw new Exception("No file attached");
		
		if(getFileTransfer().getInputStream().available() == 0) return null;		

		String fileSystemPath = GlobalContext.FILE_SYSTEM_DIR;
		String portraitPath = fileSystemPath + File.separatorChar + "portrait";
		String srcName = portraitPath + File.separatorChar + getEmpCode() + ".jpg";
		String thumnailName = portraitPath + File.separatorChar + getEmpCode() + ".thumnail.jpg";
		
		// ���濡���� ��대�� ������ 諛� ������
		File portraitFile = new File(portraitPath);
		if(!portraitFile.exists() || !portraitFile.isDirectory())
			portraitFile.mkdirs();

		File thumnailNameFile =new File(thumnailName);
		if(thumnailNameFile.exists()){
			thumnailNameFile.delete();
		}
		
		InputStream is = null;
		FileOutputStream os = null;
		
		try {						
			is = this.getFileTransfer().getInputStream();
			os = new FileOutputStream(srcName);
			
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
		};
		
		setFileTransfer(null); // ensure to clear the data
		
		return null;
	}

}
