package org.uengine.codi.mw3.model;

import org.metaworks.annotation.Face;
import org.metaworks.widget.Label;

/**
 * Created by jangjinyoung on 15. 8. 14..
 */

@Face(ejsPath="dwr/metaworks/genericfaces/CleanObjectFace.ejs")
public class AddProcessMapPanel{

    Label bpmsNotSupportedLabel;

        public Label getBpmsNotSupportedLabel() {
            return bpmsNotSupportedLabel;
        }

        public void setBpmsNotSupportedLabel(Label bpmsNotSupportedLabel) {
            this.bpmsNotSupportedLabel = bpmsNotSupportedLabel;
        }

    public AddProcessMapPanel() {
        setBpmsNotSupportedLabel(new Label("<center><h1>This version of Codi doesn't support BPMS functionality</h1> Please check out the product information at uengine.org.</center>"));
    }

    public void load(){}
}
