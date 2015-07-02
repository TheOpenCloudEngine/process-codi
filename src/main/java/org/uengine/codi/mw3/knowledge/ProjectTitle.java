package org.uengine.codi.mw3.knowledge;

import org.metaworks.*;
import org.metaworks.annotation.*;
import org.metaworks.annotation.Face;
import org.metaworks.widget.ModalWindow;
import org.uengine.codi.mw3.model.InstanceListPanel;
import org.uengine.codi.mw3.model.ListPanel;
import org.uengine.codi.mw3.model.Perspective;
import org.uengine.codi.mw3.model.Session;

import java.util.Date;

/**
 * Created by soo on 2015. 6. 18..
 */
@Face(ejsPath = "dwr/metaworks/genericfaces/FormFace.ejs", options = {"fieldOrder"}, values = {"topicTitle"})
public class ProjectTitle implements ContextAware {

    MetaworksContext metaworksContext;

    public MetaworksContext getMetaworksContext() {
        return metaworksContext;
    }

    public void setMetaworksContext(MetaworksContext metaworksContext) {
        this.metaworksContext = metaworksContext;
    }

    String topicId;

    @Hidden
    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    String topicTitle;

    @Face(displayName = "$ProjectName", options = {"size"}, values = {"51"})
    public String getTopicTitle() {
        return topicTitle;
    }

    public void setTopicTitle(String topicTitle) {
        this.topicTitle = topicTitle;
    }

    @AutowiredFromClient
    transient public Session session;

    @Face(displayName = "$Create")
    @Available(when = {MetaworksContext.WHEN_NEW})
    @ServiceMethod(callByContent = true, target = ServiceMethodContext.TARGET_APPEND)
    public Object[] save() throws Exception {
        this.saveMe();

        InstanceListPanel instanceListPanel = Perspective.loadInstanceList(session, Perspective.MODE_PROJECT, Perspective.TYPE_NEWSFEED, this.getTopicId());
        ListPanel listPanel = new ListPanel(instanceListPanel, new ProjectInfo(session, this.getTopicId(), this.getTopicTitle()));

        return new Object[]{new ToEvent(new ProjectPerspective(), EventContext.EVENT_CHANGE), new Remover(new ModalWindow(), true), new Refresh(session), new Refresh(listPanel)};
    }

    public void saveMe() throws Exception {
        WfNode wfNode = new WfNode();

        wfNode.setName(this.getTopicTitle());
        wfNode.setType("project");
        wfNode.setIsDistributed(false);
        wfNode.setIsReleased(false);
        wfNode.setParentId(session.getCompany().getComCode());
        wfNode.setAuthorId(session.getUser().getUserId());
        wfNode.setCompanyId(session.getCompany().getComCode());
        wfNode.setStartDate(new Date());
        wfNode.setSecuopt("0");
        wfNode.createMe();

        TopicMapping tm = new TopicMapping();
        tm.setTopicId(wfNode.getId());
        tm.setUserId(session.getUser().getUserId());
        tm.setUserName(session.getUser().getName());
        tm.getMetaworksContext().setWhen(this.getMetaworksContext().getWhen());

        tm.saveMe();
        tm.flushDatabaseMe();

        this.setTopicId(wfNode.getId());
    }
}
