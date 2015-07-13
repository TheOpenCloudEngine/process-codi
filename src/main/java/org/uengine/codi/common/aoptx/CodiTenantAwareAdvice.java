package org.uengine.codi.common.aoptx;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.metaworks.dao.TransactionContext;

//disabled and just moved to CodiMetaworksRemoteService
//@Aspect
public class CodiTenantAwareAdvice {

  /**
	 * 
	 * @param joinPoint
	 */
	@Before("execution(* org.uengine.codi.mw3.CodiMetaworksRemoteService.callMetaworksService(..))")
   public void before(JoinPoint joinPoint ) throws java.rmi.RemoteException {

      Object[] args = joinPoint.getArgs();



   }

    

}
