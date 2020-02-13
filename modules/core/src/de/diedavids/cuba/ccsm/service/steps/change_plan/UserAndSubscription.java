package de.diedavids.cuba.ccsm.service.steps.change_plan;

import com.haulmont.cuba.security.entity.User;
import de.diedavids.cuba.ccsm.entity.Subscription;

public class UserAndSubscription {

    private final User user;
    private final Subscription subscription;

    public UserAndSubscription(User user, Subscription subscription) {
        this.user = user;
        this.subscription = subscription;
    }

    public User getUser() {
        return user;
    }

    public Subscription getSubscription() {
        return subscription;
    }
}
