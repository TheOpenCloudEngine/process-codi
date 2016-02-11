package org.uengine.codi.mw3.model;

import org.metaworks.annotation.Face;
import org.uengine.kernel.NeedArrangementToSerialize;
import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.RoleMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jangjinyoung on 15. 9. 27..
 */
@Face(faceClass = RoleMappedUserFace.class)
public class RoleMappedUser extends RoleMapping implements NeedArrangementToSerialize{

    List<IUser> users;
        public List<IUser> getUsers() {
            return users;
        }
        public void setUsers(List<IUser> users) {
            this.users = users;
        }



    public RoleMappedUser(){
        super();


        setUsers(new ArrayList<IUser>());
//        user.setMetaworksContext(new MetaworksContext());
//        user.getMetaworksContext().setWhere(MetaworksContext.WHEN_NEW);
//        user.getMetaworksContext().setHow(User.HOW_PICKER);
    }


    @Override
    public void beforeSerialization() {

    }

    @Override
    public void afterDeserialization() {
        if (getUsers() != null) {
            for(IUser user : getUsers()){
                setEndpoint(user.getUserId());
                moveToAdd();
            }
        }
        beforeFirst();
    }

    @Override
    public String getEndpoint() {
        if(endpoint==null && users!=null){
            afterDeserialization();
        }

        return super.getEndpoint();
    }

    @Override
    public void fill(ProcessInstance instance) throws Exception {
//        User user = new User();
//
//        user.setUserId(getEndpoint());
//
//        IUser databaseOne = user.databaseMe();

        Employee employee = new Employee();
        employee.setEmpCode(getEndpoint());
        IEmployee databaseOne = employee.databaseMe();

        setResourceName(databaseOne.getEmpName());
    }
}
