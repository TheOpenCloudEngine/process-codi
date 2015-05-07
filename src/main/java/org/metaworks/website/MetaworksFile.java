package org.metaworks.website;

import org.metaworks.website.AbstractMetaworksFile;
import org.uengine.kernel.GlobalContext;
import org.uengine.util.UEngineUtil;

public class MetaworksFile extends AbstractMetaworksFile{

	@Override
	public String overrideUploadPathPrefix() {
		
		String base = GlobalContext.FILE_SYSTEM_DIR;
		
		return base + "/";
		
	}

	@Override
	public String renameUploadFile(String filename) {
		return UEngineUtil.getCalendarDir() + "/" + super.renameUploadFile(filename);
	}

	@Override
	public String renameUploadFileWithMimeType(String filename, String mimeType) {
		// TODO Auto-generated method stub
		return UEngineUtil.getCalendarDir() + "/" + super.renameUploadFileWithMimeType(filename, mimeType);
	}
	
	

}
