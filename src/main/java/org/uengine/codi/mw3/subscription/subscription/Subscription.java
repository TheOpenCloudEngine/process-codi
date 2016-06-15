package org.uengine.codi.mw3.subscription.subscription;

import org.metaworks.annotation.ServiceMethod;
import org.metaworks.component.SelectBox;

/**
 * Created by jjy on 2016. 6. 15..
 */
public class Subscription {

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

}
