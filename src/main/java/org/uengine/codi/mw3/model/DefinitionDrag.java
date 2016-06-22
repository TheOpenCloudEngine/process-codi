package org.uengine.codi.mw3.model;

import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.Payload;
import org.metaworks.annotation.ServiceMethod;

/**
 * Created by jjy on 2016. 6. 19..
 */
public class DefinitionDrag{


    String id;
        @Id
        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }


    @ServiceMethod(mouseBinding="drag", callByContent = true)
    public Session cut(
            @AutowiredFromClient
             Session session) throws Exception {

        session.setClipboard(this);

        return session;
    }
}
