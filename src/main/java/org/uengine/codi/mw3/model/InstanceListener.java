package org.uengine.codi.mw3.model;

public class InstanceListener {

	public final static String COMMAND_APPEND 		= "append";
	public final static String COMMAND_REFRESH 		= "refresh";
	public final static String COMMAND_REMOVE		= "remove";
	
	String command;
		public String getCommand() {
			return command;
		}
		public void setCommand(String command) {
			this.command = command;
		}

	Object applyItem;
		public Object getApplyItem() {
			return applyItem;
		}
		public void setApplyItem(Object applyItem) {
			this.applyItem = applyItem;
		}
	
	public InstanceListener(Object applyItem){
		this(COMMAND_APPEND, applyItem);
	}
	
	public InstanceListener(String command, Object applyItem){
		this.setCommand(command);
		this.setApplyItem(applyItem);
	}
}
