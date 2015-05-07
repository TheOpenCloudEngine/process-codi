package org.uengine.codi.mw3.model;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.ServiceMethod;

public class DynamicComboBox{
	
	public DynamicComboBox(){
		
	}
	
	
	
	@ServiceMethod(target="stick")
	public DropDownPanel onSelect(){
		DropDownPanel dropdownPanel = new DropDownPanel();
		
		dropdownPanel.setSelections(new String[]{"a", "b", "c"});
		
		return dropdownPanel;
	}
	
}


