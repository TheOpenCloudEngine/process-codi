package org.metaworks.example.ide;

import org.metaworks.annotation.ServiceMethod;

import java.util.ArrayList;

public class CodeAssist {
	
	public CodeAssist(){
		setDocument(new CodeAssistDocument());
		setAssistances(new ArrayList<String>());

	}
	
	ArrayList<String> assistances;
		public ArrayList<String> getAssistances() {
			return assistances;
		}
		public void setAssistances(ArrayList<String> assistances) {
			this.assistances = assistances;
		}
		
	String srcCodeObjectId;
		public String getSrcCodeObjectId() {
			return srcCodeObjectId;
		}
		public void setSrcCodeObjectId(String srcCodeObjectId) {
			this.srcCodeObjectId = srcCodeObjectId;
		}
		
	String selectedItem;
		public String getSelectedItem() {
			return selectedItem;
		}
		public void setSelectedItem(String selectedItem) {
			this.selectedItem = selectedItem;
		}
		
	String lineAssistRequested;
			
		public String getLineAssistRequested() {
			return lineAssistRequested;
		}
		public void setLineAssistRequested(String lineAssistRequested) {
			this.lineAssistRequested = lineAssistRequested;
		}

	CodeAssistDocument document; 
			
		public CodeAssistDocument getDocument() {
			return document;
		}
		public void setDocument(CodeAssistDocument document) {
			this.document = document;
		}
		
		
	@ServiceMethod(callByContent=true)
	public CodeAssistDocument showDoc(){
		return new CodeAssistDocument(this);
	}
	
}
