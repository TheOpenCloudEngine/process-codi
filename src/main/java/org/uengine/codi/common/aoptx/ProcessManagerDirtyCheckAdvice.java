package org.uengine.codi.common.aoptx;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.metaworks.dao.TransactionContext;
import org.metaworks.dao.TransactionListener;
import org.metaworks.dwr.MetaworksRemoteService;
import org.metaworks.dwr.TransactionalDwrServlet;
import org.springframework.context.ApplicationContext;
import org.uengine.processmanager.ProcessManagerRemote;

import java.util.Map;
import java.util.Set;

@Aspect
// @Scope(value="request")
public class ProcessManagerDirtyCheckAdvice {

    private void checkFirstlyDirtyAndListenTransaction() {

        if(TransactionContext.getThreadLocalInstance().getSharedContext("processManagerBeanChanged") == null){

            TransactionContext.getThreadLocalInstance().setSharedContext("processManagerBeanChanged", new Boolean(true));

            TransactionContext.getThreadLocalInstance().addTransactionListener(new TransactionListener() {
                @Override
                public void beforeCommit(TransactionContext tx) throws Exception {
                    ProcessManagerRemote processManager = getDirtyProcessManager(null);

                    if(processManager!=null){
                        processManager.applyChanges();
                    }
                }

                @Override
                public void beforeRollback(TransactionContext tx) throws Exception {
                    ProcessManagerRemote processManager = getDirtyProcessManager(null);

                    if(processManager!=null){
                        processManager.cancelChanges();
                    }
                }

                @Override
                public void afterCommit(TransactionContext tx) throws Exception {

                }

                @Override
                public void afterRollback(TransactionContext tx) throws Exception {

                }
            });

        }

    }

   @After("execution(* org.uengine.processmanager.ProcessManagerBean.*(..))")
    public void afterUsed() throws java.rmi.RemoteException {

       TransactionContext.getThreadLocalInstance().setSharedContext("processManagerBeanUsed", new Boolean(true));


   }



    @After("execution(* org.uengine.processmanager.ProcessManagerBean.set*(..))")
    public void afterSet() throws java.rmi.RemoteException {

        checkFirstlyDirtyAndListenTransaction();
    }

    @After("execution(* org.uengine.processmanager.ProcessManagerBean.add*(..))")
    public void afterAdd() throws java.rmi.RemoteException {

        checkFirstlyDirtyAndListenTransaction();
    }

    @After("execution(* org.uengine.processmanager.ProcessManagerBean.initialize*(..))")
    public void afterInitialize() throws java.rmi.RemoteException {

        checkFirstlyDirtyAndListenTransaction();
    }

    @After("execution(* org.uengine.processmanager.ProcessManagerBean.execute*(..))")
    public void afterExecute() throws java.rmi.RemoteException {

        checkFirstlyDirtyAndListenTransaction();
    }

    @After("execution(* org.uengine.processmanager.ProcessManagerBean.delegate*(..))")
    public void afterDelegate() throws java.rmi.RemoteException {

        checkFirstlyDirtyAndListenTransaction();
    }

    @After("execution(* org.uengine.processmanager.ProcessManagerBean.complete*(..))")
    public void afterComplete() throws java.rmi.RemoteException {

        checkFirstlyDirtyAndListenTransaction();
    }

    @After("execution(* org.uengine.processmanager.ProcessManagerBean.put*(..))")
    public void afterPut() throws java.rmi.RemoteException {

        checkFirstlyDirtyAndListenTransaction();
    }

   @After("execution(* org.uengine.processmanager.ProcessManagerBean.move*(..))")
   public void afterMove() throws java.rmi.RemoteException {

       checkFirstlyDirtyAndListenTransaction();
  }

  @After("execution(* org.uengine.processmanager.ProcessManagerBean.save*(..))")
  public void afterSave() throws java.rmi.RemoteException {
   afterComplete();
  }

    @After("execution(* org.uengine.processmanager.ProcessManagerBean.applyChanges(..))")
    public void afterApplyChanges() throws java.rmi.RemoteException {
    	//cancel the changes
    	TransactionContext.getThreadLocalInstance().setSharedContext("processManagerBeanChanged", null);
    }


    private ProcessManagerRemote getDirtyProcessManager(
            ProcessManagerRemote processManager) {
        //2. try the case that the one of inner classes issued the processmanager
        if(TransactionContext.getThreadLocalInstance().getSharedContext("processManagerBeanChanged") != null){
            if(TransactionalDwrServlet.useSpring){
                ApplicationContext springAppContext = MetaworksRemoteService.getInstance().getBeanFactory();

                Map beanMap = springAppContext.getBeansOfType(ProcessManagerRemote.class);
                Set keys = beanMap.keySet();

                Object springBean = null;

                for (Object key : keys) {
                    if(springBean != null) {
                        break;
                    }
                    springBean = beanMap.get(key);
                }

                processManager = (ProcessManagerRemote) springBean;
            }

            return processManager;
        }

        return null;
        //return processManager;
    }

}
