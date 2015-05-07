package org.metaworks.example.ide;

import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.ServiceMethod;

public class SourceCode {
	
	public SourceCode(){
	}
	
	String id;
		@Id
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}

	String code;
		public String getCode() {
			return code;
		}
		
		public void setCode(String code) {
			this.code = code;
		}
	
	CompileError[] compileErrors;
		public CompileError[] getCompileErrors() {
			return compileErrors;
		}
		public void setCompileErrors(CompileError[] compileErrors) {
			this.compileErrors = compileErrors;
		}
	
	String language;
		public String getLanguage() {
			return language;
		}
	
		public void setLanguage(String language) {
			this.language = language;
		}
	
	Position cursorPosition;
		public Position getCursorPosition() {
			return cursorPosition;
		}	
		public void setCursorPosition(Position cursorPosition) {
			this.cursorPosition = cursorPosition;
		}
		
	String clientObjectId;
		public String getClientObjectId() {
			return clientObjectId;
		}
		public void setClientObjectId(String clientObjectId) {
			this.clientObjectId = clientObjectId;
		}
	
	String lineAssistRequested;
			
		public String getLineAssistRequested() {
			return lineAssistRequested;
		}
	
		public void setLineAssistRequested(String lineAssistRequested) {
			this.lineAssistRequested = lineAssistRequested;
		}

	@ServiceMethod(callByContent=true, target= ServiceMethodContext.TARGET_STICK)
	public CodeAssist requestAssist(){
		CodeAssist codeAssist = new CodeAssist();
		
		codeAssist.setSrcCodeObjectId(clientObjectId);
		
		return codeAssist;
	}
	
	@ServiceMethod(callByContent=true, target= ServiceMethodContext.TARGET_NONE)
	public void installBreakpoint(){		
		System.out.println("install breakpoint at : " + lineAssistRequested);
	}
}
