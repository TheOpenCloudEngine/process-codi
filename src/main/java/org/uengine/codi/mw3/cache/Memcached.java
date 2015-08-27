package org.uengine.codi.mw3.cache;

import net.spy.memcached.MemcachedClient;
import net.spy.memcached.spring.MemcachedClientFactoryBean;
import org.uengine.bean.factory.MetaworksSpringBeanFactory;
import org.uengine.kernel.GlobalContext;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by soo on 2015. 8. 3..
 */
public class Memcached {

    public static MemcachedClient MemcachedClient;
    public static int Expiration_time = 259200;

    public static MemcachedClient getMemcachedClient(){
        if(MemcachedClient != null) {
            return MemcachedClient;
        }else{
            try {
                MemcachedClient = new MemcachedClient(new InetSocketAddress(GlobalContext.getPropertyString("memcache_ip"), 11211));

//                MetaworksSpringBeanFactory.getBean(MemcachedClientFactoryBean.class);
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (InstantiationException e) {
//                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return MemcachedClient;
        }
    }
}
