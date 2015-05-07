package org.uengine.codi.mw3;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;

import org.metaworks.dwr.MetaworksRemoteService;
import org.uengine.components.serializers.XPDSerializer;
import org.uengine.kernel.NeedArrangementToSerialize;

public class CodiDefaultSerializer extends XPDSerializer{

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
		MetaworksRemoteService.getInstance().autowire(this);
		//
		
		if(obj instanceof NeedArrangementToSerialize)
			((NeedArrangementToSerialize)obj).afterDeserialization();
		
		return obj;
	}
	

}
