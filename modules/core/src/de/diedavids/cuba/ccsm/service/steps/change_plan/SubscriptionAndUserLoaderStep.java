package de.diedavids.cuba.ccsm.service.steps.change_plan;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.security.entity.User;
import de.diedavids.cuba.ccsm.entity.Customer;
import de.diedavids.cuba.ccsm.entity.Subscription;
import de.diedavids.cuba.ccsm.service.steps.LoadStep;

public class SubscriptionAndUserLoaderStep implements LoadStep<String, UserAndSubscription> {
    private final DataManager dataManager;

    public SubscriptionAndUserLoaderStep(
            DataManager dataManager
    ) {
        this.dataManager = dataManager;
    }

    @Override
    public UserAndSubscription apply(String customerId) {

        Customer customer = dataManager.load(Customer.class)
                .query("select e from ccsm_Customer e where e.externalId = :customerId")
                .parameter("customerId", customerId)
                .view("customer-view")
                .one();

        Subscription subscription = customer.getSubscriptions().get(0);

        User user = dataManager.reload(customer.getTenant().getAdmin(), "user.edit");

        return new UserAndSubscription(user, subscription);
    }
}
