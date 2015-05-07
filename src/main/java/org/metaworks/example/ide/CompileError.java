package org.metaworks.example.ide;

public class CompileError {
	int column;
		public int getColumn() {
			return column;
		}
		public void setColumn(int column) {
			this.column = column;
		}

	int line;
		public int getLine() {
			return line;
		}
		public void setLine(int line) {
			this.line = line;
		}

	String message;

		public String getMessage() {
			return message;
		}
		public void setMessage(String errorMessage) {
			this.message = errorMessage;
		}
		
	
}
