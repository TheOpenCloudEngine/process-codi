package org.uengine.codi.mw3;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.metaworks.Refresh;
import org.metaworks.dwr.MetaworksRemoteService;
import org.metaworks.dwr.TransactionalDwrServlet;
import org.oce.garuda.multitenancy.TenantContext;
import org.uengine.codi.mw3.model.Session;
//import org.uengine.codi.platform.SecurityContext;
import org.uengine.kernel.GlobalContext;

public class CodiDwrServlet extends TransactionalDwrServlet{

	final static String SECURITYMSG_SYSTEM_USAGE_PROHIBITED = " 패키지 이하의 클래스는 사용할 수 없습니다.".intern();///"Your App can't use any of classes under :".intern();
	
	final static HashMap<String, String> securedPackages = new HashMap<String, String>();
	
	static PrintStream originalSystemOut = System.out;
	static PrintStream originalSystemErr = System.err;

	static{
		securedPackages.put("java.sql", SECURITYMSG_SYSTEM_USAGE_PROHIBITED);
//		securedPackages.put(File.class.getPackage().getName(), SECURITYMSG_SYSTEM_USAGE_PROHIBITED);
///		securedPackages.put(Mongo.class.getPackage().getName(), SECURITYMSG_SYSTEM_USAGE_PROHIBITED);
	}

