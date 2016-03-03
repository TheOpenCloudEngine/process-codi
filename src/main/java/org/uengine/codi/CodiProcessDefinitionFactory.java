package org.uengine.codi;

import com.thoughtworks.xstream.converters.ConversionException;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.metaworks.dao.TransactionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.uengine.codi.mw3.CodiClassLoader;
import org.uengine.kernel.NoSuchProcessDefinitionException;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.ProcessDefinitionFactory;
import org.uengine.modeling.resource.DefaultResource;
import org.uengine.processmanager.ProcessTransactionContext;
import org.uengine.processmanager.SimulatorTransactionContext;
import org.uengine.util.UEngineUtil;

import java.io.*;
import java.util.Calendar;
import java.util.Map;

@Component
public class CodiProcessDefinitionFactory extends ProcessDefinitionFactory{
	
	public final static String unstructuredProcessDefinitionLocation = "Unstructured.process";
	public final static String codiProcessDefinitionFolder = "codi";

	public CodiProcessDefinitionFactory() {
		super(new SimulatorTransactionContext());  //since CodiProcessDefinitionFactory don't use any db connection, so it's ok.
	}

	public CodiProcessDefinitionFactory(ProcessTransactionContext tc) {
		super(tc);
	}


	@Autowired
	org.uengine.modeling.resource.ResourceManager resourceManager;
		public org.uengine.modeling.resource.ResourceManager getResourceManager() {
			return resourceManager;
		}
		public void setResourceManager(org.uengine.modeling.resource.ResourceManager resourceManager) {
			this.resourceManager = resourceManager;
		}


	@Override
	protected Object getDefinitionSourceImpl(String location,
			boolean fromCompilationVersion, boolean shouldBeObjectResult)
			throws Exception {
		// TODO Auto-generated method stub
		
		
		if(unstructuredProcessDefinitionLocation.equals(location)){
			ProcessDefinition obj = new ProcessDefinition();
			
			obj.setModifiedDate(Calendar.getInstance());

			return obj;
		}


		try {
			DefaultResource processResource = new DefaultResource();

			//processResource.setPath("codi/" + location);
			processResource.setPath(location);

			Object object = resourceManager.getObject(processResource);

			if(object==null)
				throw new Exception("No definition found where location = '" + location + "'");
			
			return object;

		} catch(Exception e) {
			throw new NoSuchProcessDefinitionException("No such definition or Some I/O Exception: " + location, e);
		}
	}



	@Override
	public void removeDefinition(String pdvid) throws Exception {
		String sourceCodeBase = CodiClassLoader.getMyClassLoader().getCodebase();
		
		String defFileName = sourceCodeBase + "/" + pdvid;
		
		new File(defFileName).delete();
	}

	
	
	
	public String[] addDefinitionImpl(String belongingPdid, String defId,
			int version, String name, String description, boolean isAdhoc,
			Object definition, String folder, boolean overwrite, Map options)
			throws Exception {


		throw new Exception("Dont' use this method.");

//
//		//ignores the version, belongingPdid, overwrite
//		// how to handle the adhoc?
//		// just delegates the version control functionalities to SVN kit
//		// in case of processdefinitions, it should be very hard to merge together
//
//		boolean isOtherResourceType = options != null
//				&& options.containsKey("objectType");
//		String objectType = "process";
//
//		if (isOtherResourceType)
//			objectType = (String) options.get("objectType");
//
//		String alias = (String)options.get("alias");
//
//		if(alias.indexOf('.') == -1)
//			alias = (UEngineUtil.isNotEmpty(folder) ? folder + "/" : "") + (String) options.get("alias")  + "." + objectType;
//
//
//		String sourceCodeBase = CodiClassLoader.getMyClassLoader().getCodebase();
//
//		String defFileName;
//
//
////		String alias = (UEngineUtil.isNotEmpty(folder) ? folder + "/" : "") + name;
//
////		if(UEngineUtil.isNotEmpty(pdvid))
////			defFileName = sourceCodeBase + pdvid;
////		else{
//
//			defFileName = sourceCodeBase + alias;
////		}
//
//		new File(defFileName).getParentFile().mkdirs();
//
//
//		FileOutputStream fos = null;
//		try {
//			File classDefFile = new File(defFileName);
//
//			fos = new FileOutputStream(classDefFile);
//
//
//			String definitionInString = (String)definition;
//
//			ByteArrayInputStream bai = new ByteArrayInputStream(definitionInString.getBytes(GlobalContext.ENCODING));
//			UEngineUtil.copyStream(bai, fos);
//
//			//GlobalContext.serialize(this, fos, Object.class);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			throw e;//e.printStackTrace();
//		} finally{
//
//			if(fos!=null)
//				fos.close();
//		}
//
//		removeFromCache(alias);
//
//		return new String[]{alias, defFileName};
		
	}

	public void deployDefinition(String path, Object definition){

		DefaultResource defaultResource = new DefaultResource();
		defaultResource.setPath(codiProcessDefinitionFolder +"/"+ path);

		try {
			getResourceManager().getStorage().save(defaultResource, definition);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	

}

