package org.uengine.codi.mw3.model;

import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.ORMapping;
import org.metaworks.annotation.Table;

/**
 * Created by hoo.lim on 8/12/2015.
 */
@Face(
        ejsPath="dwr/metaworks/org/uengine/codi/mw3/model/GoogleDriveAttachmentWorkItem.ejs",
        ejsPathMappingByContext=
                {
                        "{when: 'new', face: 'dwr/metaworks/org/uengine/codi/mw3/model/GoogleDriveAttachmentWorkItem_edit.ejs'}",
                        "{when: 'edit', face: 'dwr/metaworks/org/uengine/codi/mw3/model/GoogleDriveAttachmentWorkItem_edit.ejs'}",
                }

)
public class GoogleDriveAttachmentWorkItem extends WorkItem{
    public GoogleDriveAttachmentWorkItem(){
        setType(WorkItem.WORKITEM_TYPE_GOOGLEDOC);
    }

    @Override
    public Object[] add() throws Exception {
        return super.add();
    }


}
