package org.uengine.components.serializers;

import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.Serializer;

import java.io.*;
import org.apache.axis.encoding.*;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
import javax.xml.namespace.*;
import java.util.Hashtable;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Jinyoung Jang
 */

public class CastorSerializer implements org.uengine.kernel.Serializer{

	public boolean isSerializable(Class srcCls){
		try{
			return (srcCls.getMethod("getTypeDesc", new Class[]{}) != null);
		}catch(Exception e){
			return false;
		}
	}

	public void serialize(Object sourceObj, OutputStream os, Hashtable extendedContext) throws Exception{

		try{
			final ClassLoader urlClassLoader = GlobalContext.getComponentClassLoader();
			/* this will replace the default system class loader with the new custom classloader, so that XMLEncoder will use the new custom classloader to lookup a class */
			Thread.currentThread().setContextClassLoader(urlClassLoader);
		} catch (Exception e) {
System.out.println("can't replace classloader");
		}
		
		try{		
			TypeDesc desc = (TypeDesc)sourceObj.getClass().getMethod("getTypeDesc", new Class[]{}).invoke(sourceObj, new Object[]{});
			QName xmlType = desc.getXmlType();
			//
			//xmlType = (QName)extendedContext.get("qName");
			//
			xmlType = new QName("http://" + sourceObj.getClass().getName(), org.uengine.util.UEngineUtil.getClassNameOnly(sourceObj.getClass()) );
//			xmlType = new QName(Constants.URI_SOAP11_ENV, Constants.ELEM_ENVELOPE );

			org.apache.axis.encoding.Serializer ser = 
				(org.apache.axis.encoding.Serializer)
				sourceObj.getClass().getMethod("getSerializer", new Class[]{String.class, Class.class, QName.class})
				.invoke(sourceObj, new Object[]{"", sourceObj.getClass(), xmlType});

			//Writer w = new OutputStreamWriter(os);
			StringWriter w = new StringWriter();
			SerializationContext context = new SerializationContextImpl(w){
				public MessageContext getMessageContext(){
					return new MessageContext(null){
						public String getEncodingStyle(){
							return "";
						}
					};
				}
			};
	 		ser.serialize(xmlType, (org.xml.sax.Attributes)null, sourceObj, context);
	 		
	 		//System.out.println(w);
	 		//w.write("SdfasdfsadfA");
	 		
	 		BufferedOutputStream bao = new BufferedOutputStream(os);
	 		
	 		bao.write(w.toString().getBytes());
	 		
	 		bao.flush();
	 		bao.close();
	 		
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public Object deserialize(InputStream is, Hashtable extendedContext) throws Exception{
	
		//getting deserializer
		Class targetCls = (Class)extendedContext.get("targetClass");
		Object objectOfTargetCls = targetCls.newInstance();	
		TypeDesc desc = (TypeDesc)objectOfTargetCls.getClass().getMethod("getTypeDesc", new Class[]{}).invoke(objectOfTargetCls, new Object[]{});
		final QName xmlType;// = desc.getXmlType();
		xmlType = new QName("http://" + objectOfTargetCls.getClass().getName(), org.uengine.util.UEngineUtil.getClassNameOnly(objectOfTargetCls.getClass()) );
	        Deserializer dser = 
			(Deserializer)objectOfTargetCls.getClass().getMethod("getDeserializer", new Class[]{String.class, Class.class, QName.class})
				.invoke(objectOfTargetCls, new Object[]{"", objectOfTargetCls.getClass(), xmlType});
		//end
		
/*		DeserializationContext context = new DeserializationContextImpl(
			new org.xml.sax.InputSource(is),		
			new MessageContext(null){
				public String getEncodingStyle(){
					return xmlType.getNamespaceURI();
				}
			},
			//Message.RESPONSE
			"PurchaseOrder"
		);	

		context.parse();
		
	        boolean oldVal = context.isDoneParsing();	        
	        ((DeserializationContextImpl)context).deserializing(true);
	        context.pushElementHandler(new EnvelopeHandler((SOAPHandler)dser));
	
	        context.getRecorder().replay(0, -1, (org.xml.sax.ContentHandler)context);
	
	        ((DeserializationContextImpl)context).deserializing(oldVal);

	        return dser.getValue();
*/

	        SAXParserFactory factory = SAXParserFactory.newInstance();
	        factory.setValidating(false);
	        try {
	            // Set up output stream
	            //out = new OutputStreamWriter (System.out, "UTF8");
	

System.out.println("deser = " + dser);
	            // Parse the input
	            SAXParser saxParser = factory.newSAXParser();
	            saxParser.parse( is, (DefaultHandler)dser );
	
	        } catch (SAXParseException spe) {
	           // Error generated by the parser
	           System.out.println ("\n** Parsing error" 
	              + ", line " + spe.getLineNumber ()
	              + ", uri " + spe.getSystemId ());
	           System.out.println("   " + spe.getMessage() );
	
	           // Use the contained exception, if any
	           Exception  x = spe;
	           if (spe.getException() != null)
	               x = spe.getException();
	           x.printStackTrace();
	
	        } catch (SAXException sxe) {
	           // Error generated by this application
	           // (or a parser-initialization error)
	           Exception  x = sxe;
	           if (sxe.getException() != null)
	               x = sxe.getException();
	           x.printStackTrace();
	
	        } catch (ParserConfigurationException pce) {
	            // Parser with specified options can't be built
	            pce.printStackTrace();
	
	        } catch (IOException ioe) {
	           // I/O error
	           ioe.printStackTrace();
	        }
	        
	        return dser.getValue();
	}
	

}