	@Override
	protected Object fromXML(InputStream is) {
		// TODO Auto-generated method stub
		try {
			return GlobalContext.deserialize(is, Object.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void toXML(Object obj, OutputStream out) {
		// TODO Auto-generated method stub
		try {
			GlobalContext.serialize(obj, out, Object.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	static public boolean okIfSystem(){
		StackTraceElement[] stackElem = Thread.currentThread().getStackTrace();
		
		for(int i = 0; i<stackElem.length; i++){
			if(MetaworksRemoteService.class.getName().equals(stackElem[i].getClassName())){

				return false;
			}
		}
		
		return true;
	
	}
	
	static public void checkPermission(String pkg) throws SecurityException{
		
		//StackTraceElement[] stackElem = Thread.currentThread().getStackTrace();
		
		boolean needToCheck = false;//SecurityContext.getThreadLocalInstance().isNeedSecurityCheck(); //false;
//		for(int i = stackElem.length-1; i > 4; i--){ //beyond 5 steps, they are the actual call stack.
//			if(MetaworksRemoteService.class.getName().equals(stackElem[i].getClassName())
//					&& "callMetaworksService".equals(stackElem[i].getMethodName())
//					){
//				
//				needToCheck = true;
//			
//			}else if(stackElem[i].getClassName().startsWith("org.metaworks") || stackElem[i].getClassName().startsWith("org.uengine")){
//				needToCheck = false;
//			}else if(JavaSourceClassLoader.class.getName().equals(stackElem[i].getClassName())
//					//&& !"getJavaFileForInput".equals(stackElem[i].getMethodName())
//					){
//				
//				needToCheck = true;
//			
//			}else if("FileInputJavaFileManager.java".equals(stackElem[i].getFileName())
//					//&& "getJavaFileForInput".equals(stackElem[i].getMethodName())
//					){
//				
//				needToCheck = false;
//			
//			}else if("WebappClassLoader.java".equals(stackElem[i].getFileName())
//					&& "findClass".equals(stackElem[i].getMethodName())
//					){
//				
//				needToCheck = false;
//			
//			}else if("ByteArrayJavaFileManager.java".equals(stackElem[i].getFileName())
//					&& "getJavaFileForOutput".equals(stackElem[i].getMethodName())
//					){
//				
//				needToCheck = false;
//			
//			}
//		}

		if(needToCheck && securedPackages.containsKey(pkg)){
			
			//we will prohibit only when user creates some object directly
			StackTraceElement[] stackElem = Thread.currentThread().getStackTrace();
			
			throw new SecurityException("'" + pkg + "' " + securedPackages.get(pkg));
		
		}
	}

	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		// TODO Auto-generated method stub
		super.init(servletConfig);

//		xstream.alias("resource", ResourceFile.class);
//		xstream.useAttributeFor(ResourceFile.class, "name");
//		xstream.useAttributeFor(ResourceFile.class, "alias");
//		xstream.aliasField("id", ResourceFile.class, "alias");
//		xstream.addImplicitCollection(ResourceFile.class, "childs");
//		xstream.processAnnotations(ResourceFile.class);
		//Security setting 1



		System.setErr(new PrintStream(new ByteArrayOutputStream()){
			public void println(String str){
				originalSystemErr.println(str);
//				addDebugInfo(new StringBuilder().append(str).append("\n").toString());
			}
			public void print(String str){
				originalSystemErr.println(str);
//				addDebugInfo(str);
			}
			public void println(Object x) {
				originalSystemOut.println(x);
//				addDebugInfo(x.toString());
			}
		});

    	try {
/////////////// TODO: disabled for easy testing. someday we need to find dettach with the server

    		
 //   		HotSwapper hs = new HotSwapper(8000);
    		
//			ClassPool pool = ClassPool.getDefault();
//			
//  		String[] securedClassMehtods = GlobalContext.getPropertyStringArray("secured.classmethods","java.io.File.createNewFile,java.io.File.delete,java.io.File.list,java.io.File.listFiles,java.io.File.renameTo,java.io.File.mkdir,java.io.File.mkdirs,java.lang.System.setOut,java.lang.System.load,java.lang.System.loadLibrary,java.lang.System.setOut,java.lang.System.setProperties,java.lang.System.setErr,java.lang.System.setSecurityManager,java.lang.System.getSecurityManager");
//			
//			for(String securedClassMethod : securedClassMehtods){
//				
//				securedClassMethod = securedClassMethod.trim();
//				
//				int whereLastDot = securedClassMethod.lastIndexOf('.');
//				final String className = securedClassMethod.substring(0, whereLastDot);
//				final String methodName = securedClassMethod.substring(whereLastDot+1);
//				
//				CtClass cc = pool.get(className);
//				CtMethod cm = cc.getDeclaredMethod(methodName);
//				
//				cm.instrument(
//						new ExprEditor() {
//							boolean checked = false;
//							
//							public void edit(MethodCall m)
//							throws CannotCompileException
//							{
//								
//								if(!checked){
//									m.replace("{ if(codi.platform.SecurityContext.getThreadLocalInstance().isNeedSecurityCheck())" +
//											" throw new SecurityException(\"platform denies your request to access '" + 
//											className + "." + methodName + "()" +
//											"' directly. Use org.uengine.codi.platform.* instead.\"); $_ = $proceed($$); }");
//									checked = true;
//								}
//							}
//						});
//				
//				Class.forName(className);
	
//				hs.reload(className, cc.toBytecode());
	//		}

		}catch(Exception e){
			throw new RuntimeException(e);
		}
    	
    	//CodiClassLoader.refreshClassLoader(null);
	}


	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		HttpSession session = request.getSession();

        if(session!=null){
            String projectId = (String) session.getAttribute("projectId");

            CodiClassLoader clForSession = CodiClassLoader.createClassLoader(projectId);

            Thread.currentThread().setContextClassLoader(clForSession);
        }else{
            System.out.println("HttpSession is null");
        }

		if("1".equals(StartCodi.USE_CAS)){
			String logoutRequest = request.getParameter("logoutRequest");
			if(logoutRequest != null){
				System.out.println(logoutRequest);
				Session codiSession = (Session)StartCodi.MANAGED_SESSIONS.get(logoutRequest);
				if (codiSession != null) {
					
//					Employee emp = new Employee();
//					emp.setEmpCode(loggeduserId);
					try {
//						codiSession.setEmployee(emp);
						MetaworksRemoteService.pushTargetClientObjects(Login.getSessionIdWithUserId(codiSession.getUser().getUserId()), new Object[]{new Refresh( new StartCodi(codiSession, "logout") )});
					}catch(Throwable e){
						throw new RuntimeException(e);
					}finally{
						StartCodi.MANAGED_SESSIONS.remove(logoutRequest);
					}
				}
			}
		}
		
		// TODO Auto-generated method stub
		super.doPost(request, response);
	}
}
