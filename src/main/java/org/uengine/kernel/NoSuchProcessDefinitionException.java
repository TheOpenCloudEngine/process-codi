package org.uengine.kernel;

/**
 * Created by jjy on 2016. 2. 16..
 */
public class NoSuchProcessDefinitionException extends UEngineException {
    public NoSuchProcessDefinitionException(String s, Exception e) {
        super(s, e);
    }

    public NoSuchProcessDefinitionException() {
        super("No Such Process Definition", null, null);
    }
}
