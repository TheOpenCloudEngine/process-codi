package org.uengine.codi.mw3.model;

import org.metaworks.Face;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.component.MultiSelectBox;
import org.metaworks.dwr.MetaworksRemoteService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jjy on 2016. 1. 11..
 */
public class RoleMappedUserFace extends MultiSelectBox implements Face<RoleMappedUser> {

    public RoleMappedUserFace() {
        super();
    }

    @AutowiredFromClient
    public Session session;

    @Override
    public void setValueToFace(RoleMappedUser value) {
        ContactPanel contactPanel = new ContactPanel();
        MetaworksRemoteService.autowire(contactPanel);

        try {
            contactPanel.load();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        //add myself first
        getOptionNames().add(session.getUser().getName());
        getOptionValues().add(session.getUser().getUserId());

        IContact contact = contactPanel.getList();

        try {
            while(contact.next()){
                getOptionNames().add(contact.getFriend().getName());
                getOptionValues().add(contact.getFriend().getUserId());
            };
        } catch (Exception e) {
            e.printStackTrace();
        }

        String selectedUserIds = "";
        String sep = "";
        if(value!=null && value.size()>0){
            for(IUser user : value.getUsers()){

                selectedUserIds += sep + user.getUserId();
                sep = ", ";
            }
        }

        setSelected(selectedUserIds);
    }

    @Override
    public RoleMappedUser createValueFromFace() {

        String userIds = getSelected();

        if(userIds==null || userIds.trim().length() == 0) return null;

        String[] userIdSplitted = userIds.split(", ");

        if(userIdSplitted.length == 0) return null;

        List<IUser> users = new ArrayList<IUser>();
        for(String userId : userIdSplitted){
            User user = new User();
            user.setUserId(userId);
            users.add(user);
        }

        RoleMappedUser roleUser = new RoleMappedUser();
        roleUser.setUsers(users);

        return roleUser;
    }
}
