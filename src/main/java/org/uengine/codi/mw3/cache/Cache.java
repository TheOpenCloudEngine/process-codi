package org.uengine.codi.mw3.cache;

/**
 * Created by soo on 2015. 8. 4..
 */
public interface Cache {

    public void set(String key, Object object);
    public Object get(String key);
}
