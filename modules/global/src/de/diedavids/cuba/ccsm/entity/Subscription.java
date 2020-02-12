package de.diedavids.cuba.ccsm.entity;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "CCSM_SUBSCRIPTION")
@Entity(name = "ccsm_Subscription")
public class Subscription extends StandardEntity {
    private static final long serialVersionUID = -2217985033355594389L;

    @Column(name = "EXTERNAL_ID")
    protected String externalId;

    @NotNull
    @Column(name = "STATUS", nullable = false)
    protected String status;

    @Lookup(type = LookupType.SCREEN, actions = {"lookup"})
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CUSTOMER_ID")
    protected Customer customer;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PLAN_ID")
    protected Plan plan;

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public SubscriptionStatus getStatus() {
        return status == null ? null : SubscriptionStatus.fromId(status);
    }

    public void setStatus(SubscriptionStatus status) {
        this.status = status == null ? null : status.getId();
    }
}