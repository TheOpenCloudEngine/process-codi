package org.uengine.codi.mw3.model;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.Validator;

import java.io.Serializable;

public class ParameterValue implements ContextAware{

	String variableName;
		public String getVariableName() {
			return variableName;
		}
		public void setVariableName(String variableName) {
			this.variableName = variableName;
		}

	String argument;
		public String getArgument() {
			return argument;
		}
		public void setArgument(String argument) {
			this.argument = argument;
		}

	boolean isMultipleInput;
		public boolean isMultipleInput() {
			return isMultipleInput;
		}
		public void setMultipleInput(boolean isMultipleInput) {
			this.isMultipleInput = isMultipleInput;
		}

	ProcessVariableValueList processVariableValueList;
		@Validator(availableUnder="metaworksContext.when == 'edit'")
		public ProcessVariableValueList getProcessVariableValueList() {
			return processVariableValueList;
		}
		public void setProcessVariableValueList(ProcessVariableValueList processVariableValueList) {
			this.processVariableValueList = processVariableValueList;
		}

	Serializable value;
		public Serializable getValue() {
			return value;
		}
		public void setValue(Serializable value) {
			this.value = value;
		}



	//
//	String valueString;
//		public String getValueString() {
//			return valueString;
//		}
//		public void setValueString(String valueString) {
//			this.valueString = valueString;
//		}
//
//	Long valueLong;
//		public Long getValueLong() {
//			return valueLong;
//		}
//		public void setValueLong(Long valueLong) {
//			this.valueLong = valueLong;
//		}
//
//	Boolean valueBoolean;
//		public Boolean getValueBoolean() {
//			return valueBoolean;
//		}
//		public void setValueBoolean(Boolean valueBoolean) {
//			this.valueBoolean = valueBoolean;
//		}
//	
//	Calendar valueCalendar;
//		public Calendar getValueCalendar() {
//			return valueCalendar;
//		}
//		public void setValueCalendar(Calendar valueCalendar) {
//			this.valueCalendar = valueCalendar;
//		}
		


	String variableType;
		public String getVariableType() {
			return variableType;
		}
		public void setVariableType(String variableType) {
			this.variableType = variableType;
		}

	MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}
	
}
