package org.uengine.codi.mw3.resource;

import org.metaworks.common.MetaworksUtil;
import org.oce.garuda.multitenancy.TenantContext;
import org.uengine.kernel.GlobalContext;
import org.uengine.util.UEngineUtil;

import java.io.*;

public class ResourceManager {
	
	// don't expose this method as possible
	protected static InputStream getResourceAsStream(String appKey, String tenantId, String alias) throws FileNotFoundException{
		String absPath = getResourcePath(appKey, tenantId, alias);
		
		if(!new File(absPath).exists()){   //if there're no existing file of metadata defined, return default one instead. 
			absPath = getResourcePath(appKey, null, alias);
		}
		
		return new FileInputStream(absPath);
	}

	// don't expose this method as possible
	protected static OutputStream getResourceAsOutputStream(String appKey, String tenantId, String alias) throws FileNotFoundException{
		String absPath = getResourcePath(appKey, tenantId, alias);
		
		if(!new File(absPath).exists()){   //if there're no existing file of metadata defined, return default one instead. 
			File file = new File(absPath);
			try {
				file.getParentFile().mkdirs();
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return new FileOutputStream(absPath);
	}

	public static String getResourcePath(String appKey, String tenantId,
			String alias) {
		String codebase = GlobalContext.getPropertyString("codebase", "codebase");
		String basePath = null;

		if(tenantId!=null){
			basePath = basePath  + 	File.separatorChar + tenantId;
		}else{
			basePath = basePath  + 	File.separatorChar + "default";
		}

		basePath = codebase + File.separatorChar + appKey;

		String absPath = basePath;
		
		if(alias!=null)
			absPath = absPath + File.separatorChar + alias;
		
		return absPath;
	}

	public static InputStream getAppResourceAsStream(String appKey, String alias) throws FileNotFoundException{
		return getResourceAsStream(appKey, null, alias);
	}
	
	public static InputStream getTenantResourceAsStream(String appKey, String alias) throws Exception{

		InputStream is;
		try{
			//File file = new File(getAlias());
			is = ResourceManager.getResourceAsStream(appKey, TenantContext.getThreadLocalInstance().getTenantId(), alias);
		}catch(Exception e){
			
			UEngineUtil.copyStream(
				getAppResourceAsStream(appKey, alias), 
				getTenantResourceAsOutputStream(appKey, alias)
			);
			
			is = ResourceManager.getResourceAsStream(appKey, TenantContext.getThreadLocalInstance().getTenantId(), alias);
		}
		
		return is;

	}
	
	public static OutputStream getAppResourceAsOutputStream(String appKey, String alias) throws FileNotFoundException{
		return getResourceAsOutputStream(appKey, null, alias);
	}

	public static OutputStream getTenantResourceAsOutputStream(String appKey, String alias) throws FileNotFoundException{
		return getResourceAsOutputStream(appKey, TenantContext.getThreadLocalInstance().getTenantId(), alias);
	}
	
	
	protected static Object getResource(String appKey, String tenantId, String alias) throws Exception{
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		
		InputStream is;
		
		if(tenantId == null)
			is = getResourceAsStream(appKey, null, alias);
		else
			is = getTenantResourceAsStream(appKey, alias);
		
		UEngineUtil.copyStream(is, bao);
		Object resource = GlobalContext.deserialize(bao.toString(GlobalContext.ENCODING));
		
		return resource;
	}
	
	public static Object getTenantResource(String appKey, String alias) throws Exception{
		return getResource(appKey, TenantContext.getThreadLocalInstance().getTenantId(), alias);
	}

	public static Object getAppResource(String appKey, String alias) throws Exception{
		return getResource(appKey, null, alias);
	}

	public static void setAppResource(String appKey, String alias, Object resource) throws Exception{
		setResource(appKey, null, alias, resource);
	}


	protected static void setResource(String appKey, String tenantId, String alias, Object resource) throws Exception{
		GlobalContext.serialize(resource, getResourceAsOutputStream(appKey, tenantId, alias), String.class);
	}
	
	public static void setTenantResource(String appKey, String alias, Object resource) throws Exception{
		setResource(appKey, TenantContext.getThreadLocalInstance().getTenantId(), alias, resource);
	}
	

}
