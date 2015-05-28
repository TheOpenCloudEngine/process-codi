package org.uengine.codi.mw3.model;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.Validator;
import org.metaworks.model.MetaworksElement;
import org.metaworks.model.MetaworksList;

public class ProcessVariableValueList extends MetaworksList<Object>{


	String type;
	@Hidden
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}


	@Override
	public MetaworksElement createNewElement() {

		MetaworksElement e = new MetaworksElement();
		e.setMetaworksContext(new MetaworksContext());
		e.getMetaworksContext().setWhen("edit");

		try {
			e.setValue(Thread.currentThread().getContextClassLoader().loadClass(getType()).newInstance());
		} catch (Exception e1) {
			throw new RuntimeException(e1);
		}

		return e;
	}
}
