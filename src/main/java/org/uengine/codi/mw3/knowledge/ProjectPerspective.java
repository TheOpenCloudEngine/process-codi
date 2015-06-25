package org.uengine.codi.mw3.knowledge;

import org.metaworks.MetaworksContext;
import org.uengine.codi.mw3.model.CollapsePerspective;

/**
 * Created by soo on 2015. 6. 17..
 */
public class ProjectPerspective extends CollapsePerspective {



    ProjectPanel projectPanel;

    public ProjectPanel getProjectPanel() {
        return projectPanel;
    }

    public void setProjectPanel(ProjectPanel projectPanel) {
        this.projectPanel = projectPanel;
    }

    public ProjectPerspective() throws Exception{
        setLabel("$Developer");
    }

    @Override
    protected void loadChildren() throws Exception{
        projectPanel = new ProjectPanel();
        projectPanel.setMetaworksContext(new MetaworksContext());
        projectPanel.getMetaworksContext().setHow("perspectivePanel");
        projectPanel.session = session;
        projectPanel.load();
    }
}
