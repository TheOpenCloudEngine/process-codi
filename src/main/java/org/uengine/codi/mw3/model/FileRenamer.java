package org.uengine.codi.mw3.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.website.MetaworksFile;
import org.uengine.codi.mw3.CodiClassLoader;
import org.uengine.util.UEngineUtil;

@Face(displayName="Rename...")
public class FileRenamer implements ContextAware{

	public FileRenamer(){
		setMetaworksContext(new MetaworksContext());
		getMetaworksContext().setWhen("edit");
	}
	
	MetaworksContext metaworksContext;
	
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
	
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}

	ResourceFile file;
	@Hidden
	
		public ResourceFile getFile() {
			return file;
		}
	
		public void setFile(ResourceFile file) {
			this.file = file;

			String oldFileName = UEngineUtil.getFileName(file.getAlias());

			setNewName(oldFileName);
		}



	String newName;
	
		public String getNewName() {
			return newName;
		}
	
		public void setNewName(String newName) {
			this.newName = newName;
		}

	@ServiceMethod(callByContent=true)
	public Object[] rename() throws FileNotFoundException, IOException, Exception{
		
		String resourceBase = CodiClassLoader.getMyClassLoader().getCodebase();
		
		String oldFileName = UEngineUtil.getFileName(file.getAlias());
		String parentFolderName = UEngineUtil.getFilePath(file.getAlias());

		new File(resourceBase + parentFolderName + "/"+ oldFileName).renameTo(new File(resourceBase + parentFolderName + "/" + getNewName()));
		
		ResourceFile resourceFile = new ResourceFile();
		resourceFile.setAlias(parentFolderName);
		resourceFile.drillDown();
		resourceFile.setMetaworksContext(getFile().getMetaworksContext());
		
		return new Object[]{resourceFile};
	}
	
}
