package org.uengine.codi.mw3.widget;

import org.metaworks.Face;
import org.metaworks.example.ide.SourceCode;

/**
 * Created by jangjinyoung on 15. 7. 28..
 */
public class SourceCodeFace extends SourceCode implements Face<String> {
    @Override
    public void setValueToFace(String s) {
        setCode(s);
    }

    @Override
    public String createValueFromFace() {
        return getCode();
    }
}
