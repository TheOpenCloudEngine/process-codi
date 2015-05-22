package org.uengine.codi.mw3.model;

/**
 * Created by ho.lim on 2015-05-21.
 */
import org.metaworks.annotation.Face;
import org.uengine.codi.mw3.model.Application;
import org.uengine.codi.mw3.widget.IFrame;

@Face(ejsPath="dwr/metaworks/genericfaces/CleanObjectFace.ejs")
public class IFrameApplication extends Application {

    IFrame content;
    public IFrame getContent() {
        return content;
    }
    public void setContent(IFrame content) {
        this.content = content;
    }

}