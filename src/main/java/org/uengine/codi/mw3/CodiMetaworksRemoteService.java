package org.uengine.codi.mw3;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import org.metaworks.MetaworksException;
import org.metaworks.ServiceMethodContext;
import org.metaworks.WebObjectType;
import org.metaworks.dao.ConnectionFactory;
import org.metaworks.dao.TransactionContext;
import org.metaworks.dwr.MetaworksRemoteService;
import org.metaworks.dwr.TransactionalDwrServlet;
import org.oce.garuda.multitenancy.TenantContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
//import org.uengine.codi.platform.SecurityContext;
import org.uengine.codi.mw3.model.Session;
import org.uengine.processmanager.ProcessManagerBean;
import org.uengine.processmanager.ProcessManagerRemote;

public class CodiMetaworksRemoteService extends MetaworksRemoteService{
	
	public static ClassLoader codiClassLoader;
	static Class metaworksServiceLocatorClass;
	
	
	
	
	public CodiMetaworksRemoteService() throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException{
//		ClassLoader cl = new JavaSourceClassLoader(
//				this.getClass().getClassLoader(), 
//				new ResourceFinder(){
//
//					@Override
//					public Resource findResource(final String resourceName) {
//						
//						try {
//							if("MetaworksRemoteServiceLocator.java".equals(resourceName)){
//								Resource resource = new Resource(){
//
//									@Override
//									public String getFileName() {
//										return resourceName;
//									}
//
//									@Override
//									public long lastModified() {
//										return 0;
//									}
//
//									@Override
//									public InputStream open() throws IOException {
//										return new StringInputStream("public class MetaworksRemoteServiceLocator{ public org.metaworks.dwr.MetaworksRemoteService getInstance(){return new org.metaworks.dwr.MetaworksRemoteService();}}");
//
//									}
//									
//								};
//								
//								return resource;
//							}
//							
//							String defVerId = processManager.getProcessDefinitionProductionVersionByAlias(resourceName);
//							String classDefinition = processManager.getResource(defVerId);
//							final ClassDefinition classDef = (ClassDefinition) GlobalContext.deserialize(classDefinition, ClassDefinition.class);
//							
//							Resource resource = new Resource(){
//
//								@Override
//								public String getFileName() {
//									return resourceName;
//								}
//
//								@Override
//								public long lastModified() {
//									return 0;
//								}
//
//								@Override
//								public InputStream open() throws IOException {
//									return new StringInputStream(classDef.getSourceCode().getCode());
//								}
//								
//							};
//							
//							return resource;
//							
//						} catch (RemoteException e) {
//							// TODO Auto-generated catch block
//							//e.printStackTrace();
//							return null;
//						}
//						// TODO Auto-generated method stub
//						catch (Exception e) {
//							// TODO Auto-generated catch block
//							//e.printStackTrace();
//							
//							return null;
//						}
//					}
//					
//				},
//				"UTF-8", 
//				DebuggingInformation.NONE
//			){
//
//				@Override
//				protected synchronized Class<?> loadClass(String name,
//						boolean resolve) throws ClassNotFoundException {
//					
//					try{
//						Class<?> clazz = CodiMetaworksRemoteService.class.getClassLoader().loadClass(name);
//						if(resolve)
//							resolveClass(clazz);
//						
//						return clazz;
//					}catch(Exception e){
//						
//					}
//					
//					return super.loadClass(name, resolve);
//				}
//			
//			
//			
//		};
//		
		
//		refreshClassLoader(null);
		
//		metaworksServiceLocatorClass = cl.loadClass(CodiClassLoader.MetaworksServiceClassLoader);
//		Object mrsLocator = metaworksServiceLocatorClass.newInstance();
//		MetaworksRemoteService mrs = (MetaworksRemoteService) mrsLocator.getClass().getMethod("getInstance", new Class[]{}).invoke(mrsLocator, null);
		
		setInstance(new MetaworksRemoteService()); //it is not setInstance(this). since this will delegate all the actions and properties to general MetaworksRemoteService. why?

	}
	
	

	@Override
	//@Transactional(propagation=Propagation.REQUIRED)
	public Object callMetaworksService(String objectTypeName, Object clientObject,
			String methodName, Map<String, Object> autowiredFields)
			throws Throwable {
		
		Class serviceClass = Thread.currentThread().getContextClassLoader().loadClass(objectTypeName);

		awareTenant(autowiredFields);


		Object returnVal = instance.callMetaworksService(objectTypeName, clientObject, methodName, autowiredFields);

		return returnVal;

		
	}


	static String[] autowirableSessionKeys={"session", ServiceMethodContext.WIRE_PARAM_CLS + Session.class.getName()};

	private void awareTenant(Map<String, Object> autowiredFields) {

		for (String autowirableSessionKey : autowirableSessionKeys) {
			if (autowiredFields.get(autowirableSessionKey) instanceof Session) {
				Session session = (Session) autowiredFields.get(autowirableSessionKey);

				if (session.getCompany() != null) {
					new TenantContext(session.getCompany().getComCode());

					break;
				}
			}

		}
	}

	@Override
	public WebObjectType getMetaworksType(String className) throws Exception {
		
//		int sepPos = className.indexOf(":");
//		if(sepPos > -1){
//			String[] classNameAndAppKey = className.split(":");
//			className = classNameAndAppKey[0];
//			String appKey = classNameAndAppKey[1];
//			
//			Thread.currentThread().setContextClassLoader(CodiClassLoader.createClassLoader(appKey, null));
//		}
		
		//Thread.currentThread().setContextClassLoader(codiClassLoader);
		// TODO Auto-generated method stub
		return instance.getMetaworksType(className);
	}
	
	
	
	
	@Override
	public ConnectionFactory getConnectionFactory() {
		//Thread.currentThread().setContextClassLoader(codiClassLoader);
		// TODO Auto-generated method stub
		return instance.getConnectionFactory();
	}

	@Override
	public void setConnectionFactory(ConnectionFactory connectionFactory) {
		//Thread.currentThread().setContextClassLoader(codiClassLoader);
		// TODO Auto-generated method stub
		instance.setConnectionFactory(connectionFactory);
	}




	@Override
	public ApplicationContext getBeanFactory() {
		// TODO Auto-generated method stub
//		if(codiClassLoader!=null)
//			Thread.currentThread().setContextClassLoader(codiClassLoader);
//		
		return instance.getBeanFactory();
	}

	@Override
	public void setLowerCaseSQL(boolean lowerCaseSQL) {
		instance.setLowerCaseSQL(lowerCaseSQL);
	}


	//	@Autowired
//	protected ProcessManagerRemote processManager;
//	

}
