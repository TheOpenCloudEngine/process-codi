package org.uengine.codi.mw3.cache;

import com.thoughtworks.xstream.XStream;
import org.metaworks.common.MetaworksUtil;
import org.uengine.kernel.GlobalContext;
import org.uengine.util.UEngineUtil;

import java.io.*;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * Created by ho.lim on 4/2/2015.
 */
public class CacheHashTable<K,V> extends Hashtable {

    public static final String CACHE_PATH = GlobalContext.getPropertyString("cache");

    private String cacheName = null;

    private Class cacheClazz = null;

    public CacheHashTable(String cacheName, Class cacheClazz){
        super();
        this.cacheName = cacheName;
        this.cacheClazz = cacheClazz;
    }

    public String getCacheName(){
        return cacheName;
    }

    public Class getCacheClazz(){
        return cacheClazz;
    }

    /*
       서버를 종료시 세션정보를 캐시 파일로 저장한다.
    */
    public void storeCacheTable() throws Exception {
        XStream stream = new XStream();
        stream.alias(cacheName, this.getClass());
        String xml = stream.toXML(this);

        OutputStream fos = null;
        ByteArrayInputStream bai = null;

        try {
            fos = new FileOutputStream(new File(getCacheFilePath()));
            bai = new ByteArrayInputStream(xml.getBytes(GlobalContext.ENCODING));
            UEngineUtil.copyStream(bai, fos);
        } catch (FileNotFoundException e) {
            throw new Exception("Login Session Serializable File Not Found.");
        } catch (UnsupportedEncodingException e) {
            throw new Exception("Unsupported Encoding Exception.");
        } catch (Exception e) {
            throw new Exception("UEngineUtil Copy Stream Failed.");
        }
    }

    /*
       서버를 시작시 세션정보를 캐시 파일로부터 위의 static변수에 넣는다.
    */
    public void recoverCacheTable() throws Exception {
        InputStream is = null;
        ByteArrayOutputStream bao = null;
        File cacheFile = null;

        try {
            cacheFile = new File(getCacheFilePath());
            if(!cacheFile.exists())
                return;

            is = new FileInputStream(cacheFile);
            bao = new ByteArrayOutputStream();
            MetaworksUtil.copyStream(is, bao);
        } catch (FileNotFoundException e) {
            throw new Exception("Login Session Deserializable File Not Found.");
        } catch (Exception e) {
            throw new Exception("MetaworksUtil Copy Stream Failed.");
        }

        String xml = bao.toString(GlobalContext.ENCODING);
        XStream stream = new XStream();
        stream.alias("root", this.getClass());

        HashMap<String, Hashtable> sessionMap = (HashMap<String, Hashtable>) stream.fromXML(xml);

//        SessionIdForCompanyMapping = sessionMap.get("SessionIdForCompanyMapping");
//        SessionIdForDeptMapping = sessionMap.get("SessionIdForDeptMapping");
//        SessionIdForEmployeeMapping = sessionMap.get("SessionIdForEmployeeMapping");
//        userIdDeviceMapping = sessionMap.get("userIdDeviceMapping");
    }

    private String getCacheFilePath(){
        return CACHE_PATH + File.separator + cacheClazz.getName() + "." + cacheName;
    }
}
