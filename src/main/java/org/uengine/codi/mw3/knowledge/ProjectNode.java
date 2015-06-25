package org.uengine.codi.mw3.knowledge;

import org.metaworks.Remover;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dao.Database;
import org.metaworks.dao.MetaworksDAO;
import org.metaworks.dao.TransactionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.uengine.codi.mw3.common.MainPanel;
import org.uengine.codi.mw3.model.*;
import org.uengine.processmanager.ProcessManagerRemote;

import java.util.Calendar;

/**
 * Created by soo on 2015. 6. 19..
 */
public class ProjectNode extends TopicNode implements IProjectNode {

    public final static String TYPE_PROJECT = "project";

    String projectAlias;

    public String getProjectAlias() {
        return projectAlias;
    }

    public void setProjectAlias(String projectAlias) {
        this.projectAlias = projectAlias;
    }

    String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ProjectNode() {
        this.setType(TYPE_PROJECT);
    }

    @ServiceMethod(callByContent = true)
    public Object[] loadTopic() throws Exception {

        session.setLastPerspecteType(TYPE_PROJECT);
        session.setLastSelectedItem(this.getId());

        Perspective perspective = new Perspective();
        perspective.session = session;

        // recentItem ��create
        RecentItem recentItem = new RecentItem();
        recentItem.setEmpCode(session.getEmployee().getEmpCode());
        recentItem.setItemId(this.getId());
        recentItem.setItemType(this.getType());
        recentItem.setUpdateDate(Calendar.getInstance().getTime());

        recentItem.add();

        if ("oce".equals(session.getUx())) {
//			session.setUx("oce_project");
//
//			Object[] returnObject =  perspective.loadInstanceListPanel(session, TYPE_PROJECT, getId());
//
//			return new Object[]{new DashboardWindow(returnObject[1])};

            session.setUx("sns");

            return new Object[]{new MainPanel(new Main(session, null, this.getId().toString()))};
        } else {
            InstanceListPanel instanceListPanel = Perspective.loadInstanceList(session, Perspective.MODE_PROJECT, Perspective.TYPE_NEWSFEED, getId());

            ListPanel listPanel = new ListPanel(instanceListPanel, new ProjectInfo(session, this.getId(), this.getName()));

            return new Object[]{session, listPanel};
        }

    }

    public static IProjectNode load(Session session) throws Exception {

        StringBuffer sb = new StringBuffer();
        sb.append("select * from bpm_knol knol");
        sb.append(" left join recentItem item on item.itemId = knol.id and item.empcode = ?userid and item.itemType=?type");
        sb.append(" where knol.type = ?type");
        sb.append(" and knol.companyId = ?companyId");
        sb.append(" and ( knol.secuopt=0 OR (knol.secuopt=1 and ( exists (select topicid from BPM_TOPICMAPPING tp where tp.userid=?userid and knol.id=tp.topicid)  ");
        sb.append(" 																	 or ?userid in ( select empcode from emptable where partcode in (  ");
        sb.append(" 																	 						select userId from BPM_TOPICMAPPING where assigntype = 2 and topicID = knol.id )))))  ");
        //sb.append(" order by updateDate desc limit " + GlobalContext.getPropertyString("topic.more.count", DEFAULT_TOPIC_COUNT));

        IProjectNode dao = (IProjectNode) MetaworksDAO.createDAOImpl(TransactionContext.getThreadLocalInstance(), sb.toString(), IProjectNode.class);
        dao.set("type", TYPE_PROJECT);
        dao.set("userid", session.getEmployee().getEmpCode());
        dao.set("companyId", session.getEmployee().getGlobalCom());
        dao.select();

        return dao;
    }

    public IProjectNode findById() throws Exception {

        StringBuffer sql = new StringBuffer();

        sql.append("SELECT knol.id, knol.name ");
        sql.append("  FROM bpm_knol knol");
        sql.append(" WHERE knol.type=?type AND knol.id=?Id");

        IProjectNode dao = (IProjectNode) Database.sql(IProjectNode.class, sql.toString());

        dao.setType(TYPE_PROJECT);
        dao.setId(this.getId());
        dao.select();

        return dao;

    }

    @Override
    public Object[] goIDE() throws Exception {
//        CloudIDE ide = new CloudIDE(session, new Project(this));
//
//        return new Object[]{ ide, ide.loadTopCenterPanel(session), new Remover(new ModalWindow())};
        return null;
    }

    public IProjectNode findByNameForProject() throws Exception {

        StringBuffer sql = new StringBuffer();

        sql.append("SELECT knol.id, knol.name ");
        sql.append("  FROM bpm_knol knol");
        sql.append(" WHERE knol.type=?type AND knol.name=?name");

        IProjectNode dao = (IProjectNode) Database.sql(IProjectNode.class, sql.toString());

        dao.setType(TYPE_PROJECT);
        dao.setName(this.getName());
        dao.select();

        if (dao.next())
            return dao;
        else
            return null;


    }

    @ServiceMethod(callByContent = true, needToConfirm = true, target = ServiceMethodContext.TARGET_APPEND)
    public Object[] remove() throws Exception {

        if (session.getUser().getUserId().equalsIgnoreCase(getAuthorId()) || session.getEmployee().getIsAdmin()) {
            // ��젣��吏꾩쭨 ��젣媛��꾨땶 topic 留��쒓굅瑜��섏뿬 吏�떇�몃뱶�먯꽌��蹂댁씠�꾨줉 �ㅼ젙��
            // deleteDatabaseMe();
            deleteDatabaseMe();

            IInstance instance = (IInstance) sql(IInstance.class, "select * from bpm_procinst where topicId= ?topicId");
            instance.set("topicId", this.getId());
            instance.select();

            if (instance.size() > 0) {
                instance.next();

                Instance inst = new Instance();
                inst.copyFrom(instance);

                inst.deleteDatabaseMe();
            }


//			StringBuffer sb = new StringBuffer();
//			sb.append("update bpm_knol");
//			sb.append("   set type = null ");
//			sb.append(" where id=?topicId");
//
//			ITopicNode updateNode = (ITopicNode) sql(ITopicNode.class,	sb.toString());
//			updateNode.set("topicId", this.getId());
//
//			updateNode.update();

        } else {
            throw new Exception("愿�━�먮굹 珥덇린�좏뵿�앹꽦�먮쭔 �섏젙媛�뒫�⑸땲��");
        }

        return new Object[]{new Remover(this)};
    }

    @Autowired
    public ProcessManagerRemote processManager;


}
