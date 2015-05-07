package org.uengine.codi.mw3.portlet;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.ArrayList; 
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.metaworks.dao.ConnectionFactory;
import org.metaworks.dao.JDBCConnectionFactory;
import org.metaworks.dao.TransactionContext;
import org.metaworks.dwr.InvocationContext;
import org.metaworks.dwr.MetaworksRemoteService;
import org.metaworks.spring.SpringConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.uengine.codi.mw3.Login;
import org.uengine.codi.mw3.model.IInstance;
import org.uengine.codi.mw3.model.Instance;
import org.uengine.codi.mw3.model.Navigation;
import org.uengine.codi.mw3.model.Session;
import org.uengine.kernel.GlobalContext;

import com.thoughtworks.xstream.XStream;

/**
 * 외부에서 호출 하는 메서드 모음
 * @author kimHyungKook
 *
 */
public class WsCall extends HttpServlet {
	public static ConnectionFactory connectionFactory;
	protected XStream xstream = new XStream();
	
	@Autowired
	public SpringConnectionFactory springConnectionFactory;
	
	public WsCall(){
		super();
	}
	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		// TODO Auto-generated method stub
		super.init(servletConfig);

//		String connectionString = getServletConfig().getInitParameter("connectionString");
		
//		if(connectionString!=null){
//			String driverClass = getServletConfig().getInitParameter("driverClass");
//			String userId = getServletConfig().getInitParameter("userId");
//			String password = getServletConfig().getInitParameter("password");
//			
//			JDBCConnectionFactory cf = new JDBCConnectionFactory();
//			cf.setConnectionString(connectionString);
//			cf.setDriverClass(driverClass);
//			cf.setUserId(userId);
//			cf.setPassword(password);
//	
//			connectionFactory = cf;
//		}else{
//			connectionFactory = springConnectionFactory; 
//		}
		
//		String connectionString = GlobalContext.getPropertyString("jdbc.url", null);
//		if(connectionString!=null){
//			String driverClass = GlobalContext.getPropertyString("jdbc.driverClassName", null);
//			String userId = GlobalContext.getPropertyString("jdbc.username", "root");
//			String password = GlobalContext.getPropertyString("jdbc.password", "");
//			
//			JDBCConnectionFactory cf = new JDBCConnectionFactory();
//			cf.setConnectionString(connectionString);
//			cf.setDriverClass(driverClass);
//			cf.setUserId(userId);
//			cf.setPassword(password);
//	
//			connectionFactory = cf;
//		}

	}
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		
		TransactionContext tx = new TransactionContext(); //once a TransactionContext is created, it would be cached by ThreadLocal.set, so, we need to remove this after the request processing. 
		tx.setManagedTransaction(false);
		tx.setAutoCloseConnection(true);
		
		tx.setRequest(request);
		tx.setResponse(response);
		
		String connectionString = GlobalContext.getPropertyString("jdbc.url", null);
		if(connectionString!=null){
			String driverClass = GlobalContext.getPropertyString("jdbc.driverClassName", null);
			String userId = GlobalContext.getPropertyString("jdbc.username", "root");
			String password = GlobalContext.getPropertyString("jdbc.password", "");
			
			JDBCConnectionFactory cf = new JDBCConnectionFactory();
			cf.setConnectionString(connectionString);
			cf.setDriverClass(driverClass);
			cf.setUserId(userId);
			cf.setPassword(password);
			connectionFactory = cf;
		}
		if(connectionFactory!=null){
			tx.setConnectionFactory(connectionFactory);
		}
		Session session = null;
    	try {
    		session = loginCheck(request);
    		if( session != null ){
    	    	if(pathInfo.startsWith("/worklist")){
    	    		session.setLastPerspecteType("inbox");
    	    		Navigation navigation = new Navigation(session);
    	    		IInstance instanceContents = Instance.load(navigation,	0, 0);
    	    		ArrayList<InstanceObject> instanceList = new ArrayList<InstanceObject>();
    	    		while(instanceContents.next()){
    	    			InstanceObject instanceObject = new InstanceObject();
    	    			instanceObject.setInstanceId(instanceContents.getInstId().toString());
    	    			instanceObject.setInstanceName(instanceContents.getName());
    	    			instanceObject.setStartDate(instanceContents.getStartedDate().toString());
    	    			instanceObject.setDueDate(instanceContents.getDueDate().toString());
    	    			instanceList.add(instanceObject);
    	    		}
    	    		xstream.alias("instance", InstanceObject.class);
    	    		Writer writer = new OutputStreamWriter(response.getOutputStream(), "UTF-8");
    	    		xstream.toXML(instanceList, writer);
    	    		response.flushBuffer();
    	    	}
    		}
    		tx.commit();
		}catch(Throwable e){
			try {
				tx.rollback();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
			try {
				tx.releaseResources();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		String className = request.getParameter("returnType");
    	String objectId = request.getParameter("methodName");
    	String methodName = request.getParameter("methodName");
    	
    	System.out.println(className);
    	Session session = null;
    	try {
    		session = loginCheck(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	if( session != null ){
    		
    	}
	}
	
	protected Session loginCheck(HttpServletRequest request) throws Exception{
		String userId = request.getParameter("userId");
		String password = request.getParameter("password");
		
		Login login = new Login();
		login.setEmail(userId);
		login.setPassword(password);
		Session session = login.loginService();
		return session;	}
	
}
