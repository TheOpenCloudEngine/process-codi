package org.uengine.codi.mw3.subscription.subscription;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.component.SelectBox;
import org.uengine.codi.mw3.model.Session;

import java.util.ArrayList;

/**
 * Created by jjy on 2016. 6. 15..
 */
public class Subscription implements ContextAware{

    @Override
    public MetaworksContext getMetaworksContext() {
        return metaworksContext;
    }

    @Override
    public void setMetaworksContext(MetaworksContext metaworksContext) {
        this.metaworksContext = metaworksContext;
    }

    MetaworksContext metaworksContext;


    @AutowiredFromClient
    public Session session;


    @ServiceMethod
    public void unsubscribe(){

    }

    @ServiceMethod
    public void changePlan(){

    }

    @ServiceMethod
    public void subscribe(){

    }


    SelectBox planSelected;
        public SelectBox getPlanSelected() {
            return planSelected;
        }
        public void setPlanSelected(SelectBox planSelected) {
            this.planSelected = planSelected;
        }

    public void load() {
        setMetaworksContext(new MetaworksContext());
        getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);

        //load current subscription for session

        setPlanSelected(new SelectBox());

        ArrayList<String> plans = new ArrayList<String>();

        getPlanSelected().setOptionNames(plans);
        getPlanSelected().setOptionValues(plans);

        getPlanSelected().setSelected("Enterprise"); //value from killbill
    }
}
