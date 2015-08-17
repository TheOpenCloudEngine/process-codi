package org.uengine.codi.mw3.cache;

/**
 * Created by soo on 2015. 8. 4..
 */
public class MemcacheCache implements Cache {

    public static int Expiration_time = 259200;

    @Override
    public void set(String key, Object object) {
        Memcached.getMemcachedClient().set(key, Expiration_time, object);
    }

    @Override
    public Object get(String key) {
        return Memcached.getMemcachedClient().get(key);
    }
}
