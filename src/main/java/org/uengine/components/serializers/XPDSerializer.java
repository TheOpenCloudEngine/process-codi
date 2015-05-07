package org.uengine.components.serializers;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;

import org.metaworks.dwr.MetaworksRemoteService;
import org.uengine.codi.mw3.model.ResourceFile;
import org.uengine.kernel.NeedArrangementToSerialize;

public class XPDSerializer extends XStreamSerializer{
	
	public XPDSerializer(){
		xstream.processAnnotations(ResourceFile.class);
	}
	

	@Override
	public Object deserialize(InputStream is, Hashtable extendedContext) throws Exception{
		Object obj = null;

		try{
			obj = xstream.fromXML(new InputStreamReader(is, "UTF-8"));
		}catch(Exception e){
			throw e;
		}finally{
			try{is.close();}catch(Exception exx){};
		}
		
		//to inject this only
		MetaworksRemoteService.getInstance().autowire(obj);
		//
		
		if(obj instanceof NeedArrangementToSerialize)
			((NeedArrangementToSerialize)obj).afterDeserialization();
		
		return obj;
	}
	
}
