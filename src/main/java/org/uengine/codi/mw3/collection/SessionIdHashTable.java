package org.uengine.codi.mw3.collection;

import org.uengine.codi.mw3.Login;

import java.util.Hashtable;

/**
 * Created by ho.lim on 4/2/2015.
 */
public class SessionIdHashTable<K,V> extends Hashtable{

    @Override
    public synchronized Object put(Object key, Object value) {
        Object obj = super.put(key, value);

        try {
//            Login.storeLoginStaticSessionInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
    }
}
