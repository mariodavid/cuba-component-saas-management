package de.diedavids.cuba.ccsm;

import java.io.Serializable;

public class ChangeSubscriptionRequest implements Serializable {

    String customerId;
    String plan;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }
}
