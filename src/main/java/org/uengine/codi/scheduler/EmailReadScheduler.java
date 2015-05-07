package org.uengine.codi.scheduler;

import org.metaworks.dao.TransactionContext;
import org.metaworks.spring.SpringConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.uengine.codi.CodiProcessManagerBean;
import org.uengine.codi.MetaworksUEngineSpringConnectionAdapter;
import org.uengine.codi.scheduler.email.EMailReader;

import java.util.Date;

/**
 * Created by ho.lim on 4/3/2015.
 */
@Component
public class EmailReadScheduler {
    @Autowired
    SpringConnectionFactory connectionFactory;

    @Scheduled(fixedDelay = 200000)
    public void postEmail2NewsFeed() {
        System.out.println("Email to News Feed!" + (new Date()).toString());
        TransactionContext tx = new TransactionContext(); //once a TransactionContext is created, it would be cached by ThreadLocal.set, so, we need to remove this after the request processing.
        try {
            System.out.println("read...");

            tx.setManagedTransaction(false);
            tx.setAutoCloseConnection(true);
            tx.setConnectionFactory(connectionFactory);

            MetaworksUEngineSpringConnectionAdapter connectionAdapter = new MetaworksUEngineSpringConnectionAdapter();

            CodiProcessManagerBean processManager = new CodiProcessManagerBean();
            processManager.setConnectionFactory(connectionAdapter);
            processManager.setAutoCloseConnection(false);
            processManager.setManagedTransaction(true);


            EMailReader emailReader = new EMailReader();
            emailReader.processManager = processManager;
            emailReader.read();


            //TODO: e-mail 읽어서 인스턴스 발행하기
                /*
				emailReader.emailWorkItem.processManager = processManager;
				emailReader.emailWorkItem.instanceViewContent = new InstanceViewContent();
				emailReader.emailWorkItem.instanceViewContent.instanceView = new InstanceView();
				emailReader.emailWorkItem.instanceViewContent.instanceView.processManager = processManager;

		//		emailReader.read();

				//TODO: 대표 SNS 계정 접속하여 담벼락에 올라온 사항 인스턴스 발행하기
				//TODO: 대표 그룹 계정 (위의 SNS계정과 아마 API가 동일할 것)에 접속하여 담벼락 긁어오기
				//TODO: worklist RESERVED 찾아서 실행하기
				//TODO: instance 완료시간 도래 된 것들 찾아서 알림 주기
				//

				// 대표계정
				emailReader.read();
				// 개인별 계정
	//			emailReader.readEmailByUser();
				*/

            tx.commit();
        } catch (Throwable e) {
            try {
                tx.rollback();
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            e.printStackTrace();

            //in DWR, we never stops the application
            //throw new ServletException(e);

        } finally {
            try {
                tx.releaseResources();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}

