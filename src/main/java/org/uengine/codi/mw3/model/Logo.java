package org.uengine.codi.mw3.model;

import org.metaworks.annotation.Resource;
import org.metaworks.website.MetaworksFile;

public class Logo {

	MetaworksFile mainLogo;	
	@Resource(def="<org.metaworks.website.MetaworksFile><uploadedPath>fileSystem/logo.gif</uploadedPath><mimeType>image/gif</mimeType></org.metaworks.website.MetaworksFile>")
		public MetaworksFile getMainLogo() {
			return mainLogo;
		}
		public void setMainLogo(MetaworksFile mainLogo) {
			this.mainLogo = mainLogo;
		}

}
