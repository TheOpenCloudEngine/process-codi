package org.uengine.codi.common.aoptx;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.uengine.processmanager.ProcessManagerRemote;

@Aspect
// @Scope(value="request"
public class ProcessManagerServiceAdvice {
    @Autowired
    ProcessManagerRemote processManagerService;

    @Before("execution(* org.springframework.transaction.PlatformTransactionManager.commit(..))")
    public void beforeCommit() throws java.rmi.RemoteException {
    	processManagerService.applyChanges();
    }

    @Before("execution(* org.springframework.transaction.PlatformTransactionManager.rollback(..))")
    public void beforeRollback() throws java.rmi.RemoteException {
    	processManagerService.cancelChanges();
    }
}
