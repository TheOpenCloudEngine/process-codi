package org.uengine.codi.common.aoptx;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.metaworks.dao.TransactionContext;

@Aspect
// @Scope(value="request")
public class ProcessManagerDirtyCheckAdvice {

   @After("execution(* org.uengine.processmanager.ProcessManagerBean.*(..))")
    public void afterUsed() throws java.rmi.RemoteException {
    	TransactionContext.getThreadLocalInstance().setSharedContext("processManagerBeanUsed", new Boolean(true));
    	
    	System.out.println("processManagerBeanUsed--------------------");

    }
   
   @After("execution(* org.uengine.processmanager.ProcessManagerBean.set*(..))")
    public void afterSet() throws java.rmi.RemoteException {
    	TransactionContext.getThreadLocalInstance().setSharedContext("processManagerBeanChanged", new Boolean(true));
    	
    	System.out.println("processManagerBeanChanged--------------------");

    }

    @After("execution(* org.uengine.processmanager.ProcessManagerBean.add*(..))")
    public void afterAdd() throws java.rmi.RemoteException {
    	TransactionContext.getThreadLocalInstance().setSharedContext("processManagerBeanChanged", new Boolean(true));
    	
    	System.out.println("processManagerBeanChanged--------------------");

    }

    @After("execution(* org.uengine.processmanager.ProcessManagerBean.initialize*(..))")
    public void afterInitialize() throws java.rmi.RemoteException {
    	TransactionContext.getThreadLocalInstance().setSharedContext("processManagerBeanChanged", new Boolean(true));
    	
    	System.out.println("processManagerBeanChanged--------------------");

    }

    @After("execution(* org.uengine.processmanager.ProcessManagerBean.execute*(..))")
    public void afterExecute() throws java.rmi.RemoteException {
    	TransactionContext.getThreadLocalInstance().setSharedContext("processManagerBeanChanged", new Boolean(true));
    	
    	System.out.println("processManagerBeanChanged--------------------");

    }

    @After("execution(* org.uengine.processmanager.ProcessManagerBean.delegate*(..))")
    public void afterDelegate() throws java.rmi.RemoteException {
    	TransactionContext.getThreadLocalInstance().setSharedContext("processManagerBeanChanged", new Boolean(true));
    	
    	System.out.println("processManagerBeanChanged--------------------");

    }

    @After("execution(* org.uengine.processmanager.ProcessManagerBean.complete*(..))")
    public void afterComplete() throws java.rmi.RemoteException {
    	TransactionContext.getThreadLocalInstance().setSharedContext("processManagerBeanChanged", new Boolean(true));
    	
    	System.out.println("processManagerBeanChanged--------------------");

    }

    @After("execution(* org.uengine.processmanager.ProcessManagerBean.put*(..))")
    public void afterPut() throws java.rmi.RemoteException {
    	TransactionContext.getThreadLocalInstance().setSharedContext("processManagerBeanChanged", new Boolean(true));
    	
    	System.out.println("processManagerBeanChanged--------------------");

    }
    
    @After("execution(* org.uengine.processmanager.ProcessManagerBean.move*(..))")
    public void afterMove() throws java.rmi.RemoteException {
    	TransactionContext.getThreadLocalInstance().setSharedContext("processManagerBeanChanged", new Boolean(true));
    	
    	System.out.println("processManagerBeanChanged--------------------");

    }
    
    @After("execution(* org.uengine.processmanager.ProcessManagerBean.applyChanges(..))")
    public void afterApplyChanges() throws java.rmi.RemoteException {
    	//cancel the changes
    	TransactionContext.getThreadLocalInstance().setSharedContext("processManagerBeanChanged", null);
    	
    	System.out.println("processManagerBeanChanged---is cancelled-----------------");

    }
    
    
    

}
