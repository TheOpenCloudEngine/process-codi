package com.defaultcompany.organization;

import org.uengine.codi.mw3.model.Employee;
import org.uengine.codi.mw3.model.IEmployee;
import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.RoleMapping;

public class DefaultCompanyRoleMapping extends RoleMapping{
	
	private static final long serialVersionUID = GlobalContext.SERIALIZATION_UID;
	
	final static String EXT_PROP_KEY_NateOnMessengerId = "EXT_PROP_KEY_NATEON_ID";

	public void fill(ProcessInstance instance) throws Exception {
		if(GlobalContext.isDesignTime()) return;
		
		Employee employee = new Employee();
		employee.setEmpCode(getEndpoint());
		IEmployee empCursor = employee.databaseMe();
		
		setResourceName(empCursor.getEmpName());
		setEmailAddress(empCursor.getEmail());
		setLocale(empCursor.getLocale());
		setCompanyId(empCursor.getGlobalCom());
		setUserPortrait("");

		
//		if (endpoint.indexOf("Facebook") != -1) {
//			FacebookClient facebookClient = new DefaultFacebookClient();
//			String facebookId = endpoint;//.substring(endpoint.indexOf("Facebook") + 8, endpoint.length());
//			String query = "SELECT uid, name, email, locale FROM user WHERE uid=" + facebookId;
//			
//			List<User> users = facebookClient.executeQuery(query, User.class);
//			if (users.size() == 0) {
//				new UEngineException("There's no such user [" + facebookId + "]");
//			} else {
//				User user = users.get(0);
//				setResourceName(user.getName());
//				setEmailAddress(user.getEmail());
//				setLocale(user.getLocale().split("_")[0]);
//				setCompanyId("uEngine");
//				setUserPortrait("http://graph.facebook.com/" + facebookId + "/picture");
//			}
//		}
		
	}
	
	public static void main(String args[]) throws Exception{
		RoleMapping rm = new DefaultCompanyRoleMapping();
		rm.fill(null);
	}
}