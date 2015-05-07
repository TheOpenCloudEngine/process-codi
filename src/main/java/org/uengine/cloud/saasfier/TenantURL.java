package org.uengine.cloud.saasfier;

import org.metaworks.dao.TransactionContext;
import org.uengine.codi.util.CodiStringUtil;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;

public class TenantURL {
	
	protected TenantURL(){
		
	}

	
	public static String getURL() throws MalformedURLException {
		return getURL(null);
	}
	
	public static String getURL(String tenantId) throws MalformedURLException {

		HttpServletRequest request = TransactionContext.getThreadLocalInstance().getRequest();
		String url = request.getRequestURL().toString();
		String codebase = url.substring( 0, url.lastIndexOf( "/" ) );
		URL urlURL = new URL(codebase);
		String host = urlURL.getHost();
		int port = urlURL.getPort();
		
		String path = urlURL.getPath();
		String contextOnly = path.substring(0, path.substring(1).indexOf("/")+1);
		String protocol = urlURL.getProtocol();

		StringBuffer serverPath = new StringBuffer();
		
		serverPath.append(protocol+"://");
		
		
		if(host.length()>0)
			serverPath.append(host);
		
		if (port>-1)
			serverPath.append(":"+urlURL.getPort());
		
		if(contextOnly.length()>0 && !contextOnly.equals("/dwr")) 
			serverPath.append(contextOnly);
		
		System.out.println(CodiStringUtil.lastLastFileSeparatorChar(serverPath.toString()));
				
		return CodiStringUtil.lastLastFileSeparatorChar(serverPath.toString());
	}
}
