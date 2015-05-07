package org.uengine.codi.mw3;

import org.metaworks.Remover;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.widget.ModalWindow;
import org.uengine.codi.mw3.model.Popup;

public class ErrorPage {
	
	String errorMessage;
		public String getErrorMessage() {
			return errorMessage;
		}
		public void setErrorMessage(String errorMessage) {
			this.errorMessage = errorMessage;
		}
		
	@ServiceMethod(target=ServiceMethodContext.TARGET_APPEND)
	public Object[] close(){
		return new Object[]{new Remover(new ModalWindow() , true), new Remover(new Popup() , true)};
	}

}
