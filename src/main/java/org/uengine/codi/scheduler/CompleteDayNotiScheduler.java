package org.uengine.codi.scheduler;

import org.metaworks.dao.Database;
import org.metaworks.dao.TransactionContext;
import org.metaworks.spring.SpringConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.uengine.codi.CodiProcessManagerBean;
import org.uengine.codi.MetaworksUEngineSpringConnectionAdapter;
import org.uengine.codi.mw3.model.*;
import org.uengine.kernel.GlobalContext;

import javax.sql.RowSet;

/**
 * Created by ho.lim on 4/6/2015.
 */
@Component
public class CompleteDayNotiScheduler {
    @Autowired
    SpringConnectionFactory connectionFactory;

    @Scheduled(cron = "0 0 8 * * ?")
    public void pushNotiInCompleteDay() {
        TransactionContext tx = new TransactionContext();

        tx.setManagedTransaction(false);
        tx.setAutoCloseConnection(true);

        MetaworksUEngineSpringConnectionAdapter connectionAdapter = new MetaworksUEngineSpringConnectionAdapter();

        CodiProcessManagerBean processManager = new CodiProcessManagerBean();
        processManager.setConnectionFactory(connectionAdapter);
        processManager.setAutoCloseConnection(false);
        processManager.setManagedTransaction(true);

        if (connectionFactory != null)
            tx.setConnectionFactory(connectionFactory);

        try {
            //workitem 생성
//			CommentWorkItem workItem = new CommentWorkItem();
            IInstance instList = (IInstance) Database.sql(IInstance.class, "select * from bpm_procinst where DATE_FORMAT(duedate,'%Y-%m-%d') = CURRENT_DATE");
            instList.select();

            RowSet instRow = instList.getImplementationObject().getRowSet();

            Session codiSession = new Session();
            Locale localeManager = new Locale();

            if (instList.size() > 0) {

                while (instRow.next()) {

                    IEmployee emp = new Employee();
                    emp.setEmpCode(GlobalContext.getPropertyString("codi.user.id"));
                    emp.setGlobalCom(GlobalContext.getPropertyString("codi.user.name"));
                    emp.setGlobalCom(instRow.getString("InitComCd"));

                    IUser writer = new User();
                    writer.setUserId(emp.getEmpCode());
                    writer.setName(emp.getEmpName());

                    ICompany company = new Company();
                    company.setComCode(emp.getGlobalCom());

                    codiSession.setUser(writer);
                    codiSession.setCompany(company);
                    codiSession.setEmployee(emp);

                    localeManager.setLanguage(emp.getLocale());
                    localeManager.load();

                    CommentWorkItem workItem = new CommentWorkItem();

                    workItem.setInstId(instRow.getLong("INSTID"));
                    workItem.setEndpoint(emp.getEmpCode());
                    workItem.setDueDate(instRow.getDate("DUEDATE"));
                    workItem.setRootInstId(instRow.getLong("INSTID"));
                    workItem.setWriter(writer);
                    workItem.setIsDeleted(instRow.getBoolean("ISDELETED"));
                    workItem.setTitle(localeManager.getResourceBundle().getProperty("TimeToComplete"));

                    workItem.processManager = processManager;
                    workItem.session = codiSession;
                    workItem.add();
                }
            }

            tx.commit();
        } catch (Throwable e) {
            try {
                tx.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                tx.releaseResources();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
