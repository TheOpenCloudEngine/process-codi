package org.uengine.codi.mw3.model;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.Validator;
import org.metaworks.model.MetaworksElement;
import org.metaworks.model.MetaworksList;
import org.uengine.kernel.GlobalContext;

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

			Object newVal = null;
			try {
				newVal = GlobalContext.deserialize(GlobalContext.serialize(getDefaultValue(), String.class));
				e.setValue(newVal);
			} catch (Exception e1) {
				throw new RuntimeException("couldn't clone default value", e1);
			}

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
