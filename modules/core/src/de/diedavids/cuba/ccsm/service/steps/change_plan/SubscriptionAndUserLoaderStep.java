package de.diedavids.cuba.ccsm.service.steps.change_plan;

import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.security.entity.User;
import de.diedavids.cuba.ccsm.ChangeSubscriptionRequest;
import de.diedavids.cuba.ccsm.CreateCustomerWithSubscriptionRequest;
import de.diedavids.cuba.ccsm.entity.Customer;
import de.diedavids.cuba.ccsm.entity.Subscription;
import de.diedavids.cuba.ccsm.service.steps.CommitStep;

public class SubscriptionAndUserLoaderStep implements CommitStep {
    private final DataManager dataManager;
    private final ChangeSubscriptionRequest request;
    private Customer customer;
    private Subscription subscription;
    private User user;

    public SubscriptionAndUserLoaderStep(
            DataManager dataManager,
            ChangeSubscriptionRequest request
    ) {

        this.dataManager = dataManager;
        this.request = request;
    }

    @Override
    public void accept(CommitContext commitContext) {


        Customer customer = dataManager.load(Customer.class)
                .query("select e from ccsm_Customer e where e.externalId = :customerId")
                .parameter("customerId", request.getCustomerId())
                .view("customer-view")
                .one();

        subscription = customer.getSubscriptions().get(0);

        user = dataManager.reload(customer.getTenant().getAdmin(), "user.edit");

    }

    public Subscription getSubscription() {
        return subscription;
    }

    public User getUser() {
        return user;
    }
}
