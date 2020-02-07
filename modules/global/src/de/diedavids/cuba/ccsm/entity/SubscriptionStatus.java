package de.diedavids.cuba.ccsm.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum SubscriptionStatus implements EnumClass<String> {

    TRIAL("TRIAL"),
    LIVE("LIVE"),
    DUNNING("DUNNING"),
    UNPAID("UNPAID"),
    FUTURE("FUTURE"),
    NON_RENEWING("NON_RENEWING"),
    CANCELED("CANCELED"),
    EXPIRED("EXPIRED"),
    TRIAL_EXPIRED("TRIAL_EXPIRED");

    private String id;

    SubscriptionStatus(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static SubscriptionStatus fromId(String id) {
        for (SubscriptionStatus at : SubscriptionStatus.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}