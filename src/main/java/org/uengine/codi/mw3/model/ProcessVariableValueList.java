package org.uengine.codi.mw3.model;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.Validator;
import org.metaworks.model.MetaworksElement;
import org.metaworks.model.MetaworksList;

import java.io.Serializable;

public class ProcessVariableValueList extends MetaworksList<Object>{


	String type;
	@Hidden
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}


	Serializable defaultValue;
	@Hidden
		public Serializable getDefaultValue() {
			return defaultValue;
		}
		public void setDefaultValue(Serializable defaultValue) {
			this.defaultValue = defaultValue;
		}


	@Override
	public MetaworksElement createNewElement() {

		MetaworksElement e = new MetaworksElement();
		e.setMetaworksContext(new MetaworksContext());
		e.getMetaworksContext().setWhen("edit");

		if(getDefaultValue()!=null) {
			e.setValue(getDefaultValue());
		} else {
			try {
				e.setValue(Thread.currentThread().getContextClassLoader().loadClass(getType()).newInstance());
			} catch (Exception e1) {
				throw new RuntimeException(e1);
			}
		}

		return e;
	}
}
