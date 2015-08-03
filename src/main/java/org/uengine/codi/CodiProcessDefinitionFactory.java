package org.uengine.codi;

import com.thoughtworks.xstream.converters.ConversionException;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.uengine.codi.mw3.CodiClassLoader;
import org.uengine.codi.mw3.resource.ResourceManager;
import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.ProcessDefinitionFactory;
import org.uengine.processmanager.ProcessTransactionContext;
import org.uengine.util.UEngineUtil;

import java.io.*;
import java.util.Calendar;
import java.util.Map;

public class CodiProcessDefinitionFactory extends ProcessDefinitionFactory{
	
	public final static String unstructuredProcessDefinitionLocation = "Unstructured.process";

	public CodiProcessDefinitionFactory(ProcessTransactionContext tc) {
		super(tc);
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
		
		InputStream is = null;
		try {
			is = getResourceInputStream(location);

			if(is==null)
				throw new Exception("No definition found where location = '" + location + "'");
			
			if(shouldBeObjectResult){
				ProcessDefinition obj = (ProcessDefinition) GlobalContext.deserialize(is, Object.class);
				
				obj.setModifiedDate(Calendar.getInstance());
				obj.setAlias(location);
				
				return obj;
			}else{
				ByteArrayOutputStream bao = new ByteArrayOutputStream();
				
				UEngineUtil.copyStream(is, bao);
			
				return bao.toString("UTF-8");
			}
		} catch(ConversionException conversion){
			throw new Exception("Conversion Exception. Maybe difference in definition object structure: ", conversion);
		} catch(IOException e) {
			throw new Exception("No such definition or Some I/O Exception: " + location, e);
		} finally{
			if(is!=null)
				is.close();
		}
	}

	private InputStream getResourceInputStream(String location) {

		InputStream is;
		//is = CodiClassLoader.getMyClassLoader().getResourceAsStream(location);

		try {
			is = ResourceManager.getTenantResourceAsStream("codi", location);
		} catch (Exception e) {
			return null;
		}

		return is;
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
		

		
		//ignores the version, belongingPdid, overwrite
		// how to handle the adhoc?
		// just delegates the version control functionalities to SVN kit
		// in case of processdefinitions, it should be very hard to merge together 
		
		boolean isOtherResourceType = options != null
				&& options.containsKey("objectType");
		String objectType = "process";
		
		if (isOtherResourceType)
			objectType = (String) options.get("objectType");

		String alias = (String)options.get("alias");
		
		if(alias.indexOf('.') == -1)
			alias = (UEngineUtil.isNotEmpty(folder) ? folder + "/" : "") + (String) options.get("alias")  + "." + objectType;
	
		
		String sourceCodeBase = CodiClassLoader.getMyClassLoader().getCodebase();
		
		String defFileName;


//		String alias = (UEngineUtil.isNotEmpty(folder) ? folder + "/" : "") + name;
		
//		if(UEngineUtil.isNotEmpty(pdvid))
//			defFileName = sourceCodeBase + pdvid;
//		else{
			
			defFileName = sourceCodeBase + alias;
//		}

		new File(defFileName).getParentFile().mkdirs();
		
		
		FileOutputStream fos = null;
		try {
			File classDefFile = new File(defFileName);

			fos = new FileOutputStream(classDefFile);

			
			String definitionInString = (String)definition;
			
			ByteArrayInputStream bai = new ByteArrayInputStream(definitionInString.getBytes(GlobalContext.ENCODING));
			UEngineUtil.copyStream(bai, fos);
			
			//GlobalContext.serialize(this, fos, Object.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;//e.printStackTrace();
		} finally{
		
			if(fos!=null)
				fos.close();
		}

		removeFromCache(alias);
		
		return new String[]{alias, defFileName};
		
	}

	
	
	

}

