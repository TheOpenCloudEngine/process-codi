package org.uengine.codi.mw3.cache;

import org.metaworks.dao.TransactionContext;

import javax.servlet.http.HttpSession;

/**
 * Created by soo on 2015. 8. 4..
 */
public class HttpSessionCache implements Cache {
    @Override
    public void set(String key, Object object) {
        HttpSession httpSession = TransactionContext.getThreadLocalInstance().getRequest().getSession();
        httpSession.setAttribute(key, object);
    }

    @Override
    public Object get(String key) {
        HttpSession httpSession = TransactionContext.getThreadLocalInstance().getRequest().getSession();

        return httpSession.getAttribute(key);
    }
}
