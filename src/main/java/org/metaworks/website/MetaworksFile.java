package org.metaworks.website;

import org.metaworks.MetaworksException;
import org.metaworks.website.AbstractMetaworksFile;
import org.uengine.kernel.GlobalContext;
import org.uengine.util.UEngineUtil;

public class MetaworksFile extends AbstractMetaworksFile{

	private static final int FILE_UPLOAD_MAX_BYTES = Integer.parseInt(GlobalContext.getPropertyString("fileUploadMaxBytes", "20971520"));

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


	@Override
	public void validate() throws MetaworksException {
		if(fileTransfer.getSize() > FILE_UPLOAD_MAX_BYTES){
			throw new MetaworksException("File Size is over " + FILE_UPLOAD_MAX_BYTES + " bytes!");
		}
	}
}
