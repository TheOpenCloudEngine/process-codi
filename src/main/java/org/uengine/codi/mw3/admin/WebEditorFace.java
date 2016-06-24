package org.uengine.codi.mw3.admin;

import org.metaworks.Face;

/**
 * Created by jjy on 2016. 6. 22..
 */
public class WebEditorFace extends WebEditor implements Face<String> {

    @Override
    public void setValueToFace(String value) {
        setContents(value);
    }

    @Override
    public String createValueFromFace() {
        return getContents();
    }
}
