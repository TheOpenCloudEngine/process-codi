package org.uengine.codi.scheduler;

import org.metaworks.dao.ConnectionFactory;
import org.metaworks.dao.TransactionContext;
import org.metaworks.spring.SpringConnectionFactory;
import org.quartz.JobExecutionContext;
import org.quartz.StatefulJob;
import org.uengine.codi.mw3.CodiClassLoader;

public class NotiJob implements StatefulJob {
	
	public static ConnectionFactory connectionFactory;
	
	public void execute(JobExecutionContext context) {

		TransactionContext tx = null;
		
		try{

			connectionFactory = (SpringConnectionFactory) context.getJobDetail().getJobDataMap().get("connectionFactory");
			
			tx = new TransactionContext(); //once a TransactionContext is created, it would be cached by ThreadLocal.set, so, we need to remove this after the request processing.
			tx.setManagedTransaction(true);
			tx.setAutoCloseConnection(true);
			
			if(connectionFactory!=null)
				tx.setConnectionFactory(connectionFactory);

		}catch(Exception e){
			e.printStackTrace();
			
			if(tx != null){
				try {
					tx.rollback();
				} catch (Exception e1) {
					e1.printStackTrace();
				}			
			}
		}finally{
			if(tx != null){
				try {
					tx.releaseResources();
				} catch (Exception e) {
					e.printStackTrace();
				}							
			}
		}
	}
}

