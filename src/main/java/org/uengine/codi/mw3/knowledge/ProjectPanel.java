package org.uengine.codi.mw3.knowledge;

import org.metaworks.ContextAware;
import org.metaworks.EventContext;
import org.metaworks.MetaworksContext;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.widget.ModalWindow;
import org.uengine.codi.mw3.model.Session;
import org.uengine.util.MeasuringContext;

/**
 * Created by soo on 2015. 6. 17..
 */
public class ProjectPanel implements ContextAware {

    @AutowiredFromClient
    transient public Session session;

    IProjectNode projectNode;

    public IProjectNode getProjectNode() {
        return projectNode;
    }

    public void setProjectNode(IProjectNode projectNode) {
        this.projectNode = projectNode;
    }

    MetaworksContext metaworksContext;

    public MetaworksContext getMetaworksContext() {
        return this.metaworksContext;
    }

    public void setMetaworksContext(MetaworksContext metaworksContext) {
        this.metaworksContext = metaworksContext;
    }

    @Face()
    @ServiceMethod(callByContent = true, target = ServiceMethodContext.TARGET_APPEND)
    public ModalWindow addProject() throws Exception {
        ProjectTitle projectTitle = new ProjectTitle();
        projectTitle.setMetaworksContext(new MetaworksContext());
        projectTitle.getMetaworksContext().setWhen(MetaworksContext.WHEN_NEW);
        projectTitle.session = session;

        return new ModalWindow(projectTitle, 360, 200, "$CreateProject");
    }

    @ServiceMethod(callByContent = true, eventBinding = EventContext.EVENT_CHANGE)
    public void load() throws Exception{
        IProjectNode projectNode = ProjectNode.load(session);

        projectNode.getMetaworksContext().setHow(this.getMetaworksContext().getHow());
        setProjectNode(projectNode);
    }
}
